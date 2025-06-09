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
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Category List</title>
    <link rel="stylesheet" href="css/sidebar.css">
    <link rel="stylesheet" href="css/category.css">
    <link rel="stylesheet" href="css/footer.css">
</head>
<body>
     <jsp:include page="sidebar.jsp" />
    <div class="main-content">
        <div class="page-content">
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
                    <button type="submit" name="sortBy" value="name" class="sort-btn <%= "name".equals(currentSortBy) ? "active" : "" %>">
                        Sort by Name <%= "name".equals(currentSortBy) ? "✓" : "" %>
                    </button>
                    <button type="submit" name="sortBy" value="id" class="sort-btn <%= "id".equals(currentSortBy) ? "active" : "" %>">
                        Sort by ID <%= "id".equals(currentSortBy) ? "✓" : "" %>
                    </button>
                </div>

                <button type="submit" class="filter-btn">Search</button>
            </form>

            <div class="table-container">
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
                            boolean hasCategories = false;
                            if (categories != null && !categories.isEmpty()) {
                                for (Category c : categories) {
                                    // Chỉ hiển thị các danh mục không phải là danh mục cha (có parentId)
                                    if (c.getParentId() != null) {
                                        hasCategories = true;
                                        String parentName = categoryDAO.getCategoryNameById(c.getParentId());
                                        if (parentName == null) {
                                            parentName = "-";
                                        }
                        %>
                        <tr>
                            <td><%= c.getCategoryId() %></td>
                            <td><%= c.getName() %></td>
                            <td><%= parentName %></td>
                            <td class="action-buttons">
                                <a href="categories?action=edit&id=<%= c.getCategoryId() %>" class="btn-edit">Edit</a>
                                <a href="categories?action=delete&id=<%= c.getCategoryId() %>"
                                   class="btn-delete"
                                   onclick="return confirm('Are you sure you want to delete this category?');">Delete</a>
                            </td>
                        </tr>
                        <%
                                    }
                                }
                            }
                            if (!hasCategories) {
                        %>
                        <tr>
                            <td colspan="4" class="no-data">No categories found.</td>
                        </tr>
                        <%
                            }
                        %>
                    </tbody>
                </table>
            </div>
        </div>
        <jsp:include page="footer.jsp" />
    </div>
</body>
</html>