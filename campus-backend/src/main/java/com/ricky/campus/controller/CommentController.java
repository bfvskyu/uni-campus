package com.ricky.campus.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ricky.campus.common.Result;
import com.ricky.campus.model.dto.CommentCreateDTO;
import com.ricky.campus.model.entity.Comment;
import com.ricky.campus.model.vo.CommentVO;
import com.ricky.campus.service.CommentService;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/comment")
public class CommentController {

    @Resource
    private CommentService commentService;

    @GetMapping("/list")
    public Result<Page<CommentVO>> list(@RequestParam Long postId,
                                        @RequestParam(defaultValue = "1") Integer page,
                                        @RequestParam(defaultValue = "20") Integer size) {
        return Result.success(commentService.getCommentList(postId, page, size));
    }

    @PostMapping
    public Result<Comment> create(@Valid @RequestBody CommentCreateDTO dto,
                                  @RequestParam String studentId) {
        return Result.success(commentService.createComment(dto, studentId));
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id, @RequestParam String studentId) {
        commentService.deleteComment(id, studentId);
        return Result.success(null);
    }
}
