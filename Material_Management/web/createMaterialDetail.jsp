<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>Thêm Mới Vật Tư</title>
    <link rel="stylesheet" href="css/addMaterialDetail.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/css/all.min.css">
</head>
<body>
    <div id="main-content">
        <div class="container">
            <h1 class="page-title">Thêm Mới Vật Tư</h1>
            <p class="page-description">Nhập thông tin vật tư mới vào hệ thống.</p>
            
            <c:if test="${not empty errorMessage}">
                <div class="alert alert-danger" style="color: red; border: 1px solid red; padding: 10px; margin-bottom: 15px; border-radius: 4px;">
                    ${errorMessage}
                </div>
            </c:if>

            <form action="CreateMaterialDetail" method="post" enctype="multipart/form-data" class="add-form">
                <div class="form-section">
                    <h3 class="form-section-title">Thông Tin Bắt Buộc</h3>
                    <div class="form-group">
                        <label for="materialId">ID vật tư <span class="required">*</span></label>
                        <input type="number" id="materialId" name="materialId" placeholder="Nhập ID vật tư" min="1" max="99999" value="${materialId}" required>
                    </div>
                    <div class="form-group">
                        <label for="name">Tên vật tư <span class="required">*</span></label>
                        <input type="text" id="name" name="name" placeholder="Ví dụ: Dây điện Cadivi CV 1.5mm" maxlength="50" value="${name}" required>
                    </div>

                    <div class="form-row">
                        <div class="form-group">
                            <label for="category">Danh mục <span class="required">*</span></label>
                            <select id="category" name="category" required>
                                <option value="">-- Chọn danh mục --</option>
                                <c:forEach items="${categories}" var="cat">
                                    <option value="${cat.categoryId}" ${category eq cat.categoryId ? 'selected' : ''}>${cat.name}</option>
                                </c:forEach>
                            </select>
                        </div>
                        <div class="form-group">
                            <label for="supplier">Nhà cung cấp <span class="required">*</span></label>
                            <select id="supplier" name="supplier" required>
                                <option value="">-- Chọn nhà cung cấp --</option>
                                <c:forEach items="${suppliers}" var="sup">
                                    <option value="${sup.supplierId}" ${supplier eq sup.supplierId ? 'selected' : ''}>${sup.supplierName}</option>
                                </c:forEach>
                            </select>
                        </div>
                    </div>

                    <div class="form-row">
                        <div class="form-group">
                            <label for="price">Giá (VNĐ) <span class="required">*</span></label>
                            <input type="number" id="price" name="price" value="${price}" step="1" min="0" max="10000000000" required>
                        </div>
                    </div>
                </div>

                <div class="form-section">
                    <h3 class="form-section-title">Thông Tin Bổ Sung (Không bắt buộc)</h3>
                    <div class="form-group">
                        <label for="imageUpload">Hình ảnh đại diện</label>
                        <input type="file" id="imageUpload" name="imageUpload" accept="image/*">
                    </div>
                    <div class="form-group">
                        <label for="description">Mô tả chi tiết</label>
                        <textarea id="description" name="description" rows="5" placeholder="Nhập mô tả, thông số kỹ thuật, quy cách...">${description}</textarea>
                    </div>
                    <div class="form-group">
                        <label for="materialCondition">Tình trạng vật tư</label>
                        <select id="materialCondition" name="materialCondition">
                            <option value="">-- Chọn tình trạng --</option>
                            <option value="Mới">Mới</option>
                            <option value="Đã qua sử dụng">Đã qua sử dụng</option>
                        </select>
                    </div>
                </div>

                <div class="form-actions">
                    <a href="MaterialListServlet" class="btn btn-secondary">
                        <i class="fas fa-times"></i> Hủy Bỏ
                    </a>
                    <button type="submit" class="btn btn-primary">
                        <i class="fas fa-plus"></i> Tạo Mới Vật Tư
                    </button>
                </div>
            </form>
        </div>
    </div>
</body>
</html>