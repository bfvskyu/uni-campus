package com.ricky.campus.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ricky.campus.common.Result;
import com.ricky.campus.mapper.AnnouncementMapper;
import com.ricky.campus.mapper.NotificationMapper;
import com.ricky.campus.mapper.UserProfileMapper;
import com.ricky.campus.model.entity.AdminKey;
import com.ricky.campus.model.entity.Announcement;
import com.ricky.campus.model.entity.Notification;
import com.ricky.campus.model.entity.UserProfile;
import com.ricky.campus.service.NotificationService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class NotificationServiceImpl extends ServiceImpl<NotificationMapper, Notification> implements NotificationService {
    @Resource
    private NotificationMapper notificationMapper;
    @Resource
    private AnnouncementMapper announcementMapper;
    @Resource
    private UserProfileMapper userProfileMapper;
    public Result<String> createForAnnouncement(Announcement announcement){
        LambdaQueryWrapper<UserProfile> wrapper = new LambdaQueryWrapper<>();
        List<Notification> notifications = new ArrayList<>();
        for (UserProfile userProfile : userProfileMapper.selectList(wrapper)) {
            String studentId = userProfile.getStudentId();
            Notification notification = new Notification();
            notification.setStudentId(studentId);
            notification.setType("announcement");
            notification.setRefId(announcement.getId());
            notification.setTitle(announcement.getTitle());
            notification.setIsRead(0);
            notification.setContent(announcement.getContent().length() > 10 ? announcement.getContent().substring(0,10) : announcement.getContent());
            notifications.add(notification);
        }
        this.saveBatch(notifications);
        return Result.success("批量添加成功");
    }

    @Override
    public Result<Long> getUnreadCount(String studentId) {
        LambdaQueryWrapper<Notification> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Notification::getStudentId, studentId)
                .eq(Notification::getIsRead, 0);
        Long len = notificationMapper.selectCount(wrapper);
        return Result.success(len);
    }

    public Result<Page<Notification>> getList(String studentId,Integer page,Integer size){
        LambdaQueryWrapper<Notification> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Notification::getStudentId, studentId)
                .orderByDesc(Notification::getCreateTime);
        return Result.success(notificationMapper.selectPage(new Page<>(page,size), wrapper));
    }
    public Result<String> markRead(Long notificationId){
        LambdaUpdateWrapper <Notification> wrapper = new LambdaUpdateWrapper<>();
        wrapper.set(Notification::getIsRead, 1).eq(Notification::getId, notificationId);
        notificationMapper.update(wrapper);
        return Result.success("已读");
    }

    @Override
    public Result<String> markAllRead(String studentId) {
        LambdaUpdateWrapper<Notification> wrapper = new LambdaUpdateWrapper<>();
        wrapper.set(Notification::getIsRead, 1).eq(Notification::getStudentId, studentId);
        notificationMapper.update(wrapper);
        return Result.success("全部已读");
    }

    @Override
    public void send(String studentId, String type, Long refId, String title, String content) {
        Notification notification = new Notification();
        notification.setStudentId(studentId);
        notification.setType(type);
        notification.setRefId(refId);
        notification.setTitle(title);
        // 截取前20字作为预览
        notification.setContent(content.length() > 20 ? content.substring(0, 20) : content);
        notification.setIsRead(0);
        this.save(notification);
    }
}
