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
    .card {
        background-color: #fff;
        padding: 25px;
        border-radius: 8px;
        box-shadow: 0 2px 10px rgba(0, 0, 0, 0.05);
        margin-bottom: 30px;
    }
    .form-inline {
        display: flex;
        gap: 15px;
    }
    .form-inline input[type="text"] {
        flex: 1;
        padding: 10px;
        border: 1px solid #ddd;
        border-radius: 4px;
        font-size: 16px;
    }
    .form-inline button {
        padding: 10px 20px;
        background-color: #28a745;
        border: none;
        border-radius: 4px;
        color: white;
        font-size: 16px;
        cursor: pointer;
        transition: background-color 0.3s;
    }
    .form-inline button:hover {
        background-color: #218838;
    }
    .data-table {
        width: 100%;
        border-collapse: collapse;
        background-color: #fff;
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
        color: #dc3545;
        text-decoration: none;
        font-weight: 500;
    }
    .action-buttons a:hover {
        text-decoration: underline;
    }
</style>

<div class="content-header">
    <h2>部门管理</h2>
</div>

<div class="card">
    <form action="<c:url value='/departments'/>" method="post" class="form-inline">
        <input type="text" name="name" placeholder="输入新部门名称..." required>
        <button type="submit">添加新部门</button>
    </form>
</div>

<div class="card">
    <table class="data-table">
        <thead>
            <tr>
                <th>部门ID</th>
                <th>部门名称</th>
                <th>操作</th>
            </tr>
        </thead>
        <tbody>
            <c:forEach items="${departments}" var="dept">
                <tr>
                    <td>${dept.id}</td>
                    <td>${dept.name}</td>
                    <td class="action-buttons">
                        <a href="<c:url value='/departments/delete/${dept.id}'/>">删除</a>
                    </td>
                </tr>
            </c:forEach>
        </tbody>
    </table>
</div>
