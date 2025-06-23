<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Lịch sử thay đổi quyền</title>
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/css/all.min.css">
        <link rel="stylesheet" href="https://fonts.googleapis.com/css2?family=Poppins:wght@400;500;600;700&display=swap">
        <link rel="stylesheet" href="css/permissionLogs.css">
    </head>
    <body>

        <div class="main-content">
            <div class="logs-container">
                <div class="header-section">
                    <h2 class="page-title">
                        <i class="fas fa-history"></i> Lịch sử thay đổi quyền
                    </h2>
                </div>

                <c:if test="${not empty error}">
                    <div class="error-message" style="background: #fee; color: #c33; padding: 10px; border-radius: 5px; margin-bottom: 20px;">
                        ${error}
                    </div>
                </c:if>

                <div class="logs-table-container">
                    <c:choose>
                        <c:when test="${empty logs}">
                            <div class="no-logs" style="text-align: center; padding: 40px; color: #666;">
                                <i class="fas fa-info-circle" style="font-size: 48px; margin-bottom: 20px;"></i>
                                <h3>Chưa có lịch sử thay đổi quyền</h3>
                                <p>Hiện chưa có thao tác nào được ghi nhận.</p>
                            </div>
                        </c:when>
                        <c:otherwise>
                            <table class="logs-table">
                                <thead>
                                    <tr>
                                        <th>Thời gian</th>
                                        <th>Người thực hiện</th>
                                        <th>Vai trò thay đổi</th>
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
                                                <fmt:formatDate value="${log.logDate}" pattern="dd/MM/yyyy HH:mm:ss" />
                                            </td>
                                            <td>${log.adminName}</td>
                                            <td>${log.roleName}</td>
                                            <td>
                                                <span class="action-badge ${log.action == 'GRANT' ? 'grant' : 'revoke'}">
                                                    ${log.action == 'GRANT' ? 'Cấp quyền' : 'Thu hồi'}
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
                        </c:otherwise>
                    </c:choose>
                </div>

                <div class="back-button">
                    <a href="permissionList" class="btn-back">
                        <i class="fas fa-arrow-left"></i> Quay lại
                    </a>
                </div>
            </div>
        </div>
    </body>
</html>
