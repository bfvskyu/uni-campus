package com.ricky.campus.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ricky.campus.common.Result;
import com.ricky.campus.mapper.AnnouncementMapper;
import com.ricky.campus.mapper.ExamInfoMapper;
import com.ricky.campus.mapper.RecruitmentMapper;
import com.ricky.campus.model.dto.SearchResult;
import com.ricky.campus.model.entity.Announcement;
import com.ricky.campus.model.entity.ExamInfo;
import com.ricky.campus.model.entity.Recruitment;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api")
public class SearchController {
    @Resource
    private ExamInfoMapper examInfoMapper;
    @Resource
    private RecruitmentMapper recruitmentMapper;
    @Resource
    private AnnouncementMapper announcementMapper;

    @GetMapping("/search")
    public Result search(@RequestParam String keyword) {
        String kw = "%" + keyword + "%";

        List<ExamInfo> exams = examInfoMapper.selectList(
                new LambdaQueryWrapper<ExamInfo>()
                        .like(ExamInfo::getTitle, kw)
                        .last("LIMIT 10")
        );
        List<Announcement> announcements = announcementMapper.selectList(
                new LambdaQueryWrapper<Announcement>()
                        .like(Announcement::getTitle, kw)
                        .last("LIMIT 10")
        );
        List<Recruitment> recruitments = recruitmentMapper.selectList(
                new LambdaQueryWrapper<Recruitment>()
                        .like(Recruitment::getTitle, kw)
                        .last("LIMIT 10")
        );

        List<SearchResult> result = new ArrayList<>();
        for (var e : exams) result.add(new SearchResult("exam", e.getId(), e.getTitle(), e.getExamDate().toString()));
        for (var a : announcements) result.add(new SearchResult("announcement", a.getId(), a.getTitle(), a.getPublishDate().toString()));
        for (var r : recruitments) result.add(new SearchResult("recruitment", r.getId(), r.getTitle(), r.getShopName()));
        return Result.success(result);
    }
}
