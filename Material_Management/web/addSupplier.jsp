<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Add New Supplier</title>
        <link rel="stylesheet" href="css/supplier.css">
    </head>
    <body>
        <div class="form-container">
            <h2>Add New Supplier</h2>
            
            <form action="${pageContext.request.contextPath}/suppliers" method="post" class="supplier-form">
                <input type="hidden" name="action" value="add">
                
                <div class="form-field">
                    <label for="name">Supplier Name</label>
                    <input type="text" id="name" name="name" required>
                </div>
                
                <div class="form-field">
                    <label for="address">Address</label>
                    <input type="text" id="address" name="address">
                </div>
                
                <div class="form-field">
                    <label for="phone">Phone Number</label>
                    <input type="tel" id="phone" name="phone">
                </div>
                
                <div class="form-field">
                    <label for="email">Email</label>
                    <input type="email" id="email" name="email">
                </div>
                
                <div class="form-field">
                    <label for="status">Status</label>
                    <select id="status" name="status">
                        <option value="inactive" selected>Inactive</option>
                        <option value="active">Active</option>
                    </select>
                </div>
                
                <div class="form-actions">
                    <button type="button" class="btn-cancel" onclick="window.location.href='${pageContext.request.contextPath}/suppliers'">
                        Cancel
                    </button>
                    <button type="submit" class="btn-submit">
                        Add New
                    </button>
                </div>
            </form>
        </div>

        <script>
            document.querySelector('form').onsubmit = function(e) {
                const name = document.getElementById('name').value.trim();
                if (!name) {
                    e.preventDefault();
                    alert('Please enter supplier name');
                    return false;
                }
                return true;
            };
        </script>
    </body>
</html> 