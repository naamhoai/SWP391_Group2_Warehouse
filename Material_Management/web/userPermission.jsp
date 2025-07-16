<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/userPermission.css"/>

<html>
    <head>
        <title>Quản lý phân quyền vai trò</title>
    </head>
    <body>
        <div class="main-content">
            <div class="permission-container">
                <div class="header-section">
                    <h2 class="page-title">Phân quyền cho vai trò: <c:out value="${role.rolename}" /></h2>
                </div>
                <c:if test="${not empty error}">
                    <div class="error">${error}</div>
                </c:if>
                <c:if test="${not empty success}">
                    <div class="success">${success}</div>
                </c:if>
                <form method="post" action="userPermission">
                    <input type="hidden" name="roleId" value="${role.roleid}" />
                    <table>
                        <tr>
                            <th>STT</th>
                            <th>Tên quyền</th>
                            <th>Đã cấp</th>
                        </tr>
                        <c:forEach var="perm" items="${permissions}" varStatus="loop">
                            <tr>
                                <td>${loop.index + 1}</td>
                                <td>${perm.permissionName}</td>
                                <td style="text-align: center;">
                                    <input type="checkbox" name="permissions" value="${perm.permissionName}"
                                           <c:if test="${rolePermissions[perm.permissionName]}">checked</c:if> />
                                    </td>
                                </tr>
                        </c:forEach>
                    </table>
                    <div class="form-actions">
                        <button type="submit" class="save-btn">Lưu thay đổi</button>
                        <input type="button" class="back-btn" value="Quay lại danh sách quyền"
                               onclick="window.location.href = 'permissionList'" />
                        <input type="button" class="back-btn" value="Xem lịch sử"
                               onclick="window.location.href = 'permissionLogs?role=${role.roleid}'" />
                    </div>
                </form>
            </div>
        </div>
    </body>
</html>