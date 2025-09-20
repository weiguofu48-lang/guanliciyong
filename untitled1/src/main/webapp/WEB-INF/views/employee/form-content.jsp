<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

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
    .form-card {
        background-color: #fff;
        padding: 30px;
        border-radius: 8px;
        box-shadow: 0 2px 10px rgba(0, 0, 0, 0.05);
    }
    .form-grid {
        display: grid;
        grid-template-columns: repeat(2, 1fr);
        gap: 20px;
    }
    .form-group {
        display: flex;
        flex-direction: column;
    }
    .form-group label {
        margin-bottom: 8px;
        font-weight: 600;
        color: #333;
    }
    .form-group input, .form-group select {
        padding: 10px;
        border: 1px solid #ddd;
        border-radius: 4px;
        font-size: 16px;
        width: 100%;
        box-sizing: border-box;
    }
    .form-actions {
        margin-top: 30px;
        display: flex;
        gap: 15px;
    }
    .btn-primary, .btn-secondary {
        padding: 12px 25px;
        border: none;
        border-radius: 4px;
        font-size: 16px;
        cursor: pointer;
        text-decoration: none;
        text-align: center;
        transition: background-color 0.3s;
    }
    .btn-primary {
        background-color: #007bff;
        color: white;
    }
    .btn-primary:hover {
        background-color: #0056b3;
    }
    .btn-secondary {
        background-color: #6c757d;
        color: white;
    }
    .btn-secondary:hover {
        background-color: #5a6268;
    }
</style>

<div class="content-header">
    <h2>${pageTitle}</h2>
</div>

<div class="form-card">
    <form:form modelAttribute="employee" action="/employees" method="post">
        <div class="form-grid">
            <div class="form-group">
                <label for="employeeId">员工编号</label>
                <form:input path="employeeId" id="employeeId" required="true"/>
            </div>
            <div class="form-group">
                <label for="name">姓名</label>
                <form:input path="name" id="name" required="true"/>
            </div>
            <div class="form-group">
                <label for="gender">性别</label>
                <form:select path="gender" id="gender" required="true">
                    <form:option value="MALE">男</form:option>
                    <form:option value="FEMALE">女</form:option>
                </form:select>
            </div>
            <div class="form-group">
                <label for="hireDate">入职日期</label>
                <form:input path="hireDate" id="hireDate" type="date" required="true"/>
            </div>
            <div class="form-group">
                <label for="department">所属部门</label>
                <form:select path="department.id" id="department" required="true">
                    <c:forEach items="${departments}" var="dept">
                        <form:option value="${dept.id}">${dept.name}</form:option>
                    </c:forEach>
                </form:select>
            </div>
            <div class="form-group">
                <label for="position">岗位</label>
                <form:input path="position" id="position" required="true"/>
            </div>
             <div class="form-group">
                <label for="status">在职状态</label>
                <form:select path="status" id="status" required="true">
                    <form:option value="ON_PROBATION">试用期</form:option>
                    <form:option value="REGULAR">正式</form:option>
                </form:select>
            </div>
        </div>
        <div class="form-actions">
            <button type="submit" class="btn-primary">保存</button>
            <a href="<c:url value='/employees'/>" class="btn-secondary">取消</a>
        </div>
    </form:form>
</div>
