package com.ricky.campus.controller;

import com.ricky.campus.common.Result;
import com.ricky.campus.mapper.FeedbackMapper;
import com.ricky.campus.model.entity.Feedback;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/feedback")
public class FeedbackController {

    @Resource
    private FeedbackMapper feedbackMapper;

    @PostMapping
    public Result<Void> submit(@RequestBody Feedback feedback) {
        feedback.setStatus(0);
        feedbackMapper.insert(feedback);
        return Result.success(null);
    }

    @GetMapping
    public Result<List<Feedback>> list() {
        return Result.success(feedbackMapper.selectList(
                new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<Feedback>()
                        .orderByDesc(Feedback::getCreateTime)));
    }

    @PutMapping("/{id}/status")
    public Result<Void> toggleStatus(@PathVariable Long id) {
        Feedback fb = feedbackMapper.selectById(id);
        if (fb == null) return Result.error("反馈不存在");
        fb.setStatus(fb.getStatus() == 1 ? 0 : 1);
        feedbackMapper.updateById(fb);
        return Result.success(null);
    }
}
