package com.ricky.campus.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("post")
public class Post {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String category;
    private String title;
    private String content;
    private String studentId;
    private String authorName;
    private String authorAvatar;
    private String phone;
    private String images;
    private Integer isPinned;
    private Integer status;
    private Integer likeCount;
    private Integer commentCount;
    private Integer viewCount;
    private Integer favoriteCount;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}
