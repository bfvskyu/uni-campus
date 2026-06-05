package com.ricky.campus.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class PostQueryDTO {
    private String category;       // null 或空 = 查全部
    private String currentStudentId; // 当前登录用户，可空
    private Integer page = 1;
    private Integer size = 10;
}
