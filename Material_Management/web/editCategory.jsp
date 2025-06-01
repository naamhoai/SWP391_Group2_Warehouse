<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="java.util.List"%>
<%@page import="model.Category"%>
<%
    Category category = (Category) request.getAttribute("category");
    List<Category> parentCategories = (List<Category>) request.getAttribute("parentCategories");
    String error = (String) request.getAttribute("error");
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Edit Category</title>
    <link rel="stylesheet" href="css/category.css">
</head>
<body>
    <div class="main-content">
        <div class="category-form-container">
            <h2>Edit Category</h2>

            <% if (error != null) { %>
                <div class="message error">
                    <%= error %>
                </div>
            <% } %>

            <div class="required-field-text">
                <span>*</span> Required fields
            </div>

            <% if (category != null) { %>
                <form action="categories" method="post">
                    <input type="hidden" name="action" value="edit">
                    <input type="hidden" name="id" value="<%= category.getCategoryId() %>">
                    
                    <div class="form-group">
                        <label for="name">Category Name</label>
                        <input type="text" id="name" name="name" 
                               value="<%= category.getName() %>" 
                               required 
                               minlength="2" 
                               maxlength="100"
                               placeholder="Enter category name">
                        <div class="error-message">Please enter a valid category name (2-100 characters)</div>
                    </div>
                    
                    <div class="form-group">
                        <label for="parentId">Parent Category</label>
                        <select id="parentId" name="parentId">
                            <option value="">Select parent category (optional)</option>
                            <% if (parentCategories != null) {
                                for (Category parent : parentCategories) {
                                    if (!parent.getCategoryId().equals(category.getCategoryId())) {
                                        boolean isSelected = category.getParentId() != null && 
                                                        category.getParentId().equals(parent.getCategoryId());
                            %>
                            <option value="<%= parent.getCategoryId() %>" <%= isSelected ? "selected" : "" %>>
                                <%= parent.getName() %>
                            </option>
                            <%      }
                                }
                            } %>
                        </select>
                    </div>
                    
                    <div class="form-actions">
                        <button type="submit" class="btn-submit">
                            <i class="fas fa-save"></i> Save Changes
                        </button>
                        <a href="categories" class="btn-cancel">
                            <i class="fas fa-times"></i> Cancel
                        </a>
                    </div>
                </form>
            <% } else { %>
                <div class="message error">Category not found.</div>
                <div class="form-actions">
                    <a href="categories" class="btn-cancel">
                        <i class="fas fa-arrow-left"></i> Back to List
                    </a>
                </div>
            <% } %>
        </div>
    </div>
    <!-- Font Awesome -->
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/css/all.min.css">
    <script>
        // Form validation
        document.querySelector('form')?.addEventListener('submit', function(e) {
            const nameInput = document.getElementById('name');
            const formGroup = nameInput.closest('.form-group');
            
            if (!nameInput.checkValidity()) {
                e.preventDefault();
                formGroup.classList.add('has-error');
            } else {
                formGroup.classList.remove('has-error');
            }
        });

        // Clear error on input
        document.getElementById('name')?.addEventListener('input', function() {
            this.closest('.form-group').classList.remove('has-error');
        });
    </script>
</body>
</html>
