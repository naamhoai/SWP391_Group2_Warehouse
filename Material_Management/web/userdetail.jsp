<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>Chi tiết người dùng</title>
    <style>
        body {
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
            background-color: #f0f2f5;
            margin: 0;
            display: flex;
            justify-content: center;
            align-items: center;
            min-height: 100vh;
        }

        .profile-container {
            background-color: #1e293b;
            color: white;
            padding: 40px 50px;
            border-radius: 16px;
            width: 500px;
            box-shadow: 0 10px 25px rgba(0, 0, 0, 0.2);
        }

        .profile-container h2 {
            text-align: center;
            margin-bottom: 30px;
        }

        .profile-row {
            display: flex;
            flex-direction: column;
            margin-bottom: 15px;
        }

        .profile-label {
            font-weight: bold;
            margin-bottom: 5px;
            color: #cbd5e1;
        }

        .profile-value {
            background-color: #334155;
            padding: 10px 14px;
            border-radius: 8px;
            color: #f1f5f9;
        }

        .avatar {
            display: flex;
            justify-content: center;
            margin-bottom: 25px;
        }

        .avatar img {
            width: 120px;
            height: 120px;
            border-radius: 50%;
            object-fit: cover;
            border: 3px solid #64748b;
        }

        .actions {
            text-align: center;
            margin-top: 30px;
        }

        .actions a {
            color: #60a5fa;
            text-decoration: none;
            font-weight: bold;
            transition: color 0.3s;
        }

        .actions a:hover {
            color: #3b82f6;
        }

    </style>
</head>
<body>
    <div class="profile-container">

        <div class="avatar">
            <c:choose>
                <c:when test="${not empty user.image}">
                    <c:choose>
                        <c:when test="${fn:startsWith(user.image, 'http')}">
                            <img src="${user.image}" alt="Ảnh đại diện" />
                        </c:when>
                        <c:otherwise>
                            <img src="${pageContext.request.contextPath}${user.image}" alt="Ảnh đại diện" />
                        </c:otherwise>
                    </c:choose>
                </c:when>
                <c:otherwise>
                    <img src="https://via.placeholder.com/120x120?text=No+Image" alt="Ảnh mặc định" />
                </c:otherwise>
            </c:choose>
        </div>

        <h2>Hồ sơ Người dùng</h2>

        <div class="profile-row">
            <div class="profile-label">ID</div>
            <div class="profile-value">${user.user_id}</div>
        </div>

        <div class="profile-row">
            <div class="profile-label">Họ và tên</div>
            <div class="profile-value">${user.fullname}</div>
        </div>

        <div class="profile-row">
            <div class="profile-label">Email</div>
            <div class="profile-value">${user.email}</div>
        </div>

        <div class="profile-row">
            <div class="profile-label">Tên đăng nhập</div>
            <div class="profile-value">${user.username}</div>
        </div>

        <div class="profile-row">
            <div class="profile-label">Số điện thoại</div>
            <div class="profile-value">${user.phone}</div>
        </div>

        <div class="profile-row">
            <div class="profile-label">Vai trò</div>
            <div class="profile-value">
                <c:choose>
                    <c:when test="${not empty roleName}">
                        ${roleName}
                    </c:when>
                    <c:otherwise>Chưa gán vai trò</c:otherwise>
                </c:choose>
            </div>
        </div>

        <div class="actions">
            <a href="${pageContext.request.contextPath}/updateUserProfileServlet?user_id=${user.user_id}">Cập nhật hồ sơ</a>
        </div>

    </div>
</body>
</html>
