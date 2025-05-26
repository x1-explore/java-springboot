# 管理系统

## 项目概述
这是一个基于 Spring Boot 的管理系统，具有用户管理、即时通讯等功能。系统采用前后端分离架构，后端使用 Spring Boot 框架，前端使用 Thymeleaf 模板引擎。

## 技术栈
- 后端框架：Spring Boot 2.7.0
- 安全框架：Spring Security
- 数据访问：Spring Data JPA
- 模板引擎：Thymeleaf
- 实时通讯：WebSocket
- 数据库：MySQL
- 工具库：Lombok

## 核心模块说明

### 控制器层（Controller）
1. `AdminController.java`
   - 处理管理员登录和仪表盘页面
   - 路径：`/login`, `/dashboard`

2. `ProfileController.java`
   - 处理用户个人资料相关功能
   - 功能：更新个人信息、上传头像、生成头像
   - 路径：`/profile/*`

3. `RegisterController.java`
   - 处理用户注册功能
   - 路径：`/register`

4. `ChatController.java`
   - 处理即时通讯功能
   - 功能：聊天页面、发送消息、获取聊天历史
   - 路径：`/chat/*`

5. `UserController.java`
   - 处理用户管理功能
   - 功能：用户搜索
   - 路径：`/users/*`

6. `InitController.java`
   - 处理系统初始化
   - 功能：创建管理员账号
   - 路径：`/init`

### 实体层（Entity）
1. `User.java`
   - 用户实体类
   - 字段：id, username, password, email, role, avatar
   - 实现了 Spring Security 的 UserDetails 接口

2. `ChatMessage.java`
   - 聊天消息实体类
   - 字段：id, sender, receiver, content, timestamp, read, type
   - 支持多种消息类型（CHAT, JOIN, LEAVE）

3. `SystemInit.java`
   - 系统初始化状态实体类
   - 用于标记系统是否已完成初始化

## 即时聊天功能完成度
即时聊天功能已经实现了以下功能：
1. 基础架构
   - WebSocket 配置已完成
   - 消息实体类已定义
   - 基本的消息发送和接收功能

2. 已实现功能
   - 用户之间的私聊
   - 聊天历史记录
   - 消息状态（已读/未读）
   - 用户在线状态

3. 待完善功能
   - 群聊功能
   - 消息通知
   - 文件传输
   - 消息撤回
   - 表情包支持

## 前端页面
项目使用 Thymeleaf 模板引擎，主要页面包括：
- 登录页面（login.html）
- 注册页面（register.html）
- 仪表盘（dashboard.html）
- 个人资料页（profile.html）
- 聊天页面（chat/index.html, chat/chat.html）

## 数据库设计
主要数据表：
1. users（用户表）
2. chat_messages（聊天消息表）
3. system_init（系统初始化状态表）

## 安全特性
- 使用 Spring Security 进行身份认证和授权
- 密码加密存储
- 基于角色的访问控制
- 会话管理

## 部署说明

### 环境要求
- JDK 1.8+
- MySQL 8.0+
- Maven 3.6+

### 本地开发环境配置
1. 克隆项目
2. 配置数据库
   ```sql
   CREATE DATABASE admin_system;
   CREATE USER 'admin_user'@'localhost' IDENTIFIED BY 'admin123';
   GRANT ALL PRIVILEGES ON admin_system.* TO 'admin_user'@'localhost';
   FLUSH PRIVILEGES;
   ```
3. 运行项目
   ```bash
   mvn clean package
   java -jar target/admin-system-1.0-SNAPSHOT.jar
   ```

### 宝塔面板部署步骤
1. 安装MySQL
2. 创建数据库和用户
   ```sql
   CREATE DATABASE admin_system;
   CREATE USER 'admin_user'@'%' IDENTIFIED BY 'admin123';
   GRANT ALL PRIVILEGES ON admin_system.* TO 'admin_user'@'%';
   FLUSH PRIVILEGES;
   ```
3. 上传文件
   - 上传`target/admin-system-1.0-SNAPSHOT.jar`
   - 上传`start.sh`
4. 配置启动脚本
   - 修改`start.sh`中的数据库连接信息
   - 给启动脚本添加执行权限：`chmod +x start.sh`
5. 在宝塔面板中配置Java项目
   - 项目名称：admin-system
   - 项目端口：8080
   - 启动命令：`./start.sh`

## 开发指南
对于新接手的程序员，建议按以下步骤熟悉项目：
1. 首先了解项目结构和依赖（pom.xml）
2. 查看数据库设计（实体类）
3. 了解业务流程（控制器层）
4. 熟悉前端页面和交互
5. 重点关注即时通讯模块的实现

## 注意事项
1. 系统需要先进行初始化才能使用
2. 用户密码需要加密存储
3. 聊天功能需要 WebSocket 支持
4. 文件上传需要配置存储路径
5. 注意处理并发和消息队列

## 默认账号
- 管理员账号：admin
- 管理员密码：123456 