package com.ricky.campus.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ricky.campus.common.Result;
import com.ricky.campus.mapper.NotificationMapper;
import com.ricky.campus.model.entity.Announcement;
import com.ricky.campus.mapper.AnnouncementMapper;
import com.ricky.campus.service.NotificationService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/announcement")
public class AnnouncementController {
    @Resource
    private AnnouncementMapper announcementMapper;
    @Resource
    private NotificationService notificationService;
    @GetMapping
    public Result<List<Announcement>> list() {
        LambdaQueryWrapper<Announcement> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Announcement::getStatus, 1).orderByDesc(Announcement::getPublishDate);
        return Result.success(announcementMapper.selectList(wrapper));
    }

    @GetMapping("/{id}")
    public Result<Announcement> detail(@PathVariable Long id) {
        Announcement a = announcementMapper.selectById(id);
        if (a == null) return Result.error("公告不存在");
        return Result.success(a);
    }

    @PostMapping
    public Result<Void> add(@RequestBody Announcement announcement) {
        announcement.setPublishDate(java.time.LocalDate.now());
        announcement.setStatus(1);
        announcementMapper.insert(announcement);
        notificationService.createForAnnouncement(announcement);
        return Result.success(null);
    }

    @PutMapping("/{id}")
    public Result<Void> update(@PathVariable Long id, @RequestBody Announcement announcement) {
        Announcement existing = announcementMapper.selectById(id);
        if (existing == null) return Result.error("公告不存在");
        announcement.setId(id);
        announcementMapper.updateById(announcement);
        return Result.success(null);
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        Announcement existing = announcementMapper.selectById(id);
        if (existing == null) return Result.error("公告不存在");
        existing.setStatus(0);
        announcementMapper.updateById(existing);
        return Result.success(null);
    }

}
