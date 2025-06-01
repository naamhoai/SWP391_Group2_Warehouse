<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="java.util.*, model.Category, dao.CategoryDAO"%>
<%
    String error = (String) session.getAttribute("error");
    String message = (String) session.getAttribute("message");
    if (error != null) {
        session.removeAttribute("error");
    }
    if (message != null) {
        session.removeAttribute("message");
    }
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Category List</title>
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
    </style>
</head>
<body>
    <div class="main-content">
        <div class="content-header">
            <h1>Category List</h1>
            <a href="categories?action=add" class="btn-new">+ New Category</a>
        </div>

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

        <form action="categories" method="get" class="filter-form">
            <%
                String currentKeyword = (String) request.getAttribute("keyword");
                String currentParentId = (String) request.getAttribute("parentId");
                String currentSortBy = (String) request.getAttribute("sortBy");
            %>
            <select name="parentId">
                <option value="">All Parent Categories</option>
                <%
                    List<Category> parentCategories = (List<Category>) request.getAttribute("parentCategories");
                    if (parentCategories != null) {
                        for (Category p : parentCategories) {
                            String selected = (currentParentId != null && currentParentId.equals(p.getCategoryId().toString())) ? "selected" : "";
                %>
                    <option value="<%= p.getCategoryId() %>" <%= selected %>><%= p.getName() %></option>
                <%
                        }
                    }
                %>
            </select>

            <input type="text" name="keyword" placeholder="Search by name"
                   value="<%= currentKeyword != null ? currentKeyword : "" %>">

            <div class="sort-buttons">
                <button type="submit" name="sortBy" value="name" class="btn sort-btn <%= "name".equals(currentSortBy) ? "active" : "" %>">
                    Sort by Name <%= "name".equals(currentSortBy) ? "✓" : "" %>
                </button>
                <button type="submit" name="sortBy" value="id" class="btn sort-btn <%= "id".equals(currentSortBy) ? "active" : "" %>">
                    Sort by ID <%= "id".equals(currentSortBy) ? "✓" : "" %>
                </button>
            </div>

            <button type="submit" class="btn filter-btn">Search</button>
        </form>

        <table class="category-table">
            <thead>
                <tr>
                    <th>ID</th>
                    <th>Name</th>
                    <th>Parent Category</th>
                    <th>Actions</th>
                </tr>
            </thead>
            <tbody>
                <%
                    List<Category> categories = (List<Category>) request.getAttribute("categories");
                    CategoryDAO categoryDAO = new CategoryDAO();
                    if (categories != null && !categories.isEmpty()) {
                        for (Category c : categories) {
                            String parentName = "-";
                            if (c.getParentId() != null) {
                                String pName = categoryDAO.getCategoryNameById(c.getParentId());
                                if (pName != null) {
                                    parentName = pName;
                                }
                            }
                %>
                <tr>
                    <td><%= c.getCategoryId() %></td>
                    <td><%= c.getName() %></td>
                    <td><%= parentName %></td>
                    <td>
                        <a href="categories?action=edit&id=<%= c.getCategoryId() %>" class="btn btn-edit">Edit</a>
                        <a href="categories?action=delete&id=<%= c.getCategoryId() %>"
                           class="btn btn-delete"
                           onclick="return confirm('Are you sure you want to delete this category?');">Delete</a>
                    </td>
                </tr>
                <%
                        }
                    } else {
                %>
                <tr>
                    <td colspan="4">No categories found.</td>
                </tr>
                <%
                    }
                %>
            </tbody>
        </table>
    </div>
</body>
</html>