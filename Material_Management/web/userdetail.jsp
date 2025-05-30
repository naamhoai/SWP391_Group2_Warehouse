<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html lang="vi">
    <head>
        <meta charset="UTF-8" />
        <title>Danh sách người dùng</title>
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/userdetail.css" />
    </head>
    <body>
        <div class="user-list-container">
            <h2>Danh sách người dùng</h2>
            <div class="search-container">
                <form method="get" action="UserDetailServlet">
                    <input type="text" name="search" placeholder="Tìm theo username hoặc họ và tên" 
                           value="${param.search != null ? param.search : ''}" />
                    <button type="submit">Tìm kiếm</button>
                </form>
            </div>
            <div class="top-right">
                <a href="${pageContext.request.contextPath}/CreateUserServlet">New User</a>
            </div>
            <table>
                <thead>
                    <tr>
                        <th>Username</th>
                        <th>Họ và tên</th>
                        <th>Email</th>
                        <th>Trạng thái</th>
                        <th>Hành động</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="user" items="${userList}">
                        <tr>
                            <td><c:out value="${user.username}" /></td>
                            <td><c:out value="${user.fullname}" /></td>
                            <td><c:out value="${user.email}" /></td>
                            <td><c:out value="${user.status}" /></td>
                            <td><a href="${pageContext.request.contextPath}/UpdateUserProfileServlet?user_id=${user.user_id}">[Xem chi tiết]</a></td>

                        </tr>
                    </c:forEach>
                    <c:if test="${empty userList}">
                        <tr>
                            <td colspan="5" style="text-align:center;">Không có dữ liệu người dùng</td>
                        </tr>
                    </c:if>
                </tbody>
            </table>
        </div>
    </body>
</html>
