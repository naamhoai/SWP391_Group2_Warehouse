<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8" />
    <title>Cập nhật thông tin cá nhân</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/updateuserprofile.css" />
</head>
<body>
    <div class="container">
        <h2>Cập nhật thông tin cá nhân</h2>

        <c:if test="${not empty error}">
            <div class="alert error">${error}</div>
        </c:if>
        <c:if test="${not empty success}">
            <div class="alert success">${success}</div>
        </c:if>

        <form action="${pageContext.request.contextPath}/UpdateUserProfileServlet" method="post" enctype="multipart/form-data">
            <input type="hidden" name="user_id" value="${user.user_id}" />

            <div class="current-image">
                <label>Ảnh đại diện hiện tại:</label><br />
                <c:choose>
                    <c:when test="${not empty user.image}">
                        <img src="${pageContext.request.contextPath}${user.image}" alt="Ảnh đại diện" />
                        <input type="hidden" name="existingImage" value="${user.image}" />
                    </c:when>
                    <c:otherwise>
                        <span>Chưa có ảnh đại diện</span>
                    </c:otherwise>
                </c:choose>
            </div>

            <label for="imageFile">Thay đổi ảnh đại diện:</label>
            <input type="file" id="imageFile" name="imageFile" accept="image/*" />

            <label for="username">Tên đăng nhập:</label>
            <input type="text" id="username" name="username" value="${user.username}" required />

            <label for="fullname">Họ và tên:</label>
            <input type="text" id="fullname" name="fullname" value="${user.fullname}" required />

            <label for="email">Email:</label>
            <input type="email" id="email" name="email" value="${user.email}" required />

            <label for="password">Mật khẩu: <small>(Nhập lại để đổi mật khẩu, để trống nếu không đổi)</small></label>
            <input type="password" id="password" name="password" />

            <label for="phone">Số điện thoại:</label>
            <input type="tel" id="phone" name="phone" value="${user.phone}" />

            <label for="role_id">Vai trò:</label>
            <input type="text" id="role_id" name="role_id" value="${user.role_id}" readonly />

            <label for="status">Trạng thái:</label>
            <input type="text" id="status" name="status" value="${user.status}" readonly />

            <label for="priority">Mức ưu tiên:</label>
            <input type="text" id="priority" name="priority" value="${user.priority}" readonly />

            <button type="submit">Cập nhật</button>
        </form>
    </div>
</body>
</html>
