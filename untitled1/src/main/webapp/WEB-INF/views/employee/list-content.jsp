<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<style>
    .content-header {
        display: flex;
        justify-content: space-between;
        align-items: center;
        margin-bottom: 20px;
    }
    .content-header h2 {
        margin: 0;
        font-size: 24px;
    }
    .content-header .btn-primary {
        padding: 10px 20px;
        background-color: #007bff;
        border: none;
        border-radius: 4px;
        color: white;
        font-size: 16px;
        cursor: pointer;
        text-decoration: none;
        transition: background-color 0.3s;
    }
    .content-header .btn-primary:hover {
        background-color: #0056b3;
    }
    .data-table {
        width: 100%;
        border-collapse: collapse;
        background-color: #fff;
        box-shadow: 0 2px 10px rgba(0, 0, 0, 0.05);
        border-radius: 8px;
        overflow: hidden;
    }
    .data-table th, .data-table td {
        padding: 15px;
        text-align: left;
        border-bottom: 1px solid #f0f0f0;
    }
    .data-table thead {
        background-color: #f7f9fc;
    }
    .data-table th {
        font-weight: 600;
        color: #333;
    }
    .data-table tbody tr:hover {
        background-color: #f7f9fc;
    }
    .action-buttons a {
        color: #007bff;
        text-decoration: none;
        margin-right: 15px;
        font-weight: 500;
    }
    .action-buttons a:hover {
        text-decoration: underline;
    }
    .action-buttons a.delete {
        color: #dc3545;
    }
</style>

<div class="content-header">
    <h2>员工档案管理</h2>
    <a href="<c:url value='/employees/new'/>" class="btn-primary ajax-link">添加新员工</a>
</div>

<div class="card">
    <table class="data-table">
        <thead>
            <tr>
                <th>员工编号</th>
                <th>姓名</th>
                <th>所属部门</th>
                <th>岗位</th>
                <th>入职日期</th>
                <th>在职状态</th>
                <th>操作</th>
            </tr>
        </thead>
        <tbody>
            <c:forEach items="${employees}" var="employee">
                <tr>
                    <td>${employee.employeeId}</td>
                    <td>${employee.name}</td>
                    <td>${employee.department.name}</td>
                    <td>${employee.position}</td>
                    <td>${employee.hireDate}</td>
                    <td>${employee.status.displayName}</td>
                    <td class="action-buttons">
                        <a href="#">详情</a>
                        <a href="#" class="delete">删除</a>
                    </td>
                </tr>
            </c:forEach>
        </tbody>
    </table>
</div>
