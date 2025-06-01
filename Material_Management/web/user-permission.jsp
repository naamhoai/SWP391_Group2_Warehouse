<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>User Permission Management</title>
        <!--     Google Fonts 
            <link href="https://fonts.googleapis.com/css2?family=Roboto:wght@300;400;500;600;700&display=swap" rel="stylesheet">
             Font Awesome 
            <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/css/all.min.css">-->
        <!-- Custom CSS -->
        <link rel="stylesheet" href="css/sidebar.css">
        <link rel="stylesheet" href="css/user-permission.css">
        <link rel="stylesheet" href="css/footer.css">
        
    </head>
    <body>
        <!-- Sidebar -->
        <jsp:include page="side.jsp" />

        <!-- Main Content -->
        <div class="main-content">
            <div class="permission-container">
                <h2 class="page-title">Manager Assign User Permission</h2>

                <div class="search-box">
                    <input type="text" id="searchUser" placeholder="Enter Username: ">
                    <button onclick="searchUsers()">
                        <i class="fas fa-search"></i>
                        Search
                    </button>
                </div>

                <div class="user-permissions">
                    <form id="permissionForm" action="savePermissions" method="POST">
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
                                <tr>
                                    <td>Manager Category</td>
                                    <td class="checkbox-wrapper"><input type="checkbox" name="category_view"></td>
                                    <td class="checkbox-wrapper"><input type="checkbox" name="category_add"></td>
                                    <td class="checkbox-wrapper"><input type="checkbox" name="category_edit"></td>
                                    <td class="checkbox-wrapper"><input type="checkbox" name="category_delete"></td>
                                </tr>
                                <tr>
                                    <td>Manager Warehouse</td>
                                    <td class="checkbox-wrapper"><input type="checkbox" name="inventory_view"></td>
                                    <td class="checkbox-wrapper"><input type="checkbox" name="inventory_add"></td>
                                    <td class="checkbox-wrapper"><input type="checkbox" name="inventory_edit"></td>
                                    <td class="checkbox-wrapper"><input type="checkbox" name="inventory_delete"></td>
                                </tr>
                                <tr>
                                    <td>Manager Order</td>
                                    <td class="checkbox-wrapper"><input type="checkbox" name="order_view"></td>
                                    <td class="checkbox-wrapper"><input type="checkbox" name="order_add"></td>
                                    <td class="checkbox-wrapper"><input type="checkbox" name="order_edit"></td>
                                    <td class="checkbox-wrapper"><input type="checkbox" name="order_delete"></td>
                                </tr>
                                <tr>
                                    <td>Manager Delivery</td>
                                    <td class="checkbox-wrapper"><input type="checkbox" name="delivery_view"></td>
                                    <td class="checkbox-wrapper"><input type="checkbox" name="delivery_add"></td>
                                    <td class="checkbox-wrapper"><input type="checkbox" name="delivery_edit"></td>
                                    <td class="checkbox-wrapper"><input type="checkbox" name="delivery_delete"></td>
                                </tr>
                                <tr>
                                    <td>Manager Users</td>
                                    <td class="checkbox-wrapper"><input type="checkbox" name="user_view"></td>
                                    <td class="checkbox-wrapper"><input type="checkbox" name="user_add"></td>
                                    <td class="checkbox-wrapper"><input type="checkbox" name="user_edit"></td>
                                    <td class="checkbox-wrapper"><input type="checkbox" name="user_delete"></td>
                                </tr>
                            </tbody>
                        </table>
                        <button type="submit" class="save-btn">
                            <i class="fas fa-save"></i>
                            Save Permission.
                        </button>
                    </form>
                </div>
            </div>
        </div>

        <!-- Footer -->
        <jsp:include page="footer.jsp" />

    </body>
</html> 