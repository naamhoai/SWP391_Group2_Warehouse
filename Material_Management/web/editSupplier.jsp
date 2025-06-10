<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Edit Supplier</title>
        <link rel="stylesheet" href="css/supplier.css">
    </head>
    <body>
        <div class="form-container">
            <h2>Edit Supplier</h2>
            
            <c:if test="${empty supplier}">
                <div class="message error">Supplier not found</div>
            </c:if>

            <c:if test="${not empty supplier}">
                <form action="${pageContext.request.contextPath}/suppliers" method="post" class="supplier-form">
                    <input type="hidden" name="action" value="edit">
                    <input type="hidden" name="id" value="${supplier.supplierId}">
                    
                    <div class="form-field">
                        <label for="name">Supplier Name</label>
                        <input type="text" id="name" name="name" required value="${supplier.supplierName}">
                    </div>
                    
                    <div class="form-field">
                        <label for="address">Address</label>
                        <input type="text" id="address" name="address" value="${supplier.address}">
                    </div>
                    
                    <div class="form-field">
                        <label for="phone">Phone Number</label>
                        <input type="tel" id="phone" name="phone" value="${supplier.supplierPhone}">
                    </div>
                    
                    <div class="form-field">
                        <label for="email">Email</label>
                        <input type="email" id="email" name="email" value="${supplier.email}">
                    </div>
                    
                    <div class="form-field">
                        <label for="status">Status</label>
                        <select id="status" name="status">
                            <option value="active" ${supplier.status == 'active' ? 'selected' : ''}>Active</option>
                            <option value="inactive" ${supplier.status == 'inactive' ? 'selected' : ''}>Inactive</option>
                        </select>
                    </div>
                    
                    <div class="form-actions">
                        <button type="button" class="btn-cancel" onclick="window.location.href='${pageContext.request.contextPath}/suppliers'">
                            <i class="icon-cancel"></i> Cancel
                        </button>
                        <button type="submit" class="btn-submit">
                            <i class="icon-save"></i> Save Changes
                        </button>
                    </div>
                </form>
            </c:if>
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