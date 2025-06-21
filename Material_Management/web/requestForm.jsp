<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html>
    <head>
        <title>Tạo yêu cầu vật tư</title>
        <link rel="stylesheet" href="css/requestForm.css">
        <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
    </head>
    <body>
        <div class="request-form-container">
            <h2>Tạo yêu cầu vật tư</h2>
            <form action="createRequest" method="post" id="requestForm">
                <div class="form-group">
                    <label class="form-label">Người làm đơn:</label>
                    <input type="text" class="input-text" value="${userName}" readonly>
                </div>
                <div class="form-group">
                    <label class="form-label">Loại yêu cầu:</label>
                    <select name="requestType" class="input-select" required>
                        <option value="Mua Vật Tư">Mua Vật Tư</option>
                        <option value="Xuất Kho">Xuất Kho</option>
                    </select>
                </div>
                <div class="form-group">
                    <label class="form-label">Lý do:</label>
                    <textarea name="reason" class="input-textarea" required></textarea>
                </div>
                <div class="form-group">
                    <h4>Danh sách vật tư</h4>
                    <table class="request-items-table" id="itemsTable">
                        <thead>
                            <tr>
                                <th>Tên vật tư</th>
                                <th>Số lượng</th>
                                <th>Đơn vị</th>
                                <th>Tình trạng</th>
                                <th></th>
                            </tr>
                        </thead>
                        <tbody id="itemsBody">
                            <tr>
                                <td>
                                    <input name="materialName" class="input-text materialNameInput" list="materialNameList" autocomplete="off" required />
                                    <datalist id="materialNameList">
                                        <c:forEach var="m" items="${materialList}">
                                            <option value="${m.name}" />
                                        </c:forEach>
                                    </datalist>
                                </td>
                                <td>
                                    <input type="number" name="quantity" class="input-text" required min="1">
                                </td>
                                <td>
                                    <input name="unit" class="input-text unitInput" list="unitList" autocomplete="off" required />
                                    <datalist id="unitList">
                                        <c:forEach var="u" items="${unitList}">
                                            <option value="${u.baseunit}" />
                                        </c:forEach>
                                    </datalist>
                                </td>
                                <td>
                                    <select name="materialCondition" class="input-select" required>
                                        <option value="Mới">Mới</option>
                                        <option value="Cũ">Cũ</option>
                                        <option value="Hỏng">Hỏng</option>
                                    </select>
                                </td>
                                <td>
                                    <button type="button" class="btn-remove" onclick="removeRow(this)">Xóa</button>
                                </td>
                            </tr>
                        </tbody>
                    </table>
                    <button type="button" class="btn-add" onclick="addRow()">Thêm vật tư</button>
                </div>
                <input type="hidden" name="itemCount" id="itemCount" value="1">
                <button type="submit" class="btn-submit">Gửi yêu cầu</button>
                <button type="button" class="btn-back" onclick="window.history.back()">Quay lại</button>
            </form>
        </div>

        <script>
            function addRow() {
                const tbody = document.getElementById('itemsBody');
                const newRow = tbody.rows[0].cloneNode(true);
                // Reset tất cả các giá trị trong hàng mới
                newRow.querySelectorAll('select, input').forEach(input => {
                    input.value = '';
                });
                tbody.appendChild(newRow);
                document.getElementById('itemCount').value = tbody.rows.length;
            }

            function removeRow(btn) {
                const tbody = document.getElementById('itemsBody');
                if (tbody.rows.length > 1) {
                    btn.closest('tr').remove();
                    document.getElementById('itemCount').value = tbody.rows.length;
                }
            }
        </script>
    </body>
</html>