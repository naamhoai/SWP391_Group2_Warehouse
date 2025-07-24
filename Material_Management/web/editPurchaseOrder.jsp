<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Chỉnh sửa đơn hàng mua</title>
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" rel="stylesheet">
    <style>
        * {
            margin: 0;
            padding: 0;
            box-sizing: border-box;
        }
        
        body {
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
            background: linear-gradient(135deg, #f5f7fa 0%, #c3cfe2 100%);
            min-height: 100vh;
            padding: 20px;
        }
        
        .form-container {
            max-width: 1200px;
            margin: 0 auto;
            background: white;
            border-radius: 10px;
            box-shadow: 0 0 20px rgba(0,0,0,0.1);
            overflow: hidden;
        }
        
        .header-section {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            color: white;
            padding: 30px;
            display: flex;
            justify-content: space-between;
            align-items: center;
        }
        
        .header-section h2 {
            font-size: 28px;
            font-weight: 600;
        }
        
        .header-section p {
            margin-top: 8px;
            opacity: 0.9;
        }
        
        .btn-back {
            background: rgba(255,255,255,0.2);
            color: white;
            text-decoration: none;
            padding: 10px 20px;
            border-radius: 5px;
            border: 1px solid rgba(255,255,255,0.3);
            transition: all 0.3s;
        }
        
        .btn-back:hover {
            background: rgba(255,255,255,0.3);
            color: white;
        }
        
        .form-content {
            padding: 30px;
        }
        
        .form-section {
            background: #f8f9fa;
            padding: 25px;
            border-radius: 8px;
            margin-bottom: 25px;
        }
        
        .form-section h4 {
            color: #333;
            margin-bottom: 20px;
            font-size: 18px;
            font-weight: 600;
        }
        
        .form-row {
            display: flex;
            gap: 20px;
            margin-bottom: 20px;
        }
        
        .form-group {
            flex: 1;
        }
        
        .form-label {
            display: block;
            margin-bottom: 8px;
            font-weight: 500;
            color: #333;
        }
        
        .form-control, .form-select {
            width: 100%;
            padding: 12px;
            border: 1px solid #ddd;
            border-radius: 6px;
            font-size: 14px;
            transition: border-color 0.3s;
        }
        
        .form-control:focus, .form-select:focus {
            outline: none;
            border-color: #667eea;
            box-shadow: 0 0 0 2px rgba(102, 126, 234, 0.2);
        }
        
        textarea.form-control {
            resize: vertical;
            min-height: 80px;
        }
        
        .material-row {
            background: white;
            padding: 20px;
            border-radius: 8px;
            margin-bottom: 15px;
            border: 1px solid #e0e0e0;
            box-shadow: 0 2px 4px rgba(0,0,0,0.05);
        }
        
        .material-grid {
            display: grid;
            grid-template-columns: 2fr 1fr 1fr 1fr 1fr 60px;
            gap: 15px;
            align-items: end;
        }
        
        .btn-add-material {
            background: linear-gradient(135deg, #28a745, #20c997);
            border: none;
            color: white;
            padding: 12px 24px;
            border-radius: 6px;
            cursor: pointer;
            font-weight: 500;
            transition: all 0.3s;
            display: inline-flex;
            align-items: center;
            gap: 8px;
        }
        
        .btn-add-material:hover {
            transform: translateY(-2px);
            box-shadow: 0 5px 15px rgba(40, 167, 69, 0.3);
        }
        
        .btn-remove-material {
            background: #dc3545;
            border: none;
            color: white;
            padding: 8px 12px;
            border-radius: 4px;
            cursor: pointer;
            transition: background 0.3s;
        }
        
        .btn-remove-material:hover {
            background: #c82333;
        }
        
        .row-total {
            margin-top: 10px;
            font-size: 14px;
            color: #666;
        }
        
        .total-section {
            background: linear-gradient(135deg, #ff6b6b, #ee5a24);
            color: white;
            padding: 25px;
            border-radius: 8px;
            text-align: center;
            margin-bottom: 25px;
        }
        
        .total-section h3 {
            font-size: 24px;
            font-weight: 600;
        }
        
        .btn-submit {
            background: linear-gradient(135deg, #667eea, #764ba2);
            border: none;
            color: white;
            padding: 15px 40px;
            border-radius: 6px;
            font-size: 16px;
            font-weight: 600;
            cursor: pointer;
            transition: all 0.3s;
            display: inline-flex;
            align-items: center;
            gap: 10px;
        }
        
        .btn-submit:hover {
            transform: translateY(-2px);
            box-shadow: 0 5px 15px rgba(102, 126, 234, 0.3);
        }
        
        .submit-section {
            text-align: center;
            padding: 20px 0;
        }
        
        .autocomplete-suggestions {
            position: absolute;
            border: 1px solid #ccc;
            border-top: none;
            z-index: 1000;
            max-height: 150px;
            overflow-y: auto;
            background-color: white;
            width: 100%;
            box-shadow: 0 2px 4px rgba(0,0,0,0.1);
        }
        
        .autocomplete-suggestion {
            padding: 10px;
            cursor: pointer;
            border-bottom: 1px solid #f0f0f0;
        }
        
        .autocomplete-suggestion:hover {
            background-color: #f8f9fa;
        }
        
        .material-name-container {
            position: relative;
        }
        
        @media (max-width: 768px) {
            .form-row {
                flex-direction: column;
            }
            
            .material-grid {
                grid-template-columns: 1fr;
                gap: 10px;
            }
            
            .header-section {
                flex-direction: column;
                gap: 15px;
                text-align: center;
            }
        }
    </style>
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
                    <!-- Nhà cung cấp: không cho sửa -->
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
                <!-- Danh sách vật tư dạng bảng -->
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
                <!-- Bảng vật tư mới, chỉ còn các trường hợp lệ -->
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