<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html lang="vi">
    <head>
        <meta charset="UTF-8" />
        <title>Cập nhật thông tin cá nhân</title>
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/updateuserprofile.css" />
    </head>
    <body>
        <div class="form-container">
            <h2>Cập nhật thông tin cá nhân</h2>

            <!-- Hiển thị thông báo lỗi hoặc thành công -->
            <c:if test="${not empty error}">
                <div class="alert error">${error}</div>
            </c:if>
            <c:if test="${not empty success}">
                <div class="alert success">${success}</div>
            </c:if>

            <!-- Form để cập nhật thông tin người dùng -->
            <form action="${pageContext.request.contextPath}/UpdateUserProfileServlet" method="post" enctype="multipart/form-data">
                <input type="hidden" name="user_id" value="${user.user_id}" />

                <!-- Ảnh đại diện -->
                <div class="avatar-container">
                    <c:choose>
                        <c:when test="${not empty user.image}">
                            <img src="${pageContext.request.contextPath}${user.image}" alt="Ảnh đại diện" />
                            <input type="hidden" name="existingImage" value="${user.image}" />
                        </c:when>
                        <c:otherwise>
                            <span>Chưa có ảnh đại diện</span>
                        </c:otherwise>
                    </c:choose>

                    <input type="file" id="imageFile" name="imageFile" accept="image/*" />
                </div>


                <!-- Họ và tên -->
                <div class="row full-width">
                    <label for="fullname">Họ và tên:</label>
                    <input type="text" id="fullname" name="fullname" value="${user.fullname}" required />
                </div>

                <div class="row">
                    <div class="column">
                        <label for="username">Tên đăng nhập:</label>
                        <input type="text" id="username" name="username" value="${user.username}" required />
                    </div>
                    <div class="column">
                        <label for="password">Mật khẩu:</label>
                        <input type="password" id="password" name="password" value="${user.password}" />
                    </div>
                </div>

                <div class="row">

                    <div class="column">
                        <label for="email">Email:</label>
                        <input type="text" id="email" name="email" value="${user.email}" readonly />
                    </div>

                    <div class="column">
                        <label for="phone">Số điện thoại:</label>
                        <input type="tel" id="phone" name="phone" value="${user.phone}" />
                    </div>
                </div>

                <div class="row">
                    <div class="column">
                        <label for="gender">Giới tính:</label>
                        <select id="gender" name="gender" required>
                            <option value="Nam" ${user.gender == 'Men' ? 'selected' : ''}>Nam</option>
                            <option value="Nữ" ${user.gender == 'Women' ? 'selected' : ''}>Nữ</option>
                            <option value="Khác" ${user.gender == 'Other' ? 'selected' : ''}>Khác</option>
                        </select>
                    </div>
                    <div class="column">
                        <label for="dayofbirth">Ngày sinh:</label>
                        <input type="date" id="dayofbirth" name="dayofbirth" value="${dob}" />
                    </div>

                </div>




                <div class="row">
                    <div class="column">
                        <label for="role_name">Vai trò:</label>
                        <input type="text" id="role_name" name="role_name" value="${user.role.rolename}" readonly />
                    </div>
                    <div class="column">
                        <label for="status">Trạng thái:</label>
                        <input type="text" id="status" name="status" value="${user.status}" readonly />
                    </div>
                </div>


                <div class="center-button">
                    <button type="submit">Cập nhật</button>
                </div>
            </form>
        </div>
    </body>
</html>
