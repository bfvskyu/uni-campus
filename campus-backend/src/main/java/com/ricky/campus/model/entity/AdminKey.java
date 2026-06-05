package com.ricky.campus.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("admin_key")
public class AdminKey {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String secretKey;
    private String remark;
    private Integer status;
    private LocalDateTime createTime;
    private LocalDateTime expireTime;
}
