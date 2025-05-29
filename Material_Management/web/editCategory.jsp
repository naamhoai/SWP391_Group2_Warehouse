<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="java.util.List"%>
<%@page import="model.Category"%>
<%
    Category category = (Category) request.getAttribute("category");
    List<Category> parentCategories = (List<Category>) request.getAttribute("parentCategories");
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Edit Category</title>
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
        <div class="form-container">
            <h1 class="form-title">Edit Category</h1>

            <%
                if (category != null) {
            %>
            <form action="categories" method="post">
                <input type="hidden" name="action" value="update" />
                <input type="hidden" name="id" value="<%= category.getCategoryId() %>" />

                <label for="name">Name:</label>
                <input type="text" id="name" name="name" value="<%= category.getName() %>" required />

                <label for="parentId">Parent Category:</label>
                <select name="parentId" id="parentId">
                    <option value="">None</option>
                    <% if (parentCategories != null) {
                        for (Category p : parentCategories) {
                            if (p.getCategoryId() != category.getCategoryId()) { // Không chọn chính nó
                                boolean selected = category.getParentId() != null && category.getParentId().equals(p.getCategoryId());
                    %>
                        <option value="<%= p.getCategoryId() %>" <%= selected ? "selected" : "" %>><%= p.getName() %></option>
                    <%
                            }
                        }
                    } %>
                </select>

                <div class="buttons">
                    <button type="submit" class="btn btn-submit">Update</button>
                    <a href="categories" class="btn btn-cancel">Cancel</a>
                </div>
            </form>
            <% } else { %>
                <p>Category not found. <a href="categories">Back to list</a></p>
            <% } %>
        </div>
    </div>
</body>
</html>
