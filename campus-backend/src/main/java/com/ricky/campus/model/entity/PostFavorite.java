package com.ricky.campus.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("post_favorite")
public class PostFavorite {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long postId;
    private String studentId;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
}
