package com.ricky.campus.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ricky.campus.common.Result;
import com.ricky.campus.mapper.LostFoundMapper;
import com.ricky.campus.mapper.UserProfileMapper;
import com.ricky.campus.model.entity.LostFound;
import com.ricky.campus.model.entity.UserProfile;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.UUID;

@RestController
@RequestMapping("/api/lost-found")
public class LostFoundController {

    @Resource
    private LostFoundMapper lostFoundMapper;
    @Resource
    private UserProfileMapper userProfileMapper;

    @GetMapping
    public Result<Page<LostFound>> list(@RequestParam(required = false) String type,
                                        @RequestParam(defaultValue = "1") int page,
                                        @RequestParam(defaultValue = "10") int size) {
        LambdaQueryWrapper<LostFound> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(LostFound::getStatus, 1);
        if (type != null) wrapper.eq(LostFound::getType, type);
        wrapper.orderByDesc(LostFound::getCreateTime);
        return Result.success(lostFoundMapper.selectPage(new Page<>(page, size), wrapper));
    }

    @GetMapping("/{id}")
    public Result<LostFound> detail(@PathVariable Long id) {
        LostFound item = lostFoundMapper.selectById(id);
        if (item == null || item.getStatus() == 0) return Result.error("信息不存在");
        return Result.success(item);
    }

    @PostMapping
    public Result<Void> add(@RequestBody LostFound input) {
        LambdaQueryWrapper<UserProfile> qw = new LambdaQueryWrapper<>();
        qw.eq(UserProfile::getStudentId, input.getStudentId());
        UserProfile user = userProfileMapper.selectOne(qw);
        if (user == null) return Result.error("学号不存在，请确认学号是否正确");
        input.setAuthorName(user.getName());
        input.setAuthorAvatar(user.getAvatarUrl());
        input.setStatus(1);
        lostFoundMapper.insert(input);
        return Result.success(null);
    }

    @PutMapping("/{id}")
    public Result<Void> update(@PathVariable Long id, @RequestBody LostFound input) {
        LostFound existing = lostFoundMapper.selectById(id);
        if (existing == null || existing.getStatus() == 0) return Result.error("信息不存在");
        if (!existing.getStudentId().equals(input.getStudentId())) return Result.error("只有发帖人才能编辑");
        input.setId(id);
        input.setAuthorName(existing.getAuthorName());
        input.setAuthorAvatar(existing.getAuthorAvatar());
        input.setStatus(1);
        lostFoundMapper.updateById(input);
        return Result.success(null);
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id, @RequestParam String studentId) {
        LostFound existing = lostFoundMapper.selectById(id);
        if (existing == null || existing.getStatus() == 0) return Result.error("信息不存在");
        if (!existing.getStudentId().equals(studentId)) return Result.error("只有发帖人才能删除");
        existing.setStatus(0);
        lostFoundMapper.updateById(existing);
        return Result.success(null);
    }

    @PostMapping("/upload")
    public Result<String> upload(@RequestParam("file") MultipartFile file) {
        try {
            if (file.isEmpty()) return Result.error("文件为空");
            String baseDir = System.getProperty("user.dir");
            File dir = new File(baseDir, "uploads/lost-found");
            if (!dir.exists()) dir.mkdirs();
            String suffix = file.getOriginalFilename();
            suffix = suffix == null ? ".jpg" : suffix.substring(suffix.lastIndexOf("."));
            String fileName = UUID.randomUUID() + suffix;
            file.transferTo(new File(dir, fileName));
            return Result.success("/uploads/lost-found/" + fileName);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("上传失败: " + e.getMessage());
        }
    }
}
