<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>Cập Nhật Vật Tư</title>
    <link rel="stylesheet" href="css/addMaterialDetail.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/css/all.min.css">
</head>
<body>
    <div id="main-content">
        <div class="container">
            <h1 class="page-title">Cập Nhật Vật Tư</h1>
            <p class="page-description">Chỉnh sửa các thông tin vật tư và lưu thay đổi vào hệ thống.</p>
            
            <c:if test="${not empty errorMessage}">
                <div class="alert alert-danger" style="color: red; border: 1px solid red; padding: 10px; margin-bottom: 15px; border-radius: 4px;">
                    ${errorMessage}
                </div>
            </c:if>

            <form action="updateMaterialServlet" method="post" enctype="multipart/form-data" class="add-form">
                
                <div class="form-section">
                    <h3 class="form-section-title">Thông Tin Bắt Buộc</h3>
                    <div class="form-group">
                        <label for="materialId">Mã Vật Tư:</label>
                        <input type="text" id="materialId" name="materialId" value="${material.materialId}" readonly>
                    </div>

                    <div class="form-group">
                        <label for="name">Tên vật tư <span class="required">*</span></label>
                        <input type="text" id="name" name="name" placeholder="Ví dụ: Dây điện Cadivi CV 1.5mm" value="${material.name}" required>
                    </div>

                    <div class="form-row">
                        <div class="form-group">
                            <label for="categoryId">Danh mục <span class="required">*</span></label>
                            <select id="categoryId" name="categoryId" required>
                                <option value="">-- Chọn danh mục --</option>
                                <c:forEach items="${categories}" var="cat">
                                    <option value="${cat.categoryId}" ${material.categoryId == cat.categoryId ? 'selected' : ''}>${cat.name}</option>
                                </c:forEach>
                            </select>
                        </div>
                        <div class="form-group">
                            <label for="supplierId">Nhà cung cấp <span class="required">*</span></label>
                            <select id="supplierId" name="supplierId" required>
                                <option value="">-- Chọn nhà cung cấp --</option>
                                <c:forEach items="${suppliers}" var="sup">
                                    <option value="${sup.supplierId}" ${material.supplierId == sup.supplierId ? 'selected' : ''}>${sup.supplierName}</option>
                                </c:forEach>
                            </select>
                        </div>
                    </div>

                    <div class="form-row">
                        <div class="form-group">
                            <label for="price">Giá (VNĐ) <span class="required">*</span></label>
                            <input type="number" id="price" name="price" value="${material.price}" step="1" min="1" required>
                        </div>
                    </div>
                </div>

                <div class="form-section">
                    <h3 class="form-section-title">Thông Tin Bổ Sung (Không bắt buộc)</h3>
                    <div class="form-group">
                        <label for="imageUrl">Hình ảnh đại diện hiện tại:</label>
                        <c:if test="${not empty material.imageUrl}">
                            <img src="image/${material.imageUrl}" alt="Current Material Image" style="max-width: 150px; margin-bottom: 10px; display: block;">
                        </c:if>
                        <label for="imageUpload">Upload ảnh mới (nếu muốn thay đổi):</label>
                        <input type="file" id="imageUpload" name="imageUpload" accept="image/*">
                    </div>
                    <div class="form-group">
                        <label for="description">Mô tả chi tiết</label>
                        <textarea id="description" name="description" rows="5" placeholder="Nhập mô tả, thông số kỹ thuật, quy cách...">${material.description}</textarea>
                    </div>
                    <div class="form-group">
                        <label for="materialCondition">Tình trạng vật tư</label>
                        <select id="materialCondition" name="materialCondition">
                            <option value="">-- Chọn tình trạng --</option>
                            <option value="Mới" ${material.materialCondition == 'Mới' ? 'selected' : ''}>Mới</option>
                            <option value="Đã qua sử dụng" ${material.materialCondition == 'Đã qua sử dụng' ? 'selected' : ''}>Đã qua sử dụng</option>
                            <option value="Hỏng" ${material.materialCondition == 'Hỏng' ? 'selected' : ''}>Hỏng</option>
                        </select>
                    </div>
                </div>

                <div class="form-actions">
                    <a href="MaterialListServlet" class="btn btn-secondary">
                        <i class="fas fa-times"></i> Hủy Bỏ
                    </a>
                    <button type="submit" class="btn btn-primary">
                        <i class="fas fa-save"></i> Cập Nhật Vật Tư
                    </button>
                </div>
            </form>
        </div>
    </div>
</body>
</html>
