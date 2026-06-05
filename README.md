# 🎓 校园服务小程序

> 集失物招领、校园论坛、消息通知、管理后台于一体的校园综合服务平台

## 📸 项目截图

| 首页 | 论坛 | 消息 |
|:---:|:---:|:---:|
| ![首页](https://via.placeholder.com/200x400/7c3aed/ffffff?text=首页) | ![论坛](https://via.placeholder.com/200x400/3b82f6/ffffff?text=论坛) | ![消息](https://via.placeholder.com/200x400/10b981/ffffff?text=消息) |

## 🛠 技术栈

| 层级 | 技术 |
|------|------|
| **前端** | uni-app 3.x + Vue 3.4 + Vite 5 |
| **后端** | SpringBoot 3.5 + MyBatis-Plus 3.5 |
| **数据库** | MySQL 8.0 |
| **权限** | Sa-Token |
| **安全** | Spring Security Crypto (BCrypt) |

## ✨ 功能模块

### 🏠 首页
- 功能入口 Grid 布局
- 最新公告展示
- 热门招聘推荐

### 📦 失物招领
- 寻物/拾物分类筛选
- 图片上传（最多2张）
- 联系方式展示（手机/微信）
- 服务端分页

### 💬 校园论坛
- 5 个分类 Tab（全部/求助/讨论/失物招领/公告）
- 帖子发布（含敏感词检测）
- 点赞/收藏 toggle
- 评论系统
- 置顶功能
- 下拉刷新 + 上拉加载

### 🔔 消息通知
- 公告推送（全员通知）
- 点赞/评论/收藏通知
- 已读/未读状态
- 全部已读
- Tab 红点提示

### 👤 个人中心
- 用户资料编辑
- 头像上传
- 我的收藏
- 个人备忘录

### 🔧 管理后台
- 公告管理
- 招聘管理
- 考试管理
- 敏感词管理（动态维护）

## 🚀 快速开始

### 环境要求

- JDK 17+
- Node.js 18+
- MySQL 8.0+
- IDEA / VS Code

### 后端启动

```bash
# 1. 创建数据库
mysql -u root -p < sql/forum_schema.sql

# 2. 修改配置
# 编辑 campus-backend/src/main/resources/application.yml
# 配置数据库连接信息

# 3. 启动
# IDEA 打开 CampusApplication.java → Run
```

### 前端启动

```bash
# 1. 安装依赖
cd uni-app
npm install

# 2. 启动开发服务器
npm run dev:h5

# 3. 访问
# 浏览器打开 http://localhost:5173
```

## 📁 项目结构

```
uni-campus/
├── campus-backend/                # 后端
│   ├── src/main/java/com/ricky/campus/
│   │   ├── common/               # 统一返回、异常处理
│   │   ├── config/               # Sa-Token、CORS、MyBatis-Plus
│   │   ├── controller/           # RESTful 接口
│   │   ├── mapper/               # MyBatis-Plus Mapper
│   │   ├── model/
│   │   │   ├── dto/              # 请求参数
│   │   │   ├── entity/           # 数据库实体
│   │   │   └── vo/               # 返回视图
│   │   ├── service/              # 业务接口
│   │   │   └── impl/             # 业务实现
│   │   └── interceptor/          # 登录拦截
│   └── sql/                      # 数据库脚本
│
├── uni-app/                      # 前端
│   └── src/
│       ├── api/                  # API 接口封装
│       ├── components/           # 公共组件
│       └── pages/                # 页面
│           ├── index/            # 首页
│           ├── forum/            # 论坛
│           ├── lost-found/       # 失物招领
│           ├── notification/     # 消息通知
│           ├── profile/          # 个人中心
│           └── admin/            # 管理后台
│
└── README.md
```

## 📐 核心设计

### Toggle 模式（点赞/收藏）

```
查记录 → 存在？ → 删除记录 + count-1
                 → 不存在？ → 插入记录 + count+1
```

### 批量查询优化（解决 N+1）

```
收集所有 ID → 一次 IN 查询 → 结果存 Map → 遍历时 O(1) 取值
```

### 通知联动

```
业务操作成功 → 判断是否自己 → 不是自己则 notificationService.send()
```

## 📝 更新日志

### v1.0 (2026-06-05)
- ✅ 完成论坛模块（帖子/评论/点赞/收藏）
- ✅ 完成消息通知系统
- ✅ 完成敏感词检测
- ✅ 完成管理后台
- ✅ 完成失物招领模块

## 📄 License

MIT License

---

**作者：Ricky**
