<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html>
    <head>
        <title>Chỉnh sửa yêu cầu vật tư</title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
        <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
    </head>
    <body>
        <div class="container mt-4">
            <h2>Chỉnh sửa yêu cầu vật tư</h2>
            <form action="editRequest" method="post" id="requestForm">
                <input type="hidden" name="requestId" value="${request.requestId}">
                <div class="mb-3">
                    <label class="form-label">Người làm đơn:</label>
                    <input type="text" class="form-control" value="${userName}" readonly>
                </div>
                <div class="mb-3">
                    <label class="form-label">Loại yêu cầu:</label>
                    <select name="requestType" class="form-select" required>
                        <option value="Mua Vật Tư" ${request.requestType == 'Mua Vật Tư' ? 'selected' : ''}>Mua Vật Tư</option>
                        <option value="Xuất Kho" ${request.requestType == 'Xuất Kho' ? 'selected' : ''}>Xuất Kho</option>
                    </select>
                </div>
                <div class="mb-3">
                    <label class="form-label">Lý do:</label>
                    <textarea name="reason" class="form-control" required>${request.reason}</textarea>
                </div>
                <div class="mb-3">
                    <h4>Danh sách vật tư</h4>
                    <table class="table" id="itemsTable">
                        <thead>
                            <tr>
                                <th>Danh mục vật tư</th>
                                <th>Loại vật tư (ID)</th>
                                <th>Tên vật tư</th>
                                <th>Số lượng</th>
                                <th>Đơn vị</th>
                                <th>Mô tả</th>
                                <th>Tình trạng</th>
                                <th></th>
                            </tr>
                        </thead>
                        <tbody id="itemsBody">
                            <c:forEach var="d" items="${details}" varStatus="loop">
                                <tr>
                                    <td>
                                        <select name="parentCategoryId" class="form-select parentCategory">
                                            <option value="">Chọn danh mục cha</option>
                                            <c:forEach var="cat" items="${parentCategories}">
                                                <option value="${cat.categoryId}" ${cat.categoryId == d.parentCategoryId ? 'selected' : ''}>${cat.name}</option>
                                            </c:forEach>
                                        </select>
                                    </td>
                                    <td>
                                        <select name="categoryId" class="form-select subCategory">
                                            <option value="">Chọn danh mục con</option>
                                            <c:forEach var="cat" items="${subCategories}">
                                                <option value="${cat.categoryId}" data-parent="${cat.parentId}" ${cat.categoryId == d.categoryId ? 'selected' : ''}>${cat.name}</option>
                                            </c:forEach>
                                        </select>
                                    </td>
                                    <td><input type="text" name="materialName" class="form-control" value="${d.materialName}" required></td>
                                    <td><input type="number" name="quantity" class="form-control" value="${d.quantity}" required min="1"></td>
                                    <td>
                                        <select name="unit" class="form-select unit-select" required>
                                            <option value="">Chọn đơn vị</option>
                                            <c:forEach var="u" items="${unitList}">
                                                <option value="${u.baseunit}" ${u.baseunit == d.unitName ? 'selected' : ''}>${u.baseunit}</option>
                                            </c:forEach>
                                        </select>
                                    </td>
                                    <td><input type="text" name="description" class="form-control" value="${d.description}"></td>
                                    <td>
                                        <select name="materialCondition" class="form-select" required>
                                            <option value="Mới" ${d.materialCondition == 'Mới' ? 'selected' : ''}>Mới</option>
                                            <option value="Cũ" ${d.materialCondition == 'Cũ' ? 'selected' : ''}>Cũ</option>
                                            <option value="Hỏng" ${d.materialCondition == 'Hỏng' ? 'selected' : ''}>Hỏng</option>
                                        </select>
                                    </td>
                                    <td>
                                        <button type="button" class="btn btn-danger btn-sm" onclick="removeRow(this)">Xóa</button>
                                    </td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                    <button type="button" class="btn btn-secondary" onclick="addRow()">Thêm vật tư</button>
                </div>
                <input type="hidden" name="itemCount" id="itemCount" value="${fn:length(details)}">
                <button type="submit" class="btn btn-primary">Gửi lại yêu cầu</button>
                <a href="staffDashboard" class="btn btn-secondary">Quay lại</a>
            </form>
        </div>
        <script>
            function addRow() {
                const tbody = document.getElementById('itemsBody');
                const newRow = tbody.rows[0].cloneNode(true);
                newRow.querySelectorAll('input, select').forEach(input => input.value = '');
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
            // Ẩn/hiện danh mục con theo danh mục cha
            $(document).on('change', '.parentCategory', function () {
                var parentId = $(this).val();
                var $row = $(this).closest('tr');
                var $subCatSelect = $row.find('.subCategory');
                $subCatSelect.find('option').each(function () {
                    var opt = $(this);
                    if (!opt.val()) {
                        opt.show();
                        return;
                    }
                    if (!parentId || opt.data('parent') == parentId) {
                        opt.show();
                    } else {
                        opt.hide();
                    }
                });
                $subCatSelect.val('');
            });
        </script>
    </body>
</html>