package com.ricky.campus.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ricky.campus.common.Result;
import com.ricky.campus.mapper.MemoMapper;
import com.ricky.campus.model.entity.Memo;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/memo")
public class MemoController {

    @Resource
    private MemoMapper memoMapper;

    @GetMapping("/list")
    public Result<List<Memo>> list(@RequestParam String studentId) {
        LambdaQueryWrapper<Memo> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Memo::getStudentId, studentId);
        wrapper.orderByDesc(Memo::getCreateTime);
        return Result.success(memoMapper.selectList(wrapper));
    }

    @GetMapping("/check")
    public Result<Memo> check(@RequestParam String refType, @RequestParam Long refId, @RequestParam String studentId) {
        LambdaQueryWrapper<Memo> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Memo::getRefType, refType).eq(Memo::getRefId, refId).eq(Memo::getStudentId, studentId);
        return Result.success(memoMapper.selectOne(wrapper));
    }

    @PostMapping
    public Result<Memo> add(@RequestBody Memo memo) {
        memo.setId(null);
        memoMapper.insert(memo);
        return Result.success(memo);
    }

    @DeleteMapping("/{id}")
    public Result<?> remove(@PathVariable Long id) {
        memoMapper.deleteById(id);
        return Result.success(null);
    }

    @PostMapping("/batch-delete")
    public Result<?> batchDelete(@RequestBody Map<String, List<Long>> body) {
        List<Long> ids = body.get("ids");
        if (ids != null && !ids.isEmpty()) {
            memoMapper.deleteBatchIds(ids);
        }
        return Result.success(null);
    }
}
