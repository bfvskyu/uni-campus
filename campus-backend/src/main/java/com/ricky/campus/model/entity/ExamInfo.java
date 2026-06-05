package com.ricky.campus.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@TableName("exam_info")
public class ExamInfo {
    @TableId(type = IdType.AUTO)
    private Long id;

    @NotBlank(message = "标题不能为空")
    private String title;

    @NotBlank(message = "类型不能为空")
    private String type;       // 考试 / 竞赛

    @NotNull(message = "考试日期不能为空")
    private LocalDate examDate;

    private LocalDate deadline;

    @NotBlank(message = "状态不能为空")
    private String status;     // 报名中 / 即将开始 / 未开始

    private String tags;
    private String detail;
    private String websiteUrl;

    @Version
    private Integer version;
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}
