package com.ricky.campus.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("lost_found")
public class LostFound {
    @TableId(type = IdType.AUTO)
    private Long id;

    private String type;       // lost-寻物 / found-拾物
    private String title;
    private String description;
    private String images;     // JSON数组 ["url1","url2"]
    private String phone;
    private String wechat;
    private String studentId;
    private String authorName;
    private String authorAvatar;
    private Integer status;    // 1-有效 0-已删除

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}
