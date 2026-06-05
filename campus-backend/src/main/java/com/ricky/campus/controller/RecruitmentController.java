package com.ricky.campus.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ricky.campus.common.Result;
import com.ricky.campus.model.entity.Recruitment;
import com.ricky.campus.mapper.RecruitmentMapper;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/recruitment")
public class RecruitmentController {
    @Resource
    private RecruitmentMapper recruitmentMapper;

    // GET /api/recruitment?type=shop  |  delivery
    @GetMapping
    public Result<List<Recruitment>> list(@RequestParam(required = false) String type) {
        LambdaQueryWrapper<Recruitment> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Recruitment::getStatus, 1);
        if (type != null) wrapper.eq(Recruitment::getType, type);
        wrapper.orderByDesc(Recruitment::getPublishDate);
        return Result.success(recruitmentMapper.selectList(wrapper));
    }

    @GetMapping("/{id}")
    public Result<Recruitment> detail(@PathVariable Long id) {
        Recruitment r = recruitmentMapper.selectById(id);
        if (r == null) return Result.error("招聘信息不存在");
        return Result.success(r);
    }

    @PostMapping
    public Result<Void> add(@RequestBody Recruitment recruitment) {
        recruitment.setPublishDate(java.time.LocalDate.now());
        recruitment.setStatus(1);
        recruitmentMapper.insert(recruitment);
        return Result.success(null);
    }

    @PutMapping("/{id}")
    public Result<Void> update(@PathVariable Long id, @RequestBody Recruitment recruitment) {
        Recruitment existing = recruitmentMapper.selectById(id);
        if (existing == null) return Result.error("招聘信息不存在");
        recruitment.setId(id);
        recruitmentMapper.updateById(recruitment);
        return Result.success(null);
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        Recruitment existing = recruitmentMapper.selectById(id);
        if (existing == null) return Result.error("招聘信息不存在");
        existing.setStatus(0);
        recruitmentMapper.updateById(existing);
        return Result.success(null);
    }
}
