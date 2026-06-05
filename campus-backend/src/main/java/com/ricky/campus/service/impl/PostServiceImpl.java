package com.ricky.campus.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ricky.campus.mapper.PostFavoriteMapper;
import com.ricky.campus.mapper.PostLikeMapper;
import com.ricky.campus.mapper.PostMapper;
import com.ricky.campus.mapper.UserProfileMapper;
import com.ricky.campus.model.dto.PostCreateDTO;
import com.ricky.campus.model.dto.PostQueryDTO;
import com.ricky.campus.model.entity.*;
import com.ricky.campus.model.vo.PostVO;
import com.ricky.campus.service.NotificationService;
import com.ricky.campus.service.PostService;
import com.ricky.campus.service.SensitiveWordService;
import jakarta.annotation.Resource;
import org.springframework.util.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class PostServiceImpl extends ServiceImpl<PostMapper, Post> implements PostService {

    @Resource
    private UserProfileMapper userProfileMapper;
    @Resource
    private PostFavoriteMapper postFavoriteMapper;
    @Resource
    private PostLikeMapper postLikeMapper;
    @Resource
    private SensitiveWordService sensitiveWordService;
    @Resource
    private NotificationService notificationService;

    @Override
    public Page<PostVO> getPostList(PostQueryDTO query) {
        Page<Post> page = new Page<>(query.getPage(), query.getSize());
        LambdaQueryWrapper<Post> qw = new LambdaQueryWrapper<>();
        qw.eq(Post::getStatus, 1)
                .eq(StringUtils.hasText(query.getCategory()), Post::getCategory, query.getCategory())
                .orderByDesc(Post::getIsPinned)
                .orderByDesc(Post::getCreateTime);
        Page<Post> pageResult = this.page(page, qw);

        // 查当前用户已赞/已收藏的帖子ID集合
        Set<Long> likedIds = new java.util.HashSet<>();
        Set<Long> favoritedIds = new java.util.HashSet<>();
        if (StringUtils.hasText(query.getCurrentStudentId())) {
            likedIds = postLikeMapper.selectList(
                    new LambdaQueryWrapper<PostLike>().eq(PostLike::getStudentId, query.getCurrentStudentId())
            ).stream().map(PostLike::getPostId).collect(Collectors.toSet());

            favoritedIds = postFavoriteMapper.selectList(
                    new LambdaQueryWrapper<PostFavorite>().eq(PostFavorite::getStudentId, query.getCurrentStudentId())
            ).stream().map(PostFavorite::getPostId).collect(Collectors.toSet());
        }

        // 查帖子作者的头像（只查一次，缓存到 Map）
        Set<String> authorIds = pageResult.getRecords().stream()
                .map(Post::getStudentId).collect(Collectors.toSet());
        Map<String, String> avatarMap = new java.util.HashMap<>();
        if (!authorIds.isEmpty()) {
            userProfileMapper.selectList(
                    new LambdaQueryWrapper<UserProfile>().in(UserProfile::getStudentId, authorIds)
            ).forEach(u -> avatarMap.put(u.getStudentId(), u.getAvatarUrl()));
        }

        Set<Long> finalLikedIds = likedIds;
        Set<Long> finalFavoritedIds = favoritedIds;
        List<PostVO> postVOList = pageResult.getRecords().stream().map(e -> {
            PostVO vo = new PostVO();
            BeanUtils.copyProperties(e, vo);
            vo.setAuthorAvatar(avatarMap.get(e.getStudentId()));
            if (e.getContent() != null && e.getContent().length() > 10) {
                vo.setContent(e.getContent().substring(0, 10));
            }
            vo.setLiked(finalLikedIds.contains(e.getId()));
            vo.setFavorited(finalFavoritedIds.contains(e.getId()));
            return vo;
        }).collect(Collectors.toList());

        Page<PostVO> result = new Page<>(pageResult.getCurrent(), pageResult.getSize());
        result.setTotal(pageResult.getTotal());
        result.setRecords(postVOList);
        return result;
    }

    @Override
    public Post getPostDetail(Long postId) {
        Post post = this.getById(postId);
        if (post == null) {
            throw new RuntimeException("帖子不存在");
        }
        // 浏览量 +1
        post.setViewCount(post.getViewCount() == null ? 1 : post.getViewCount() + 1);
        this.updateById(post);
        return post;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Post createPost(PostCreateDTO dto, String studentId) {
        // 查用户
        UserProfile author = userProfileMapper.selectOne(
                new LambdaQueryWrapper<UserProfile>().eq(UserProfile::getStudentId, studentId));
        if (author == null) {
            throw new RuntimeException("用户不存在");
        }

        // 公告权限：普通用户不能发 notice
        if ("notice".equals(dto.getCategory()) && author.getRole() != 0) {
            throw new RuntimeException("普通用户不允许发布公告");
        }

        // 敏感词校验
        Map<String, Object> check = sensitiveWordService.checkText(dto.getContent());
        if ((Boolean) check.get("hasSensitive")) {
            throw new RuntimeException("内容包含敏感词：" + check.get("word"));
        }

        // 构建帖子
        Post post = new Post();
        BeanUtils.copyProperties(dto, post);
        post.setStudentId(studentId);
        post.setAuthorName(author.getName());
        post.setAuthorAvatar(author.getAvatarUrl());
        post.setViewCount(0);
        post.setIsPinned(0);
        post.setLikeCount(0);
        post.setCommentCount(0);
        post.setStatus(1);
        post.setFavoriteCount(0);
        this.save(post);
        return post;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deletePost(Long postId, String studentId) {
        Post post = this.getById(postId);
        if (post == null) {
            throw new RuntimeException("帖子不存在");
        }

        UserProfile user = userProfileMapper.selectOne(
                new LambdaQueryWrapper<UserProfile>().eq(UserProfile::getStudentId, studentId));
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }

        // 本人可以删 OR 管理员可以删
        if (post.getStudentId().equals(studentId) || user.getRole() == 0) {
            this.removeById(postId);
        }
    }

    @Override
    public void togglePin(Long postId) {
        Post post = this.getById(postId);
        if (post == null) {
            throw new RuntimeException("帖子不存在");
        }
        post.setIsPinned(post.getIsPinned() == 1 ? 0 : 1);
        this.updateById(post);
    }

    @Override
    public void toggleLike(Long postId, String studentId) {
        PostLike existing = postLikeMapper.selectOne(
                new LambdaQueryWrapper<PostLike>()
                        .eq(PostLike::getPostId, postId)
                        .eq(PostLike::getStudentId, studentId));

        Post post = this.getById(postId);
        if (post == null) {
            throw new RuntimeException("帖子不存在");
        }

        if (existing != null) {
            // 已赞 → 取消
            postLikeMapper.deleteById(existing.getId());
            post.setLikeCount(Math.max(0, post.getLikeCount() - 1));
        } else {
            // 未赞 → 点赞
            PostLike newLike = new PostLike();
            newLike.setPostId(postId);
            newLike.setStudentId(studentId);
            postLikeMapper.insert(newLike);
            post.setLikeCount(post.getLikeCount() + 1);

            // 点赞通知（不给自己发）
            if (!studentId.equals(post.getStudentId())) {
                notificationService.send(
                        post.getStudentId(),
                        "like",
                        postId,
                        "有人点赞了你的帖子",
                        "赞了《" + post.getTitle() + "》"
                );
            }
        }
        this.updateById(post);
    }

    @Override
    public void toggleFavorite(Long postId, String studentId) {
        PostFavorite existing = postFavoriteMapper.selectOne(
                new LambdaQueryWrapper<PostFavorite>()
                        .eq(PostFavorite::getPostId, postId)
                        .eq(PostFavorite::getStudentId, studentId));

        Post post = this.getById(postId);
        if (post == null) {
            throw new RuntimeException("帖子不存在");
        }

        if (existing != null) {
            // 已收藏 → 取消
            postFavoriteMapper.deleteById(existing.getId());
            post.setFavoriteCount(Math.max(0, post.getFavoriteCount() - 1));
        } else {
            // 未收藏 → 收藏
            PostFavorite newFav = new PostFavorite();
            newFav.setPostId(postId);
            newFav.setStudentId(studentId);
            postFavoriteMapper.insert(newFav);
            post.setFavoriteCount(post.getFavoriteCount() + 1);

            // 收藏通知（不给自己发）
            if (!studentId.equals(post.getStudentId())) {
                notificationService.send(
                        post.getStudentId(),
                        "favorite",
                        postId,
                        "有人收藏了你的帖子",
                        "收藏了《" + post.getTitle() + "》"
                );
            }
        }
        this.updateById(post);
    }

    @Override
    public Page<PostVO> getMyFavorites(String studentId, Integer page, Integer size) {
        Page<PostFavorite> pages = new Page<>(page, size);
        Page<PostFavorite> pageResult = postFavoriteMapper.selectPage(pages,
                new LambdaQueryWrapper<PostFavorite>()
                        .eq(PostFavorite::getStudentId, studentId)
                        .orderByDesc(PostFavorite::getCreateTime));

        // 根据收藏记录拿真正的帖子
        List<Long> postIds = pageResult.getRecords().stream()
                .map(PostFavorite::getPostId).collect(Collectors.toList());

        List<Post> posts = postIds.isEmpty() ? java.util.Collections.emptyList()
                : this.listByIds(postIds);

        List<PostVO> postVOList = posts.stream().map(e -> {
            PostVO vo = new PostVO();
            BeanUtils.copyProperties(e, vo);
            if (e.getContent() != null && e.getContent().length() > 10) {
                vo.setContent(e.getContent().substring(0, 10));
            }
            return vo;
        }).collect(Collectors.toList());

        Page<PostVO> result = new Page<>(pageResult.getCurrent(), pageResult.getSize());
        result.setTotal(pageResult.getTotal());
        result.setRecords(postVOList);
        return result;
    }
}
