<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html lang="vi">
    <head>
        <meta charset="UTF-8" />
        <title>User Information</title>

        <!-- Stylesheets -->
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/userdetail.css" />
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/footer.css" />
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/dashboard.css" />
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/sidebar.css" />

        <!-- Font Awesome -->
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/css/all.min.css" />
    </head>
    <body>

        <div class="layout">
            <!-- Include Sidebar -->
            <jsp:include page="side.jsp" />

            <div class="main-content">
                <div class="page-header">
                    <h2>User Information</h2>

                    <!-- Search form -->
                    <form method="get" action="UserDetailServlet">
                        <div class="filter-section">
                            <input 
                                type="text" 
                                name="search" 
                                placeholder="search by username or fullname" 
                                value="${param.search != null ? param.search : ''}" 
                                />
                            <button type="submit">Search</button>
                        </div>
                    </form>

                    <!-- Create new user link -->
                    <div class="top-right">
                        <a href="${pageContext.request.contextPath}/CreateUserServlet">Create User</a>
                    </div>

                    <!-- User list table -->
                    <div class="table-container">
                        <table>
                            <thead>
                                <tr>
                                    <th>User ID</th>
                                    <th>Full Name</th>
                                    <th>Email</th>
                                    <th>Role</th>
                                    <th>Status</th>
                                    <th>Action</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:forEach var="user" items="${userList}">
                                    <tr>
                                        <td><c:out value="${user.user_id}" /></td>
                                        <td><c:out value="${user.fullname}" /></td>
                                        <td><c:out value="${user.email}" /></td>
                                        <td><c:out value="${user.role.rolename}" /></td>
                                        <td><c:out value="${user.status}" /></td>
                                        <td class="action">
                                            <a href="${pageContext.request.contextPath}/UpdateUserProfileServlet?user_id=${user.user_id}">[See details]</a>
                                        </td>
                                    </tr>
                                </c:forEach>

                                <c:if test="${empty userList}">
                                    <tr>
                                        <td colspan="6" style="text-align:center;">No user data</td>
                                    </tr>
                                </c:if>
                            </tbody>
                        </table>
                    </div>

                    <div class="pagination">
                        <!-- Nút trang trước -->
                        <c:if test="${currentPage > 1}">
                            <a href="UserDetailServlet?page=${currentPage - 1}&search=${param.search != null ? param.search : ''}">&lt;</a>
                        </c:if>

                        <!-- Luôn hiện trang 1 -->
                        <a href="UserDetailServlet?page=1&search=${param.search != null ? param.search : ''}" class="${currentPage == 1 ? 'active' : ''}">1</a>

                        <!-- Hiện dấu ... nếu trang hiện tại cách trang 1 hơn 2 -->
                        <c:if test="${currentPage > 3}">
                            <span>...</span>
                        </c:if>

                        <!-- Hiển thị trang xung quanh currentPage (trang trước và sau) -->
                        <c:forEach begin="${currentPage - 1 < 2 ? 2 : currentPage - 1}" end="${currentPage + 1 > pages - 1 ? pages - 1 : currentPage + 1}" var="p">
                            <a href="UserDetailServlet?page=${p}&search=${param.search != null ? param.search : ''}" class="${p == currentPage ? 'active' : ''}">${p}</a>
                        </c:forEach>

                        <!-- Hiện dấu ... nếu trang hiện tại cách trang cuối hơn 2 -->
                        <c:if test="${currentPage < pages - 2}">
                            <span>...</span>
                        </c:if>

                        <!-- Luôn hiện trang cuối nếu pages > 1 -->
                        <c:if test="${pages > 1}">
                            <a href="UserDetailServlet?page=${pages}&search=${param.search != null ? param.search : ''}" class="${currentPage == pages ? 'active' : ''}">${pages}</a>
                        </c:if>

                        <!-- Nút trang sau -->
                        <c:if test="${currentPage < pages}">
                            <a href="UserDetailServlet?page=${currentPage + 1}&search=${param.search != null ? param.search : ''}">&gt;</a>
                        </c:if>
                    </div>





                </div>
            </div> <!-- END main-content -->
        </div>
    </body>
</html>
