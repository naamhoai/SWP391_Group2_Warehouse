<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Permission Change History</title>
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/css/all.min.css">
        <link rel="stylesheet" href="https://fonts.googleapis.com/css2?family=Poppins:wght@400;500;600;700&display=swap">
        <link rel="stylesheet" href="css/sidebar.css">
        <link rel="stylesheet" href="css/permissionLogs.css">
        <link rel="stylesheet" href="css/footer.css">
    </head>
    <body>
        <jsp:include page="sidebar.jsp" />
        <div class="main-content">
            <div class="logs-container">
                <div class="header-section">
                    <h2 class="page-title">
                        <i class="fas fa-history"></i> Lịch sử thay đổi quyền
                    </h2>
                    <div class="theme-toggle">
                        <button id="themeToggle" aria-label="Toggle theme">
                            <i class="fas fa-moon"></i> Dark
                        </button>
                    </div>
                </div>

                <div class="role-info">
                    <h3>Vai trò: ${role.rolename}</h3>
                </div>

                <div class="logs-table-container">
                    <table class="logs-table">
                        <thead>
                            <tr>
                                <th>Thời gian</th>
                                <th>Người thực hiện</th>
                                <th>Hành động</th>
                                <th>Quyền</th>
                                <th>Giá trị cũ</th>
                                <th>Giá trị mới</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach items="${logs}" var="log">
                                <tr>
                                    <td>
                                        <fmt:formatDate value="${log.logDate}" pattern="dd/MM/yyyy HH:mm:ss"/>
                                    </td>
                                    <td>${log.adminName}</td>
                                    <td>
                                        <span class="action-badge ${log.action == 'GRANT' ? 'grant' : 'revoke'}">
                                            ${log.action == 'GRANT' ? 'Cấp quyền' : 'Thu hồi quyền'}
                                        </span>
                                    </td>
                                    <td>${log.permissionName}</td>
                                    <td>
                                        <span class="value-badge ${log.oldValue ? 'true' : 'false'}">
                                            ${log.oldValue ? 'Có' : 'Không'}
                                        </span>
                                    </td>
                                    <td>
                                        <span class="value-badge ${log.newValue ? 'true' : 'false'}">
                                            ${log.newValue ? 'Có' : 'Không'}
                                        </span>
                                    </td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </div>

                <div class="back-button">
                    <a href="userPermission?roleId=${role.roleid}" class="btn-back">
                        <i class="fas fa-arrow-left"></i> Quay lại
                    </a>
                </div>
            </div>
        </div>

        <script>
            // Theme toggle functionality
            const themeToggle = document.getElementById('themeToggle');
            if (themeToggle) {
                themeToggle.addEventListener('click', function() {
                    document.body.classList.toggle('dark-theme');
                    const isDark = document.body.classList.contains('dark-theme');
                    themeToggle.innerHTML = isDark 
                        ? '<i class="fas fa-sun"></i> Light'
                        : '<i class="fas fa-moon"></i> Dark';
                    localStorage.setItem('theme', isDark ? 'dark' : 'light');
                });

                // Load saved theme
                if (localStorage.getItem('theme') === 'dark') {
                    document.body.classList.add('dark-theme');
                    themeToggle.innerHTML = '<i class="fas fa-sun"></i> Light';
                }
            }
        </script>
    </body>
</html> 