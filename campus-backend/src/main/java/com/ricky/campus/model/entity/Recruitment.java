package com.ricky.campus.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@TableName("recruitment")
public class Recruitment {
    @TableId(type = IdType.AUTO)
    private Long id;

    @NotBlank(message = "类型不能为空")
    private String type;      // shop / delivery

    @NotBlank(message = "标题不能为空")
    private String title;

    private String shopName;

    @NotBlank(message = "薪资不能为空")
    private String salary;

    private String tags;

    @NotBlank(message = "职位描述不能为空")
    private String description;

    @NotBlank(message = "任职要求不能为空")
    private String requirements;

    @NotBlank(message = "工作地点不能为空")
    private String location;

    @NotBlank(message = "联系方式不能为空")
    private String contact;

    private LocalDate publishDate;
    private Integer status;

    @Version
    private Integer version;
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}
