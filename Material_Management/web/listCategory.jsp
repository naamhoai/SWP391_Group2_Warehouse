<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="java.util.*, model.Category"%>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Category List</title>
    <link rel="stylesheet" href="css/category.css">
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
            <h1>Category List</h1>
            <a href="addCategory.jsp" class="btn-new">+ New Category</a>
        </div>

        <form action="categories" method="get" class="filter-form">
            <select name="parentId">
                <option value="">All Parent Categories</option>
                <%
                    List<Category> parentCategories = (List<Category>) request.getAttribute("parentCategories");
                    String selectedParentId = request.getParameter("parentId");
                    if (parentCategories != null) {
                        for (Category p : parentCategories) {
                            String selected = (selectedParentId != null && selectedParentId.equals(p.getCategoryId().toString())) ? "selected" : "";
                %>
                    <option value="<%= p.getCategoryId() %>" <%= selected %>><%= p.getName() %></option>
                <% 
                        }
                    } 
                %>
            </select>

            <input type="text" name="keyword" placeholder="Search by name"
                   value="<%= request.getAttribute("keyword") != null ? request.getAttribute("keyword") : "" %>">

            <select name="sortBy">
                <option value="">Sort By</option>
                <option value="name" <%= "name".equals(request.getParameter("sortBy")) ? "selected" : "" %>>Name A-Z</option>
                <option value="id" <%= "id".equals(request.getParameter("sortBy")) ? "selected" : "" %>>ID</option>
            </select>

            <button type="submit" class="btn filter-btn">Search</button>
        </form>

        <table class="category-table">
            <thead>
                <tr>
                    <th>ID</th>
                    <th>Name</th>
                    <th>Parent</th>
                    <th>Actions</th>
                </tr>
            </thead>
            <tbody>
                <%
                    List<Category> categories = (List<Category>) request.getAttribute("categories");
                    if (categories != null && !categories.isEmpty()) {
                        for (Category c : categories) {
                            Integer pid = c.getParentId();
                            String parentName = "-";
                            if (pid != null && parentCategories != null) {
                                for (Category p : parentCategories) {
                                    if (p.getCategoryId() != null && p.getCategoryId().equals(pid)) {
                                        parentName = p.getName();
                                        break;
                                    }
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
