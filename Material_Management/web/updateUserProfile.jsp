<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page import="model.User" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<!DOCTYPE html>
<html lang="vi">
    <head>
        <meta charset="UTF-8" />
        <title>Chỉnh sửa thông tin cá nhân</title>

        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <link rel="stylesheet" href="css/updateUserProfile.css" />
        <link rel="stylesheet" href="css/footer.css">
        <link rel="stylesheet" href="css/sidebar.css">
        <!-- Font Awesome -->
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/css/all.min.css">
    </head>
    <body>
        <div class="layout">
            <%@include file="sidebar.jsp" %>
            <div class="user-center-wrapper">
                <div class="main-content" id="main-content">
                    <div class="form-wrapper">
                        <div class="page-header">
                            <h2 class="form-title">Chỉnh sửa thông tin cá nhân</h2>

                            <c:if test="${not empty error}">
                                <div class="alert error">
                                    <i class="fas fa-exclamation-circle"></i> ${error}
                                </div>
                            </c:if>

                            <form action="${pageContext.request.contextPath}/UpdateUserProfileServlet" method="post" enctype="multipart/form-data" id="updateForm">
                                <input type="hidden" name="user_id" value="${user.user_id}" />
                                <input type="hidden" id="originalFullname" value="${user.fullname}">

                                <div class="avatar-container">
                                    <c:choose>
                                        <c:when test="${not empty user.image}">
                                            <img id="avatarPreview" src="${pageContext.request.contextPath}${user.image}?v=<%= System.currentTimeMillis() %>" alt="Avatar" />
                                        </c:when>
                                        <c:otherwise>
                                            <img id="avatarPreview" src="${pageContext.request.contextPath}/image/gender.png?v=<%= System.currentTimeMillis() %>" alt="Avatar" />
                                        </c:otherwise>
                                    </c:choose>
                                    <label for="imageFile" class="custom-file-label"><i class="fas fa-upload"></i> Chọn ảnh đại diện</label>
                                    <input type="file" id="imageFile" name="imageFile" accept="image/*" />
                                </div>

                                <div class="row">
                                    <div class="column">
                                        <label for="fullname">Họ và tên:</label>
                                        <input type="text" id="fullname" name="fullname" value="${user.fullname}" required />
                                    </div>
                                    <div class="column">
                                        <label for="email">Email:</label>
                                        <input type="text" id="email" name="email" value="${user.email}" readonly />
                                    </div>
                                </div>

                                <div class="row">


                                    <div class="column">
                                        <label for="phone">Số điện thoại:</label>
                                        <input type="tel" id="phone" name="phone" value="${user.phone}" placeholder="VD: 0123456789" 
                                               pattern="[0-9]+" 
                                               oninput="this.value = this.value.replace(/[^0-9]/g, '')" 
                                               maxlength="11" />
                                        <div class="error-message" id="phoneError"></div>
                                    </div>
                                    <div class="column">
                                        <label for="gender">Giới tính:</label>
                                        <select id="gender" name="gender" required>
                                            <option value="Nam" ${user.gender == 'Nam' ? 'selected' : ''}>Nam</option>
                                            <option value="Nữ" ${user.gender == 'Nữ' ? 'selected' : ''}>Nữ</option>
                                            <option value="Khác" ${user.gender == 'Khác' ? 'selected' : ''}>Khác</option>
                                        </select>
                                    </div>
                                </div>

                                <div class="row">
                                    <div class="column">
                                        <label for="status">Trạng thái:</label>
                                        <input type="text" id="status" name="status" value="${user.status}" readonly />
                                    </div>

                                    <div class="column">
                                        <label for="role_name">Vai trò:</label>
                                        <input type="text" id="role_name" name="role_name" value="${user.role.rolename}" readonly />
                                    </div>
                                </div>

                                <div class="buttons">

                                    <a href="${pageContext.request.contextPath}/UserDetailServlet" class="btn-back"> Huỷ</a>
                                    <button type="submit" class="btn save-btn">Lưu</button>
                                </div>

                            </form>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <script src="${pageContext.request.contextPath}/js/sidebar.js"></script>
        <script src="${pageContext.request.contextPath}/js/updateUserProfile.js"></script>
    </body>
</html>




