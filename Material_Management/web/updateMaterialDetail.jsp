<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>Chỉnh Sửa Vật Tư</title>
    <link rel="stylesheet" href="css/sidebar.css">
    <link rel="stylesheet" href="css/viewMaterialDetail.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/css/all.min.css">
    <script src="https://kit.fontawesome.com/4b7b2b6e8b.js" crossorigin="anonymous"></script>
    <script src="js/sidebar.js"></script>
</head>
<body>
    <jsp:include page="sidebar.jsp" />
    <div class="main-content">
        <div class="header-section">
            <h1 class="page-title">Chỉnh Sửa Vật Tư</h1>
            <div class="header-buttons">
                <a href="MaterialListServlet" class="btn btn-secondary"><i class="fas fa-arrow-left"></i> Quay Lại</a>
            </div>
        </div>
        <div class="content-card">
            <div class="detail-container">
                <div class="image-panel">
                    <h3>Hình ảnh vật tư</h3>
                    <div class="image-wrapper">
                        <c:choose>
                            <c:when test="${not empty material.imageUrl}">
                                <img src="image/${material.imageUrl}" alt="${material.name}" class="material-image" />
                            </c:when>
                            <c:otherwise>
                                <img src="image/default.png" alt="No Image" class="material-image" />
                            </c:otherwise>
                        </c:choose>
                    </div>
                    <form action="updateMaterialServlet" method="post" enctype="multipart/form-data" style="margin-top:18px;">
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
                            <input type="text" id="materialId" name="materialId" value="${material.materialId}" class="value" readonly />
                        </div>
                        <div class="info-item">
                            <label for="name">Tên vật tư:</label>
                            <input type="text" id="name" name="name" value="${material.name}" maxlength="50" class="value" required />
                        </div>
                        <div class="info-item">
                            <label for="categoryId">Danh mục:</label>
                            <select id="categoryId" name="categoryId" class="value" required>
                                <option value="">-- Chọn danh mục --</option>
                                <c:forEach items="${categories}" var="cat">
                                    <c:if test="${cat.parentId != null}">
                                        <option value="${cat.categoryId}" ${material.categoryId == cat.categoryId ? 'selected' : ''}>${cat.name}</option>
                                    </c:if>
                                </c:forEach>
                            </select>
                        </div>
                        <div class="info-item">
                            <label for="supplierId">Nhà cung cấp:</label>
                            <select id="supplierId" name="supplierId" class="value" required>
                                <option value="">-- Chọn nhà cung cấp --</option>
                                <c:forEach items="${suppliers}" var="sup">
                                    <option value="${sup.supplierId}" ${material.supplierId == sup.supplierId ? 'selected' : ''}>${sup.supplierName}</option>
                                </c:forEach>
                            </select>
                        </div>
                        <div class="info-item">
                            <label for="unit">Đơn vị tính:</label>
                            <select id="unit" name="unit" class="value" required>
                                <option value="">-- Chọn đơn vị --</option>
                                <c:forEach items="${units}" var="unit">
                                    <option value="${unit.unit_id}" ${material.unitId == unit.unit_id ? 'selected' : ''}>${unit.unit_name}</option>
                                </c:forEach>
                            </select>
                        </div>
                        <div class="info-item">
                            <label for="price">Giá:</label>
                            <input type="number" id="price" name="price" value="${material.price}" step="1" min="1" max="10000000000" class="value" required />
                        </div>
                        <div class="info-item">
                            <label for="status">Trạng thái:</label>
                            <select id="status" name="status" class="value" required>
                                <option value="">-- Chọn trạng thái --</option>
                                <option value="active" ${material.status == 'active' ? 'selected' : ''}>Đang kinh doanh</option>
                                <option value="inactive" ${material.status == 'inactive' ? 'selected' : ''}>Ngừng kinh doanh</option>
                            </select>
                        </div>
                        <div class="info-item info-item-full">
                            <label for="description">Mô tả:</label>
                            <textarea id="description" name="description" rows="4" class="value">${material.description}</textarea>
                        </div>
                    </div>
                    <div class="button-container">
                        <button type="submit" class="btn btn-primary"><i class="fas fa-save"></i> Chỉnh sửa</button>
                    </div>
                </div> <!-- info-panel -->
                </form>
            </div>
        </div>
    </div>
</body>
</html>
