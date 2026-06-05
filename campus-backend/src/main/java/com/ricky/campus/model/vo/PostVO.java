package com.ricky.campus.model.vo;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class PostVO {
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
    private LocalDateTime createTime;
    private LocalDateTime updateTime;

    private boolean liked;
    private boolean favorited;
}
