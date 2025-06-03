<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>User Permission Management</title>
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/css/all.min.css">
        <link rel="stylesheet" href="https://fonts.googleapis.com/css2?family=Poppins:wght@400;500;600;700&display=swap">
        <link rel="stylesheet" href="css/sidebar.css">
        <link rel="stylesheet" href="css/user-permission.css">
        <link rel="stylesheet" href="css/footer.css">

    </head>

    <body>
        <jsp:include page="side.jsp" />

        <div class="main-content">
            <div class="permission-container">
                <h2 class="page-title">User Permission Management</h2>

                <div class="search-box">
                    <form action="user-permissions" method="get">
                        <input type="text" id="searchUser" name="keyword" placeholder="Enter Username or ID" value="${param.keyword}">
                        <button type="submit" id="searchBtn" aria-label="Search for user">
                            <i class="fas fa-search"></i> Search
                        </button>
                    </form>
                </div>


                <c:if test="${not empty success}">
                    <div class="message-box success-message">
                        <i class="fas fa-check-circle"></i> ${success}
                    </div>
                </c:if>

                <c:if test="${not empty message and not empty multipleUsers}">
                    <div class="message-box info-message">
                        <i class="fas fa-info-circle"></i> ${message}
                    </div>
                    <table class="user-selection-table">
                        <thead>
                            <tr>
                                <th>User ID</th>
                                <th>Full Name</th>
                                <th>Role</th>
                                <th>Action</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="user" items="${multipleUsers}">
                                <tr>
                                    <td>${user.userId}</td>
                                    <td>${user.fullName}</td>
                                    <td>${user.roleName}</td>
                                    <td>
                                        <a href="user-permissions?keyword=${user.userId}">Select</a>
                                    </td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                    <a href="user-permissions" class="back-btn" aria-label="Back to initial page">
                        <i class="fas fa-arrow-left"></i> Back
                    </a>
                </c:if>

                <c:if test="${not empty fullName and empty multipleUsers}">
                    <div class="user-info">
                        <h3><i class="fas fa-user"></i> User: ${fullName}</h3>
                        <p><i class="fas fa-shield-alt"></i> Role: ${roleName}</p>
                    </div>

                    <form id="permissionForm" action="savePermissions" method="POST">
                        <input type="hidden" name="userId" value="${userId}">
                        <input type="hidden" name="fullName" value="${fullName}">
                        <table class="permission-table">
                            <thead>
                                <tr>
                                    <th>Function</th>
                                    <th>View</th>
                                    <th>Add</th>
                                    <th>Edit</th>
                                    <th>Delete</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:forEach items="${['category','inventory','order','delivery','user']}" var="module">
                                    <tr>
                                        <td>Manage ${module == 'category' ? 'Categories' : module == 'inventory' ? 'Inventory' : module == 'order' ? 'Orders' : module == 'delivery' ? 'Deliveries' : 'Users'}</td>
                                        <c:forEach items="${['view','add','edit','delete']}" var="action">
                                            <c:set var="permissionKey" value="${module}_${action}" />
                                            <td>
                                                <input type="checkbox" name="${permissionKey}"
                                                       ${empty rolePermissions || empty rolePermissions[permissionKey] ? 'disabled' : ''}
                                                       ${not empty userPermissions && userPermissions.contains(permissionKey) ? 'checked' : ''}>
                                            </td>
                                           
                                        </c:forEach>
                                    </tr>
                                </c:forEach>
                            </tbody>
                        </table>
                        <button type="submit" class="save-btn" id="saveBtn">
                            <i class="fas fa-save"></i> Save Permissions
                        </button>

                        <a href="user-permissions" class="back-btn" aria-label="Back to initial page">
                            <i class="fas fa-arrow-left"></i> Back
                        </a>
                    </form>
                </c:if>
            </div>
        </div>

        <div class="loading" id="loadingOverlay" style="display: none;">
            <div class="loading-spinner">
                <i class="fas fa-spinner fa-spin"></i> Processing...
            </div>
        </div>

        <jsp:include page="footer.jsp" />
    </body>
</html>