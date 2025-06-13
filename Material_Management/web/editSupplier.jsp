<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page import="model.Supplier" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Chỉnh sửa nhà cung cấp</title>
    <link rel="stylesheet" href="css/supplier.css">
    <link rel="stylesheet" href="css/style.css">
</head>
<body>
    <div class="container">
        <div class="form-container">
            <h2>Chỉnh sửa thông tin nhà cung cấp</h2>
            <%
                Supplier supplier = (Supplier) request.getAttribute("supplier");
                if (supplier == null) {
                    response.sendRedirect("suppliers");
                    return;
                }
            %>
            
            <c:if test="${not empty sessionScope.error}">
                <div class="alert alert-danger">
                    ${sessionScope.error}
                    <% session.removeAttribute("error"); %>
                </div>
            </c:if>

            <form action="suppliers" method="post" class="supplier-form" onsubmit="return validateForm()">
                <input type="hidden" name="action" value="edit" />
                <input type="hidden" name="supplierId" value="<%=supplier.getSupplierId()%>" />

                <div class="form-field">
                    <label for="supplierName">Tên nhà cung cấp <span class="required">*</span></label>
                    <input type="text" id="supplierName" name="supplierName" required 
                           value="<%=supplier.getSupplierName()%>" 
                           placeholder="Nhập tên nhà cung cấp"
                           maxlength="100"/>
                    <span class="error-message" id="nameError"></span>
                </div>

                <div class="form-field">
                    <label for="contactPerson">Người liên hệ <span class="required">*</span></label>
                    <input type="text" id="contactPerson" name="contactPerson" required 
                           value="<%=supplier.getContactPerson()%>" 
                           placeholder="Nhập tên người liên hệ"
                           maxlength="100"/>
                    <span class="error-message" id="contactError"></span>
                </div>

                <div class="form-field">
                    <label for="supplierPhone">Số điện thoại <span class="required">*</span></label>
                    <input type="tel" id="supplierPhone" name="supplierPhone" required 
                           value="<%=supplier.getSupplierPhone()%>" 
                           placeholder="Nhập số điện thoại"
                           pattern="[0-9]{10,11}"/>
                    <span class="error-message" id="phoneError"></span>
                </div>

                <div class="form-field">
                    <label for="address">Địa chỉ <span class="required">*</span></label>
                    <input type="text" id="address" name="address" required 
                           value="<%=supplier.getAddress()%>" 
                           placeholder="Nhập địa chỉ"
                           maxlength="255"/>
                    <span class="error-message" id="addressError"></span>
                </div>

                <div class="form-field">
                    <label for="status">Trạng thái <span class="required">*</span></label>
                    <select id="status" name="status" required>
                        <option value="active" <%= "active".equals(supplier.getStatus()) ? "selected" : "" %>>Hợp tác</option>
                        <option value="inactive" <%= "inactive".equals(supplier.getStatus()) ? "selected" : "" %>>Không hợp tác</option>
                    </select>
                </div>

                <div class="form-actions">
                    <a href="suppliers" class="btn-cancel">Hủy</a>
                    <button type="submit" class="btn-submit">Cập nhật</button>
                </div>
            </form>
        </div>
    </div>

    <script>
        function validateForm() {
            let isValid = true;
            const nameInput = document.getElementById('supplierName');
            const contactInput = document.getElementById('contactPerson');
            const phoneInput = document.getElementById('supplierPhone');
            const addressInput = document.getElementById('address');
            
            // Reset error messages
            document.querySelectorAll('.error-message').forEach(elem => elem.textContent = '');
            
            // Validate supplier name
            if (nameInput.value.trim().length < 2) {
                document.getElementById('nameError').textContent = 'Tên nhà cung cấp phải có ít nhất 2 ký tự';
                isValid = false;
            }
            
            // Validate contact person
            if (contactInput.value.trim().length < 2) {
                document.getElementById('contactError').textContent = 'Tên người liên hệ phải có ít nhất 2 ký tự';
                isValid = false;
            }
            
            // Validate phone number
            const phonePattern = /^[0-9]{10,11}$/;
            if (!phonePattern.test(phoneInput.value)) {
                document.getElementById('phoneError').textContent = 'Số điện thoại phải có 10-11 chữ số';
                isValid = false;
            }
            
            // Validate address
            if (addressInput.value.trim().length < 5) {
                document.getElementById('addressError').textContent = 'Địa chỉ phải có ít nhất 5 ký tự';
                isValid = false;
            }
            
            return isValid;
        }
    </script>
</body>
</html> 