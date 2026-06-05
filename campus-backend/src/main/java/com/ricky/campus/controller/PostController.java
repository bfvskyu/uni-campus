package com.ricky.campus.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ricky.campus.common.Result;
import com.ricky.campus.model.dto.PostCreateDTO;
import com.ricky.campus.model.dto.PostQueryDTO;
import com.ricky.campus.model.entity.Post;
import com.ricky.campus.model.vo.PostVO;
import com.ricky.campus.service.PostService;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/post")
public class PostController {

    @Resource
    private PostService postService;

    @GetMapping("/list")
    public Result<Page<PostVO>> list(PostQueryDTO query) {
        return Result.success(postService.getPostList(query));
    }

    @GetMapping("/{id}")
    public Result<Post> detail(@PathVariable Long id) {
        return Result.success(postService.getPostDetail(id));
    }

    @PostMapping
    public Result<Post> create(@Valid @RequestBody PostCreateDTO dto,
                               @RequestParam String studentId) {
        return Result.success(postService.createPost(dto, studentId));
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id, @RequestParam String studentId) {
        postService.deletePost(id, studentId);
        return Result.success(null);
    }

    @PutMapping("/{id}/pin")
    public Result<Void> togglePin(@PathVariable Long id, @RequestParam String studentId) {
        // 管理员权限校验由前端控制，后端此处信任请求
        postService.togglePin(id);
        return Result.success(null);
    }

    @PostMapping("/{id}/like")
    public Result<Void> toggleLike(@PathVariable Long id, @RequestParam String studentId) {
        postService.toggleLike(id, studentId);
        return Result.success(null);
    }

    @PostMapping("/{id}/favorite")
    public Result<Void> toggleFavorite(@PathVariable Long id, @RequestParam String studentId) {
        postService.toggleFavorite(id, studentId);
        return Result.success(null);
    }

    @GetMapping("/favorites")
    public Result<Page<PostVO>> myFavorites(@RequestParam String studentId,
                                            @RequestParam(defaultValue = "1") Integer page,
                                            @RequestParam(defaultValue = "10") Integer size) {
        return Result.success(postService.getMyFavorites(studentId, page, size));
    }
}
