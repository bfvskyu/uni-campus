package com.ricky.campus.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ricky.campus.common.Result;
import com.ricky.campus.model.entity.Announcement;
import com.ricky.campus.model.entity.Notification;

import java.util.List;

public interface NotificationService extends IService<Notification> {
    Result<String> createForAnnouncement(Announcement announcement);
    Result<Long> getUnreadCount(String studentId);
    Result<Page<Notification>> getList(String studentId, Integer page, Integer size);
    Result<String> markRead(Long notificationId);
    Result<String> markAllRead(String studentId);

    /**
     * 发送单条通知
     * @param studentId 接收人学号
     * @param type 类型：like / comment / favorite / announcement
     * @param refId 关联的帖子ID
     * @param title 通知标题
     * @param content 通知内容预览
     */
    void send(String studentId, String type, Long refId, String title, String content);
}
