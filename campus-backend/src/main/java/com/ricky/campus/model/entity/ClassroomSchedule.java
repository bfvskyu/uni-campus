package com.ricky.campus.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@TableName("classroom_schedule")
public class ClassroomSchedule {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String roomId;
    private Integer slotId;
    private Integer occupied;   // 0-空闲 1-占用
    private String courseName;
    private String teacher;
    private LocalDate scheduleDate;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}
