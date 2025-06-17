<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>Thêm Vật Tư Mới</title>
    <link rel="stylesheet" href="css/sidebar.css">
    <link rel="stylesheet" href="css/addMaterialDetail.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/css/all.min.css">
</head>
<body>
    <jsp:include page="sidebar.jsp" />

    <div id="main-content">
        <div class="container">
            <h1 class="page-title">Thêm Vật Tư Mới</h1>
            <p class="page-description">Điền các thông tin dưới đây để tạo một vật tư mới trong hệ thống.</p>
            
            <%-- Hiển thị thông báo lỗi nếu có --%>
            <c:if test="${not empty errorMessage}">
                <div class="alert alert-danger" style="color: red; border: 1px solid red; padding: 10px; margin-bottom: 15px; border-radius: 4px;">
                    ${errorMessage}
                </div>
            </c:if>

            <%-- Form sẽ gửi dữ liệu đến Servlet /addMaterial --%>
            <form action="addMaterial" method="post" enctype="multipart/form-data" class="add-form">
                
                <div class="form-section">
                    <h3 class="form-section-title">Thông Tin Bắt Buộc</h3>
                    <div class="form-group">
                        <label for="name">Tên vật tư <span class="required">*</span></label>
                        <input type="text" id="name" name="name" placeholder="Ví dụ: Dây điện Cadivi CV 1.5mm" required>
                    </div>

                    <div class="form-row">
                        <div class="form-group">
                            <label for="categoryId">Danh mục <span class="required">*</span></label>
                            <select id="categoryId" name="categoryId" required>
                                <option value="">-- Chọn danh mục --</option>
                                <c:forEach items="${categories}" var="cat">
                                    <option value="${cat.categoryId}">${cat.name}</option>
                                </c:forEach>
                            </select>
                        </div>
                        <div class="form-group">
                            <label for="supplierId">Nhà cung cấp <span class="required">*</span></label>
                            <select id="supplierId" name="supplierId" required>
                                <option value="">-- Chọn nhà cung cấp --</option>
                                <c:forEach items="${suppliers}" var="sup">
                                    <option value="${sup.supplierId}">${sup.supplierName}</option>
                                </c:forEach>
                            </select>
                        </div>
                    </div>

                    <div class="form-row">
                        <div class="form-group">
                            <label for="price">Giá (VNĐ) <span class="required">*</span></label>
                            <input type="number" id="price" name="price" value="0" step="100" min="0" required>
                        </div>
                        <div class="form-group">
                            <label for="conversionId">Đơn vị tính <span class="required">*</span></label>
                            <select id="conversionId" name="conversionId" required>
                                <option value="">-- Chọn đơn vị --</option>
                                <c:forEach items="${units}" var="unit">
                                    <option value="${unit.material.conversionId}">${unit.baseunit}</option>
                                </c:forEach>
                            </select>
                        </div>
                    </div>
                </div>

                <div class="form-section">
                    <h3 class="form-section-title">Thông Tin Bổ Sung (Không bắt buộc)</h3>
                    <div class="form-group">
                        <label for="imageUrl">Hình ảnh đại diện</label>
                        <input type="file" id="imageUrl" name="imageUrl" accept="image/*">
                    </div>
                    <div class="form-group">
                        <label for="description">Mô tả chi tiết</label>
                        <textarea id="description" name="description" rows="5" placeholder="Nhập mô tả, thông số kỹ thuật, quy cách..."></textarea>
                    </div>
                </div>

                <div class="form-actions">
                    <a href="materialDetailList.jsp" class="btn btn-secondary">
                        <i class="fas fa-times"></i> Hủy Bỏ
                    </a>
                    <button type="submit" class="btn btn-primary">
                        <i class="fas fa-plus-circle"></i> Tạo Mới Vật Tư
                    </button>
                </div>
            </form>
        </div>
    </div>
</body>
</html>