<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Thêm nhà cung cấp mới</title>
        <link rel="stylesheet" href="css/sidebar.css">
        <link rel="stylesheet" href="css/common.css">
        <link rel="stylesheet" href="css/supplier.css">
        <style>
            .alert-danger {
                background-color: #dc3545 !important;
                color: #fff !important;
                border: 2px solid #b52a37 !important;
                font-size: 16px;
                font-weight: bold;
                padding: 12px 16px;
                border-radius: 6px;
                margin-bottom: 20px;
            }
        </style>
    </head>
    <body>
        <div class="form-container">
            <h2>Thêm nhà cung cấp mới</h2>
            
            <c:if test="${not empty sessionScope.message}">
                <div class="alert alert-success">
                    ${sessionScope.message}
                    <% session.removeAttribute("message"); %>
                </div>
            </c:if>
            <c:if test="${not empty error}">
                <div class="alert alert-danger">
                    ${error}
                </div>
            </c:if>
            
            <form action="${pageContext.request.contextPath}/suppliers" method="post" class="supplier-form">
                <input type="hidden" name="action" value="add">
                
                <div class="form-field">
                    <label for="supplierName">Tên nhà cung cấp <span class="required">*</span></label>
                    <input type="text" id="supplierName" name="supplierName" required 
                           placeholder="Nhập tên nhà cung cấp"
                           maxlength="100"
                           value="${supplierName != null ? supplierName : ''}"/>
                </div>
                
                <div class="form-field">
                    <label for="contactPerson">Người liên hệ <span class="required">*</span></label>
                    <input type="text" id="contactPerson" name="contactPerson" required 
                           placeholder="Nhập tên người liên hệ"
                           maxlength="100"
                           value="${contactPerson != null ? contactPerson : ''}"/>
                </div>
                
                <div class="form-field">
                    <label for="supplierPhone">Số điện thoại <span class="required">*</span></label>
                    <input type="tel" id="supplierPhone" name="supplierPhone" required 
                           placeholder="Nhập số điện thoại"
                           value="${supplierPhone != null ? supplierPhone : ''}"/>
                </div>
                
                <div class="form-field">
                    <label for="address">Địa chỉ <span class="required">*</span></label>
                    <input type="text" id="address" name="address" required 
                           placeholder="Nhập địa chỉ"
                           maxlength="255"
                           value="${address != null ? address : ''}"/>
                </div>
                
                <div class="form-field">
                    <label for="status">Trạng thái <span class="required">*</span></label>
                    <select id="status" name="status" required>
                        <option value="inactive" ${status == 'inactive' ? 'selected' : ''}>Chưa hợp tác</option>
                        <option value="active" ${status == 'active' ? 'selected' : ''}>Hợp tác</option>
                    </select>
                </div>
                
                <div class="form-actions">
                    <a href="suppliers" class="btn-cancel">Hủy</a>
                    <button type="submit" class="btn-submit">Thêm mới</button>
                </div>
            </form>
        </div>
    </body>
</html> 