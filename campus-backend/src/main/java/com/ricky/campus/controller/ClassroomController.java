package com.ricky.campus.controller;

import com.ricky.campus.common.Result;
import com.ricky.campus.model.entity.Classroom;
import com.ricky.campus.model.entity.ClassroomSchedule;
import com.ricky.campus.mapper.ClassroomMapper;
import com.ricky.campus.mapper.ClassroomScheduleMapper;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/classroom")
public class ClassroomController {
    @Resource
    private ClassroomMapper classroomMapper;
    @Resource
    private ClassroomScheduleMapper scheduleMapper;

    // 建筑列表
    @GetMapping("/buildings")
    public Result<List<String>> buildings() {
        List<Classroom> list = classroomMapper.selectList(null);
        List<String> buildings = list.stream()
                .map(Classroom::getBuilding).distinct().sorted().collect(Collectors.toList());
        return Result.success(buildings);
    }

    // 某建筑的教室
    @GetMapping
    public Result<List<Classroom>> rooms(@RequestParam String building) {
        return Result.success(classroomMapper.selectList(
                new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<Classroom>()
                        .eq(Classroom::getBuilding, building)));
    }

    // 全部占用数据（前端按需过滤）
    @GetMapping("/schedule")
    public Result<Map<String, Object>> schedule() {
        List<Classroom> rooms = classroomMapper.selectList(null);
        List<ClassroomSchedule> schedules = scheduleMapper.selectList(null);
        Map<String, Object> map = new HashMap<>();
        map.put("rooms", rooms);
        map.put("schedules", schedules);
        return Result.success(map);
    }
}
