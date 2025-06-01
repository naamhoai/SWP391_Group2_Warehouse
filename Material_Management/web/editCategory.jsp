<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="java.util.*, model.Category"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Edit Category</title>
    <link rel="stylesheet" href="css/category.css">
    <style>
        .form-container {
            max-width: 600px;
            margin: 20px auto;
            padding: 20px;
            background: #fff;
            border-radius: 8px;
            box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
        }
        .form-group {
            margin-bottom: 15px;
        }
        .form-group label {
            display: block;
            margin-bottom: 5px;
            font-weight: bold;
        }
        .form-group input[type="text"],
        .form-group select {
            width: 100%;
            padding: 8px;
            border: 1px solid #ddd;
            border-radius: 4px;
            box-sizing: border-box;
        }
        .error-message {
            color: #c62828;
            background-color: #ffebee;
            padding: 10px;
            border-radius: 4px;
            margin-bottom: 15px;
            border: 1px solid #ef9a9a;
        }
        .btn-container {
            margin-top: 20px;
            display: flex;
            gap: 10px;
        }
        .btn-save {
            background-color: #4CAF50;
            color: white;
            padding: 10px 20px;
            border: none;
            border-radius: 4px;
            cursor: pointer;
        }
        .btn-cancel {
            background-color: #f44336;
            color: white;
            padding: 10px 20px;
            border: none;
            border-radius: 4px;
            cursor: pointer;
            text-decoration: none;
        }
        .btn-save:hover {
            background-color: #45a049;
        }
        .btn-cancel:hover {
            background-color: #da190b;
        }
    </style>
</head>
<body>
    <div class="sidebar">
        <div class="logo">Logo</div>
        <a href="#">Dashboard</a>
        <a href="#">Inventory</a>
        <a href="#">Orders</a>
        <a href="#">Suppliers</a>
        <a href="#">Reports</a>
        <a href="#">Settings</a>
    </div>

    <div class="main-content">
        <div class="content-header">
            <h1>Edit Category</h1>
        </div>

        <div class="form-container">
            <% String error = (String) request.getAttribute("error"); %>
            <% if (error != null) { %>
                <div class="error-message">
                    <%= error %>
                </div>
            <% } %>

            <%
                Category category = (Category) request.getAttribute("category");
                if (category != null) {
            %>
            <form action="categories" method="post">
                <input type="hidden" name="action" value="edit">
                <input type="hidden" name="id" value="<%= category.getCategoryId() %>">
                
                <div class="form-group">
                    <label for="name">Tên danh mục:</label>
                    <input type="text" id="name" name="name" value="<%= category.getName() %>" required 
                           minlength="2" maxlength="100"
                           oninvalid="this.setCustomValidity('Vui lòng nhập tên danh mục (2-100 ký tự)')"
                           oninput="this.setCustomValidity('')">
                </div>
                
                <div class="form-group">
                    <label for="parentId">Danh mục cha:</label>
                    <select id="parentId" name="parentId">
                        <option value="">Không có danh mục cha</option>
                        <%
                            List<Category> parentCategories = (List<Category>) request.getAttribute("parentCategories");
                            if (parentCategories != null) {
                                for (Category parent : parentCategories) {
                                    boolean isSelected = category.getParentId() != null && 
                                                       category.getParentId().equals(parent.getCategoryId());
                        %>
                        <option value="<%= parent.getCategoryId() %>" <%= isSelected ? "selected" : "" %>>
                            <%= parent.getName() %>
                        </option>
                        <%
                                }
                            }
                        %>
                    </select>
                </div>
                
                <div class="btn-container">
                    <button type="submit" class="btn-save">Lưu thay đổi</button>
                    <a href="categories" class="btn-cancel">Hủy bỏ</a>
                </div>
            </form>
            <% } else { %>
                <div class="error-message">Category not found.</div>
                <div class="btn-container">
                    <a href="categories" class="btn-cancel">Back to List</a>
                </div>
            <% } %>
        </div>
    </div>
</body>
</html>
