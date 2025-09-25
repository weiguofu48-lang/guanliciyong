# 文件传输功能说明

## 功能概述

企业管理系统现已支持用户之间的文件传输功能，允许用户安全地发送和接收文件。

## 主要特性

### 1. 文件上传
- 支持多种文件格式
- 文件大小限制：100MB
- 自动生成唯一文件名防止冲突
- 文件哈希验证确保完整性

### 2. 文件传输管理
- 发送者可以选择接收者
- 支持添加备注信息
- 文件状态跟踪（待发送、已发送、已接收、已过期）
- 自动过期机制（默认72小时）

### 3. 安全特性
- 用户权限验证
- 文件访问控制
- 文件哈希校验
- 过期文件自动清理

### 4. 用户界面
- 直观的文件传输界面
- 实时状态更新
- 文件列表管理
- 未读文件提醒

## 技术实现

### 后端组件

1. **实体类**
   - `FileTransfer`: 文件传输记录实体
   - `TransferStatus`: 传输状态枚举

2. **数据访问层**
   - `FileTransferRepository`: 文件传输数据访问接口

3. **业务逻辑层**
   - `FileTransferService`: 文件传输服务接口
   - `FileTransferServiceImpl`: 文件传输服务实现

4. **控制器层**
   - `FileTransferController`: 文件传输API控制器

### 前端组件

1. **文件传输页面**
   - `file-transfer.html`: 主要的文件传输界面
   - 支持文件上传、下载、管理功能

2. **集成到主系统**
   - 在主页面添加了"文件传输"导航按钮
   - 点击后在新窗口中打开文件传输页面

## API接口

### 文件上传
```
POST /api/file-transfer/upload
参数：
- file: 上传的文件
- senderId: 发送者ID
- receiverId: 接收者ID
- message: 备注信息（可选）
```

### 文件下载
```
GET /api/file-transfer/download/{transferId}?userId={userId}
```

### 获取文件列表
```
GET /api/file-transfer/sent/{userId}          # 已发送文件
GET /api/file-transfer/received/{userId}      # 已接收文件
GET /api/file-transfer/user/{userId}          # 用户所有文件记录
```

### 文件管理
```
PUT /api/file-transfer/mark-received/{transferId}?userId={userId}  # 标记已接收
DELETE /api/file-transfer/{transferId}?userId={userId}            # 删除文件记录
```

## 配置说明

### 应用配置 (application.properties)
```properties
# 文件上传配置
spring.servlet.multipart.enabled=true
spring.servlet.multipart.max-file-size=100MB
spring.servlet.multipart.max-request-size=100MB
spring.servlet.multipart.file-size-threshold=2KB

# 文件传输配置
file.upload.path=./uploads/files
file.transfer.expire.hours=72
file.transfer.max-size=100MB
```

### 数据库表结构
```sql
CREATE TABLE t_file_transfer (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    file_name VARCHAR(255) NOT NULL,
    original_file_name VARCHAR(255) NOT NULL,
    file_path VARCHAR(500) NOT NULL,
    file_size BIGINT NOT NULL,
    file_type VARCHAR(100) NOT NULL,
    file_hash VARCHAR(255) NOT NULL,
    sender_id BIGINT NOT NULL,
    receiver_id BIGINT NOT NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'PENDING',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    sent_at TIMESTAMP NULL,
    received_at TIMESTAMP NULL,
    expires_at TIMESTAMP NULL,
    message TEXT,
    CONSTRAINT fk_file_transfer_sender FOREIGN KEY (sender_id) REFERENCES t_user(id),
    CONSTRAINT fk_file_transfer_receiver FOREIGN KEY (receiver_id) REFERENCES t_user(id)
);
```

## 使用方法

### 1. 访问文件传输功能
- 登录系统后，点击导航栏中的"📁 文件传输"按钮
- 系统会在新窗口中打开文件传输页面

### 2. 发送文件
- 在"上传文件"标签页中选择要发送的文件
- 选择接收者
- 可选填写备注信息
- 点击"上传文件"按钮

### 3. 接收文件
- 在"已接收"标签页中查看收到的文件
- 点击"下载"按钮下载文件
- 点击"标记已接收"按钮确认接收

### 4. 管理文件
- 在"已发送"标签页中管理发送的文件
- 在"全部记录"标签页中查看所有文件传输记录
- 可以删除不需要的文件记录

## 注意事项

1. **文件大小限制**: 单个文件最大100MB
2. **文件过期**: 文件默认72小时后过期，过期后无法下载
3. **权限控制**: 只有发送者和接收者可以访问文件
4. **存储位置**: 文件存储在 `./uploads/files` 目录下
5. **安全性**: 系统会验证文件完整性并控制访问权限

## 扩展功能建议

1. **文件预览**: 支持图片、PDF等文件的在线预览
2. **批量操作**: 支持批量上传、下载、删除
3. **文件分类**: 按文件类型、大小等分类管理
4. **传输进度**: 显示大文件的上传/下载进度
5. **通知系统**: 文件传输完成后发送通知
6. **版本控制**: 支持文件版本管理
7. **分享链接**: 生成文件分享链接供外部访问

## 故障排除

### 常见问题

1. **文件上传失败**
   - 检查文件大小是否超过限制
   - 确认网络连接正常
   - 检查服务器存储空间

2. **文件下载失败**
   - 确认文件未过期
   - 检查用户权限
   - 验证文件是否存在

3. **权限错误**
   - 确认用户已登录
   - 检查用户是否有访问权限
   - 验证文件传输记录是否存在

### 日志查看
系统会在 `logs/application.log` 中记录文件传输相关的操作日志，可以通过查看日志来诊断问题。

