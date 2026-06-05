package com.ricky.campus.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class PostCreateDTO {

    @NotBlank(message = "分类不能为空")
    @Pattern(regexp = "^(notice|lost_found|discuss|help)$", message = "分类无效")
    private String category;

    @NotBlank(message = "标题不能为空")
    @Size(max = 200, message = "标题最长200字")
    private String title;

    @NotBlank(message = "内容不能为空")
    private String content;

    @Size(max = 500, message = "联系方式最长500字")
    private String phone;

    private String images;
}
