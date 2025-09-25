# 文件上传问题排查指南

## 🔍 问题诊断

您遇到"上传不了文件"的问题，我已经创建了文件上传目录并提供了测试工具。

## 🛠️ 解决步骤

### 步骤1：访问测试页面
访问：http://localhost:8082/untitled1/static/test-upload.html

这个页面会帮您逐步诊断问题：

1. **检查用户信息** - 确认localStorage中的用户数据
2. **测试用户列表API** - 验证后端API是否正常
3. **测试文件上传** - 直接测试文件上传功能
4. **检查文件传输记录** - 查看是否有上传记录

### 步骤2：检查文件上传目录
我已经创建了必要的目录：
- ✅ `uploads/` 目录已创建
- ✅ `uploads/files/` 目录已创建

### 步骤3：常见问题排查

#### 问题1：用户信息无效
**症状**：测试页面显示"没有找到用户信息"
**解决方案**：
1. 访问主页面：http://localhost:8082/untitled1/static/index.html
2. 使用 admin/admin123 登录
3. 返回测试页面重新检查

#### 问题2：API调用失败
**症状**：用户列表API测试失败
**解决方案**：
1. 检查应用程序是否在运行：`netstat -an | findstr :8082`
2. 检查浏览器控制台是否有CORS错误
3. 确认API路径正确：`/untitled1/api/users`

#### 问题3：文件上传失败
**症状**：文件上传测试失败
**可能原因**：
- 文件大小超过限制（100MB）
- 文件类型不被支持
- 服务器存储空间不足
- 权限问题

#### 问题4：没有接收者可选
**症状**：文件传输页面中接收者选择框为空
**解决方案**：
1. 确保有多个用户账户
2. 检查用户列表API是否返回数据
3. 确认当前用户不是唯一的用户

## 🔧 手动测试方法

### 方法1：使用浏览器开发者工具
1. 打开文件传输页面
2. 按F12打开开发者工具
3. 在Console标签页中执行：
```javascript
// 检查用户信息
console.log('用户信息:', JSON.parse(localStorage.getItem('currentUser')));

// 测试API
fetch('/untitled1/api/users')
  .then(response => response.json())
  .then(data => console.log('用户列表:', data));
```

### 方法2：使用curl命令测试
```bash
# 测试用户列表API
curl -X GET "http://localhost:8082/untitled1/api/users"

# 测试文件上传（需要先准备一个测试文件）
curl -X POST "http://localhost:8082/untitled1/api/file-transfer/upload" \
  -F "file=@test.txt" \
  -F "senderId=1" \
  -F "receiverId=1" \
  -F "message=测试上传"
```

## 📋 检查清单

在报告问题前，请确认：

- [ ] 应用程序正在运行（端口8082）
- [ ] 用户已成功登录
- [ ] localStorage中有用户信息
- [ ] 文件上传目录存在
- [ ] 浏览器控制台没有JavaScript错误
- [ ] 网络请求没有失败

## 🎯 预期结果

测试页面应该显示：
1. ✅ 用户信息获取成功
2. ✅ 用户列表API正常（至少显示admin用户）
3. ✅ 文件上传成功（如果选择了文件）
4. ✅ 文件传输记录获取成功

## 🚨 如果问题仍然存在

请提供以下信息：
1. 测试页面的具体错误信息
2. 浏览器控制台的完整错误日志
3. 应用程序日志中的相关错误
4. 您尝试上传的文件类型和大小

## 🔗 相关链接

- **主页面**: http://localhost:8082/untitled1/static/index.html
- **文件传输页面**: http://localhost:8082/untitled1/static/file-transfer.html
- **测试页面**: http://localhost:8082/untitled1/static/test-upload.html
- **用户信息测试**: http://localhost:8082/untitled1/static/test-user.html

---

**注意**: 文件上传功能的后端代码已经完全实现，问题通常出现在前端配置或环境设置上。使用测试页面可以快速定位具体问题。

