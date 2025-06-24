<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html lang="vi">
    <head>
        <meta charset="UTF-8" />
        <title>Thông tin người dùng</title>
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/userProfile.css" />
        <link rel="stylesheet" href="css/footer.css">
        <link rel="stylesheet" href="css/dashboard.css">

        <!-- Font Awesome -->
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/css/all.min.css">

    </head>
    <body>
        <div class="form-wrapper">
            <h2 class="form-title">Thông tin người dùng</h2>

            <c:if test="${not empty error}">
                <div class="alert error">${error}</div>
            </c:if>
            <c:if test="${not empty success}">
                <div class="alert success">${success}</div>
            </c:if>

            <form>
                <div class="avatar-container">
                    <c:choose>
                        <c:when test="${not empty user.image}">
                            <img src="${pageContext.request.contextPath}${user.image}" alt="Avatar" />
                        </c:when>
                        <c:otherwise>
                            <img src="${pageContext.request.contextPath}/image/boy.png" alt="Default Avatar" />
                        </c:otherwise>
                    </c:choose>
                </div>

                <div class="row">
                    <div class="column">
                        <label for="fullname">Họ và tên:</label>
                        <input type="text" id="fullname" value="${user.fullname}" readonly />
                    </div>
                    <div class="column">
                        <label for="email">Email:</label>
                        <input type="text" id="email" value="${user.email}" readonly />
                    </div>
                </div>

                <div class="row">
                    <div class="column">
                        <label for="phone">Số điện thoại:</label>
                        <input type="tel" id="phone" value="${user.phone}" readonly />
                    </div>
                    <div class="column">
                        <label for="gender">Giới tính:</label>
                        <input type="text" id="gender" value="${user.gender}" readonly />
                    </div>
                </div>

                <div class="row">
                    <div class="column">
                        <label for="dayofbirth">Ngày sinh:</label>
                        <input type="date" id="dayofbirth" value="${user.dayofbirth}" readonly />
                    </div>
                    <div class="column">
                        <label for="role_name">Vai trò:</label>
                        <input type="text" id="role_name" value="${user.role.rolename}" readonly />
                    </div>
                </div>

                <div class="row">

                    <div class="column">
                        <label for="status">Trạng thái:</label>
                        <input type="text" id="status" value="${user.status}" readonly />
                    </div>
                </div>

                <div class="action-buttons">
                    <c:choose>
                        <c:when test="${sessionScope.roleId == 1}">
                            <a class="btn-back" href="adminDashboard.jsp"><i class="fas fa-arrow-left"></i> Quay lại</a>
                        </c:when>
                        <c:when test="${sessionScope.roleId == 2}">
                            <a class="btn-back" href="director"><i class="fas fa-arrow-left"></i> Quay lại</a>
                        </c:when>
                        <c:when test="${sessionScope.roleId == 3}">
                            <a class="btn-back" href="warehouseEmployeeDashboard"><i class="fas fa-arrow-left"></i> Quay lại</a>
                        </c:when>
                        <c:when test="${sessionScope.roleId == 4}">
                            <a class="btn-back" href="staffDashboard"><i class="fas fa-arrow-left"></i> Quay lại</a>
                        </c:when>
                        
                    </c:choose>

                    <a href="${pageContext.request.contextPath}/UpdateUserProfileServlet?userId=${user.user_id}" class="btn-edit">
                        <i class="fas fa-edit"></i> Chỉnh sửa
                    </a>
                </div>
            </form>
        </div>

        <c:if test="${param.updated == 'true'}">
            <script>
                var dashboardUrl = '${dashboardUrl}';
                history.replaceState(null, '', dashboardUrl);
            </script>
        </c:if>
    </body>
</html>
