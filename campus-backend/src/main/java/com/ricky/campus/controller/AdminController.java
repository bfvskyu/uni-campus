package com.ricky.campus.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ricky.campus.common.Result;
import com.ricky.campus.mapper.UserProfileMapper;
import com.ricky.campus.model.entity.UserProfile;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @Resource
    private UserProfileMapper userProfileMapper;

    @PostMapping("/check-role")
    public Result<Map<String, Object>> checkRole(@RequestBody Map<String, String> body) {
        String studentId = body.get("studentId");
        Map<String, Object> result = new HashMap<>();
        result.put("isAdmin", false);

        if (studentId == null) return Result.success(result);

        LambdaQueryWrapper<UserProfile> qw = new LambdaQueryWrapper<>();
        qw.eq(UserProfile::getStudentId, studentId);
        UserProfile user = userProfileMapper.selectOne(qw);
        if (user != null && user.getRole() != null && user.getRole() == 0) {
            result.put("isAdmin", true);
        }
        return Result.success(result);
    }
}
