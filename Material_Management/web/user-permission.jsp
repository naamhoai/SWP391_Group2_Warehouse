<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
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
                    <input type="text" id="searchUser" placeholder="Enter Username or ID" value="${param.keyword}">
                    <button id="searchBtn">
                        <i class="fas fa-search"></i> Search
                    </button>
                </div>

                <c:if test="${not empty error}">
                    <div class="error-message">
                        <i class="fas fa-exclamation-circle"></i> ${error}
                    </div>
                </c:if>
                <c:if test="${not empty success}">
                    <div class="success-message">
                        <i class="fas fa-check-circle"></i> ${success}
                    </div>
                </c:if>

                <c:if test="${not empty fullName}">
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
                    </form>
                </c:if>
            </div>
        </div>

        <div class="loading" id="loadingOverlay">
            <div class="loading-spinner">
                <i class="fas fa-spinner fa-spin"></i> Processing...
            </div>
        </div>

        <script src="js/user-permission.js"></script>

        <jsp:include page="footer.jsp" />
    </body>
</html>