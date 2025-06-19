<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html>
    <head>
        <title>Chỉnh sửa yêu cầu vật tư</title>
        <link rel="stylesheet" href="css/requestForm.css">
        <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
    </head>
    <body>
        <div class="request-form-container">
            <h2>Chỉnh sửa yêu cầu vật tư</h2>
            <form action="editRequest" method="post" id="requestForm">
                <input type="hidden" name="requestId" value="${request.requestId}">
                <div class="form-group">
                    <label class="form-label">Người làm đơn:</label>
                    <input type="text" class="input-text" value="${userName}" readonly>
                </div>
                <div class="form-group">
                    <label class="form-label">Loại yêu cầu:</label>
                    <select name="requestType" class="input-select" required>
                        <option value="Mua Vật Tư" ${request.requestType == 'Mua Vật Tư' ? 'selected' : ''}>Mua Vật Tư</option>
                        <option value="Xuất Kho" ${request.requestType == 'Xuất Kho' ? 'selected' : ''}>Xuất Kho</option>
                    </select>
                </div>
                <div class="form-group">
                    <label class="form-label">Lý do:</label>
                    <textarea name="reason" class="input-textarea" required>${request.reason}</textarea>
                </div>

                <!-- Thêm phần ghi chú của giám đốc -->
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
                            <c:forEach var="d" items="${details}" varStatus="loop">
                                <tr>
                                    <td>
                                        <select name="parentCategoryId" class="input-select parentCategory" required>
                                            <option value="">Chọn danh mục cha</option>
                                            <c:forEach var="cat" items="${parentCategories}">
                                                <option value="${cat.categoryId}" ${cat.categoryId == d.parentCategoryId ? 'selected' : ''}>${cat.name}</option>
                                            </c:forEach>
                                        </select>
                                    </td>
                                    <td>
                                        <select name="categoryId" class="input-select subCategory" required>
                                            <option value="">Chọn danh mục con</option>
                                            <c:forEach var="cat" items="${subCategories}">
                                                <option value="${cat.categoryId}" data-parent="${cat.parentId}" ${cat.categoryId == d.categoryId ? 'selected' : ''}>${cat.name}</option>
                                            </c:forEach>
                                        </select>
                                    </td>
                                    <td>
                                        <select name="materialName" class="input-select materialSelect" required data-selected="${d.materialName}">
                                            <option value="">Chọn vật tư</option>
                                            <c:if test="${d.materialName != null}">
                                                <option value="${d.materialName}" selected>${d.materialName}</option>
                                            </c:if>
                                        </select>
                                    </td>
                                    <td><input type="number" name="quantity" class="input-text" value="${d.quantity}" required min="1"></td>
                                    <td>
                                        <select name="unit" class="input-select unit-select" required>
                                            <option value="">Chọn đơn vị</option>
                                            <c:forEach var="u" items="${unitList}">
                                                <option value="${u.baseunit}" ${u.baseunit == d.unitName ? 'selected' : ''}>${u.baseunit}</option>
                                            </c:forEach>
                                        </select>
                                    </td>
                                    <td><input type="text" name="description" class="input-text" value="${d.description}"></td>
                                    <td>
                                        <select name="materialCondition" class="input-select" required>
                                            <option value="Mới" ${d.materialCondition == 'Mới' ? 'selected' : ''}>Mới</option>
                                            <option value="Cũ" ${d.materialCondition == 'Cũ' ? 'selected' : ''}>Cũ</option>
                                            <option value="Hỏng" ${d.materialCondition == 'Hỏng' ? 'selected' : ''}>Hỏng</option>
                                        </select>
                                    </td>
                                    <td>
                                        <button type="button" class="btn-remove" onclick="removeRow(this)">Xóa</button>
                                    </td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                    <button type="button" class="btn-add" onclick="addRow()">Thêm vật tư</button>
                </div>
                <input type="hidden" name="itemCount" id="itemCount" value="${fn:length(details)}">
                <button type="submit" class="btn-submit">Gửi lại yêu cầu</button>
                <a href="staffDashboard" class="btn-back">Quay lại</a>
            </form>
        </div>

        <script>
            $(document).ready(function () {
                // Khởi tạo object lưu danh sách vật tư theo danh mục
                const materialsByCategory = {};
            <c:forEach var="material" items="${materialList}">
                if (!materialsByCategory['${material.categoryId}']) {
                    materialsByCategory['${material.categoryId}'] = [];
                }
                materialsByCategory['${material.categoryId}'].push({
                    name: '${fn:escapeXml(material.name)}'
                });
            </c:forEach>

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
                    var currentSelectedMaterial = $materialSelect.data('selected');

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

                        // Nếu có giá trị đã chọn trước đó và nó tồn tại trong danh sách mới
                        if (currentSelectedMaterial) {
                            $materialSelect.val(currentSelectedMaterial);
                        }
                    }
                });

                // Khởi tạo ban đầu cho các hàng đã có
                initializeExistingRows();
            });

            function initializeExistingRows() {
                $('.parentCategory').each(function () {
                    var $row = $(this).closest('tr');
                    var $subCatSelect = $row.find('.subCategory');
                    var $materialSelect = $row.find('.materialSelect');

                    // Lưu giá trị đã chọn
                    var selectedParentId = $(this).val();
                    var selectedSubCatId = $subCatSelect.val();
                    var selectedMaterial = $materialSelect.data('selected');

                    if (selectedParentId) {
                        // Cập nhật danh mục con
                        $subCatSelect.html('<option value="">Chọn danh mục con</option>');
            <c:forEach var="subCat" items="${subCategories}">
                        if ('${subCat.parentId}' === selectedParentId) {
                            $subCatSelect.append(
                                    $('<option>', {
                                        value: '${subCat.categoryId}',
                                        text: '${fn:escapeXml(subCat.name)}'
                                    })
                                    );
                        }
            </c:forEach>

                        // Khôi phục giá trị đã chọn cho danh mục con
                        if (selectedSubCatId) {
                            $subCatSelect.val(selectedSubCatId);

                            // Cập nhật danh sách vật tư
                            if (materialsByCategory[selectedSubCatId]) {
                                materialsByCategory[selectedSubCatId].forEach(function (material) {
                                    $materialSelect.append(
                                            $('<option>', {
                                                value: material.name,
                                                text: material.name
                                            })
                                            );
                                });

                                // Khôi phục giá trị đã chọn cho vật tư
                                if (selectedMaterial) {
                                    $materialSelect.val(selectedMaterial);
                                }
                            }
                        }
                    }
                });
            }

            function resetMaterialSelect($select) {
                $select.html('<option value="">Chọn vật tư</option>');
            }

            function addRow() {
                const tbody = document.getElementById('itemsBody');
                const newRow = tbody.rows[0].cloneNode(true);

                // Reset tất cả các giá trị trong hàng mới
                newRow.querySelectorAll('select, input').forEach(input => {
                    input.value = '';
                    if (input.classList.contains('materialSelect')) {
                        $(input).removeData('selected');
                    }
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