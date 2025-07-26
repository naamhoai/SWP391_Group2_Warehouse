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
    <script src="https://kit.fontawesome.com/4b7b2b6e8b.js" crossorigin="anonymous"></script>
    <script src="js/sidebar.js"></script>
</head>
<body>
    <jsp:include page="sidebar.jsp" />
    <div class="main-content">
        <div class="header-section">
            <h1 class="page-title">Thêm Mới Vật Tư</h1>
            <div class="header-buttons">
                <a href="MaterialListServlet" class="btn btn-secondary"><i class="fas fa-arrow-left"></i> Quay Lại</a>
            </div>
        </div>
        <div class="content-card">
            <div class="detail-container">
                <form action="CreateMaterialDetail" method="post" enctype="multipart/form-data" style="margin-top:18px; width:100%; display:flex;">
                    <div class="image-panel">
                        <h3>Hình ảnh vật tư</h3>
                        <div class="image-wrapper">
                            <img id="materialImagePreview" src="image/default.png" alt="No Image" class="material-image" />
                        </div>
                        <label for="imageUpload" style="font-weight:500; color:#1e3a8a;">Đường dẫn hình ảnh:</label>
                        <input type="file" id="imageUpload" name="imageUpload" accept="image/*" style="margin-top:8px;">
                    </div>
                    <div class="info-panel">
                        <h3>Thông tin vật tư</h3>
                        <c:if test="${not empty errorMessage}">
                            <div class="alert alert-danger" style="color: red; margin-bottom: 10px;">
                                ${errorMessage}
                            </div>
                        </c:if>
                        <div class="info-grid">
                            <div class="info-item">
                                <label for="materialId">Mã VT:</label>
                                <input type="text" id="materialId" name="materialId" value="${materialId}" class="value" readonly />
                            </div>
                            <div class="info-item">
                                <label for="name">Tên vật tư:</label>
                                <input type="text" id="name" name="name" class="value" maxlength="50" required />
                            </div>
                            <div class="info-item">
                                <label for="categoryId">Danh mục:</label>
                                <select id="categoryId" name="categoryId" class="value" required>
                                    <option value="">-- Chọn danh mục --</option>
                                    <c:forEach items="${categories}" var="cat">
                                        <c:if test="${cat.parentId != null}">
                                            <option value="${cat.categoryId}">${cat.name}</option>
                                        </c:if>
                                    </c:forEach>
                                </select>
                            </div>
                            <div class="info-item">
                                <label for="unit">Đơn vị tính:</label>
                                <select id="unit" name="unit" class="value" required>
                                    <option value="">-- Chọn đơn vị --</option>
                                    <c:forEach items="${units}" var="unit">
                                        <option value="${unit.unit_id}">${unit.unit_name}</option>
                                    </c:forEach>
                                </select>
                            </div>
                            <div class="info-item">
                                <label for="status">Trạng thái:</label>
                                <select id="status" name="status" class="value" required>
                                    <option value="">-- Chọn trạng thái --</option>
                                    <option value="active">Đang kinh doanh</option>
                                    <option value="inactive">Ngừng kinh doanh</option>
                                </select>
                            </div>
                            <div class="info-item info-item-full">
                                <label for="description">Mô tả:</label>
                                <textarea id="description" name="description" rows="4" class="value"></textarea>
                            </div>
                        </div>
                        <div class="button-container">
                            <button type="submit" class="btn btn-primary"><i class="fas fa-plus"></i> Thêm Mới Vật Tư</button>
                        </div>
                    </div> <!-- info-panel -->
                </form>
            </div>
        </div>
    </div>
<script>
document.getElementById('imageUpload').addEventListener('change', function(event) {
    const [file] = event.target.files;
    if (file) {
        const preview = document.getElementById('materialImagePreview');
        preview.src = URL.createObjectURL(file);
    }
});
</script>
</body>
</html>