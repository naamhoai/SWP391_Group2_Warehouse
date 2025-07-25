<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
    <head>
        <title>Tạo Đơn Mua Vật Tư</title>
        <link href="https://fonts.googleapis.com/css2?family=Roboto:wght@400;700&display=swap" rel="stylesheet">
        <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" rel="stylesheet">
    <link rel="stylesheet" href="css/createPurchaseOrder.css">
    <link rel="stylesheet" href="css/sidebar.css">
    </head>
    <body>
        <jsp:include page="sidebar.jsp" />
<div class="create-purchase-order-container">
        <div class="form-section">
            <div class="header-section">
            <h2 class="create-purchase-order-title">Tạo Đơn Mua Vật Tư</h2>
                <a href="purchaseOrderList" class="btn-outline-primary">
                    <i class="fas fa-list"></i> Danh sách đơn mua
                </a>
            </div>
            <c:if test="${not empty successMessage}">
                <div style="background:#d4edda; color:#155724; border:1px solid #c3e6cb; padding:15px; margin-bottom:20px; border-radius:8px; display:flex; align-items:center;">
                    <i class="fas fa-check-circle" style="margin-right:8px;"></i>
                    <strong>Thành công!</strong> ${successMessage}
                </div>
            </c:if>
            <c:if test="${not empty errorMessage}">
                <div style="background:#f8d7da; color:#721c24; border:1px solid #f5c6cb; padding:15px; margin-bottom:20px; border-radius:8px;">
                    <i class="fas fa-exclamation-triangle" style="margin-right:8px;"></i>
                    <strong>Lỗi!</strong> ${errorMessage}
                </div>
            </c:if>
            <c:if test="${not empty errorMsg}">
                <div style="background:#f8d7da; color:#721c24; border:1px solid #f5c6cb; padding:10px; margin-bottom:16px; border-radius:6px;">
                    <b>LỖI KHI LƯU CHI TIẾT:</b><br>
                    <c:out value="${errorMsg}" escapeXml="false"/>
                </div>
            </c:if>
            <c:if test="${param.error == 'true'}">
                <div style="background:#f8d7da; color:#721c24; border:1px solid #f5c6cb; padding:15px; margin-bottom:20px; border-radius:8px;">
                    <i class="fas fa-exclamation-triangle" style="margin-right:8px;"></i>
                    <strong>Lỗi!</strong> ${param.message}
                </div>
            </c:if>
            <form action="createPurchaseOrder" method="post" id="createOrderForm">
                <div class="form-header-row">
                    <div class="form-header-col">
                        <label class="form-label">Người tạo đơn</label>
                        <input type="text" class="form-control" value="${currentUser.fullname}" readonly>
                    </div>
                    <div class="form-header-col">
                        <label class="form-label">Vai trò hiện tại</label>
                        <input type="text" class="form-control" value="${currentUser.role.rolename}" readonly>
                    </div>
                    <div class="form-header-col">
                        <label class="form-label">Ngày tạo</label>
                        <input type="text" class="form-control" id="createdDate" value="${createdDate}" readonly>
                    </div>
                </div>
                <div class="form-row">
                    <div class="form-group">
                        <label class="form-label">Nhà cung cấp *</label>
                        <select name="supplierId" id="supplierSelect" class="form-select" required>
                            <option value="">Chọn nhà cung cấp</option>
                            <c:forEach var="supplier" items="${suppliers}">
                                <option value="${supplier.supplierId}" data-contact="${supplier.contactPerson}" data-phone="${supplier.supplierPhone}">${supplier.supplierName}</option>
                            </c:forEach>
                        </select>
                    </div>
                    <div class="form-group">
                        <label class="form-label">Người liên hệ</label>
                        <input type="text" id="contactInfo" name="contactPerson" class="form-control" value="" readonly>
                    </div>
                    <div class="form-group">
                        <label class="form-label">Số điện thoại</label>
                        <input type="text" id="contactPhone" name="supplierPhone" class="form-control" value="" readonly>
                    </div>
                </div>
<script>
document.getElementById('supplierSelect').addEventListener('change', function() {
    var selected = this.options[this.selectedIndex];
    document.getElementById('contactInfo').value = selected.getAttribute('data-contact') || '';
    document.getElementById('contactPhone').value = selected.getAttribute('data-phone') || '';
});
</script>
<style>
.form-header-row {
    display: flex;
    gap: 24px;
    margin-bottom: 10px;
}
.form-header-col {
    flex: 1 1 0;
    min-width: 220px;
    max-width: 340px;
    display: flex;
    flex-direction: column;
}
.form-header-col label {
    font-weight: 500;
    font-size: 15px;
    margin-bottom: 3px;
    color: #222;
}
.form-header-col input,
.form-header-col select {
    padding: 7px 12px;
    border: 1.5px solid #e3e3e3;
    border-radius: 6px;
    font-size: 15px;
    background: #f8fafd;
    margin-bottom: 2px;
    width: 100%;
    box-sizing: border-box;
}
</style>
                <h4 style="font-weight:600; color:#222; margin-bottom: 20px;">Danh sách vật tư</h4>
                <div class="table-responsive">
                    <table class="table">
                        <thead>
                            <tr>
                                <th style="width: 25%">Tên vật tư</th>
                                <th style="width: 12%">Số lượng</th>
                                <th style="width: 15%">Đơn vị</th>
                                <th style="width: 18%">Đơn giá</th>
                                <th style="width: 8%"></th>
                            </tr>
                        </thead>
                        <tbody id="materialsContainer">
                            <tr class="material-row">
                                <td>
                                    <input type="text" name="materialName[]" class="form-control material-name-input" placeholder="Nhập tên vật tư" autocomplete="off" required oninput="showMaterialSuggestions(this)">
                                </td>
                                <td><input type="number" name="quantity[]" class="form-control" placeholder="Số lượng" min="1" required oninput="updateTotal()"></td>
                                <td><input type="text" name="unit[]" class="form-control base-unit-input" placeholder="Đơn vị" required readonly></td>
                                <td><input type="number" name="unitPrice[]" class="form-control price-input" placeholder="Đơn giá" min="0" step="0.01" required oninput="updateTotal()"></td>
                                <td><button type="button" class="btn-remove-row" onclick="removeMaterialRow(this)" title="Xóa dòng"><i class="fas fa-trash"></i></button></td>
                            </tr>
                        </tbody>
                    </table>
                </div>
                <div class="total-section">
                    <span>Tổng tiền:&nbsp;</span>
                    <span id="totalAmount" class="total-amount">0</span>
                    <span>VNĐ</span>
                </div>
                <div class="button-group">
                    <button type="button" class="btn-add" onclick="addMaterialRow()">
                        <i class="fas fa-plus"></i> Thêm dòng vật tư
                    </button>
                </div>
                <div class="form-group">
    <label for="note" style="font-weight:600;color:#1976d2;">
        <i class="fas fa-sticky-note" style="margin-right:6px;"></i>Ghi chú (không bắt buộc)
    </label>
    <textarea id="note" name="note" class="note-textarea" placeholder="Nhập ghi chú cho đơn mua này..." rows="3">${param.note != null ? param.note : ''}</textarea>
</div>
<style>
.note-textarea {
    width: 100%;
    max-width: 600px;
    min-height: 80px;
    padding: 12px 16px;
    border: 2px solid #e3e3e3;
    border-radius: 8px;
    font-family: inherit;
    font-size: 15px;
    line-height: 1.5;
    background: #f8fafd;
    color: #1976d2;
    transition: border-color 0.3s, box-shadow 0.3s;
    margin-top: 6px;
    margin-bottom: 18px;
}
.note-textarea:focus {
    outline: none;
    border-color: #1976d2;
    box-shadow: 0 0 0 3px rgba(25, 118, 210, 0.1);
}
.note-textarea::placeholder {
    color: #999;
    font-style: italic;
}
</style>
                <div class="submit-section">
                    <a href="purchaseOrderList" class="btn-secondary">Hủy</a>
                    <button type="submit" class="btn-primary">Tạo đơn mua</button>
                </div>
            </form>
    </div>
        </div>
        <div id="global-autocomplete-suggestions" class="autocomplete-suggestions" style="display: none;"></div>
        <script>
            function updateContactInfo() {
                const select = document.getElementById('supplierSelect');
                const contact = select.options[select.selectedIndex]?.getAttribute('data-contact') || '';
                const phone = select.options[select.selectedIndex]?.getAttribute('data-phone') || '';
                document.getElementById('contactInfo').value = contact;
                document.getElementById('contactPhone').value = phone;
            }
            function addMaterialRow() {
                const container = document.getElementById('materialsContainer');
                const row = document.createElement('tr');
                row.className = 'material-row';
                row.innerHTML = `
                    <td><input type="text" name="materialName[]" class="form-control material-name-input" placeholder="Nhập tên vật tư" autocomplete="off" required oninput="showMaterialSuggestions(this)"></td>
                    <td><input type="number" name="quantity[]" class="form-control" placeholder="Số lượng" min="1" required oninput="updateTotal()"></td>
                    <td><input type="text" name="unit[]" class="form-control base-unit-input" placeholder="Đơn vị" required readonly></td>
                    <td><input type="number" name="unitPrice[]" class="form-control price-input" placeholder="Đơn giá" min="0" step="0.01" required oninput="updateTotal()"></td>
                    <td><button type="button" class="btn-remove-row" onclick="removeMaterialRow(this)" title="Xóa dòng"><i class="fas fa-trash"></i></button></td>
                `;
                container.appendChild(row);
                console.log("Đã thêm dòng mới, tổng số dòng: " + container.rows.length);
            }
            function removeMaterialRow(btn) {
                const row = btn.closest('tr');
                const container = document.getElementById('materialsContainer');
                if (container.rows.length > 1) {
                    row.remove();
                    updateTotal();
                } else {
                    // Nếu chỉ còn 1 dòng, reset các input thay vì xóa
                    row.querySelectorAll('input, select').forEach(el => {
                        if (el.type === 'number' || el.type === 'text') el.value = '';
                        if (el.tagName === 'SELECT') el.selectedIndex = 0;
                    });
                }
            }
            function updateTotal() {
                let total = 0;
                document.querySelectorAll('#materialsContainer tr').forEach(row => {
                    const qty = parseFloat(row.querySelector('input[name="quantity[]"]')?.value) || 0;
                    const price = parseFloat(row.querySelector('input[name="unitPrice[]"]')?.value) || 0;
                    total += qty * price;
                });
                document.getElementById('totalAmount').textContent = total.toLocaleString('vi-VN');
            }
    document.addEventListener('input', function(e) {
        if (e.target.name === 'quantity[]' || e.target.name === 'unitPrice[]') updateTotal();
            });
            function showMaterialSuggestions(input) {
                const value = input.value.trim();
                const suggestionBox = document.getElementById('global-autocomplete-suggestions');
                
                // Ẩn suggestions nếu input rỗng hoặc không có giá trị
                if (value.length < 1) {
                    hideSuggestions();
                    return;
                }
                
                // Chỉ hiện suggestions khi đang nhập (có ít nhất 1 ký tự)
                const rect = input.getBoundingClientRect();
                suggestionBox.style.position = 'fixed';
                suggestionBox.style.top = (rect.bottom + window.scrollY) + 'px';
                suggestionBox.style.left = (rect.left + window.scrollX) + 'px';
                suggestionBox.style.width = rect.width + 'px';
                suggestionBox.style.zIndex = 99999;
                suggestionBox.innerHTML = '';
                
                fetch('${pageContext.request.contextPath}/MaterialAutocompleteServlet?query=' + encodeURIComponent(value))
                    .then(response => response.json())
                    .then(data => {
                        if (data.length > 0) {
                            suggestionBox.innerHTML = data.map(item =>
                                "<div class='suggestion-item' style='padding:8px 12px;cursor:pointer;' onclick=\"selectMaterialSuggestionGlobal('" +
                                input.name + "', '" + item.name.replace(/'/g, "\\'") + "', '" + item.unitName.replace(/'/g, "\\'") + "', '" + item.price + "', this)\">" +
                                item.name + " <span style='color:#888;font-size:12px;'>(" + item.categoryName + ")</span></div>"
                            ).join('');
                            suggestionBox.classList.add('show');
                            suggestionBox.style.display = 'block'; // Đảm bảo hiện
                            suggestionBox._activeInput = input;
                        } else {
                            hideSuggestions();
                        }
                    })
                    .catch(error => {
                        console.error('Error fetching suggestions:', error);
                        hideSuggestions();
                    });
            }
            
            function hideSuggestions() {
                const suggestionBox = document.getElementById('global-autocomplete-suggestions');
                if (suggestionBox) {
                    suggestionBox.classList.remove('show');
                    suggestionBox.innerHTML = '';
                    suggestionBox._activeInput = null;
                    suggestionBox.style.display = 'none'; // Đảm bảo ẩn hoàn toàn
                }
            }
            
            function selectMaterialSuggestionGlobal(inputName, value, unit, price, element) {
                const suggestionBox = document.getElementById('global-autocomplete-suggestions');
                const input = suggestionBox._activeInput;
                if (!input) return;
                
                input.value = value;
                const row = input.closest('tr');
                if (row) {
                    const baseUnitInput = row.querySelector('input[name="unit[]"]');
                    if (baseUnitInput) baseUnitInput.value = unit || '';
                    // Không tự động gán giá vào input unitPrice[] nữa
                }
                
                // Ẩn suggestions sau khi chọn
                hideSuggestions();
            }
            
            // Ẩn suggestions khi click ra ngoài
            document.addEventListener('click', function(e) {
                const suggestionBox = document.getElementById('global-autocomplete-suggestions');
                if (!suggestionBox.contains(e.target)) {
                    hideSuggestions();
                }
            });
            
            // Ẩn suggestions khi focus ra khỏi input
            document.addEventListener('focusout', function(e) {
                if (e.target.classList.contains('material-name-input')) {
                    // Delay một chút để cho phép click vào suggestions
                    setTimeout(() => {
                        const suggestionBox = document.getElementById('global-autocomplete-suggestions');
                        if (!suggestionBox.contains(document.activeElement)) {
                            hideSuggestions();
                        }
                    }, 150);
                }
            });

            // Chỉ reset nút khi có lỗi (load lại trang)
            window.onload = function() {
                updateTotal();
                // Đảm bảo suggestions ẩn khi trang load
                hideSuggestions();
            };

            // Debug khi submit form
    document.getElementById('createOrderForm').addEventListener('submit', function(e) {
                console.log("=== DEBUG: SUBMIT FORM ===");
                const materialNames = document.querySelectorAll('input[name="materialName[]"]');
                const quantities = document.querySelectorAll('input[name="quantity[]"]');
                const units = document.querySelectorAll('input[name="unit[]"]');
                const unitPrices = document.querySelectorAll('input[name="unitPrice[]"]');

                console.log("Số lượng dòng vật tư: " + materialNames.length);

                for (let i = 0; i < materialNames.length; i++) {
            console.log("Dòng " + (i+1) + ":");
                    console.log("  - Tên: '" + materialNames[i].value + "'");
                    console.log("  - SL: '" + quantities[i].value + "'");
                    console.log("  - Đơn vị: '" + units[i].value + "'");
                    console.log("  - Giá: '" + unitPrices[i].value + "'");
                }
            });
        </script>
    </body>
</html>

