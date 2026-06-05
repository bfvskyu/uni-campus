-- 创建数据库
CREATE DATABASE IF NOT EXISTS campus_service DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE campus_service;

-- 招聘信息（店铺 / 外送）
CREATE TABLE recruitment (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    type VARCHAR(20) NOT NULL COMMENT 'shop-店铺招聘, delivery-外送兼职',
    title VARCHAR(100) NOT NULL COMMENT '职位名称',
    shop_name VARCHAR(100) COMMENT '店铺/公司名称',
    salary VARCHAR(50) COMMENT '薪资待遇',
    tags VARCHAR(200) COMMENT '标签，逗号分隔',
    description TEXT COMMENT '职位描述',
    requirements TEXT COMMENT '任职要求',
    location VARCHAR(200) COMMENT '工作地点',
    contact VARCHAR(200) COMMENT '联系方式',
    publish_date DATE COMMENT '发布日期',
    status TINYINT DEFAULT 1 COMMENT '0-下架 1-上架',
    create_time DATETIME,
    update_time DATETIME,
    version INT DEFAULT 0 COMMENT '乐观锁'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='招聘信息';

-- 校园公告
CREATE TABLE announcement (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(200) NOT NULL COMMENT '公告标题',
    category VARCHAR(50) COMMENT '分类',
    content TEXT COMMENT '正文',
    publisher VARCHAR(50) COMMENT '发布单位',
    publish_date DATE COMMENT '发布日期',
    status TINYINT DEFAULT 1 COMMENT '0-草稿 1-发布',
    create_time DATETIME,
    update_time DATETIME,
    version INT DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='校园公告';

-- 教室
CREATE TABLE classroom (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    room_id VARCHAR(20) NOT NULL COMMENT '教室编号 A101',
    building VARCHAR(50) NOT NULL COMMENT '所属建筑',
    capacity INT COMMENT '容量',
    type VARCHAR(50) COMMENT '类型',
    create_time DATETIME,
    update_time DATETIME
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='教室';

-- 教室占用
CREATE TABLE classroom_schedule (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    room_id VARCHAR(20) NOT NULL COMMENT '教室编号',
    slot_id INT NOT NULL COMMENT '节次ID 1-5',
    occupied TINYINT DEFAULT 0 COMMENT '0-空闲 1-占用',
    course_name VARCHAR(100) COMMENT '课程名称',
    teacher VARCHAR(50) COMMENT '授课教师',
    schedule_date DATE COMMENT '日期',
    create_time DATETIME,
    update_time DATETIME
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='教室占用情况';

-- 考试竞赛
CREATE TABLE exam_info (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(200) NOT NULL COMMENT '标题',
    type VARCHAR(20) NOT NULL COMMENT '考试/竞赛',
    exam_date DATE COMMENT '考试日期',
    deadline DATE COMMENT '报名截止',
    status VARCHAR(20) DEFAULT '报名中' COMMENT '报名中/即将开始/未开始',
    tags VARCHAR(200) COMMENT '标签',
    detail TEXT COMMENT '详情',
    website_url VARCHAR(500) COMMENT '官网链接',
    create_time DATETIME,
    update_time DATETIME,
    version INT DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='考试竞赛';

-- ======== 初始数据 ========

INSERT INTO recruitment (type, title, shop_name, salary, tags, `description`, requirements, location, contact, publish_date) VALUES
('shop', '奶茶店店员', '一点点奶茶（南门店）', '15-20元/时', '校内,时间灵活,包饮品', '负责前台点单、饮品制作、店铺卫生。', '在校大学生，每周至少3个半天', '校园南门商业街A区12号', '张店长 138****5678', '2026-05-20'),
('shop', '书店兼职管理员', '博雅书吧', '12元/时', '校内,安静,可自习', '图书整理上架、收银、维护阅读区秩序。', '细心负责，每周四下午有空', '图书馆一楼东侧', '李老师 138****9012', '2026-05-19'),
('shop', '咖啡馆学徒', '研磨时光咖啡', '16元/时', '校内,学技术,餐补', '学习咖啡制作、拉花、日常清洁。', '热爱咖啡文化，每周至少20h', '大学生活动中心1楼', '王店长 139****3456', '2026-05-18'),
('delivery', '外卖配送员（午高峰）', '美团专送（校园站）', '4元/单', '校内配送,午高峰,自备电车', '校园内商家到宿舍/教学楼的外卖配送。', '自备电动车，中午11:00-14:00有空', '校园配送站（北门）', '赵站长 150****2345', '2026-05-20'),
('delivery', '校园跑腿员', '趣拿校园生活', '5-8元/单', '时间自由,按单结算', '代取快递、代买午餐、代送文件等。', '有电动车或自行车', '全校范围', '趣拿客服 400-800-8888', '2026-05-19'),
('delivery', '夜宵配送骑手', '饿了么（校园站）', '5元/单', '晚班,补贴高', '晚21:00-00:00 校园夜宵配送。', '自备电动车，能熬夜', '校园配送站（西门）', '钱站长 151****6789', '2026-05-18');

INSERT INTO announcement (title, category, content, publisher, publish_date) VALUES
('关于2026年端午节放假安排的通知', '教务通知', '全校各单位：根据国家法定节假日安排，2026年端午节6月8日至10日放假调休，共3天。6月6日（周六）补6月8日（周一）的课程。', '校长办公室', '2026-05-22'),
('图书馆2026年暑期开放时间调整', '服务通知', '暑期（7月1日-8月30日）图书馆开放时间：一楼自习区7:00-23:00，二至四楼阅览区8:30-17:30（周一至周五）。', '图书馆', '2026-05-21'),
('关于校园电动自行车登记上牌的通知', '保卫通知', '5月25日-6月15日开展校园电动自行车登记上牌工作，地点保卫处一楼大厅。', '保卫处', '2026-05-20');

INSERT INTO classroom (room_id, building, capacity, type) VALUES
('A101', '教学楼A', 60, '普通教室'), ('A102', '教学楼A', 60, '普通教室'), ('A201', '教学楼A', 80, '多媒体教室'),
('B101', '教学楼B', 45, '普通教室'), ('B102', '教学楼B', 45, '普通教室'), ('B201', '教学楼B', 60, '多媒体教室'),
('C101', '教学楼C', 100, '阶梯教室'), ('C201', '教学楼C', 50, '语音室'),
('S101', '实验楼', 30, '计算机房'), ('S201', '实验楼', 25, '化学实验室');

INSERT INTO classroom_schedule (room_id, slot_id, occupied, course_name, teacher, schedule_date) VALUES
-- 第1-2节 (08:00-09:40)
('A101', 1, 1, '高等数学', '张教授', CURDATE()),
('A102', 1, 1, '大学英语', '李老师', CURDATE()),
('A201', 1, 0, '', '', CURDATE()),
('B101', 1, 0, '', '', CURDATE()),
('B102', 1, 0, '', '', CURDATE()),
('B201', 1, 1, '计算机基础', '王老师', CURDATE()),
('C101', 1, 1, '马克思主义原理', '陈教授', CURDATE()),
('C201', 1, 1, '英语听力', '刘老师', CURDATE()),
('S101', 1, 0, '', '', CURDATE()),
('S201', 1, 1, '有机化学实验', '赵老师', CURDATE()),
-- 第3-4节 (10:00-11:40)
('A101', 2, 0, '', '', CURDATE()),
('A102', 2, 0, '', '', CURDATE()),
('A201', 2, 1, '数据结构', '周老师', CURDATE()),
('B101', 2, 1, '线性代数', '吴教授', CURDATE()),
('B102', 2, 0, '', '', CURDATE()),
('B201', 2, 0, '', '', CURDATE()),
('C101', 2, 0, '', '', CURDATE()),
('C201', 2, 1, '大学英语', '李老师', CURDATE()),
('S101', 2, 1, 'Python程序设计', '郑老师', CURDATE()),
('S201', 2, 0, '', '', CURDATE()),
-- 第5-6节 (14:00-15:40)
('A101', 3, 1, '大学物理', '孙教授', CURDATE()),
('A102', 3, 0, '', '', CURDATE()),
('A201', 3, 0, '', '', CURDATE()),
('B101', 3, 0, '', '', CURDATE()),
('B102', 3, 1, '概率论与数理统计', '吴教授', CURDATE()),
('B201', 3, 1, '操作系统', '周老师', CURDATE()),
('C101', 3, 0, '', '', CURDATE()),
('C201', 3, 1, '英语口语', '刘老师', CURDATE()),
('S101', 3, 1, 'Java企业级开发', '郑老师', CURDATE()),
('S201', 3, 0, '', '', CURDATE()),
-- 第7-8节 (16:00-17:40)
('A101', 4, 0, '', '', CURDATE()),
('A102', 4, 1, '中国近现代史纲要', '陈教授', CURDATE()),
('A201', 4, 0, '', '', CURDATE()),
('B101', 4, 0, '', '', CURDATE()),
('B102', 4, 0, '', '', CURDATE()),
('B201', 4, 0, '', '', CURDATE()),
('C101', 4, 1, '学术讲座', '校外专家', CURDATE()),
('C201', 4, 0, '', '', CURDATE()),
('S101', 4, 0, '', '', CURDATE()),
('S201', 4, 0, '', '', CURDATE()),
-- 第9-10节 (19:00-20:40)
('A101', 5, 1, '晚自习', '', CURDATE()),
('A102', 5, 1, '晚自习', '', CURDATE()),
('A201', 5, 0, '', '', CURDATE()),
('B101', 5, 0, '', '', CURDATE()),
('B102', 5, 0, '', '', CURDATE()),
('B201', 5, 0, '', '', CURDATE()),
('C101', 5, 0, '', '', CURDATE()),
('C201', 5, 0, '', '', CURDATE()),
('S101', 5, 0, '', '', CURDATE()),
('S201', 5, 0, '', '', CURDATE());

INSERT INTO exam_info (title, type, exam_date, deadline, status, tags, detail) VALUES
('2026年6月大学英语四六级考试', '考试', '2026-06-15', '2026-05-30', '报名中', '英语,国家级,证书', '四级6月15日9:00-11:20，六级15:00-17:25。需自备听力耳机。', 'https://cet.neea.edu.cn'),
('全国大学生数学建模竞赛', '竞赛', '2026-09-10', '2026-06-30', '报名中', '数学,国家级,团队', '每组3人，可跨专业组队。竞赛时长72小时。', 'https://cumcm.cnki.net'),
('2026年研究生入学考试', '考试', '2026-12-21', '2026-10-15', '未开始', '考研,国家级,升学', '12月21日-22日初试。研招网报名。', 'https://yz.chsi.com.cn'),
('ACM-ICPC 校选赛', '竞赛', '2026-06-20', '2026-06-10', '报名中', '编程,算法,团队', '6月20日14:00-18:00，5小时团队赛。', 'https://icpc.global');

ALTER TABLE exam_info ADD COLUMN website_url VARCHAR(500) COMMENT '官网链接';

UPDATE exam_info SET website_url = 'https://cet.neea.edu.cn' WHERE id = 1;
UPDATE exam_info SET website_url = 'https://cumcm.cnki.net' WHERE id = 2;
UPDATE exam_info SET website_url = 'https://yz.chsi.com.cn' WHERE id = 3;
UPDATE exam_info SET website_url = 'https://icpc.global' WHERE id = 4;

-- 管理员密钥
CREATE TABLE IF NOT EXISTS admin_key (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    secret_key VARCHAR(32) NOT NULL UNIQUE COMMENT '密钥',
    remark VARCHAR(100) COMMENT '持有人备注',
    status TINYINT DEFAULT 1 COMMENT '1-有效 0-已注销',
    create_time DATETIME,
    expire_time DATETIME
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='管理员密钥';

INSERT INTO admin_key (secret_key, remark, status, create_time, expire_time) VALUES
('ADM-a7Fk9pQ2wR4xZ8mN3vB6cXyL1jK5tH0g', '教务处-张主任', 1, NOW(), DATE_ADD(NOW(), INTERVAL 7 DAY)),
('ADM-b8Gm0qR3xS5yA9nO4wC7dZ2eK6uI1fH', '教务处-李副处长', 1, NOW(), DATE_ADD(NOW(), INTERVAL 7 DAY)),
('ADM-c9Hn1rS4yT6zB0oP5xD8eA3fL7vJ2gI', '信息中心-王老师', 1, NOW(), DATE_ADD(NOW(), INTERVAL 7 DAY)),
('ADM-d0Io2sT5zU7aC1pQ6yE9fB4gM8wK3hJ', '信息中心-赵老师', 1, NOW(), DATE_ADD(NOW(), INTERVAL 7 DAY)),
('ADM-e1Jp3tU6aV8bD2qR7zF0gC5hN9xL4iK', '辅导员-刘老师', 1, NOW(), DATE_ADD(NOW(), INTERVAL 7 DAY));

-- 用户资料
CREATE TABLE IF NOT EXISTS user_profile (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(50) NOT NULL DEFAULT '' COMMENT '姓名',
    student_id VARCHAR(30) NOT NULL DEFAULT '' COMMENT '学号',
    major VARCHAR(50) NOT NULL DEFAULT '' COMMENT '院系/专业',
    avatar_url VARCHAR(255) NOT NULL DEFAULT '' COMMENT '头像URL',
    phone VARCHAR(20) NOT NULL DEFAULT '' COMMENT '手机号',
    role TINYINT DEFAULT 1 COMMENT '0-管理员 1-用户',
    password VARCHAR(100) NOT NULL DEFAULT '' COMMENT '密码(BCrypt)',
    need_change_pwd TINYINT DEFAULT 1 COMMENT '1-首次登录需改密码 0-已修改',
    create_time DATETIME,
    update_time DATETIME
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户资料';

ALTER TABLE user_profile ADD COLUMN role TINYINT DEFAULT 1 COMMENT '0-管理员 1-用户';

-- 设定管理员（学号 20261401 张伟）
UPDATE user_profile SET role = 0 WHERE student_id = '20261401';

-- 38名软件工程学生，学号20261401~20261438，默认密码 Stu+学号+355（BCrypt 在改密时生成）
INSERT IGNORE INTO user_profile (name, student_id, major, password, need_change_pwd) VALUES
('张伟', '20261401', '软件工程', '', 1),
('王芳', '20261402', '软件工程', '', 1),
('李娜', '20261403', '软件工程', '', 1),
('刘洋', '20261404', '软件工程', '', 1),
('陈静', '20261405', '软件工程', '', 1),
('杨磊', '20261406', '软件工程', '', 1),
('赵敏', '20261407', '软件工程', '', 1),
('黄勇', '20261408', '软件工程', '', 1),
('周婷', '20261409', '软件工程', '', 1),
('吴杰', '20261410', '软件工程', '', 1),
('徐丽', '20261411', '软件工程', '', 1),
('孙强', '20261412', '软件工程', '', 1),
('马超', '20261413', '软件工程', '', 1),
('朱艳', '20261414', '软件工程', '', 1),
('胡涛', '20261415', '软件工程', '', 1),
('郭慧', '20261416', '软件工程', '', 1),
('林峰', '20261417', '软件工程', '', 1),
('何雪', '20261418', '软件工程', '', 1),
('高峰', '20261419', '软件工程', '', 1),
('梁燕', '20261420', '软件工程', '', 1),
('宋鑫', '20261421', '软件工程', '', 1),
('郑杰', '20261422', '软件工程', '', 1),
('谢琳', '20261423', '软件工程', '', 1),
('韩冰', '20261424', '软件工程', '', 1),
('唐亮', '20261425', '软件工程', '', 1),
('冯萍', '20261426', '软件工程', '', 1),
('程鹏', '20261427', '软件工程', '', 1),
('曹颖', '20261428', '软件工程', '', 1),
('许飞', '20261429', '软件工程', '', 1),
('邓婷', '20261430', '软件工程', '', 1),
('苏洋', '20261431', '软件工程', '', 1),
('潘浩', '20261432', '软件工程', '', 1),
('魏蕾', '20261433', '软件工程', '', 1),
('贾超', '20261434', '软件工程', '', 1),
('汪静', '20261435', '软件工程', '', 1),
('田磊', '20261436', '软件工程', '', 1),
('沈倩', '20261437', '软件工程', '', 1),
('任勇', '20261438', '软件工程', '', 1);

-- 备忘录
CREATE TABLE IF NOT EXISTS memo (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    ref_type VARCHAR(20) NOT NULL COMMENT '关联类型: recruitment/exam',
    ref_id BIGINT NOT NULL COMMENT '关联ID',
    title VARCHAR(200) NOT NULL COMMENT '冗余标题',
    student_id VARCHAR(30) NOT NULL DEFAULT '' COMMENT '学号',
    create_time DATETIME,
    update_time DATETIME
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='备忘录';

-- 旧表补字段
ALTER TABLE memo ADD COLUMN student_id VARCHAR(30) NOT NULL DEFAULT '' COMMENT '学号';

-- 用户反馈
CREATE TABLE IF NOT EXISTS feedback (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(200) NOT NULL COMMENT '反馈标题',
    detail TEXT COMMENT '详细描述',
    create_time DATETIME,
    status TINYINT DEFAULT 0 COMMENT '0-待处理 1-已处理'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户反馈';

-- 失物招领
CREATE TABLE IF NOT EXISTS lost_found (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    type VARCHAR(10) NOT NULL COMMENT 'lost-寻物/found-拾物',
    title VARCHAR(200) NOT NULL,
    description TEXT NOT NULL,
    images VARCHAR(500) COMMENT 'JSON数组，最多2张',
    phone VARCHAR(20),
    wechat VARCHAR(50),
    student_id VARCHAR(30) NOT NULL COMMENT '发帖人学号',
    author_name VARCHAR(50) COMMENT '发帖人姓名',
    author_avatar VARCHAR(255) COMMENT '发帖人头像',
    status TINYINT DEFAULT 1 COMMENT '1-有效 0-已删除',
    create_time DATETIME,
    update_time DATETIME
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='失物招领';