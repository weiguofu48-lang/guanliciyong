// 全局变量
let currentUser = null;
let currentView = 'dashboard';

// API基础URL
const API_BASE = '/untitled1/api';

// 页面加载完成后初始化
document.addEventListener('DOMContentLoaded', function() {
    checkLoginStatus();
    const loginForm = document.getElementById('loginForm');
    const registerForm = document.getElementById('registerForm');
    if (loginForm) loginForm.addEventListener('submit', handleLogin);
    if (registerForm) registerForm.addEventListener('submit', handleRegister);
});

// 检查登录状态
function checkLoginStatus() {
    showLoginForm();
}

// 显示登录/注册表单
function showLoginForm() {
    document.getElementById('loginSection').classList.remove('hidden');
    document.getElementById('registerSection').classList.add('hidden');
    document.getElementById('mainApp').classList.add('hidden');
}

function showRegisterForm() {
    document.getElementById('loginSection').classList.add('hidden');
    document.getElementById('registerSection').classList.remove('hidden');
    document.getElementById('mainApp').classList.add('hidden');
}

// 处理登录和注册
async function handleLogin(e) {
    e.preventDefault();
    const formData = new FormData(e.target);
    const loginData = {
        username: formData.get('username'),
        password: formData.get('password')
    };
    try {
        const response = await fetch(`${API_BASE}/users/login`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(loginData)
        });
        const result = await response.json();
        if (result.success) {
            currentUser = result.user;
            localStorage.setItem('currentUser', JSON.stringify(currentUser));
            showMainApp();
        } else {
            showAlert('loginAlert', result.message, 'danger');
        }
    } catch (error) {
        showAlert('loginAlert', '登录失败，请重试', 'danger');
    }
}

async function handleRegister(e) {
    e.preventDefault();
    const formData = new FormData(e.target);
    const userData = {
        username: formData.get('username'),
        realName: formData.get('realName'),
        password: formData.get('password'),
        email: formData.get('email'),
        phone: formData.get('phone')
    };
    try {
        const response = await fetch(`${API_BASE}/users/register`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(userData)
        });
        const result = await response.json();
        if (result.success) {
            showAlert('registerAlert', '注册成功！请登录', 'success');
            setTimeout(() => showLoginForm(), 1500);
        } else {
            showAlert('registerAlert', result.message, 'danger');
        }
    } catch (error) {
        showAlert('registerAlert', '注册失败，请重试', 'danger');
    }
}

// 主应用界面
function showMainApp() {
    document.getElementById('loginSection').classList.add('hidden');
    document.getElementById('registerSection').classList.add('hidden');
    document.getElementById('mainApp').classList.remove('hidden');
    showDashboard();
}

// 导航
function showDashboard() { loadSection('dashboard', 'dashboardBtn', loadDashboard); }
function showEmployees() { loadSection('employees', 'employeesBtn', loadEmployees); }
function showDepartments() { loadSection('departments', 'departmentsBtn', loadDepartments); }
function showUsers() { loadSection('users', 'usersBtn', loadUsers); }
function showNotifications() { loadSection('notifications', 'notificationsBtn', loadNotifications); }

function showFileTransfer() { 
    // 直接跳转到文件传输页面
    window.open('/untitled1/static/file-transfer.html', '_blank');
}

function loadSection(view, btnId, loader) {
    currentView = view;
    updateNavButtons(btnId);
    loader();
}

function updateNavButtons(activeBtnId) {
    document.querySelectorAll('.nav-btn').forEach(btn => btn.classList.remove('active'));
    document.getElementById(activeBtnId).classList.add('active');
}

// 加载内容
async function loadDashboard() {
    const content = document.getElementById('content');
    content.innerHTML = '<div class="loading"><div class="spinner"></div><p>加载中...</p></div>';
    try {
        const response = await fetch(`${API_BASE}/dashboard/stats`);
        const stats = await response.json();
        content.innerHTML = `
            <div class="dashboard-header"><h2>📊 仪表板</h2><p>欢迎回来，Admin！这是您的系统概览。</p></div>
            <div class="stats-grid">
                <div class="stat-card"><h3>员工总数</h3><div class="stat-number">${stats.employeeCount}</div></div>
                <div class="stat-card"><h3>部门总数</h3><div class="stat-number">${stats.departmentCount}</div></div>
                <div class="stat-card"><h3>通知数量</h3><div class="stat-number">${stats.notificationCount || 0}</div></div>
                <div class="stat-card"><h3>用户总数</h3><div class="stat-number">${stats.userCount || 0}</div></div>
            </div>
        `;
    } catch (error) {
        content.innerHTML = '<div class="alert alert-danger">加载仪表板数据失败</div>';
    }
}

async function loadEmployees() {
    const content = document.getElementById('content');
    content.innerHTML = '<div class="loading"><div class="spinner"></div><p>加载中...</p></div>';
    try {
        const response = await fetch(`${API_BASE}/employees`);
        const employees = await response.json();
        let tableHTML = `
            <div class="page-header"><h2 class="page-title">员工管理</h2><button class="btn btn-success" onclick="showAddEmployeeModal()">➕ 添加员工</button></div>
            <table class="table"><thead><tr><th>员工编号</th><th>姓名</th><th>性别</th><th>部门</th><th>职位</th><th>状态</th><th>入职日期</th><th>操作</th></tr></thead><tbody>`;
        employees.forEach(employee => {
            tableHTML += `<tr>
                <td>${employee.employeeId || ''}</td><td>${employee.name || ''}</td><td>${employee.gender === 'MALE' ? '男' : '女'}</td>
                <td>${employee.department ? employee.department.name : ''}</td><td>${employee.position ? employee.position.name : ''}</td>
                <td>${employee.status || ''}</td><td>${employee.hireDate || ''}</td>
                <td class="actions"><button class="btn btn-secondary" onclick="editEmployee(${employee.id})">编辑</button><button class="btn btn-danger" onclick="deleteEmployee(${employee.id})">删除</button></td>
            </tr>`;
        });
        tableHTML += '</tbody></table>';
        content.innerHTML = tableHTML;
    } catch (error) {
        content.innerHTML = '<div class="alert alert-danger">加载员工数据失败</div>';
    }
}

async function loadDepartments() {
    const content = document.getElementById('content');
    content.innerHTML = '<div class="loading"><div class="spinner"></div><p>加载中...</p></div>';
    try {
        const response = await fetch(`${API_BASE}/departments`);
        const departments = await response.json();
        let tableHTML = `
            <div class="page-header"><h2 class="page-title">部门管理</h2><button class="btn btn-success" onclick="showAddDepartmentModal()">➕ 添加部门</button></div>
            <table class="table"><thead><tr><th>部门编号</th><th>部门名称</th><th>描述</th><th>岗位数量</th><th>操作</th></tr></thead><tbody>`;
        departments.forEach(department => {
            tableHTML += `<tr>
                <td>${department.id}</td><td>${department.name}</td><td class="description">${department.description || ''}</td>
                <td><span class="position-count" id="posCount-${department.id}">加载中...</span></td>
                <td class="actions">
                    <button class="btn btn-secondary" onclick="editDepartment(${department.id})">编辑</button>
                    <button class="btn btn-danger" onclick="deleteDepartment(${department.id})">删除</button>
                </td>
            </tr>`;
        });
        tableHTML += '</tbody></table>';
        content.innerHTML = tableHTML;
        
        // 加载每个部门的岗位数量
        departments.forEach(department => {
            loadPositionCount(department.id);
        });
    } catch (error) {
        content.innerHTML = '<div class="alert alert-danger">加载部门数据失败</div>';
    }
}

async function loadUsers() {
    const content = document.getElementById('content');
    content.innerHTML = '<div class="loading"><div class="spinner"></div><p>加载中...</p></div>';
    try {
        const response = await fetch(`${API_BASE}/users`);
        const users = await response.json();
        let tableHTML = `
            <div class="page-header"><h2 class="page-title">用户管理</h2><button class="btn btn-success" onclick="showAddUserModal()">➕ 添加用户</button></div>
            <table class="table"><thead><tr><th>ID</th><th>用户名</th><th>真实姓名</th><th>邮箱</th><th>电话</th><th>角色</th><th>操作</th></tr></thead><tbody>`;
        users.forEach(user => {
            tableHTML += `<tr>
                <td>${user.id}</td><td>${user.username}</td><td>${user.realName || ''}</td><td>${user.email || ''}</td><td>${user.phone || ''}</td><td>${user.role ? user.role.name : ''}</td>
                <td class="actions"><button class="btn btn-secondary" onclick="editUser(${user.id})">编辑</button><button class="btn btn-danger" onclick="deleteUser(${user.id})">删除</button></td>
            </tr>`;
        });
        tableHTML += '</tbody></table>';
        content.innerHTML = tableHTML;
    } catch (error) {
        content.innerHTML = '<div class="alert alert-danger">加载用户数据失败</div>';
    }
}

async function loadNotifications() {
    const content = document.getElementById('content');
    content.innerHTML = '<div class="loading"><div class="spinner"></div><p>加载中...</p></div>';
    try {
        const response = await fetch(`${API_BASE}/notifications`);
        const notifications = await response.json();
        let tableHTML = `
            <div class="page-header"><h2 class="page-title">通知管理</h2><button class="btn btn-success" onclick="showAddNotificationModal()">➕ 添加通知</button></div>
            <table class="table"><thead><tr><th>标题</th><th>内容</th><th>类型</th><th>创建时间</th><th>操作</th></tr></thead><tbody>`;
        notifications.forEach(notification => {
            const message = notification.message || '';
            const type = message.substring(message.indexOf('[') + 1, message.indexOf(']'));
            const title = message.substring(message.indexOf(']') + 1, message.indexOf(':'));
            const content = message.substring(message.indexOf(':') + 1);
            tableHTML += `<tr>
                <td>${title}</td><td class="description">${content}</td><td>${type}</td><td>${notification.createdAt || ''}</td>
                <td class="actions"><button class="btn btn-secondary" onclick="editNotification(${notification.id})">编辑</button><button class="btn btn-danger" onclick="deleteNotification(${notification.id})">删除</button></td>
            </tr>`;
        });
        tableHTML += '</tbody></table>';
        content.innerHTML = tableHTML;
    } catch (error) {
        content.innerHTML = '<div class="alert alert-danger">加载通知数据失败</div>';
    }
}

// Modal Functions
function showModal(title, formContent, formId, submitHandler, buttonText = '保存') {
    const modal = document.getElementById('modal');
    const modalBody = document.getElementById('modalBody');
    
    modalBody.innerHTML = `
        <div class="modal-header">
            <h3>${title}</h3>
            <span class="close" onclick="closeModal()">&times;</span>
        </div>
        <div class="modal-body">
            <form id="${formId}">${formContent}</form>
        </div>
        <div class="modal-footer">
            <button type="button" class="btn btn-secondary" onclick="closeModal()">取消</button>
            <button type="submit" form="${formId}" class="btn btn-success">${buttonText}</button>
        </div>
    `;
    modal.style.display = 'block';
    document.getElementById(formId).addEventListener('submit', submitHandler);
}

function closeModal() {
    document.getElementById('modal').style.display = 'none';
}

// Add/Edit Modals
function showAddEmployeeModal() {
    const formContent = `
        <div class="form-group"><label for="empName">员工姓名:</label><input type="text" id="empName" name="name" required></div>
        <div class="form-group"><label for="empId">员工编号:</label><input type="text" id="empId" name="employeeId" required></div>
        <div class="form-group"><label for="empGender">性别:</label><select id="empGender" name="gender" required><option value="">请选择</option><option value="MALE">男</option><option value="FEMALE">女</option></select></div>
        <div class="form-group"><label for="empDept">部门:</label><select id="empDept" name="departmentId" required onchange="updatePositionOptions()"></select></div>
        <div class="form-group"><label for="empPosition">职位:</label><select id="empPosition" name="positionId" required></select></div>
        <div class="form-group"><label for="empHireDate">入职日期:</label><input type="date" id="empHireDate" name="hireDate" required></div>
        <div class="form-group"><label for="empStatus">状态:</label><select id="empStatus" name="status" required><option value="">请选择</option><option value="ON_PROBATION">试用期</option><option value="REGULAR">正式</option><option value="ON_LEAVE">休假中</option><option value="RESIGNED">已离职</option></select></div>
    `;
    showModal('添加员工', formContent, 'addEmployeeForm', handleAddEmployee, '添加');
    loadDepartmentOptions();
    loadPositionOptions();
}

async function editEmployee(id) {
    const response = await fetch(`${API_BASE}/employees/${id}`);
    const employee = await response.json();
    const formContent = `
        <div class="form-group"><label for="empName">员工姓名:</label><input type="text" id="empName" name="name" value="${employee.name}" required></div>
        <div class="form-group"><label for="empId">员工编号:</label><input type="text" id="empId" name="employeeId" value="${employee.employeeId}" required></div>
        <div class="form-group"><label for="empGender">性别:</label><select id="empGender" name="gender" required><option value="MALE" ${employee.gender === 'MALE' ? 'selected' : ''}>男</option><option value="FEMALE" ${employee.gender === 'FEMALE' ? 'selected' : ''}>女</option></select></div>
        <div class="form-group"><label for="empDept">部门:</label><select id="empDept" name="departmentId" required onchange="updatePositionOptions()"></select></div>
        <div class="form-group"><label for="empPosition">职位:</label><select id="empPosition" name="positionId" required></select></div>
        <div class="form-group"><label for="empHireDate">入职日期:</label><input type="date" id="empHireDate" name="hireDate" value="${employee.hireDate}" required></div>
        <div class="form-group"><label for="empStatus">状态:</label><select id="empStatus" name="status" required><option value="ON_PROBATION" ${employee.status === 'ON_PROBATION' ? 'selected' : ''}>试用期</option><option value="REGULAR" ${employee.status === 'REGULAR' ? 'selected' : ''}>正式</option><option value="ON_LEAVE" ${employee.status === 'ON_LEAVE' ? 'selected' : ''}>休假中</option><option value="RESIGNED" ${employee.status === 'RESIGNED' ? 'selected' : ''}>已离职</option></select></div>
    `;
    showModal('编辑员工', formContent, 'editEmployeeForm', (e) => handleEditEmployee(e, id));
    loadDepartmentOptions(employee.department.id);
    // 延迟加载职位选项，确保部门选项已加载
    setTimeout(() => {
        loadPositionOptionsForDepartment(employee.department.id, employee.position.id);
    }, 100);
}

function showAddDepartmentModal() {
    const formContent = `
        <div class="form-group"><label for="deptName">部门名称:</label><input type="text" id="deptName" name="name" required></div>
        <div class="form-group"><label for="deptDesc">描述:</label><textarea id="deptDesc" name="description" rows="4"></textarea></div>
    `;
    showModal('添加部门', formContent, 'addDepartmentForm', handleAddDepartment, '添加');
}

async function editDepartment(id) {
    const response = await fetch(`${API_BASE}/departments/${id}`);
    const department = await response.json();
    
    // 获取该部门的职位列表
    const positionsResponse = await fetch(`${API_BASE}/positions/department/${id}`);
    const positions = await positionsResponse.json();
    
    // 生成职位列表HTML
    let positionsHtml = '';
    if (positions && positions.length > 0) {
        positionsHtml = positions.map(pos => `
            <div class="position-item" style="display: flex; justify-content: space-between; align-items: center; padding: 10px; border: 1px solid #ddd; border-radius: 5px; margin: 5px 0;">
                <div>
                    <strong>${pos.name}</strong>
                    <div style="font-size: 0.9em; color: #666;">${pos.description || '无描述'}</div>
                </div>
                <div>
                    <button type="button" class="btn btn-secondary btn-sm" onclick="editPosition(${pos.id}, '${pos.name}', '${pos.description || ''}')">编辑</button>
                    <button type="button" class="btn btn-danger btn-sm" onclick="deletePosition(${pos.id})">删除</button>
                </div>
            </div>
        `).join('');
    } else {
        positionsHtml = '<div style="text-align: center; color: #666; padding: 20px;">暂无职位</div>';
    }
    
    const formContent = `
        <div class="form-group">
            <label for="deptName">部门名称:</label>
            <input type="text" id="deptName" name="name" value="${department.name}" required>
        </div>
        <div class="form-group">
            <label for="deptDesc">部门描述:</label>
            <textarea id="deptDesc" name="description" rows="4">${department.description || ''}</textarea>
        </div>
        
        <div class="form-group">
            <div style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 15px;">
                <label style="margin: 0;">部门职位管理:</label>
                <button type="button" class="btn btn-success btn-sm" onclick="showAddPositionModal(${id})">➕ 添加职位</button>
            </div>
            <div id="positionsList" style="max-height: 300px; overflow-y: auto; border: 1px solid #ddd; border-radius: 5px; padding: 10px;">
                ${positionsHtml}
            </div>
        </div>
    `;
    showModal('编辑部门', formContent, 'editDepartmentForm', (e) => handleEditDepartment(e, id));
}

function showAddUserModal() {
    const formContent = `
        <div class="form-group"><label for="userUsername">用户名:</label><input type="text" id="userUsername" name="username" required></div>
        <div class="form-group"><label for="userPassword">密码:</label><input type="password" id="userPassword" name="password" required></div>
        <div class="form-group"><label for="userRealName">真实姓名:</label><input type="text" id="userRealName" name="realName" required></div>
        <div class="form-group"><label for="userEmail">邮箱:</label><input type="email" id="userEmail" name="email"></div>
        <div class="form-group"><label for="userPhone">电话:</label><input type="tel" id="userPhone" name="phone"></div>
        <div class="form-group"><label for="userRole">角色:</label><select id="userRole" name="roleId" required><option value="">请选择角色</option><option value="1">管理员</option><option value="2">经理</option><option value="3">员工</option></select></div>
    `;
    showModal('添加用户', formContent, 'addUserForm', handleAddUser, '添加');
}

async function editUser(id) {
    const response = await fetch(`${API_BASE}/users/${id}`);
    const user = await response.json();
    const formContent = `
        <div class="form-group"><label for="editUsername">用户名:</label><input type="text" id="editUsername" name="username" value="${user.username}" required></div>
        <div class="form-group"><label for="editRealName">真实姓名:</label><input type="text" id="editRealName" name="realName" value="${user.realName || ''}" required></div>
        <div class="form-group"><label for="editEmail">邮箱:</label><input type="email" id="editEmail" name="email" value="${user.email || ''}"></div>
        <div class="form-group"><label for="editPhone">电话:</label><input type="text" id="editPhone" name="phone" value="${user.phone || ''}"></div>
        <div class="form-group"><label for="editPassword">新密码 (留空表示不修改):</label><input type="password" id="editPassword" name="password"></div>
    `;
    showModal('编辑用户', formContent, 'editUserForm', (e) => handleEditUser(e, id));
}

function showAddNotificationModal() {
    const formContent = `
        <div class="form-group"><label for="notifTitle">通知标题:</label><input type="text" id="notifTitle" name="title" required></div>
        <div class="form-group"><label for="notifContent">通知内容:</label><textarea id="notifContent" name="content" rows="4" required></textarea></div>
        <div class="form-group"><label for="notifType">通知类型:</label><select id="notifType" name="type" required><option value="">请选择</option><option value="INFO">信息</option><option value="WARNING">警告</option><option value="ERROR">错误</option></select></div>
    `;
    showModal('添加通知', formContent, 'addNotificationForm', handleAddNotification, '添加');
}

async function editNotification(id) {
    const response = await fetch(`${API_BASE}/notifications/${id}`);
    const notification = await response.json();
    const message = notification.message || '';
    const type = message.substring(message.indexOf('[') + 1, message.indexOf(']'));
    const title = message.substring(message.indexOf(']') + 1, message.indexOf(':'));
    const content = message.substring(message.indexOf(':') + 1);
    const formContent = `
        <div class="form-group"><label for="editNotifTitle">通知标题:</label><input type="text" id="editNotifTitle" name="title" value="${title}" required></div>
        <div class="form-group"><label for="editNotifContent">通知内容:</label><textarea id="editNotifContent" name="content" rows="4" required>${content}</textarea></div>
        <div class="form-group"><label for="editNotifType">通知类型:</label><select id="editNotifType" name="type" required><option value="INFO" ${type === 'INFO' ? 'selected' : ''}>信息</option><option value="WARNING" ${type === 'WARNING' ? 'selected' : ''}>警告</option><option value="ERROR" ${type === 'ERROR' ? 'selected' : ''}>错误</option></select></div>
    `;
    showModal('编辑通知', formContent, 'editNotificationForm', (e) => handleEditNotification(e, id));
}

// Form Handlers
async function handleAddEmployee(e) {
        e.preventDefault();
        const formData = new FormData(e.target);
    const employeeData = Object.fromEntries(formData.entries());
    await sendRequest('/employees', 'POST', employeeData, '添加员工失败', () => loadEmployees());
}

async function handleEditEmployee(e, id) {
    e.preventDefault();
    const formData = new FormData(e.target);
    const employeeData = Object.fromEntries(formData.entries());
    await sendRequest(`/employees/${id}`, 'PUT', employeeData, '更新员工失败', () => loadEmployees());
}

async function handleAddDepartment(e) {
    e.preventDefault();
    const formData = new FormData(e.target);
    const departmentData = Object.fromEntries(formData.entries());
    await sendRequest('/departments', 'POST', departmentData, '添加部门失败', () => loadDepartments());
}

async function handleEditDepartment(e, id) {
        e.preventDefault();
        const formData = new FormData(e.target);
    const departmentData = Object.fromEntries(formData.entries());
    await sendRequest(`/departments/${id}`, 'PUT', departmentData, '更新部门失败', () => loadDepartments());
}

// 职位管理相关函数
function showAddPositionModal(departmentId) {
    const formContent = `
        <div class="form-group">
            <label for="posName">职位名称:</label>
            <input type="text" id="posName" name="name" required>
        </div>
        <div class="form-group">
            <label for="posDesc">职位描述:</label>
            <textarea id="posDesc" name="description" rows="3"></textarea>
        </div>
    `;
    showModal('添加职位', formContent, 'addPositionForm', (e) => handleAddPosition(e, departmentId));
}

function editPosition(id, name, description) {
    const formContent = `
        <div class="form-group">
            <label for="editPosName">职位名称:</label>
            <input type="text" id="editPosName" name="name" value="${name}" required>
        </div>
        <div class="form-group">
            <label for="editPosDesc">职位描述:</label>
            <textarea id="editPosDesc" name="description" rows="3">${description}</textarea>
        </div>
    `;
    showModal('编辑职位', formContent, 'editPositionForm', (e) => handleEditPosition(e, id));
}

async function handleAddPosition(e, departmentId) {
    e.preventDefault();
    const formData = new FormData(e.target);
    const positionData = Object.fromEntries(formData.entries());
    positionData.departmentId = departmentId;
    
    try {
        const response = await fetch(`${API_BASE}/positions`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(positionData)
        });
        
        if (response.ok) {
            alert('职位添加成功');
            closeModal();
            // 重新加载部门编辑页面以更新职位列表
            const deptId = departmentId;
            setTimeout(() => editDepartment(deptId), 500);
        } else {
            alert('职位添加失败');
        }
    } catch (error) {
        alert('职位添加失败: ' + error.message);
    }
}

async function handleEditPosition(e, id) {
    e.preventDefault();
    const formData = new FormData(e.target);
    const positionData = Object.fromEntries(formData.entries());
    
    try {
        const response = await fetch(`${API_BASE}/positions/${id}`, {
            method: 'PUT',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(positionData)
        });
        
        if (response.ok) {
            alert('职位更新成功');
            closeModal();
            // 重新加载部门编辑页面以更新职位列表
            const deptId = await getDepartmentIdByPositionId(id);
            setTimeout(() => editDepartment(deptId), 500);
        } else {
            alert('职位更新失败');
        }
    } catch (error) {
        alert('职位更新失败: ' + error.message);
    }
}

async function deletePosition(id) {
    if (!confirm('确定要删除这个职位吗？')) return;
    
    try {
        const response = await fetch(`${API_BASE}/positions/${id}`, {
            method: 'DELETE'
        });
        
        if (response.ok) {
            alert('职位删除成功');
            // 重新加载部门编辑页面以更新职位列表
            const deptId = await getDepartmentIdByPositionId(id);
            setTimeout(() => editDepartment(deptId), 500);
        } else {
            alert('职位删除失败');
        }
    } catch (error) {
        alert('职位删除失败: ' + error.message);
    }
}

async function getDepartmentIdByPositionId(positionId) {
    try {
        const response = await fetch(`${API_BASE}/positions/${positionId}`);
        const position = await response.json();
        return position.departmentId;
    } catch (error) {
        console.error('获取部门ID失败:', error);
        return null;
    }
}

async function handleAddUser(e) {
    e.preventDefault();
    const formData = new FormData(e.target);
    const userData = Object.fromEntries(formData.entries());
    await sendRequest('/users/register', 'POST', userData, '添加用户失败', () => loadUsers());
}

async function handleEditUser(e, id) {
        e.preventDefault();
        const formData = new FormData(e.target);
    const userData = Object.fromEntries(formData.entries());
    if (!userData.password) delete userData.password;
    await sendRequest(`/users/${id}`, 'PUT', userData, '更新用户失败', () => loadUsers());
}

async function handleAddNotification(e) {
    e.preventDefault();
    const formData = new FormData(e.target);
    const notificationData = Object.fromEntries(formData.entries());
    await sendRequest('/notifications', 'POST', notificationData, '添加通知失败', () => loadNotifications());
}

async function handleEditNotification(e, id) {
    e.preventDefault();
    const formData = new FormData(e.target);
    const notificationData = Object.fromEntries(formData.entries());
    await sendRequest(`/notifications/${id}`, 'PUT', notificationData, '更新通知失败', () => loadNotifications());
}

// Delete Functions
async function deleteEmployee(id) {
    if (confirm('确定要删除这个员工吗？')) {
        await sendRequest(`/employees/${id}`, 'DELETE', null, '删除员工失败', () => loadEmployees());
    }
}

async function deleteDepartment(id) {
    if (confirm('确定要删除这个部门吗？')) {
        await sendRequest(`/departments/${id}`, 'DELETE', null, '删除部门失败', () => loadDepartments());
    }
}

async function deleteUser(id) {
    if (confirm('确定要删除这个用户吗？')) {
        await sendRequest(`/users/${id}`, 'DELETE', null, '删除用户失败', () => loadUsers());
    }
}

async function deleteNotification(id) {
    if (confirm('确定要删除这个通知吗？')) {
        await sendRequest(`/notifications/${id}`, 'DELETE', null, '删除通知失败', () => loadNotifications());
    }
}

// Utility Functions
async function sendRequest(endpoint, method, body, errorMessage, successCallback) {
    try {
        const options = { method, headers: { 'Content-Type': 'application/json' } };
        if (body) options.body = JSON.stringify(body);
        const response = await fetch(`${API_BASE}${endpoint}`, options);
            const result = await response.json();
            if (result.success) {
            closeModal();
            successCallback();
            } else {
                alert(result.message);
            }
        } catch (error) {
        alert(errorMessage);
    }
}

async function loadDepartmentOptions(selectedId) {
    const deptSelect = document.getElementById('empDept');
        const response = await fetch(`${API_BASE}/departments`);
        const departments = await response.json();
        deptSelect.innerHTML = '<option value="">请选择部门</option>';
        departments.forEach(dept => {
            const option = document.createElement('option');
            option.value = dept.id;
            option.textContent = dept.name;
        if (dept.id === selectedId) option.selected = true;
            deptSelect.appendChild(option);
        });
}

async function loadPositionOptions(selectedId) {
    const posSelect = document.getElementById('empPosition');
        const response = await fetch(`${API_BASE}/positions`);
        const positions = await response.json();
        posSelect.innerHTML = '<option value="">请选择职位</option>';
        positions.forEach(pos => {
            const option = document.createElement('option');
            option.value = pos.id;
            option.textContent = pos.name;
        if (pos.id === selectedId) option.selected = true;
            posSelect.appendChild(option);
        });
}

// 根据选择的部门更新职位选项
async function updatePositionOptions() {
    const deptSelect = document.getElementById('empDept');
    const posSelect = document.getElementById('empPosition');
    
    if (!deptSelect || !posSelect) {
        return;
    }
    
    const selectedDeptId = deptSelect.value;
    
    if (!selectedDeptId) {
        posSelect.innerHTML = '<option value="">请先选择部门</option>';
        return;
    }
    
    try {
        const response = await fetch(`${API_BASE}/positions/department/${selectedDeptId}`);
        const positions = await response.json();
        
        posSelect.innerHTML = '<option value="">请选择职位</option>';
        
        if (positions.length === 0) {
            posSelect.innerHTML += '<option value="" disabled>该部门暂无职位</option>';
            return;
        }
        
        positions.forEach(pos => {
            const option = document.createElement('option');
            option.value = pos.id;
            option.textContent = pos.name;
            posSelect.appendChild(option);
        });
        
    } catch (error) {
        console.error('加载职位选项失败:', error);
        posSelect.innerHTML = '<option value="">加载职位失败</option>';
    }
}

// 为指定部门加载职位选项（用于编辑员工时）
async function loadPositionOptionsForDepartment(departmentId, selectedPositionId) {
    const posSelect = document.getElementById('empPosition');
    
    if (!posSelect) {
        return;
    }
    
    try {
        const response = await fetch(`${API_BASE}/positions/department/${departmentId}`);
        const positions = await response.json();
        
        posSelect.innerHTML = '<option value="">请选择职位</option>';
        
        if (positions.length === 0) {
            posSelect.innerHTML += '<option value="" disabled>该部门暂无职位</option>';
            return;
        }
        
        positions.forEach(pos => {
            const option = document.createElement('option');
            option.value = pos.id;
            option.textContent = pos.name;
            if (pos.id === selectedPositionId) {
                option.selected = true;
            }
            posSelect.appendChild(option);
        });
        
    } catch (error) {
        console.error('加载职位选项失败:', error);
        posSelect.innerHTML = '<option value="">加载职位失败</option>';
    }
}

// 岗位管理相关函数
async function loadPositionCount(departmentId) {
    try {
        const response = await fetch(`${API_BASE}/positions/department/${departmentId}`);
        const positions = await response.json();
        const countElement = document.getElementById(`posCount-${departmentId}`);
        if (countElement) {
            countElement.textContent = positions.length;
            countElement.style.color = positions.length > 0 ? '#28a745' : '#6c757d';
        }
    } catch (error) {
        const countElement = document.getElementById(`posCount-${departmentId}`);
        if (countElement) {
            countElement.textContent = '错误';
            countElement.style.color = '#dc3545';
        }
    }
}




function showAlert(elementId, message, type) {
    const alertElement = document.getElementById(elementId);
    alertElement.textContent = message;
    alertElement.className = `alert alert-${type}`;
    alertElement.classList.remove('hidden');
    setTimeout(() => alertElement.classList.add('hidden'), 3000);
}

function logout() {
    if (confirm('确定要退出登录吗？')) {
        currentUser = null;
        localStorage.removeItem('currentUser');
        showLoginForm();
    }
}
