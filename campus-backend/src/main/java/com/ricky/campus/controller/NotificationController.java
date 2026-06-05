package com.ricky.campus.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ricky.campus.common.Result;
import com.ricky.campus.model.entity.Notification;
import com.ricky.campus.service.NotificationService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/notification")
public class NotificationController {
    @Resource
    private NotificationService notificationService;
    @GetMapping("/list")
    public Result<Page<Notification>> getList(
            @RequestParam String studentId,
            @RequestParam Integer page,
            @RequestParam Integer size
    ){
        return notificationService.getList(studentId,page,size);
    }
    @GetMapping("/unread-count")
    public Result<Long> unread(@RequestParam String studentId){
        return notificationService.getUnreadCount(studentId);
    }
    @PutMapping("/read/{id}")
    public Result<String> markRead(@PathVariable Long id){
        return notificationService.markRead(id);
    }
    @PutMapping("/read-all")
    public  Result<String> markAll(@RequestParam String studentId){
        return notificationService.markAllRead(studentId);
    }
}
