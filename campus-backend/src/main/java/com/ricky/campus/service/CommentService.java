package com.ricky.campus.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ricky.campus.model.dto.CommentCreateDTO;
import com.ricky.campus.model.entity.Comment;
import com.ricky.campus.model.vo.CommentVO;

public interface CommentService extends IService<Comment> {

    /**
     * 评论列表（按时间正序）
     */
    Page<CommentVO> getCommentList(Long postId, Integer page, Integer size);

    /**
     * 发表评论（校验敏感词）
     */
    Comment createComment(CommentCreateDTO commentCreateDTO,String studentId);

    /**
     * 删评论（本人或管理员）
     */
    void deleteComment(Long commentId, String studentId);
}
