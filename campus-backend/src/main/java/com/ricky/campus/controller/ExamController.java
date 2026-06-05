package com.ricky.campus.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ricky.campus.common.Result;
import com.ricky.campus.model.entity.ExamInfo;
import com.ricky.campus.mapper.ExamInfoMapper;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/exam")
public class ExamController {
    @Resource
    private ExamInfoMapper examInfoMapper;

    @GetMapping
    public Result<List<ExamInfo>> list(@RequestParam(required = false) String type) {
        LambdaQueryWrapper<ExamInfo> wrapper = new LambdaQueryWrapper<>();
        if (type != null) wrapper.eq(ExamInfo::getType, type);
        wrapper.orderByDesc(ExamInfo::getExamDate);
        return Result.success(examInfoMapper.selectList(wrapper));
    }

    @GetMapping("/{id}")
    public Result<ExamInfo> detail(@PathVariable Long id) {
        ExamInfo e = examInfoMapper.selectById(id);
        if (e == null) return Result.error("信息不存在");
        return Result.success(e);
    }

    // ===== TODO: 由用户实现考试增删改 =====
    // POST /api/exam — 新增考试
    @PostMapping
    public Result<ExamInfo> save(@RequestBody @Valid ExamInfo examInfo) {
        examInfoMapper.insert(examInfo);
        return Result.success(examInfo);
    }
    // PUT /api/exam/{id} — 修改考试
    @PutMapping("/api/exam/{id}")
    public Result<String> update(@PathVariable Long id ,@RequestBody @Valid ExamInfo examInfo) {
        ExamInfo existing = examInfoMapper.selectById(id);
        if (existing == null) {
            return Result.error("考试信息不存在");
        }
        existing.setId(id);
        examInfoMapper.updateById(examInfo);
        return Result.success("修改考试信息成功");
    }
    // DELETE /api/exam/{id} — 删除考试
    @DeleteMapping("/api/exam/{id}")
    public Result<String> delete(@PathVariable Long id) {
        ExamInfo existing = examInfoMapper.selectById(id);
        if (existing == null) {
            return Result.error("考试信息不存在");
        }
        examInfoMapper.deleteById(id);
        return Result.success("删除考试信息成功");
    }
}
