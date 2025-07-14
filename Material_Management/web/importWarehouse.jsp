<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
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
        <a href="#" class="btn-history"><i class="fa fa-history"></i> Lịch sử</a>
    </div>
    <h3 style="color: green">${requestScope.mess}</h3>
    <form action="ImportWarehouseServlet" method="post">
        <div class="form-section">
            <h3><i class="fa fa-info-circle"></i> Thông tin nhập kho</h3>
            <div class="form-group">
                <label for="nguoiNhap">Người nhâp đơn:</label>
                <input type="text" id="nguoiNhap" name="name" required readonly value="${requestScope.username}">
            </div>
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
                            <input type="text" name="namevt" required list="materialNameList" autocomplete="off" placeholder="Tên vật tư">
                            <datalist id="materialNameList">
                                <c:forEach var="m" items="${materialList}">
                                    <option value="${m.materialId}-${m.name}" />
                                </c:forEach>
                            </datalist>
                        </td>
                        <td><input type="number" name="number" min="1" required placeholder="Số lượng"></td>
                        <td><input type="text" name="unit" required readonly placeholder="Đơn vị"></td>
                        <td>
                            <select name="status" required>
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
        materialUnitMap['${fn:replace(m.materialId, "'", "\\'")}-${fn:replace(m.name, "'", "\\'")}'] = '${fn:replace(m.unit, "'", "\\'")}';
    </c:forEach>

    function autoFillUnit(input) {
        const tr = input.closest('tr');
        const unitInput = tr.querySelector('input[name="unit"]');
        const key = input.value.trim();
        unitInput.value = materialUnitMap[key] || '';
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
                if (el.tagName === 'SELECT') el.selectedIndex = 0;
                else el.value = '';
            });
        }
    }

    document.addEventListener('DOMContentLoaded', () => {
        addInputEvents(document.querySelector('input[name="namevt"]'));

        document.getElementById('btnThemVatTu').addEventListener('click', () => {
            const tbody = document.querySelector('#tableVatTu tbody');
            const newRow = document.createElement('tr');
            newRow.innerHTML = `
                <td><input type="text" name="namevt" required list="materialNameList" autocomplete="off" placeholder="Tên vật tư"></td>
                <td><input type="number" name="number" min="1" required placeholder="Số lượng"></td>
                <td><input type="text" name="unit" required readonly placeholder="Đơn vị"></td>
                <td>
                    <select name="status" required>
                        <option value="Mới">Mới</option>
                        <option value="Cũ">Cũ</option>
                    </select>
                </td>
                <td><button type="button" class="btn-delete" onclick="xoaDong(this)">Xóa</button></td>`;
            tbody.appendChild(newRow);
            addInputEvents(newRow.querySelector('input[name="namevt"]'));
        });
    });
</script>
</body>
</html>
