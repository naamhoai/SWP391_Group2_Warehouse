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
    <style>
        .permission-container {
            padding: 20px;
            background: #fff;
            border-radius: 8px;
            box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
        }

        .search-box {
            display: flex;
            align-items: center;
            gap: 10px;
            margin-bottom: 20px;
        }

        .search-box input[type="text"] {
            flex: 1;
            padding: 8px;
            border: 1px solid #ddd;
            border-radius: 4px;
            font-size: 14px;
        }

        .search-box button {
            padding: 8px 16px;
            background-color: #28a745;
            color: white;
            border: none;
            border-radius: 4px;
            cursor: pointer;
            display: flex;
            align-items: center;
            gap: 5px;
            font-size: 14px;
        }

        .search-box button:hover {
            background-color: #218838;
        }

        .message-box {
            margin: 10px 0;
            padding: 10px;
            border-radius: 4px;
        }

        .error-message {
            background-color: #f8d7da;
            color: #721c24;
            border: 1px solid #f5c6cb;
        }

        .success-message {
            background-color: #d4edda;
            color: #155724;
            border: 1px solid #c3e6cb;
        }

        .info-message {
            background-color: #cce5ff;
            color: #004085;
            border: 1px solid #b8daff;
        }

        .user-selection-table {
            width: 100%;
            border-collapse: collapse;
            margin-top: 20px;
        }

        .user-selection-table th, .user-selection-table td {
            border: 1px solid #ddd;
            padding: 8px;
            text-align: left;
        }

        .user-selection-table th {
            background-color: #f2f2f2;
            font-weight: bold;
        }

        .user-selection-table tr:nth-child(even) {
            background-color: #f9f9f9;
        }

        .user-selection-table a {
            color: #007bff;
            text-decoration: none;
        }

        .user-selection-table a:hover {
            text-decoration: underline;
        }

        .permission-table {
            width: 100%;
            border-collapse: collapse;
            margin-top: 20px;
        }

        .permission-table th, .permission-table td {
            border: 1px solid #ddd;
            padding: 8px;
            text-align: center;
        }

        .permission-table th {
            background-color: #f2f2f2;
            font-weight: bold;
        }

        .save-btn {
            margin-top: 20px;
            padding: 10px 20px;
            background-color: #28a745;
            color: white;
            border: none;
            border-radius: 4px;
            cursor: pointer;
            display: flex;
            align-items: center;
            gap: 5px;
            font-size: 14px;
        }

        .save-btn:hover {
            background-color: #218838;
        }

        .back-btn {
            display: inline-block;
            margin-top: 10px;
            padding: 8px 16px;
            background-color: #6c757d;
            color: white;
            text-decoration: none;
            border-radius: 4px;
            font-size: 14px;
        }

        .back-btn:hover {
            background-color: #5a6268;
        }

        @media (max-width: 600px) {
            .search-box {
                flex-direction: column;
            }
            .search-box input[type="text"],
            .search-box button {
                width: 100%;
            }
        }
    </style>
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

            <c:if test="${not empty error}">
                <div class="message-box error-message">
                    <i class="fas fa-exclamation-circle"></i> ${error}
                </div>
            </c:if>
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