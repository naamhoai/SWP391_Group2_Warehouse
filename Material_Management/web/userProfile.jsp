<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html lang="vi">
    <head>
        <meta charset="UTF-8" />
        <title>Thông tin cá nhân</title>
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <link rel="stylesheet" href="css/userProfile.css" />
        <link rel="stylesheet" href="css/dashboard.css">
        <link rel="stylesheet" href="css/footer.css">
        <link rel="stylesheet" href="css/sidebar.css">
        <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/css/all.min.css">

        <link href="https://fonts.googleapis.com/css2?family=Roboto:wght@400;500;700&display=swap" rel="stylesheet">
        <script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
        <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/flatpickr/dist/flatpickr.min.css">
        <script src="https://cdn.jsdelivr.net/npm/flatpickr"></script>
        <script src="https://cdn.jsdelivr.net/npm/flatpickr/dist/l10n/vi.js"></script>
        <!-- Font Awesome -->
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/css/all.min.css">

    </head>
    <body>
        <div class="layout">

            <%@include file="sidebar.jsp" %>
            <div class="user-center-wrapper">
                <div class="main-content" id="main-content">
                    <div class="form-wrapper">
                        <h2 class="form-title">Thông tin cá nhân</h2>
                        <c:if test="${not empty error}">
                            <div class="alert error">
                                <i class="fas fa-exclamation-circle"></i> ${error}
                            </div>
                        </c:if>
                        <c:if test="${not empty success}">
                            <div class="alert success">
                                <i class="fas fa-check-circle"></i> ${success}
                            </div>
                        </c:if>
                        <form>
                            <div class="avatar-container">
                                <c:choose>
                                    <c:when test="${not empty user.image}">
                                        <% 
                                            model.User userObj = (model.User) request.getAttribute("user");
                                            if (userObj != null) {
                                                System.out.println("[userProfile.jsp] Đường dẫn ảnh user: " + request.getServletContext().getRealPath(userObj.getImage()));
                                            }
                                        %>
                                        <img src="${pageContext.request.contextPath}${user.image}?v=<%= System.currentTimeMillis() %>" alt="Avatar" />
                                    </c:when>
                                    <c:otherwise>
                                        <img src="${pageContext.request.contextPath}/image/gender.png?v=<%= System.currentTimeMillis() %>" alt="Default Avatar" />
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
                </div>
            </div>
            <script src="js/sidebar.js"></script>
        </div>
        <c:if test="${param.updated == 'true'}">
            <script>
                var dashboardUrl = '${dashboardUrl}';
                history.replaceState(null, '', dashboardUrl);
            </script>
        </c:if>

        <script>
            // Tự động ẩn thông báo thành công sau 5 giây
            document.addEventListener('DOMContentLoaded', function () {
                const successAlert = document.querySelector('.alert.success');
                if (successAlert) {
                    setTimeout(() => {
                        successAlert.style.opacity = '0';
                        setTimeout(() => {
                            successAlert.remove();
                        }, 300);
                    }, 5000);
                }
            });
        </script>
    </body>
</html>
