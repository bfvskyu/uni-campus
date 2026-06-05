package com.ricky.campus.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ricky.campus.common.Result;
import com.ricky.campus.mapper.UserProfileMapper;
import com.ricky.campus.model.entity.UserProfile;
import com.ricky.campus.util.PasswordUtil;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Resource
    private UserProfileMapper userProfileMapper;

    @PostMapping("/login")
    public Result<UserProfile> login(@RequestBody Map<String, String> body) {
        String studentId = body.get("studentId");
        String password = body.get("password");
        if (studentId == null || password == null) return Result.error("学号和密码不能为空");

        LambdaQueryWrapper<UserProfile> qw = new LambdaQueryWrapper<>();
        qw.eq(UserProfile::getStudentId, studentId);
        UserProfile user = userProfileMapper.selectOne(qw);
        if (user == null) return Result.error("学号不存在");

        if (user.getNeedChangePwd() == 1) {
            if (!password.equals(PasswordUtil.defaultPassword(studentId)))
                return Result.error("密码错误");
        } else {
            if (!PasswordUtil.matches(password, user.getPassword()))
                return Result.error("密码错误");
        }

        user.setPassword(null);
        return Result.success(user);
    }

    @PutMapping("/password")
    public Result<Void> changePassword(@RequestBody Map<String, String> body) {
        String studentId = body.get("studentId");
        String oldPassword = body.get("oldPassword");
        String newPassword = body.get("newPassword");
        if (studentId == null || oldPassword == null || newPassword == null)
            return Result.error("参数不完整");

        LambdaQueryWrapper<UserProfile> qw = new LambdaQueryWrapper<>();
        qw.eq(UserProfile::getStudentId, studentId);
        UserProfile user = userProfileMapper.selectOne(qw);
        if (user == null) return Result.error("学号不存在");

        if (user.getNeedChangePwd() == 1) {
            if (!oldPassword.equals(PasswordUtil.defaultPassword(studentId)))
                return Result.error("旧密码错误");
        } else {
            if (!PasswordUtil.matches(oldPassword, user.getPassword()))
                return Result.error("旧密码错误");
        }

        user.setPassword(PasswordUtil.hash(newPassword));
        user.setNeedChangePwd(0);
        userProfileMapper.updateById(user);
        return Result.success(null);
    }

    @GetMapping("/profile")
    public Result<UserProfile> profile(@RequestParam(required = false) String studentId) {
        if (studentId != null) {
            LambdaQueryWrapper<UserProfile> qw = new LambdaQueryWrapper<>();
            qw.eq(UserProfile::getStudentId, studentId);
            UserProfile user = userProfileMapper.selectOne(qw);
            if (user == null) return Result.error("用户不存在");
            user.setPassword(null);
            return Result.success(user);
        }
        // fallback
        UserProfile user = userProfileMapper.selectById(1L);
        if (user == null) {
            user = new UserProfile();
            user.setName("用户");
            userProfileMapper.insert(user);
        }
        return Result.success(user);
    }

    @PutMapping("/profile")
    public Result<UserProfile> updateProfile(@RequestBody UserProfile input) {
        if (input.getStudentId() == null) return Result.error("学号不能为空");
        LambdaQueryWrapper<UserProfile> qw = new LambdaQueryWrapper<>();
        qw.eq(UserProfile::getStudentId, input.getStudentId());
        UserProfile user = userProfileMapper.selectOne(qw);
        if (user == null) return Result.error("用户不存在");
        user.setName(input.getName());
        user.setMajor(input.getMajor());
        user.setPhone(input.getPhone());
        userProfileMapper.updateById(user);
        return Result.success(user);
    }

    @PostMapping("/avatar")
    public Result<String> uploadAvatar(@RequestParam("file") MultipartFile file,
                                       @RequestParam(required = false) String studentId) {
        try {
            if (file.isEmpty()) return Result.error("文件为空");

            String baseDir = System.getProperty("user.dir");
            File dir = new File(baseDir, "uploads/avatars");
            if (!dir.exists()) dir.mkdirs();

            String suffix = file.getOriginalFilename();
            suffix = suffix == null ? ".jpg" : suffix.substring(suffix.lastIndexOf("."));
            String fileName = UUID.randomUUID() + suffix;
            File dest = new File(dir, fileName);
            file.transferTo(dest);
            System.out.println("头像已保存到: " + dest.getAbsolutePath());

            String avatarUrl = "/uploads/avatars/" + fileName;

            LambdaQueryWrapper<UserProfile> qw = new LambdaQueryWrapper<>();
            qw.eq(UserProfile::getStudentId, studentId);
            UserProfile user = studentId != null ? userProfileMapper.selectOne(qw) : userProfileMapper.selectById(1L);
            if (user != null) {
                // 删除旧头像
                if (user.getAvatarUrl() != null) {
                    File oldFile = new File(baseDir, user.getAvatarUrl());
                    if (oldFile.exists()) oldFile.delete();
                }
                user.setAvatarUrl(avatarUrl);
                userProfileMapper.updateById(user);
            }
            return Result.success(avatarUrl);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("上传失败: " + e.getMessage());
        }
    }

    @PostMapping("/verify-phone")
    public Result<Void> verifyPhone(@RequestBody Map<String, String> body) {
        String studentId = body.get("studentId");
        String phone = body.get("phone");
        if (studentId == null || phone == null) return Result.error("参数不完整");

        LambdaQueryWrapper<UserProfile> qw = new LambdaQueryWrapper<>();
        qw.eq(UserProfile::getStudentId, studentId);
        UserProfile user = userProfileMapper.selectOne(qw);
        if (user == null) return Result.error("学号不存在");
        if (!phone.equals(user.getPhone())) return Result.error("手机号不匹配");
        return Result.success(null);
    }
}
