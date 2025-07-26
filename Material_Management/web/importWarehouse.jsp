<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<html lang="vi">
    <head>
        <meta charset="UTF-8">
        <title>Nhập lại vật tư</title>
        <link rel="stylesheet" href="./css/importWarehouse.css">
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">
        <link rel="stylesheet" href="css/sidebar.css">

    </head>
    <body>
        <jsp:include page="sidebar.jsp" />

        <div class="container">
            <div class="top-right-history">
                <a href="${pageContext.request.contextPath}/importHistoryList" class="btn-history"><i class="fa fa-history"></i>danh sách đơn nhập­</a>
            </div>
            <h3 style="color: green">${requestScope.mess}</h3>
            <form action="ImportWarehouseServlet" method="post">
                <div class="form-section ">
                    <h2><i class="fa fa-info-circle"></i> thông tin nhập kho</h2>
                </div>
                <div class="form-row">
                    <div class="form-group">
                        <label for="projectSelect">Tên dự án đã xuất:</label>
                        <select id="projectSelect" name="project" required>
                            <option value="">-- Chọn dự án --</option>
                            <c:forEach var="p" items="${projectList}">
                                <option value="${p}">${p}</option>
                            </c:forEach>
                        </select>
                    </div>
                    <div class="form-group" style="flex:1; min-width:180px;">
                        <label for="reason">Lí do nhập lại:</label>
                        <input id="reason" name="reason" placeholder="Lí do nhập lại" required>
                    </div>
                </div>
                <div class="form-row">
                    <div class="form-group" style="flex:0.33; min-width:120px;">
                        <label for="nguoiNhan">Người nhận:</label>
                        <input type="text" id="nguoiNhan" name="nguoiNhan" value="${requestScope.username}">
                    </div>
                </div>
                <div class="form-row">
                    <div class="form-group" style="flex:1; min-width:180px;">
                        <label for="nguoiGiao">Người giao đến:</label>
                        <input type="text" id="nguoiGiao" name="nguoiGiao" placeholder="Người giao đến " required maxlength="20"" title="nhập tên">
                    </div>
                    <div class="form-group" style="flex:1; min-width:180px;">

                        <label for="soDienThoaiNguoiGiao">Số điện thoại người giao:</label>
                        <input type="text" id="soDienThoaiNguoiGiao" name="soDienThoaiNguoiGiao" placeholder="Số điện thoại người giao" required pattern="[0-9]{10}" maxlength="10" inputmode="numeric" title="Vui lòng nhập đúng 10 chữ số, không chứa chữ hay ký tự đặc biệt">
                    </div>
                </div>
                <div class="form-section">
                    <h3><i class="fa fa-list"></i> Danh sách vật tư</h3>
                    <table class="table-vattu" id="tableVatTu">
                        <thead>
                            <tr>
                                <th>tên vật tư</th>
                                <th>Số lương</th>
                                <th>đơn vị</th>
                                <th>Trạng thái</th>
                                <th>Hành động</th>
                            </tr>
                        </thead>
                        <tbody>
                            <tr>
                                <td>
                                    <div class="form-group">
                                        <label for="materialSelect">Tên vật tư:</label>
                                        <select class="material-select" name="namevt[]" required disabled>
                                            <option value="">-- Chọn vật tư --</option>
                                            <c:forEach var="list" items="${requestScope.list}">
                                                <option value="${list.materialId}">${list.name}</option>
                                            </c:forEach>
                                        </select>
                                    </div>
                                </td>
                                <td><input type="number" name="number[]" min="1" required placeholder="Số lượng" title="nhập số lượng"></td>
                                <td>
                                    <div class="form-group">
                                        <label for="unitInput">Đơn vị:</label>
                                        <input type="text" class="unit-input" name="unit[]" readonly>
                                    </div>
                                </td>
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
        <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
        <script>
            let materialUnitMap = {};
            let materialNameMap = {};

            function updateAllMaterialSelects(data) {
                materialUnitMap = {};
                materialNameMap = {};
                document.querySelectorAll('.material-select').forEach(select => {
                    select.innerHTML = '<option value="">Chọn vật tư</option>';
                    data.forEach(material => {
                        const key = material.materialId;
                        const option = document.createElement('option');
                        option.value = key;
                        option.textContent = material.name;
                        select.appendChild(option);
                        materialUnitMap[key] = material.unitName;
                        materialNameMap[key] = material.name;
                    });
                    select.disabled = false;
                });
                document.querySelectorAll('.unit-input').forEach(input => input.value = '');
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
                document.querySelectorAll('.material-select').forEach(select => {
                    select.addEventListener('change', () => autoFillUnit(select));
                });
                document.getElementById('btnThemVatTu').addEventListener('click', () => {
                    const tbody = document.querySelector('#tableVatTu tbody');
                    const newRow = document.createElement('tr');
                    newRow.innerHTML = `
                        <td>
                            <div class="form-group">
                                <label for="materialSelect">Tên vật tư:</label>
                                <select class="material-select" name="namevt[]" required>
                                    <option value="">-- Chọn vật tư --</option>
                                </select>
                            </div>
                        </td>
                        <td><input type="number" name="number[]" min="1" required placeholder="Số lượng"></td>
                        <td>
                            <div class="form-group">
                                <label for="unitInput">Đơn vị:</label>
                                <input type="text" class="unit-input" name="unit[]" readonly>
                            </div>
                        </td>
                        <td>
                            <select name="status[]" required>
                                <option value="Mới">Mới</option>
                                <option value="Cũ">Cũ</option>
                            </select>
                        </td>
                        <td><button type="button" class="btn-delete" onclick="xoaDong(this)">Xóa</button></td>`;
                    tbody.appendChild(newRow);
                    const newSelect = newRow.querySelector('.material-select');
                    newSelect.addEventListener('change', () => autoFillUnit(newSelect));
                    
                    // Nếu đã có dữ liệu vật tư, render lại option cho select mới
                    if (Object.keys(materialNameMap).length > 0) {
                        newSelect.innerHTML = '<option value="">Chọn vật tư</option>';
                        for (const key in materialNameMap) {
                            const option = document.createElement('option');
                            option.value = key;
                            option.textContent = materialNameMap[key];
                            newSelect.appendChild(option);
                        }
                        newSelect.disabled = false;
                    } else {
                        newSelect.disabled = true;
                    }
                });
            });

            $('#projectSelect').on('change', function() {
                var project = $(this).val();
                if (!project) {
                    document.querySelectorAll('.material-select').forEach(select => {
                        select.innerHTML = '<option value="">-- Chọn vật tư --</option>';
                        select.disabled = true;
                    });
                    document.querySelectorAll('.unit-input').forEach(input => input.value = '');
                    materialUnitMap = {};
                    materialNameMap = {};
                    return;
                }
                $.ajax({
                    url: 'ImportWarehouseServlet',
                    method: 'GET',
                    data: { action: 'getMaterialsByProject', project: project },
                    success: function(data) {
                        updateAllMaterialSelects(data);
                    }
                });
            });

            // Khi chọn vật tư, chỉ fill đơn vị cho dòng đó
            $(document).on('change', '.material-select', function() {
                const key = $(this).val();
                const row = $(this).closest('tr');
                const unitInput = row.find('.unit-input');
                unitInput.val(materialUnitMap[key] || '');
            });
        </script>
    </body>
</html>