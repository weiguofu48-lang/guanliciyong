// 全局变量
let currentUser = null;
let currentView = 'dashboard';

// API基础URL
const API_BASE = '/untitled1/api';

// 页面加载完成后初始化
document.addEventListener('DOMContentLoaded', function() {
    // 检查是否已登录
    checkLoginStatus();
    
    // 绑定表单事件
    document.getElementById('loginForm').addEventListener('submit', handleLogin);
    document.getElementById('registerForm').addEventListener('submit', handleRegister);
});

// 检查登录状态
function checkLoginStatus() {
    // 这里可以检查localStorage或sessionStorage中的登录状态
    // 暂时显示登录表单
    showLoginForm();
}

// 显示登录表单
function showLoginForm() {
    document.getElementById('loginSection').classList.remove('hidden');
    document.getElementById('registerSection').classList.add('hidden');
    document.getElementById('mainApp').classList.add('hidden');
}

// 显示注册表单
function showRegisterForm() {
    document.getElementById('loginSection').classList.add('hidden');
    document.getElementById('registerSection').classList.remove('hidden');
    document.getElementById('mainApp').classList.add('hidden');
}

// 处理登录
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
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(loginData)
        });
        
        const result = await response.json();
        
        if (result.success) {
            currentUser = result.user;
            localStorage.setItem('currentUser', JSON.stringify(currentUser));
            showMainApp();
            showAlert('loginAlert', '登录成功！', 'success');
        } else {
            showAlert('loginAlert', result.message, 'danger');
        }
    } catch (error) {
        showAlert('loginAlert', '登录失败，请重试', 'danger');
        console.error('Login error:', error);
    }
}

// 处理注册
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
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(userData)
        });
        
        const result = await response.json();
        
        if (result.success) {
            showAlert('registerAlert', '注册成功！请登录', 'success');
            setTimeout(() => {
                showLoginForm();
            }, 1500);
        } else {
            showAlert('registerAlert', result.message, 'danger');
        }
    } catch (error) {
        showAlert('registerAlert', '注册失败，请重试', 'danger');
        console.error('Register error:', error);
    }
}

// 显示主应用界面
function showMainApp() {
    document.getElementById('loginSection').classList.add('hidden');
    document.getElementById('registerSection').classList.add('hidden');
    document.getElementById('mainApp').classList.remove('hidden');
    
    // 默认显示仪表板
    showDashboard();
}

// 显示提示信息
function showAlert(elementId, message, type) {
    const alertElement = document.getElementById(elementId);
    alertElement.textContent = message;
    alertElement.className = `alert alert-${type}`;
    alertElement.classList.remove('hidden');
    
    // 3秒后隐藏提示
    setTimeout(() => {
        alertElement.classList.add('hidden');
    }, 3000);
}

// 导航功能
function showDashboard() {
    currentView = 'dashboard';
    updateNavButtons('dashboardBtn');
    loadDashboard();
}

function showEmployees() {
    currentView = 'employees';
    updateNavButtons('employeesBtn');
    loadEmployees();
}

function showDepartments() {
    currentView = 'departments';
    updateNavButtons('departmentsBtn');
    loadDepartments();
}

function showUsers() {
    currentView = 'users';
    updateNavButtons('usersBtn');
    loadUsers();
}

function showNotifications() {
    currentView = 'notifications';
    updateNavButtons('notificationsBtn');
    loadNotifications();
}

// 更新导航按钮状态
function updateNavButtons(activeBtnId) {
    document.querySelectorAll('.nav-btn').forEach(btn => {
        btn.classList.remove('active');
    });
    document.getElementById(activeBtnId).classList.add('active');
}

// 加载仪表板
async function loadDashboard() {
    const content = document.getElementById('content');
    content.innerHTML = '<div class="loading"><div class="spinner"></div><p>加载中...</p></div>';
    
    try {
        const response = await fetch(`${API_BASE}/dashboard/stats`);
        const stats = await response.json();
        
        content.innerHTML = `
            <h2>仪表板</h2>
            <div style="display: grid; grid-template-columns: repeat(auto-fit, minmax(250px, 1fr)); gap: 20px; margin-top: 20px;">
                <div style="background: linear-gradient(135deg, #667eea 0%, #764ba2 100%); color: white; padding: 30px; border-radius: 10px; text-align: center;">
                    <h3>员工总数</h3>
                    <h1 style="font-size: 3em; margin: 10px 0;">${stats.employeeCount}</h1>
                </div>
                <div style="background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%); color: white; padding: 30px; border-radius: 10px; text-align: center;">
                    <h3>部门总数</h3>
                    <h1 style="font-size: 3em; margin: 10px 0;">${stats.departmentCount}</h1>
                </div>
            </div>
        `;
    } catch (error) {
        content.innerHTML = '<div class="alert alert-danger">加载仪表板数据失败</div>';
        console.error('Dashboard error:', error);
    }
}

// 加载员工列表
async function loadEmployees() {
    const content = document.getElementById('content');
    content.innerHTML = '<div class="loading"><div class="spinner"></div><p>加载中...</p></div>';
    
    try {
        const response = await fetch(`${API_BASE}/employees`);
        const employees = await response.json();
        
        let tableHTML = `
            <div style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 20px;">
                <h2>员工管理</h2>
                <button class="btn btn-success" onclick="showAddEmployeeModal()">添加员工</button>
            </div>
            <table class="table">
                <thead>
                    <tr>
                        <th>员工编号</th>
                        <th>姓名</th>
                        <th>性别</th>
                        <th>部门</th>
                        <th>职位</th>
                        <th>状态</th>
                        <th>入职日期</th>
                        <th>操作</th>
                    </tr>
                </thead>
                <tbody>
        `;
        
        employees.forEach(employee => {
            tableHTML += `
                <tr>
                    <td>${employee.employeeNumber || ''}</td>
                    <td>${employee.name || ''}</td>
                    <td>${employee.gender || ''}</td>
                    <td>${employee.department ? employee.department.name : ''}</td>
                    <td>${employee.position ? employee.position.name : ''}</td>
                    <td>${employee.status || ''}</td>
                    <td>${employee.hireDate || ''}</td>
                    <td>
                        <button class="btn btn-secondary" onclick="editEmployee(${employee.id})">编辑</button>
                        <button class="btn btn-danger" onclick="deleteEmployee(${employee.id})">删除</button>
                    </td>
                </tr>
            `;
        });
        
        tableHTML += '</tbody></table>';
        content.innerHTML = tableHTML;
    } catch (error) {
        content.innerHTML = '<div class="alert alert-danger">加载员工数据失败</div>';
        console.error('Employees error:', error);
    }
}

// 加载部门列表
async function loadDepartments() {
    const content = document.getElementById('content');
    content.innerHTML = '<div class="loading"><div class="spinner"></div><p>加载中...</p></div>';
    
    try {
        const response = await fetch(`${API_BASE}/departments`);
        const departments = await response.json();
        
        let tableHTML = `
            <div style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 20px;">
                <h2>部门管理</h2>
                <button class="btn btn-success" onclick="showAddDepartmentModal()">添加部门</button>
            </div>
            <table class="table">
                <thead>
                    <tr>
                        <th>部门编号</th>
                        <th>部门名称</th>
                        <th>描述</th>
                        <th>创建时间</th>
                        <th>操作</th>
                    </tr>
                </thead>
                <tbody>
        `;
        
        departments.forEach(department => {
            tableHTML += `
                <tr>
                    <td>${department.id}</td>
                    <td>${department.name}</td>
                    <td>${department.description || ''}</td>
                    <td>${department.createdAt || ''}</td>
                    <td>
                        <button class="btn btn-secondary" onclick="editDepartment(${department.id})">编辑</button>
                        <button class="btn btn-danger" onclick="deleteDepartment(${department.id})">删除</button>
                    </td>
                </tr>
            `;
        });
        
        tableHTML += '</tbody></table>';
        content.innerHTML = tableHTML;
    } catch (error) {
        content.innerHTML = '<div class="alert alert-danger">加载部门数据失败</div>';
        console.error('Departments error:', error);
    }
}

// 加载用户列表
async function loadUsers() {
    const content = document.getElementById('content');
    content.innerHTML = '<div class="loading"><div class="spinner"></div><p>加载中...</p></div>';
    
    try {
        const response = await fetch(`${API_BASE}/users`);
        const users = await response.json();
        
        let tableHTML = `
            <h2>用户管理</h2>
            <table class="table">
                <thead>
                    <tr>
                        <th>ID</th>
                        <th>用户名</th>
                        <th>真实姓名</th>
                        <th>邮箱</th>
                        <th>电话</th>
                        <th>角色</th>
                        <th>操作</th>
                    </tr>
                </thead>
                <tbody>
        `;
        
        users.forEach(user => {
            tableHTML += `
                <tr>
                    <td>${user.id}</td>
                    <td>${user.username}</td>
                    <td>${user.realName || ''}</td>
                    <td>${user.email || ''}</td>
                    <td>${user.phone || ''}</td>
                    <td>${user.role ? user.role.name : ''}</td>
                    <td>
                        <button class="btn btn-secondary" onclick="editUser(${user.id})">编辑</button>
                        <button class="btn btn-danger" onclick="deleteUser(${user.id})">删除</button>
                    </td>
                </tr>
            `;
        });
        
        tableHTML += '</tbody></table>';
        content.innerHTML = tableHTML;
    } catch (error) {
        content.innerHTML = '<div class="alert alert-danger">加载用户数据失败</div>';
        console.error('Users error:', error);
    }
}

// 加载通知列表
async function loadNotifications() {
    const content = document.getElementById('content');
    content.innerHTML = '<div class="loading"><div class="spinner"></div><p>加载中...</p></div>';
    
    try {
        const response = await fetch(`${API_BASE}/notifications`);
        const notifications = await response.json();
        
        let tableHTML = `
            <div style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 20px;">
                <h2>通知管理</h2>
                <button class="btn btn-success" onclick="showAddNotificationModal()">添加通知</button>
            </div>
            <table class="table">
                <thead>
                    <tr>
                        <th>标题</th>
                        <th>内容</th>
                        <th>类型</th>
                        <th>创建时间</th>
                        <th>操作</th>
                    </tr>
                </thead>
                <tbody>
        `;
        
        notifications.forEach(notification => {
            tableHTML += `
                <tr>
                    <td>${notification.title || ''}</td>
                    <td>${notification.content || ''}</td>
                    <td>${notification.type || ''}</td>
                    <td>${notification.createdAt || ''}</td>
                    <td>
                        <button class="btn btn-danger" onclick="deleteNotification(${notification.id})">删除</button>
                    </td>
                </tr>
            `;
        });
        
        tableHTML += '</tbody></table>';
        content.innerHTML = tableHTML;
    } catch (error) {
        content.innerHTML = '<div class="alert alert-danger">加载通知数据失败</div>';
        console.error('Notifications error:', error);
    }
}

// 模态框功能
function showModal(title, content) {
    const modal = document.getElementById('modal');
    const modalBody = document.getElementById('modalBody');
    modalBody.innerHTML = `<h3>${title}</h3>${content}`;
    modal.style.display = 'block';
}

function closeModal() {
    document.getElementById('modal').style.display = 'none';
}

// 添加部门模态框
function showAddDepartmentModal() {
    const content = `
        <form id="addDepartmentForm">
            <div class="form-group">
                <label for="deptName">部门名称:</label>
                <input type="text" id="deptName" name="name" required>
            </div>
            <div class="form-group">
                <label for="deptDesc">描述:</label>
                <textarea id="deptDesc" name="description" rows="4"></textarea>
            </div>
            <div style="text-align: right; margin-top: 20px;">
                <button type="button" class="btn btn-secondary" onclick="closeModal()">取消</button>
                <button type="submit" class="btn btn-success">添加</button>
            </div>
        </form>
    `;
    
    showModal('添加部门', content);
    
    document.getElementById('addDepartmentForm').addEventListener('submit', async function(e) {
        e.preventDefault();
        const formData = new FormData(e.target);
        const departmentData = {
            name: formData.get('name'),
            description: formData.get('description')
        };
        
        try {
            const response = await fetch(`${API_BASE}/departments`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify(departmentData)
            });
            
            const result = await response.json();
            
            if (result.success) {
                closeModal();
                loadDepartments();
            } else {
                alert(result.message);
            }
        } catch (error) {
            alert('添加部门失败');
            console.error('Add department error:', error);
        }
    });
}

// 删除功能
async function deleteDepartment(id) {
    if (confirm('确定要删除这个部门吗？')) {
        try {
            const response = await fetch(`${API_BASE}/departments/${id}`, {
                method: 'DELETE'
            });
            
            const result = await response.json();
            
            if (result.success) {
                loadDepartments();
            } else {
                alert(result.message);
            }
        } catch (error) {
            alert('删除部门失败');
            console.error('Delete department error:', error);
        }
    }
}

async function deleteEmployee(id) {
    if (confirm('确定要删除这个员工吗？')) {
        try {
            const response = await fetch(`${API_BASE}/employees/${id}`, {
                method: 'DELETE'
            });
            
            const result = await response.json();
            
            if (result.success) {
                loadEmployees();
            } else {
                alert(result.message);
            }
        } catch (error) {
            alert('删除员工失败');
            console.error('Delete employee error:', error);
        }
    }
}

async function deleteUser(id) {
    if (confirm('确定要删除这个用户吗？')) {
        try {
            const response = await fetch(`${API_BASE}/users/${id}`, {
                method: 'DELETE'
            });
            
            const result = await response.json();
            
            if (result.success) {
                loadUsers();
            } else {
                alert(result.message);
            }
        } catch (error) {
            alert('删除用户失败');
            console.error('Delete user error:', error);
        }
    }
}

async function deleteNotification(id) {
    if (confirm('确定要删除这个通知吗？')) {
        try {
            const response = await fetch(`${API_BASE}/notifications/${id}`, {
                method: 'DELETE'
            });
            
            const result = await response.json();
            
            if (result.success) {
                loadNotifications();
            } else {
                alert(result.message);
            }
        } catch (error) {
            alert('删除通知失败');
            console.error('Delete notification error:', error);
        }
    }
}

// 退出登录
function logout() {
    if (confirm('确定要退出登录吗？')) {
        currentUser = null;
        localStorage.removeItem('currentUser');
        showLoginForm();
    }
}

// 点击模态框外部关闭
window.onclick = function(event) {
    const modal = document.getElementById('modal');
    if (event.target == modal) {
        closeModal();
    }
}

