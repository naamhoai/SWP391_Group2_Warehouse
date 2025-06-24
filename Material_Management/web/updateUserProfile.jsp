<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html lang="vi">
    <head>
        <meta charset="UTF-8" />
        <title>Chỉnh sửa thông tin cá nhân</title>
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/updateUserProfile.css" />
        <!-- Font Awesome -->
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/css/all.min.css">
    </head>
    <body>
        <div class="form-wrapper">
            <h2 class="form-title">Chỉnh sửa thông tin cá nhân</h2>

            <c:if test="${not empty error}">
                <div class="alert error">${error}</div>
            </c:if>
            <c:if test="${not empty success}">
                <div class="alert success">${success}</div>
            </c:if>

            <form action="${pageContext.request.contextPath}/UpdateUserProfileServlet" method="post" enctype="multipart/form-data">
                <input type="hidden" name="user_id" value="${user.user_id}" />

                <div class="avatar-container">
                    <c:choose>
                        <c:when test="${not empty user.image}">
                            <img src="${pageContext.request.contextPath}${user.image}" alt="Avatar" id="avatarPreview" />
                            <input type="hidden" name="existingImage" value="${user.image}" />
                        </c:when>
                        <c:otherwise>
                            <img src="${pageContext.request.contextPath}/image/boy.png" alt="Avatar" id="avatarPreview" />
                        </c:otherwise>
                    </c:choose>
                    <label for="imageFile" class="custom-file-label"><i class="fas fa-upload"></i> Chọn ảnh đại diện</label>
                    <input type="file" id="imageFile" name="imageFile" accept="image/*" onchange="previewAvatar(event)" />
                </div>

                <div class="row">
                    <div class="column">
                        <label for="fullname">Họ và tên:</label>
                        <input type="text" id="fullname" name="fullname" value="${user.fullname}" required />
                    </div>
                    <div class="column">
                        <label for="password">Mật khẩu:</label>
                        <input type="password" id="password" name="password" placeholder="Nhập mật khẩu mới nếu muốn đổi" />
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
                            <option value="Men" ${user.gender == 'Men' ? 'selected' : ''}>Nam</option>
                            <option value="Women" ${user.gender == 'Women' ? 'selected' : ''}>Nữ</option>
                            <option value="Other" ${user.gender == 'Other' ? 'selected' : ''}>Khác</option>
                        </select>
                    </div>
                    <div class="column">
                        <label for="dayofbirth">Ngày sinh:</label>
                        <input type="date" id="dayofbirth" name="dayofbirth" value="${user.dayofbirth}" />
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
                
                <div class="buttons">
                    <a href="userProfile.jsp" class="btn-back"><i class="fas fa-arrow-left"></i> Quay lại</a>
                    <button type="submit" class="save-btn"><i class="fas fa-save"></i> Lưu thay đổi</button>
                </div>
                    
            </form>
        </div>
        <script>
        function previewAvatar(event) {
            const [file] = event.target.files;
            if (file) {
                document.getElementById('avatarPreview').src = URL.createObjectURL(file);
            }
        }
        </script>
    </body>
</html>
