<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta http-equiv="Cache-Control" content="no-cache, no-store, must-revalidate">
    <meta http-equiv="Pragma" content="no-cache">
    <meta http-equiv="Expires" content="0">
    <title>Chỉnh sửa nhà cung cấp</title>
    <link rel="stylesheet" href="css/sidebar.css">
    <link rel="stylesheet" href="css/common.css">
    <link rel="stylesheet" href="css/supplier.css?v=1.2">
    <style>
        /* Force override any conflicting styles */
        body .container {
            max-width: 980px !important;
            margin: 40px auto !important;
            padding: 0 20px !important;
            width: 980px !important;
        }
        body .form-container {
            max-width: 770px !important;
            margin: 40px auto !important;
            width: 770px !important;
            padding: 0 0 40px 0 !important;
        }
        body .form-container h2 {
            padding: 35px 40px 25px 40px !important;
            font-size: 26px !important;
        }
        body .supplier-form {
            padding: 0 40px !important;
        }
        body .form-field {
            margin-bottom: 16px !important;
        }
        body .form-field input,
        body .form-field select {
            padding: 14px 18px !important;
            font-size: 16px !important;
        }
        body .form-actions {
            padding: 30px 40px 0 40px !important;
            margin-top: 30px !important;
        }
    </style>
</head>
<body>
    <div class="container">
        <div class="form-container">
            <h2>Chỉnh sửa thông tin nhà cung cấp</h2>
            
            <c:if test="${empty supplier}">
                <c:redirect url="suppliers"/>
            </c:if>
            
            <c:if test="${not empty error}">
                <div class="message error">
                    ${error}
                </div>
            </c:if>

            <form action="suppliers" method="post" class="supplier-form">
                <input type="hidden" name="action" value="edit" />
                <input type="hidden" name="supplierId" value="${supplier.supplierId}" />
                <input type="hidden" id="initialStatus" name="initialStatus" value="${supplier.status}"/>

                <div class="form-field">
                    <label for="supplierName">Tên nhà cung cấp <span class="required">*</span></label>
                    <input type="text" id="supplierName" name="supplierName" required 
                           value="${not empty supplierName ? supplierName : supplier.supplierName}" 
                           placeholder="Nhập tên nhà cung cấp"
                           maxlength="100"/>
                </div>

                <div class="form-field">
                    <label for="contactPerson">Người liên hệ <span class="required">*</span></label>
                    <input type="text" id="contactPerson" name="contactPerson" required 
                           value="${not empty contactPerson ? contactPerson : supplier.contactPerson}" 
                           placeholder="Nhập tên người liên hệ"
                           maxlength="100"/>
                </div>

                <div class="form-field">
                    <label for="supplierPhone">Số điện thoại <span class="required">*</span></label>
                    <input type="tel" id="supplierPhone" name="supplierPhone" required 
                           value="${not empty supplierPhone ? supplierPhone : supplier.supplierPhone}" 
                           placeholder="Nhập số điện thoại"
                           pattern="[0-9]{10,11}"/>
                </div>

                <div class="form-field">
                    <label for="address">Địa chỉ <span class="required">*</span></label>
                    <input type="text" id="address" name="address" required 
                           value="${not empty address ? address : supplier.address}" 
                           placeholder="Nhập địa chỉ"
                           maxlength="255"/>
                </div>

                <div class="form-field">
                    <label for="status">Trạng thái <span class="required">*</span></label>
                    <select id="status" name="status" required>
                        <option value="active" ${(not empty status ? status : supplier.status) == 'active' ? 'selected' : ''}>Hợp tác</option>
                        <option value="inactive" ${(not empty status ? status : supplier.status) == 'inactive' ? 'selected' : ''}>Chưa hợp tác</option>
                        <option value="terminated" ${(not empty status ? status : supplier.status) == 'terminated' ? 'selected' : ''}>Ngừng hợp tác</option>
                    </select>
                </div>

                <div class="form-field" id="reasonField" style="display:none;">
                    <label for="statusReason">Lý do thay đổi trạng thái <span class="required">*</span></label>
                    <input type="text" id="statusReason" name="statusReason" maxlength="255"
                        placeholder="Nhập lý do thay đổi trạng thái..."
                        value="${not empty statusReason ? statusReason : supplier.statusReason}"/>
                </div>

                <div class="form-actions">
                    <a href="suppliers" class="btn-cancel">Hủy</a>
                    <button type="submit" class="btn-submit">Cập nhật</button>
                </div>
            </form>
        </div>
    </div>

    <script>
        const initialStatus = document.getElementById('initialStatus').value;
        const statusSelect = document.getElementById('status');
        const reasonField = document.getElementById('reasonField');
        
        function checkShowReason() {
            const newStatus = statusSelect.value;
            if (newStatus !== initialStatus) {
                reasonField.style.display = '';
            } else {
                reasonField.style.display = 'none';
            }
        }
        
        statusSelect.addEventListener('change', checkShowReason);
        window.onload = checkShowReason;
    </script>
</body>
</html> 