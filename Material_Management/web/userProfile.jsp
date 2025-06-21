<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html lang="vi">
    <head>
        <meta charset="UTF-8" />
        <title>Thông tin người dùng</title>
        <link rel="stylesheet" href="css/userProfile.css" />
        <link rel="stylesheet" href="css/footer.css">
        <link rel="stylesheet" href="css/sidebar.css">
        <!-- Font Awesome -->
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/css/all.min.css">
    </head>
    <body>
        <div class="layout">
            <!-- Include Sidebar -->
            <jsp:include page="sidebar.jsp" />
            <div class="main-content">
                <div class="form-wrapper">
                    <div class="page-header">
                        <h2 class="form-title">Thông tin người dùng</h2>

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
                                        <img src="${pageContext.request.contextPath}${user.image}" alt="Avatar" />
                                    </c:when>
                                    <c:otherwise>
                                        <img src="${pageContext.request.contextPath}/image/boy.png" alt="Avatar" />
                                    </c:otherwise>
                                </c:choose>
                                <!-- Disabled file input as it shouldn't be editable -->
                                <input type="file" id="imageFile" name="imageFile" accept="image/*" disabled />
                            </div>


                            <div class="row">
                                <div class="column">
                                    <label for="fullname">Họ và tên:</label>
                                    <input type="text" id="fullname" name="fullname" value="${user.fullname}" readonly />
                                </div>
                                <div class="column">
                                    <label for="password">Mật khẩu:</label>
                                    <input type="password" id="password" name="password" value="${user.password}" readonly />
                                </div>
                            </div>

                            <div class="row">
                                <div class="column">
                                    <label for="email">Email:</label>
                                    <input type="text" id="email" name="email" value="${user.email}" readonly />
                                </div>
                                <div class="column">
                                    <label for="phone">Số điện thoại:</label>
                                    <input type="tel" id="phone" name="phone" value="${user.phone}" readonly />
                                </div>
                            </div>

                            <div class="row">
                                <div class="column">
                                    <label for="gender">Giới tính:</label>
                                    <input type="text" id="gender" name="gender" value="${user.gender}" readonly />
                                </div>
                                <div class="column">
                                    <label for="dayofbirth">Ngày sinh:</label>
                                    <input type="date" id="dayofbirth" name="dayofbirth" value="${user.dayofbirth}" readonly />
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
                                <a href="${pageContext.request.contextPath}/UpdateUserProfileServlet?userId=${user.user_id}" class="btn edit-btn">Chỉnh sửa</a>
                            </div>
                        </form>
                    </div>
                </div>
            </div>
        </div>
    </body>
</html>
