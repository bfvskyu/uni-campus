package com.ricky.campus.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("memo")
public class Memo {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String refType;
    private Long refId;
    private String title;
    private String studentId;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}
