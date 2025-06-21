<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html>
    <head>
        <title>Chỉnh sửa yêu cầu vật tư</title>
        <link rel="stylesheet" href="css/requestForm.css">
        <style>
            .info-message {
                padding: 15px;
                background-color: #e7f3fe;
                border-left: 6px solid #2196F3;
                margin-bottom: 20px;
                border-radius: 4px;
            }
            .info-message p {
                margin: 0;
                font-size: 15px;
            }
        </style>
    </head>
    <body>
        <c:set var="isEditable" value="${sessionScope.roleId == 4 && request.requestStatus == 'Rejected'}" />

        <div class="request-form-container">
            <c:if test="${!isEditable}">
                <div class="info-message">
                    <p><strong>Thông báo:</strong> Bạn chỉ có thể xem nội dung. Việc chỉnh sửa yêu cầu chỉ được phép khi bạn là nhân viên và yêu cầu ở trạng thái "Đã từ chối".</p>
                </div>
            </c:if>

            <h2>Chỉnh sửa yêu cầu vật tư</h2>
            <form action="editRequest" method="post" id="requestForm">
                <input type="hidden" name="requestId" value="${request.requestId}">
                <div class="form-group">
                    <label class="form-label">Người làm đơn:</label>
                    <input type="text" class="input-text" value="${userName}" readonly>
                </div>
                <div class="form-group">
                    <label class="form-label">Loại yêu cầu:</label>
                    <select name="requestType" class="input-select" required ${!isEditable ? 'disabled' : ''}>
                        <option value="Mua Vật Tư" ${request.requestType == 'Mua Vật Tư' ? 'selected' : ''}>Mua Vật Tư</option>
                        <option value="Xuất Kho" ${request.requestType == 'Xuất Kho' ? 'selected' : ''}>Xuất Kho</option>
                    </select>
                </div>
                <div class="form-group">
                    <label class="form-label">Lý do:</label>
                    <textarea name="reason" class="input-textarea" required ${!isEditable ? 'disabled' : ''}>${request.reason}</textarea>
                </div>

                <c:if test="${request.directorNote != null && !empty request.directorNote}">
                    <div class="form-group">
                        <label class="form-label">Ghi chú từ giám đốc:</label>
                        <textarea class="input-textarea" readonly>${request.directorNote}</textarea>
                    </div>
                </c:if>

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
                            <c:forEach var="d" items="${details}" varStatus="loop">
                                <tr>
                                    <td>
                                        <input type="text" name="materialName" class="input-text" value="${d.materialName}" required ${!isEditable ? 'disabled' : ''} list="materialNameList" autocomplete="off" />
                                    </td>
                                    <td><input type="number" name="quantity" class="input-text" value="${d.quantity}" required min="1" ${!isEditable ? 'disabled' : ''}></td>
                                    <td>
                                        <input type="text" name="unit" class="input-text" value="${d.unitName}" required ${!isEditable ? 'disabled' : ''} list="unitDataList" autocomplete="off" />
                                    </td>
                                    <td>
                                        <select name="materialCondition" class="input-select" required ${!isEditable ? 'disabled' : ''}>
                                            <option value="Mới" ${d.materialCondition == 'Mới' ? 'selected' : ''}>Mới</option>
                                            <option value="Cũ" ${d.materialCondition == 'Cũ' ? 'selected' : ''}>Cũ</option>
                                            <option value="Hỏng" ${d.materialCondition == 'Hỏng' ? 'selected' : ''}>Hỏng</option>
                                        </select>
                                    </td>
                                    <td><button type="button" class="btn-remove" onclick="removeRow(this)" ${!isEditable ? 'disabled' : ''}>Xóa</button></td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                    <datalist id="materialNameList">
                        <c:forEach var="m" items="${materialList}">
                            <option value="${m.name}" />
                        </c:forEach>
                    </datalist>
                    <datalist id="unitDataList">
                        <c:forEach var="u" items="${unitList}">
                            <option value="${u.baseunit}" />
                        </c:forEach>
                    </datalist>
                    <button type="button" class="btn-add" onclick="addRow()" ${!isEditable ? 'disabled' : ''}>Thêm vật tư</button>
                </div>
                <input type="hidden" name="itemCount" id="itemCount" value="${fn:length(details)}">
                <button type="submit" class="btn-submit" ${!isEditable ? 'disabled' : ''}>Gửi lại yêu cầu</button>
                <a href="javascript:history.back()" class="btn-back">Quay lại</a>
            </form>
        </div>

        <script>
            function addRow() {
                const tbody = document.getElementById('itemsBody');
                const newRow = tbody.insertRow();
                newRow.innerHTML = `
                    <td><input type="text" name="materialName" class="input-text" required list="materialNameList" autocomplete="off"></td>
                    <td><input type="number" name="quantity" class="input-text" required min="1"></td>
                    <td>
                        <input type="text" name="unit" class="input-text" required list="unitDataList" autocomplete="off">
                    </td>
                    <td>
                        <select name="materialCondition" class="input-select" required>
                            <option value="Mới">Mới</option>
                            <option value="Cũ">Cũ</option>
                            <option value="Hỏng">Hỏng</option>
                        </select>
                    </td>
                    <td><button type="button" class="btn-remove" onclick="removeRow(this)">Xóa</button></td>
                `;
            }

            function removeRow(button) {
                const row = button.closest('tr');
                if (document.getElementById('itemsBody').rows.length > 1) {
                    row.remove();
                } else {
                    alert('Phải có ít nhất một vật tư trong yêu cầu.');
                }
            }
        </script>
    </body>
</html>