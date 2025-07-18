<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Thêm Loại Vật Tư</title>
        <link rel="stylesheet" href="css/category.css">
    </head>
    <body>
        <div class="container">
            <div class="form-container">
                <h2>Thêm danh mục mới</h2>

                <!-- Thông báo thành công -->
                <c:if test="${not empty success}">
                    <div class="message success">
                        ${success}
                    </div>
                </c:if>
                
                <!-- Thông báo lỗi từ session -->
                <c:if test="${not empty sessionScope.error}">
                    <div class="message error">
                        ${sessionScope.error}
                    </div>
                    <% session.removeAttribute("error"); %>
                </c:if>
                
                <!-- Thông báo lỗi từ request -->
                <c:if test="${not empty error}">
                    <div class="message error">
                        ${error}
                    </div>
                </c:if>

                <form action="${pageContext.request.contextPath}/categories" method="post" class="category-form" onsubmit="return validateForm()">
                    <input type="hidden" name="action" value="add">

                    <div class="form-field">
                        <label for="name">Loại Vật Tư <span class="required">*</span></label>
                        <input type="text" id="name" name="name" 
                               placeholder="Nhập tên Loại Vật Tư" maxlength="255"
                               value="${not empty error ? name : ''}">
                        <span class="error-message" id="nameError"></span>
                    </div>

                    <div class="form-field">
                        <label for="parentId">Danh mục vật tư</label>
                        <select id="parentId" name="parentId">
                            <option value="">Không chọn (tạo danh mục cha)</option>
                            <c:forEach items="${parentCategories}" var="category">
                                <option value="${category.categoryId}" 
                                    <c:if test="${not empty error && parentId != null && parentId == category.categoryId}">selected</c:if>>
                                    ${category.name}
                                </option>
                            </c:forEach>
                        </select>
                        <span class="error-message" id="parentError"></span>
                    </div>

                    <div class="form-actions">
                        <a href="${pageContext.request.contextPath}/categories" class="btn-cancel">Hủy</a>
                        <button type="submit" class="btn-submit">Thêm mới</button>
                    </div>
                </form>
            </div>
        </div>

        <script>
            function validateForm() {
                let isValid = true;
                const nameInput = document.getElementById('name');
                const nameError = document.getElementById('nameError');
                // Reset error messages
                nameError.textContent = '';
                // Validate name
                if (!nameInput.value.trim()) {
                    nameError.textContent = 'Vui lòng nhập tên danh mục';
                    isValid = false;
                } else if (nameInput.value.trim().length < 2) {
                    nameError.textContent = 'Tên danh mục phải có ít nhất 2 ký tự';
                    isValid = false;
                }
                // Không cần validate parentId nữa
                return isValid;
            }
        </script>
    </body>
</html>
