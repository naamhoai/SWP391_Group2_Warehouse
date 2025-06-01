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
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Add Category</title>
    <link rel="stylesheet" href="css/category.css">
</head>
<body>
    <div class="main-content">
        <div class="category-form-container">
            <h2>Add New Category</h2>

            <% if (error != null) { %>
                <div class="message error">
                    <%= error %>
                </div>
            <% } %>
            
            <% if (message != null) { %>
                <div class="form-success-message">
                    <%= message %>
                </div>
            <% } %>

            <div class="required-field-text">
                <span>*</span> Required fields
            </div>

            <form action="categories" method="post">
                <input type="hidden" name="action" value="add" />

                <div class="form-group">
                    <label for="name">Category Name</label>
                    <input type="text" id="name" name="name" required 
                           minlength="2" maxlength="100"
                           placeholder="Enter category name">
                    <div class="error-message">Please enter a valid category name (2-100 characters)</div>
                </div>

                <div class="form-group">
                    <label for="parentId">Parent Category</label>
                    <select name="parentId" id="parentId">
                        <option value="">Select parent category (optional)</option>
                        <% if (parentCategories != null) {
                            for (Category p : parentCategories) { %>
                                <option value="<%= p.getCategoryId() %>"><%= p.getName() %></option>
                        <%  }
                        } %>
                    </select>
                </div>

                <div class="form-actions">
                    <button type="submit" class="btn-submit">
                        <i class="fas fa-plus"></i> Add Category
                    </button>
                    <a href="categories" class="btn-cancel">
                        <i class="fas fa-times"></i> Cancel
                    </a>
                </div>
            </form>
        </div>
    </div>
    <!-- Font Awesome -->
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/css/all.min.css">
    <script>
        
        document.querySelector('form').addEventListener('submit', function(e) {
            const nameInput = document.getElementById('name');
            const formGroup = nameInput.closest('.form-group');
            
            if (!nameInput.checkValidity()) {
                e.preventDefault();
                formGroup.classList.add('has-error');
            } else {
                formGroup.classList.remove('has-error');
            }
        });

        
        document.getElementById('name').addEventListener('input', function() {
            this.closest('.form-group').classList.remove('has-error');
        });
    </script>
</body>
</html>
