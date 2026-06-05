package com.ricky.campus.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ricky.campus.mapper.CommentMapper;
import com.ricky.campus.mapper.PostMapper;
import com.ricky.campus.mapper.UserProfileMapper;
import com.ricky.campus.model.dto.CommentCreateDTO;
import com.ricky.campus.model.entity.Comment;
import com.ricky.campus.model.entity.Post;
import com.ricky.campus.model.entity.UserProfile;
import com.ricky.campus.model.vo.CommentVO;
import com.ricky.campus.service.CommentService;
import com.ricky.campus.service.NotificationService;
import com.ricky.campus.service.SensitiveWordService;
import jakarta.annotation.Resource;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment> implements CommentService {

    @Resource
    private SensitiveWordService sensitiveWordService;
    @Resource
    private UserProfileMapper userProfileMapper;
    @Resource
    private PostMapper postMapper;
    @Resource
    private NotificationService notificationService;

    @Override
    public Page<CommentVO> getCommentList(Long postId, Integer page, Integer size) {
        Page<Comment> pages = new Page<>(page, size);
        Page<Comment> pageResult = this.page(pages,
                new LambdaQueryWrapper<Comment>()
                        .eq(Comment::getPostId, postId)
                        .orderByAsc(Comment::getCreateTime));

        // 查作者头像
        Set<String> authorIds = pageResult.getRecords().stream()
                .map(Comment::getStudentId).collect(Collectors.toSet());
        java.util.Map<String, String> avatarMap = new java.util.HashMap<>();
        if (!authorIds.isEmpty()) {
            userProfileMapper.selectList(
                    new LambdaQueryWrapper<UserProfile>().in(UserProfile::getStudentId, authorIds)
            ).forEach(u -> avatarMap.put(u.getStudentId(), u.getAvatarUrl()));
        }

        List<CommentVO> voList = pageResult.getRecords().stream().map(e -> {
            CommentVO vo = new CommentVO();
            BeanUtils.copyProperties(e, vo);
            vo.setAuthorAvatar(avatarMap.get(e.getStudentId()));
            return vo;
        }).collect(Collectors.toList());

        Page<CommentVO> result = new Page<>(pageResult.getCurrent(), pageResult.getSize());
        result.setTotal(pageResult.getTotal());
        result.setRecords(voList);
        return result;
    }

    @Override
    public Comment createComment(CommentCreateDTO dto, String studentId) {
        UserProfile user = userProfileMapper.selectOne(
                new LambdaQueryWrapper<UserProfile>().eq(UserProfile::getStudentId, studentId));
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }

        // 敏感词校验
        Map<String, Object> check = sensitiveWordService.checkText(dto.getContent());
        if ((Boolean) check.get("hasSensitive")) {
            throw new RuntimeException("评论内容包含敏感词：" + check.get("word"));
        }

        Comment comment = new Comment();
        BeanUtils.copyProperties(dto, comment);
        comment.setStudentId(studentId);
        comment.setAuthorName(user.getName());
        comment.setStatus(1);
        this.save(comment);

        // 帖子评论数 +1
        Post post = postMapper.selectById(dto.getPostId());
        if (post != null) {
            post.setCommentCount(post.getCommentCount() == null ? 1 : post.getCommentCount() + 1);
            postMapper.updateById(post);

            // 评论通知（不给自己发）
            if (!studentId.equals(post.getStudentId())) {
                notificationService.send(
                        post.getStudentId(),
                        "comment",
                        post.getId(),
                        "有人评论了你的帖子",
                        user.getName() + " 评论了《" + post.getTitle() + "》"
                );
            }
        }

        return comment;
    }

    @Override
    public void deleteComment(Long commentId, String studentId) {
        Comment comment = this.getById(commentId);
        if (comment == null) {
            throw new RuntimeException("评论不存在");
        }

        UserProfile user = userProfileMapper.selectOne(
                new LambdaQueryWrapper<UserProfile>().eq(UserProfile::getStudentId, studentId));
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }

        // 本人可以删 OR 管理员可以删
        if (comment.getStudentId().equals(studentId) || user.getRole() == 0) {
            this.removeById(commentId);
        }
    }
}
