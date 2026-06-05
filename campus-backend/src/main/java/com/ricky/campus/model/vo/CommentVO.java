package com.ricky.campus.model.vo;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class CommentVO {
    private Long id;
    private Long postId;
    private String studentId;
    private String authorName;
    private String authorAvatar;
    private String content;
    private Integer status;
    private LocalDateTime createTime;
}
