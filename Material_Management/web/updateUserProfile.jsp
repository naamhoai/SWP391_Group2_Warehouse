<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page import="model.User" %>
<!DOCTYPE html>
<html lang="vi">
    <head>
        <meta charset="UTF-8" />
        <title>Chỉnh sửa thông tin cá nhân</title>

        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/updateUserProfile.css" />
        <link rel="stylesheet" href="css/footer.css">
        <link rel="stylesheet" href="css/sidebar.css">
        <!-- Font Awesome -->
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/css/all.min.css">
    </head>
    <body>
        <div class="layout">
            <c:choose>
                <c:when test="${sessionScope.roleId == 1}">
                    <jsp:include page="sidebar.jsp" />
                </c:when>
                <c:when test="${sessionScope.roleId == 4}">
                    <div class="sidebar">
                        <div class="sidebar-header">
                            <div class="logo-container" id="sidebarToggle">
                                <i class="fas fa-bars menu-icon"></i>
                                <span class="logo-text">Danh Mục</span>
                            </div>
                        </div>
                        <ul class="sidebar-menu">
                            <li class="menu-item">
                                <a href="${pageContext.request.contextPath}/UserDetailServlet?userId=${sessionScope.userId}" class="menu-link">
                                    <i class="fas fa-tachometer-alt menu-icon"></i>
                                    <span class="menu-text">Thông tin người dùng</span>
                                </a>
                            </li>
                            <li class="menu-item">
                                <a href="${pageContext.request.contextPath}/MaterialListServlet" class="menu-link">
                                    <i class="fas fa-clipboard-list menu-icon"></i>
                                    <span class="menu-text">Danh Sách Vật Tư</span>
                                </a>
                            </li>
                            <li class="menu-item">
                                <a href="${pageContext.request.contextPath}/suppliers" class="menu-link">
                                    <i class="fas fa-shopping-cart menu-icon"></i>
                                    <span class="menu-text">Nhà Cung Cấp</span>
                                </a>
                            </li>
                            <li class="menu-item">
                                <a href="${pageContext.request.contextPath}/createRequest" class="menu-link">
                                    <i class="fas fa-warehouse menu-icon"></i>
                                    <span class="menu-text">Tạo yêu cầu</span>
                                </a>
                            </li>
                            <li class="menu-item">
                                <a href="${pageContext.request.contextPath}/RequestListServlet" class="menu-link">
                                    <i class="fas fa-truck menu-icon"></i>
                                    <span class="menu-text">Lịch sử yêu cầu</span>
                                </a>
                            </li>
                            <li class="menu-item">
                                <a href="homepage.jsp" class="menu-link">
                                    <i class="fas fa-sign-out-alt menu-icon"></i>
                                    <span class="menu-text">Đăng Xuất</span>
                                </a>
                            </li>
                        </ul>
                    </div>
                </c:when>
                
            </c:choose>
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

                            <form action="${pageContext.request.contextPath}/UpdateUserProfileServlet" method="post" enctype="multipart/form-data">
                                <input type="hidden" name="user_id" value="${user.user_id}" />
                                <input type="hidden" id="originalFullname" value="${user.fullname}">

                                <div class="avatar-container">
                                    <c:choose>
                                        <c:when test="${not empty user.image}">
                                            <img id="avatarPreview" src="${pageContext.request.contextPath}${user.image}?v=<%= System.currentTimeMillis() %>" alt="Avatar" />
                                        </c:when>
                                        <c:otherwise>
                                            <img id="avatarPreview" src="${pageContext.request.contextPath}/image/boy.png?v=<%= System.currentTimeMillis() %>" alt="Avatar" />
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
                                        <input type="tel" id="phone" name="phone" value="${user.phone}" />
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
                                        <label for="dayofbirth">Ngày sinh:</label>
                                        <input type="date" id="dayofbirth" name="dayofbirth" value="${user.dayofbirth}" />
                                    </div>
                                    <div class="column">
                                        <label for="role_name">Vai trò:</label>
                                        <input type="text" id="role_name" name="role_name" value="${user.role.rolename}" readonly />
                                    </div>
                                </div>

                                <div class="row">

                                    <div class="column">
                                        <label for="status">Trạng thái:</label>
                                        <input type="text" id="status" name="status" value="${user.status}" readonly />
                                    </div>
                                </div>


                                <div class="buttons">

                                    <a href="${pageContext.request.contextPath}/UserDetailServlet" class="btn-back"></i> Huỷ</a>
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
