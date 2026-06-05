package com.ricky.campus.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ricky.campus.common.Result;
import com.ricky.campus.mapper.UserProfileMapper;
import com.ricky.campus.model.dto.SensitiveWordDTO;
import com.ricky.campus.model.entity.SensitiveWord;
import com.ricky.campus.model.entity.UserProfile;
import com.ricky.campus.service.SensitiveWordService;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api")
public class SensitiveWordController {

    @Resource
    private SensitiveWordService sensitiveWordService;

    @Resource
    private UserProfileMapper userProfileMapper;

    /**
     * 敏感词列表（管理员）
     */
    @GetMapping("/admin/sensitive-word/list")
    public Result<Page<SensitiveWord>> list(@RequestParam(defaultValue = "1") Integer page,
                                            @RequestParam(defaultValue = "20") Integer size) {
        return Result.success(sensitiveWordService.page(new Page<>(page, size)));
    }

    /**
     * 新增敏感词（管理员）
     */
    @PostMapping("/admin/sensitive-word")
    public Result<Void> add(@Valid @RequestBody SensitiveWordDTO dto,
                            @RequestParam String studentId) {
        checkAdmin(studentId);
        sensitiveWordService.addWord(dto.getWord());
        return Result.success(null);
    }

    /**
     * 删除敏感词（管理员）
     */
    @DeleteMapping("/admin/sensitive-word/{id}")
    public Result<Void> delete(@PathVariable Long id, @RequestParam String studentId) {
        checkAdmin(studentId);
        sensitiveWordService.deleteWord(id);
        return Result.success(null);
    }

    /**
     * 校验文本是否含敏感词（公开）
     */
    @PostMapping("/sensitive-word/check")
    public Result<Map<String, Object>> check(@RequestBody Map<String, String> body) {
        String text = body.get("text");
        return Result.success(sensitiveWordService.checkText(text));
    }

    /**
     * 管理员权限校验
     */
    private void checkAdmin(String studentId) {
        UserProfile user = userProfileMapper.selectOne(
                new LambdaQueryWrapper<UserProfile>().eq(UserProfile::getStudentId, studentId));
        if (user == null || user.getRole() != 0) {
            throw new RuntimeException("无管理员权限");
        }
    }
}
