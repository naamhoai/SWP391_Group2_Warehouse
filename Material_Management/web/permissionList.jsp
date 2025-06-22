<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="vi">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Quản lý quyền hệ thống</title>
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/css/all.min.css">
        <link rel="stylesheet" href="css/permissionList.css">
    </head>
    <body>
        <div class="main-content">
            <div class="permission-list-container">
                <div class="header-section">
                    <h2 class="page-title">
                        <i class="fas fa-key"></i>
                        Quản lý quyền hệ thống
                    </h2>
                    <div class="theme-toggle">
                        <button id="themeToggle" aria-label="Toggle theme">
                            <i class="fas fa-moon"></i>
                        </button>
                    </div>
                </div>
                <div style="overflow-x:auto;">
                    <table class="permission-table">
                        <thead>
                            <tr>
                                <th>Chức năng</th>
                                    <c:forEach var="role" items="${roles}">
                                    <th style="text-align:center;">
                                        <span style="display:inline-flex;align-items:center;gap:5px;">
                                            ${role.rolename}
                                            <a href="userPermission?roleId=${role.roleid}" class="edit-role-btn" title="Chỉnh sửa quyền">
                                                <i class="fas fa-edit"></i>
                                            </a>
                                        </span>
                                    </th>
                                </c:forEach>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="permission" items="${allPermissions}">
                                <tr>
                                    <td class="permission-name">
                                        <b>${permission.permissionName}</b>
                                        <br>
                                        <span class="permission-desc">${permission.description}</span>
                                    </td>
                                    <c:forEach var="role" items="${roles}">
                                        <c:set var="rolePerms" value="${rolePermissions[role.roleid]}" />
                                        <td>
                                            <c:if test="${rolePerms[permission.permissionName]}">
                                                <i class="fas fa-check-circle"></i>
                                            </c:if>
                                        </td>
                                    </c:forEach>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </div>
                <div class="back-button-container" style="margin-top: 30px;">
                    <a href="adminDashboard.jsp" class="back-button">
                        <i class="fas fa-arrow-left"></i>
                        Quay lại Dashboard
                    </a>
                    <a href="permissionLogs" class="back-button">
                        <i class="fas fa-history"></i>
                        Xem lịch sử 
                    </a>
                </div>
            </div>
        </div>
        <script>
            const themeToggle = document.getElementById('themeToggle');
            if (themeToggle) {
                themeToggle.addEventListener('click', function () {
                    document.body.classList.toggle('dark-theme');
                    const isDark = document.body.classList.contains('dark-theme');
                    themeToggle.innerHTML = isDark
                            ? '<i class="fas fa-sun"></i>'
                            : '<i class="fas fa-moon"></i>';
                    localStorage.setItem('theme', isDark ? 'dark' : 'light');
                });

                if (localStorage.getItem('theme') === 'dark') {
                    document.body.classList.add('dark-theme');
                    themeToggle.innerHTML = '<i class="fas fa-sun"></i>';
                }
            }
        </script>
    </body>
</html>
