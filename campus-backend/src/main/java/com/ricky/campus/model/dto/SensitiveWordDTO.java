package com.ricky.campus.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class SensitiveWordDTO {

    @NotBlank(message = "敏感词不能为空")
    @Size(max = 100, message = "敏感词最长100字")
    private String word;
}
