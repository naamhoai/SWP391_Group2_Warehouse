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
                                <th>Danh mục vật tư</th>
                                <th>Loại vật tư</th>
                                <th>Tên vật tư</th>
                                <th>Số lượng</th>
                                <th>Đơn vị</th>
                                <th>Mô tả</th>
                                <th>Tình trạng</th>
                                <th></th>
                            </tr>
                        </thead>
                        <tbody id="itemsBody">
                            <tr>
                                <td>
                                    <select name="parentCategoryId" class="input-select parentCategory" required>
                                        <option value="">Chọn danh mục cha</option>
                                        <c:forEach var="cat" items="${parentCategories}">
                                            <option value="${cat.categoryId}">${cat.name}</option>
                                        </c:forEach>
                                    </select>
                                </td>
                                <td>
                                    <select name="categoryId" class="input-select subCategory" required>
                                        <option value="">Chọn danh mục con</option>
                                        <c:forEach var="cat" items="${subCategories}">
                                            <option value="${cat.categoryId}" data-parent="${cat.parentId}">${cat.name}</option>
                                        </c:forEach>
                                    </select>
                                </td>
                                <td>
                                    <select name="materialName" class="input-select materialSelect" required>
                                        <option value="">Chọn vật tư</option>
                                    </select>
                                </td>
                                <td>
                                    <input type="number" name="quantity" class="input-text" required min="1">
                                </td>
                                <td>
                                    <select name="unit" class="input-select" required>
                                        <option value="">Chọn đơn vị</option>
                                        <c:forEach var="u" items="${unitList}">
                                            <option value="${u.baseunit}">${u.baseunit}</option>
                                        </c:forEach>
                                    </select>
                                </td>
                                <td>
                                    <input type="text" name="description" class="input-text">
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
            </form>
        </div>

        <c:set var="categoryIds" value=""/>
        <c:forEach var="material" items="${materialList}">
            <c:if test="${not fn:contains(categoryIds, material.categoryId)}">
                <c:set var="categoryIds" value="${categoryIds}${material.categoryId},"/>
            </c:if>
        </c:forEach>
        <script>
            // Sinh materialsByCategory từ server
            const materialsByCategory = {
            <c:forEach var="catId" items="${fn:split(categoryIds, ',')}" varStatus="catLoop">
                <c:if test="${not empty catId}">
                '${catId}': [
                    <c:forEach var="m" items="${materialList}" varStatus="matLoop">
                        <c:if test="${m.categoryId == catId}">
                            { name: '${fn:escapeXml(m.name)}' }<c:if test="${!matLoop.last}">,</c:if>
                        </c:if>
                    </c:forEach>
                ]<c:if test="${!catLoop.last}">,</c:if>
                </c:if>
            </c:forEach>
            };

            $(document).ready(function () {
                // Xử lý khi thay đổi danh mục cha
                $(document).on('change', '.parentCategory', function () {
                    var parentId = $(this).val();
                    var $row = $(this).closest('tr');
                    var $subCatSelect = $row.find('.subCategory');
                    var $materialSelect = $row.find('.materialSelect');

                    // Reset các select phía sau
                    $subCatSelect.html('<option value="">Chọn danh mục con</option>');
                    resetMaterialSelect($materialSelect);

                    if (parentId) {
                        // Lọc và thêm các danh mục con tương ứng
                        <c:forEach var="subCat" items="${subCategories}">
                        if ('${subCat.parentId}' === parentId) {
                            $subCatSelect.append(
                                    $('<option>', {
                                        value: '${subCat.categoryId}',
                                        text: '${fn:escapeXml(subCat.name)}'
                                    })
                                    );
                        }
                        </c:forEach>
                    }
                });

                // Xử lý khi thay đổi danh mục con
                $(document).on('change', '.subCategory', function () {
                    var categoryId = $(this).val();
                    var $row = $(this).closest('tr');
                    var $materialSelect = $row.find('.materialSelect');

                    resetMaterialSelect($materialSelect);

                    if (categoryId && materialsByCategory[categoryId]) {
                        materialsByCategory[categoryId].forEach(function (material) {
                            $materialSelect.append(
                                    $('<option>', {
                                        value: material.name,
                                        text: material.name
                                    })
                                    );
                        });
                    }
                });
            });

            function resetMaterialSelect($select) {
                $select.html('<option value="">Chọn vật tư</option>');
            }

            function addRow() {
                const tbody = document.getElementById('itemsBody');
                const newRow = tbody.rows[0].cloneNode(true);
                // Reset tất cả các giá trị trong hàng mới
                newRow.querySelectorAll('select, input').forEach(input => {
                    input.value = '';
                });
                // Reset các dropdown
                $(newRow).find('.subCategory').html('<option value="">Chọn danh mục con</option>');
                $(newRow).find('.materialSelect').html('<option value="">Chọn vật tư</option>');
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