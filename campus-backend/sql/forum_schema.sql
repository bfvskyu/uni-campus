-- ======== 校园论坛 Phase 1 — 5 张新表 ========
USE campus_service;

-- 帖子表
CREATE TABLE IF NOT EXISTS post (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    category VARCHAR(20) NOT NULL COMMENT '分类: notice/lost/found/discuss/exam',
    title VARCHAR(200) NOT NULL COMMENT '帖子标题',
    content TEXT COMMENT '正文',
    student_id VARCHAR(30) NOT NULL DEFAULT '' COMMENT '发帖人学号',
    author_name VARCHAR(50) NOT NULL DEFAULT '' COMMENT '冗余姓名',
    author_avatar VARCHAR(255) NOT NULL DEFAULT '' COMMENT '冗余头像',
    phone VARCHAR(20) NOT NULL DEFAULT '' COMMENT '联系方式(仅失物类)',
    images VARCHAR(500) NOT NULL DEFAULT '' COMMENT '图片JSON(仅失物类)',
    is_pinned TINYINT DEFAULT 0 COMMENT '1-置顶 0-普通',
    status TINYINT DEFAULT 1 COMMENT '1-正常 0-已删除',
    like_count INT DEFAULT 0 COMMENT '点赞数',
    comment_count INT DEFAULT 0 COMMENT '评论数',
    create_time DATETIME,
    update_time DATETIME,
    INDEX idx_post_category (category),
    INDEX idx_post_student (student_id),
    INDEX idx_post_status (status, create_time)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='论坛帖子';

-- 评论表
CREATE TABLE IF NOT EXISTS comment (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    post_id BIGINT NOT NULL COMMENT '关联帖子',
    student_id VARCHAR(30) NOT NULL DEFAULT '' COMMENT '评论人学号',
    author_name VARCHAR(50) NOT NULL DEFAULT '' COMMENT '冗余姓名',
    content TEXT NOT NULL COMMENT '评论内容',
    status TINYINT DEFAULT 1 COMMENT '1-正常 0-已删除',
    create_time DATETIME,
    INDEX idx_comment_post (post_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='评论';

-- 点赞表
CREATE TABLE IF NOT EXISTS post_like (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    post_id BIGINT NOT NULL COMMENT '帖子ID',
    student_id VARCHAR(30) NOT NULL DEFAULT '' COMMENT '点赞人学号',
    create_time DATETIME,
    UNIQUE KEY uk_like (post_id, student_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='点赞';

-- 收藏表
CREATE TABLE IF NOT EXISTS post_favorite (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    post_id BIGINT NOT NULL COMMENT '帖子ID',
    student_id VARCHAR(30) NOT NULL DEFAULT '' COMMENT '收藏人学号',
    create_time DATETIME,
    UNIQUE KEY uk_favorite (post_id, student_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='收藏';

-- 敏感词表
CREATE TABLE IF NOT EXISTS sensitive_word (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    word VARCHAR(100) NOT NULL COMMENT '敏感词',
    create_time DATETIME
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='敏感词';

-- 测试敏感词
INSERT INTO sensitive_word (word) VALUES ('test'), ('敏感词');
