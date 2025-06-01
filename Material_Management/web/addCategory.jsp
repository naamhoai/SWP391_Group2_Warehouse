<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="java.util.List"%>
<%@page import="model.Category"%>
<%
    List<Category> parentCategories = (List<Category>) request.getAttribute("parentCategories");
    String error = (String) request.getAttribute("error");
    String message = (String) session.getAttribute("message");
    if (message != null) {
        session.removeAttribute("message");
    }
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Add Category</title>
    <link rel="stylesheet" href="css/category.css">
    <style>
        .message {
            padding: 10px;
            margin-bottom: 15px;
            border-radius: 4px;
        }
        .error {
            background-color: #ffebee;
            color: #c62828;
            border: 1px solid #ef9a9a;
        }
        .success {
            background-color: #e8f5e9;
            color: #2e7d32;
            border: 1px solid #a5d6a7;
        }
        .form-container {
            background: white;
            padding: 20px;
            border-radius: 8px;
            box-shadow: 0 2px 4px rgba(0,0,0,0.1);
            max-width: 600px;
            margin: 20px auto;
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
        .form-actions {
            margin-top: 20px;
            display: flex;
            gap: 10px;
        }
        .btn {
            padding: 8px 16px;
            border: none;
            border-radius: 4px;
            cursor: pointer;
            font-size: 14px;
            text-decoration: none;
            color: white;
        }
        .btn-submit {
            background-color: #4CAF50;
        }
        .btn-submit:hover {
            background-color: #45a049;
        }
        .btn-cancel {
            background-color: #f44336;
        }
        .btn-cancel:hover {
            background-color: #da190b;
        }
    </style>
</head>
<body>
    <div class="main-content">
        <div class="form-container">
            <h1 class="form-title">Add New Category</h1>

            <% if (error != null) { %>
                <div class="message error">
                    <%= error %>
                </div>
            <% } %>
            
            <% if (message != null) { %>
                <div class="message success">
                    <%= message %>
                </div>
            <% } %>

            <form action="categories" method="post" class="category-form">
                <input type="hidden" name="action" value="add" />

                <div class="form-group">
                    <label for="name">Category Name:</label>
                    <input type="text" id="name" name="name" required>
                </div>

                <div class="form-group">
                    <label for="parentId">Parent Category:</label>
                    <select name="parentId" id="parentId">
                        <option value="">None</option>
                        <% if (parentCategories != null) {
                            for (Category p : parentCategories) { %>
                                <option value="<%= p.getCategoryId() %>"><%= p.getName() %></option>
                        <%  }
                        } %>
                    </select>
                </div>

                <div class="form-actions">
                    <button type="submit" class="btn btn-submit">Add Category</button>
                    <a href="categories" class="btn btn-cancel">Cancel</a>
                </div>
            </form>
        </div>
    </div>
</body>
</html>
