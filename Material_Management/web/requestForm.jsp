<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
    <head>
        <title>Tạo yêu cầu vật tư</title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
        <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
    </head>
    <body>
        <div class="container mt-4">
            <h2>Tạo yêu cầu vật tư</h2>
            <form action="createRequest" method="post" id="requestForm">
                <div class="mb-3">
                    <label class="form-label">Người làm đơn:</label>
                    <input type="text" class="form-control" value="${userName}" readonly>
                </div>
                <div class="mb-3">
                    <label class="form-label">Loại yêu cầu:</label>
                    <select name="requestType" class="form-select" required>
                        <option value="Mua Vật Tư">Mua Vật Tư</option>
                        <option value="Xuất Kho">Xuất Kho</option>
                    </select>
                </div>
                <div class="mb-3">
                    <label class="form-label">Lý do:</label>
                    <textarea name="reason" class="form-control" required></textarea>
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
                                <th></th>
                            </tr>
                        </thead>
                        <tbody id="itemsBody">
                            <tr>
                                <td>
                                    <select id="parentCategory" name="parentCategoryId">
                                        <option value="">Chọn danh mục cha</option>
                                        <c:forEach var="cat" items="${parentCategories}">
                                            <option value="${cat.categoryId}">${cat.name}</option>
                                        </c:forEach>
                                    </select>

                                </td>
                                <td>
                                    <select id="subCategory" name="categoryId">
                                        <option value="">Chọn danh mục con</option>
                                        <c:forEach var="cat" items="${subCategories}">
                                            <option value="${cat.categoryId}" data-parent="${cat.parentId}">${cat.name}</option>
                                        </c:forEach>
                                    </select>
                                </td>
                                <td><input type="text" name="materialName" class="form-control" required></td>

                                <td><input type="number" name="quantity" class="form-control" required min="1"></td>
                                <td>
                                    <select name="unit" class="form-select unit-select" required>
                                        <option value="">Chọn đơn vị</option>
                                        <c:forEach var="u" items="${unitList}">
                                            <option value="${u.baseunit}" data-category="${u.category.categoryId}">${u.baseunit}</option>
                                        </c:forEach>
                                    </select>
                                </td>
                                <td><input type="text" name="description" class="form-control"></td>
                                <td><button type="button" class="btn btn-danger btn-sm" onclick="removeRow(this)">Xóa</button></td>
                            </tr>
                        </tbody>
                    </table>
                    <button type="button" class="btn btn-secondary" onclick="addRow()">Thêm vật tư</button>
                </div>
                <input type="hidden" name="itemCount" id="itemCount" value="1">
                <button type="submit" class="btn btn-primary">Gửi yêu cầu</button>
            </form>
        </div>
        <script>
            function addRow() {
                const tbody = document.getElementById('itemsBody');
                const newRow = tbody.rows[0].cloneNode(true);
                newRow.querySelectorAll('input').forEach(input => input.value = '');
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
            document.getElementById('parentCategory').addEventListener('change', function () {
                var parentId = this.value;
                var subCatSelect = document.getElementById('subCategory');
                for (var i = 0; i < subCatSelect.options.length; i++) {
                    var opt = subCatSelect.options[i];
                    if (!opt.value) { // option "Chọn danh mục con"
                        opt.style.display = '';
                        continue;
                    }
                    if (!parentId || opt.getAttribute('data-parent') === parentId) {
                        opt.style.display = '';
                    } else {
                        opt.style.display = 'none';
                    }
                }
                subCatSelect.value = '';
            });


        </script>
    </body>
</html>