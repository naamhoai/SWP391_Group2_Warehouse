<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html>
<html lang="vi">
    <head>
        <meta charset="UTF-8">
        <title>Thêm Mới Vật Tư</title>
        <link rel="stylesheet" href="css/sidebar.css">
        <link rel="stylesheet" href="css/addMaterialDetail.css">
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/css/all.min.css">
    </head>
    <body>
        <jsp:include page="sidebar.jsp" />
        <div class="main-content">
            <div class="content-card">
                <div class="card-header-row">
                    <div></div>
                    <a href="MaterialListServlet" class="btn-back"><i class="fas fa-arrow-left"></i> Quay lại</a>
                </div>
                <div class="detail-container">
                    <div class="image-panel">
                        <h3 class="section-title">Hình ảnh</h3>
                        <div class="image-wrapper">
                            <img src="image/default.png" alt="No Image" class="material-image" />
                        </div>
                        <div class="file-label">Đường dẫn hình ảnh:</div>
                        <input type="file" id="imageUpload" name="imageUpload" class="input-blue" accept="image/*">
                    </div>
                    <div class="info-panel">
                        <h3 class="section-title">Thông tin chi tiết</h3>
                        <c:if test="${not empty errorMessage}">
                            <div class="alert alert-danger" style="color: red; margin-bottom: 10px;">
                                ${errorMessage}
                            </div>
                        </c:if>
                        <form action="CreateMaterialDetail" method="post" enctype="multipart/form-data">
                            <div class="info-grid">
                                <div class="info-item">
                                    <label for="materialId">Mã VT:</label>
                                    <input type="number" id="materialId" name="materialId" class="input-blue" min="1" max="99999" required>
                                </div>
                                <div class="info-item">
                                    <label for="name">Tên vật tư:</label>
                                    <input type="text" id="name" name="name" class="input-blue" maxlength="50" required>
                                </div>
                                <div class="info-item">
                                    <label for="categoryId">Danh mục:</label>
                                    <select id="categoryId" name="categoryId" class="input-blue" required>
                                        <option value="">-- Chọn danh mục --</option>
                                        <c:forEach items="${categories}" var="cat">
                                            <c:if test="${cat.parentId != null}">
                                                <option value="${cat.categoryId}">${cat.name}</option>
                                            </c:if>
                                        </c:forEach>
                                    </select>
                                </div>
                                <div class="info-item">
                                    <label for="supplierId">Nhà cung cấp:</label>
                                    <select id="supplierId" name="supplierId" class="input-blue" required>
                                        <option value="">-- Chọn nhà cung cấp --</option>
                                        <c:forEach items="${suppliers}" var="sup">
                                            <option value="${sup.supplierId}">${sup.supplierName}</option>
                                        </c:forEach>
                                    </select>
                                </div>
                                <div class="info-item">
                                    <label for="unit">Đơn vị tính:</label>
                                    <select id="unit" name="unit" class="input-blue" required>
                                        <option value="">-- Chọn đơn vị --</option>
                                        <c:forEach items="${units}" var="unit">
                                            <option value="${unit.unit_id}">${unit.unit_name}</option>
                                        </c:forEach>
                                    </select>
                                </div>
                                <div class="info-item">
                                    <label for="price">Giá:</label>
                                    <input type="number" id="price" name="price" class="input-blue" min="1" max="10000000000" required>
                                </div>
                                <div class="info-item">
                                    <label for="status">Trạng thái vật tư:</label>
                                    <select id="status" name="status" class="input-blue" required>
                                        <option value="">-- Chọn trạng thái --</option>
                                        <option value="active">Đang Hoạt động</option>
                                        <option value="inactive">Ngừng hoạt động</option>
                                    </select>
                                </div>
                                <div class="info-item info-item-full">
                                    <label for="description">Mô tả:</label>
                                    <textarea id="description" name="description" rows="3" class="input-blue"></textarea>
                                </div>
                            </div>
                            <div class="button-container">
                                <button type="submit" class="btn btn-primary"><i class="fas fa-plus"></i> Thêm Mới Vật Tư</button>
                            </div>
                        </form>
                    </div>
                </div>
            </div>
        </div>
        <script>
            document.addEventListener('DOMContentLoaded', function () {
                const input = document.getElementById('imageUpload');
                const img = document.querySelector('.material-image');
                input.addEventListener('change', function (e) {
                    if (input.files && input.files[0]) {
                        const reader = new FileReader();
                        reader.onload = function (ev) {
                            img.src = ev.target.result;
                        }
                        reader.readAsDataURL(input.files[0]);
                    }
                });
            });
        </script>
    </body>
</html>