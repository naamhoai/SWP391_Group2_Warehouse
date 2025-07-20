<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<html lang="vi">
    <head>
        <meta charset="UTF-8">
        <title>Nhập kho vật tư</title>
        <link rel="stylesheet" href="./css/importWarehouse.css">
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">
        <link rel="stylesheet" href="css/sidebar.css">
      
    </head>
    <body>
        <jsp:include page="sidebar.jsp" />

        <div class="container">
            <div class="top-right-history">
                <a href="importHistory" class="btn-history"><i class="fa fa-history"></i> Lịch sử</a>
            </div>
            <h3 style="color: green">${requestScope.mess}</h3>
            <form action="ImportWarehouseServlet" method="post">
                <div class="form-section ">
                    <h3><i class="fa fa-info-circle"></i> Thông tin nhập kho</h3>
                    
                    <div class="form-group">
                       <label for="nguoiNhap">Vai trò:</label> 
                        <input type="text" id="nguoiNhap" name="name" required readonly value="${requestScope.role}">
                    </div>
                </div>
                <div class="form-group">
                    <label for="reason">Lý do nhập lại:</label>
                    <input id="reason" name="reason" rows="2" placeholder="Nhập lý do nhập lại"required 
                              
                    >
                </div>
                <div class="form-group">
                    <label for="nguoiGiao">Ai giao đến:</label>
                    <input type="text" id="nguoiGiao" name="nguoiGiao" placeholder="Tên người giao vật tư" required 
                           maxlength="20" 
                           pattern="[a-zA-ZÀ-ỹ0-9 ]+"
                           title="Tên không quá 20 kí tự"
                           >
                </div>
                <div class="form-group">
                    <label for="nguoiNhan">Ai nhận:</label>
                    <input type="text" id="nguoiNhan" name="nguoiNhan" value="${requestScope.username}">
                </div>
                <div class="form-group">
                    <label for="phone">Số điện thoại người giao:</label>
                    <input type="number" id="soDienThoaiNguoiGiao" name="soDienThoaiNguoiGiao" placeholder="Số điện thoại người giao" required 
                           min="0" maxlength="10"
                           title="độ dài số điện thoại >= 10"
                           >
                </div>
                <div class="form-group">
                    <label for="tenDuAn">Tên dự án:</label>
                    <input type="text" id="tenDuAn" name="tenDuAn" placeholder="Tên dự án liên quan" required
                           maxlength="20"
                           title="Không quá 20 kí tự."
                           
                           >
                </div>
                <div class="form-section">
                    <h3><i class="fa fa-list"></i> Danh sách vật tư</h3>
                    <table class="table-vattu" id="tableVatTu">
                        <thead>
                            <tr>
                                <th>Tên vật tư</th>
                                <th>Số lượng</th>
                                <th>Đơn vị</th>
                                <th>Tình trạng</th>
                                <th>Hành động</th>
                            </tr>
                        </thead>
                        <tbody>
                            <tr>
                                <td>
                                    <input type="text" name="namevt[]" required list="materialNameList" autocomplete="off" placeholder="Tên vật tư">
                                    <datalist id="materialNameList">
                                        <c:forEach var="m" items="${materialList}">
                                            <option value="${m.materialId}-${m.name}" />
                                        </c:forEach>
                                    </datalist>
                                </td>
                                <td><input type="number" name="number[]" min="1" required placeholder="Số lượng" title="Nhập số lượng"></td>
                                <td><input type="text" name="unit[]" class="unit-input" required readonly placeholder="Đơn vị"></td>
                                <td>
                                    <select name="status[]" required>
                                        <option value="Mới">Mới</option>
                                        <option value="Cũ">Cũ</option>
                                    </select>
                                </td>
                                <td><button type="button" class="btn-delete" onclick="xoaDong(this)">Xóa</button></td>
                            </tr>
                        </tbody>
                    </table>
                    <button type="button" class="btn-add" id="btnThemVatTu">Thêm vật tư</button>
                </div>
                <div style="text-align:right">
                    <button type="submit" class="btn-add">Nhập kho</button>
                </div>
            </form>
        </div>

        <script>
            var materialUnitMap = {};
            <c:forEach var="m" items="${materialList}">
            materialUnitMap['${m.materialId}-${m.name}'] = '${m.unitName}';
            </c:forEach>
                console.log('materialUnitMap:', materialUnitMap);

                function autoFillUnit(materialInput) {
                    const row = materialInput.closest('tr');
                    const unitInput = row.querySelector('.unit-input');
                    const key = materialInput.value; // sẽ là "74-Thép V5 (sắt V 50x50x5mm)"
                    if (unitInput) {
                        unitInput.value = materialUnitMap[key] || '';
                    }
                }

                function addInputEvents(input) {
                    input.addEventListener('input', () => autoFillUnit(input));
                    input.addEventListener('change', () => autoFillUnit(input));
                    input.addEventListener('blur', () => setTimeout(() => autoFillUnit(input), 200));
                }

                function xoaDong(btn) {
                    const row = btn.closest('tr');
                    const tbody = row.parentNode;
                    if (tbody.rows.length > 1) {
                        row.remove();
                    } else {
                        row.querySelectorAll('input, select').forEach(el => {
                            if (el.tagName === 'SELECT')
                                el.selectedIndex = 0;
                            else
                                el.value = '';
                        });
                    }
                }

                document.addEventListener('DOMContentLoaded', () => {
                    document.querySelectorAll('input[name="namevt[]"]').forEach(addInputEvents);

                    document.getElementById('btnThemVatTu').addEventListener('click', () => {
                        const tbody = document.querySelector('#tableVatTu tbody');
                        const newRow = document.createElement('tr');
                        newRow.innerHTML = `
                        <td><input type="text" name="namevt[]" required list="materialNameList" autocomplete="off" placeholder="Tên vật tư"></td>
                        <td><input type="number" name="number[]" min="1" required placeholder="Số lượng"></td>
                        <td><input type="text" name="unit[]" class="unit-input" required readonly placeholder="Đơn vị"></td>
                        <td>
                            <select name="status[]" required>
                                <option value="Mới">Mới</option>
                                <option value="Cũ">Cũ</option>
                            </select>
                        </td>
                        <td><button type="button" class="btn-delete" onclick="xoaDong(this)">Xóa</button></td>`;
                        tbody.appendChild(newRow);
                        // Gắn lại sự kiện cho input mới
                        addInputEvents(newRow.querySelector('input[name="namevt[]"]'));
                    });
                });
        </script>
    </body>
</html>
