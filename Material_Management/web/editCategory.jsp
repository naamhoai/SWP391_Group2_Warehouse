<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Sửa Loại Vật Tư</title>
        <link rel="stylesheet" href="css/category.css">
    </head>
    <body>
        <div class="container">
            <div class="form-container">
                <h2>Chỉnh sửa danh mục</h2>

                <!-- Thông báo thành công -->
                <c:if test="${not empty success}">
                    <div class="message success">
                        ${success}
                    </div>
                </c:if>

                <!-- Thông báo lỗi -->
                <c:if test="${not empty error}">
                    <div class="message error">
                        ${error}
                    </div>
                </c:if>

                <form action="${pageContext.request.contextPath}/categories" method="post" class="category-form" onsubmit="return validateForm()">
                    <input type="hidden" name="action" value="edit">
                    <input type="hidden" name="id" value="${category.categoryId}">

                    <div class="form-field">
                        <label for="name">Loại Vật Tư</label>
                        <input type="text" id="name" name="name" value="${category.name}" placeholder="Nhập tên Loại Vật Tư" required>
                        <span class="error-message" id="nameError"></span>
                    </div>

                    <div class="form-field">
                        <label for="parentId">Danh mục vật tư <span class="required">*</span></label>
                        <select id="parentId" name="parentId">
                            <option value="">Chọn danh mục vật tư</option>
                            <c:forEach items="${parentCategories}" var="parent">
                                <option value="${parent.categoryId}" ${parent.categoryId == category.parentId ? 'selected' : ''}>
                                    ${parent.name}
                                </option>
                            </c:forEach>
                        </select>
                        <span class="error-message" id="parentError"></span>
                    </div>

                    <div class="form-actions">
                        <a href="${pageContext.request.contextPath}/categories" class="btn-cancel">Hủy</a>
                        <button type="submit" class="btn-submit">Lưu thay đổi</button>
                    </div>
                </form>
            </div>
        </div>

        <script>
            function validateForm() {
                let isValid = true;
                const nameInput = document.getElementById('name');
                const parentInput = document.getElementById('parentId');
                const nameError = document.getElementById('nameError');
                const parentError = document.getElementById('parentError');
                
                // Reset error messages
                nameError.textContent = '';
                parentError.textContent = '';
                
                // Validate name
                if (!nameInput.value.trim()) {
                    nameError.textContent = 'Vui lòng nhập tên danh mục';
                    isValid = false;
                } else if (nameInput.value.trim().length < 2) {
                    nameError.textContent = 'Tên danh mục phải có ít nhất 2 ký tự';
                    isValid = false;
                }
                
                // Validate parent category
                if (!parentInput.value) {
                    parentError.textContent = 'Vui lòng chọn danh mục vật tư';
                    isValid = false;
                }
                
                return isValid;
            }
        </script>
    </body>
</html>
