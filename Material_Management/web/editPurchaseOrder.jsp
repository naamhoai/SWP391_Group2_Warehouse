<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Chỉnh sửa đơn hàng mua</title>
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" rel="stylesheet">
    <link rel="stylesheet" href="css/editPurchaseOrder.css">
</head>
<body>
    <div class="form-container">
        <!-- Header -->
        <div class="header-section">
            <h2><i class="fas fa-edit"></i> Chỉnh Sửa Đơn Mua Vật Tư</h2>
            <a href="purchaseOrderList" class="btn-back" style="font-size:16px;display:flex;align-items:center;gap:6px;">
                <i class="fas fa-clipboard-list"></i> Danh sách đơn mua
            </a>
        </div>

        
        <div class="form-content">
            <form action="editPurchaseOrder" method="post" id="editOrderForm">
                <input type="hidden" name="orderId" value="${order.purchaseOrderId}">
                <!-- Thông tin chung -->
                <div class="form-section">
                    <div class="form-row">
                        <div class="form-group">
                            <label class="form-label">Người tạo đơn</label>
                            <input type="text" class="form-control" value="${order.creatorName}" readonly>
                        </div>
                        <div class="form-group">
                            <label class="form-label">Vai trò hiện tại</label>
                            <input type="text" class="form-control" value="${order.creatorRoleName}" readonly>
                        </div>
                        <div class="form-group">
                            <label class="form-label">Ngày tạo</label>
                            <input type="text" class="form-control" value="${order.orderDate}" readonly>
                        </div>
                    </div>
                    
                    <div class="form-group">
                        <label class="form-label">Nhà cung cấp *</label>
                        <select name="supplierId" class="form-select" required>
                            <option value="">Chọn nhà cung cấp</option>
                            <c:forEach var="supplier" items="${suppliers}">
                                <option value="${supplier.supplierId}" ${order.supplierId == supplier.supplierId ? 'selected' : ''}>
                                    ${supplier.supplierName} - ${supplier.supplierPhone}
                                </option>
                            </c:forEach>
                        </select>
                    </div>
                    <div class="form-row">
                        <div class="form-group">
                            <label class="form-label">Người liên hệ</label>
                            <input type="text" class="form-control" value="${order.contactPerson}" readonly>
                        </div>
                        <div class="form-group">
                            <label class="form-label">Số điện thoại</label>
                            <input type="text" class="form-control" value="${order.supplierPhone}" readonly>
                        </div>
                    </div>
                </div>
                
                <c:if test="${not empty errorMsg}">
                    <div style="background:#f8d7da; color:#721c24; border:1px solid #f5c6cb; padding:15px; margin-bottom:20px; border-radius:8px;">
                        <i class="fas fa-exclamation-triangle" style="margin-right:8px;"></i>
                        <strong>LỖI!</strong> <c:out value="${errorMsg}" escapeXml="false"/>
                    </div>
                </c:if>
                <c:if test="${not empty errorMessage}">
                    <div style="background:#f8d7da; color:#721c24; border:1px solid #f5c6cb; padding:15px; margin-bottom:20px; border-radius:8px;">
                        <i class="fas fa-exclamation-triangle" style="margin-right:8px;"></i>
                        <strong>LỖI!</strong> <c:out value="${errorMessage}" escapeXml="false"/>
                    </div>
                </c:if>
                
                <div class="form-section">
                    <h4 style="margin-bottom:16px;"><i class="fas fa-boxes"></i> Danh sách vật tư</h4>
                    <div style="overflow-x:auto;">
                        <table class="table table-bordered" style="width:100%;background:white;">
                            <thead>
                                <tr style="background:#f8f9fa;">
                                    <th>Tên vật tư</th>
                                    <th>Số lượng</th>
                                    <th>Đơn vị</th>
                                    <th>Đơn vị gốc</th>
                                    <th>Đơn giá</th>
                                    <th></th>
                                </tr>
                            </thead>
                            <tbody id="materialsTableBody">
                                <c:forEach var="detail" items="${details}" varStatus="status">
                                    <tr>
                                        <td><input type="text" name="materialName[]" class="form-control material-name" value="${detail.materialName}" required></td>
                                        <td><input type="number" name="quantity[]" class="form-control quantity" value="${detail.quantity}" min="1" required></td>
                                        <td>
                                            <select name="unit[]" class="form-select unit">
                                                <c:forEach var="unit" items="${supplierUnits}">
                                                    <option value="${unit.unit_name}" ${detail.unit == unit.unit_name ? 'selected' : ''}>${unit.unit_name}</option>
                                                </c:forEach>
                                            </select>
                                        </td>
                                        <td><input type="text" name="baseUnit[]" class="form-control base-unit-input" value="${detail.convertedUnit}" placeholder="Đơn vị gốc" required readonly></td>
                                        <td><input type="number" name="unitPrice[]" class="form-control unit-price" value="${detail.unitPrice}" min="0" step="0.01" required></td>
                                        <td><button type="button" class="btn-remove-material" onclick="removeMaterialRow(this)"><i class="fas fa-trash"></i></button></td>
                                    </tr>
                                </c:forEach>
                            </tbody>
                        </table>
                    </div>
                    <div style="margin-top:12px;display:flex;gap:12px;">
                        <button type="button" class="btn-add-material" onclick="addMaterialRow()"><i class="fas fa-plus"></i> Thêm dòng vật tư</button>
                        <button type="button" class="btn-remove-material" onclick="removeLastMaterialRow()"><i class="fas fa-minus"></i> Xóa dòng cuối</button>
                    </div>
                    <div style="margin-top:10px;text-align:right;font-weight:500;">
                        Tổng tiền: <span id="grandTotal">0</span> VNĐ
                    </div>
                </div>
                <!-- Ghi chú -->
                <div class="form-section">
                    <label class="form-label">Ghi chú</label>
                    <textarea name="note" class="form-control" rows="3" placeholder="Nhập ghi chú chung cho đơn mua...">${order.note}</textarea>
                </div>
                <!-- Nút thao tác -->
                <div style="display:flex;justify-content:flex-end;gap:12px;margin-top:20px;">
                    <a href="purchaseOrderList" class="btn-back" style="background:#6c757d;color:white;padding:12px 28px;font-size:16px;"><i class="fas fa-arrow-left"></i> Hủy</a>
                    <button type="submit" class="btn-submit" style="font-size:16px;"><i class="fas fa-save"></i> Lưu thay đổi</button>
                </div>
            </form>
        </div>
    </div>

    <script>
    function updateEditOrderTotal() {
        let total = 0;
        document.querySelectorAll('#materialsTableBody tr').forEach(row => {
            const qty = parseFloat(row.querySelector('input[name="quantity[]"]')?.value) || 0;
            const price = parseFloat(row.querySelector('input[name="unitPrice[]"]')?.value) || 0;
            total += qty * price;
        });
        document.getElementById('grandTotal').textContent = total.toLocaleString('vi-VN');
    }
    document.addEventListener('input', function(e) {
        if (e.target.name === 'quantity[]' || e.target.name === 'unitPrice[]') updateEditOrderTotal();
    });
    window.onload = function() {
        updateEditOrderTotal();
    };
    </script>
</body>
</html> 