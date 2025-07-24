<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%
    java.util.Date now = new java.util.Date();
    request.setAttribute("now", now);
%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Thêm mới loại vật tư</title>
        <link rel="stylesheet" href="css/sidebar.css">
        <link rel="stylesheet" href="css/common.css">
        <link rel="stylesheet" href="css/category.css">
    </head>
    <body>
        <div class="container">
            <div class="form-container">
                <h1>Thêm mới loại vật tư</h1>
                <div style="height: 24px;"></div>
                <!-- Thông báo thành công -->
                <c:if test="${not empty success}">
                    <div class="message success">
                        ${success}
                    </div>
                </c:if>
                <!-- Thông báo lỗi từ session -->
                <c:if test="${not empty sessionScope.error}">
                    <div class="message error" style="margin-left: 32px; margin-right: 32px;">
                        Tên loại vật tư đã tồn tại. Vui lòng nhập tên khác.
                    </div>
                    <c:remove var="error" scope="session"/>
                </c:if>
                <!-- Thông báo lỗi từ request -->
                <c:if test="${not empty error}">
                    <div class="message error" style="margin-left: 32px; margin-right: 32px;">
                        Tên loại vật tư đã tồn tại. Vui lòng nhập tên khác.
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
                    <div class="form-field">
                        <label for="createdAt">Ngày tạo</label>
                        <input type="text" id="createdAt" 
                               value="<fmt:formatDate value='${now}' pattern='dd/MM/yyyy'/>"
                               readonly style="background-color: #f5f5f5; color: #666;">
                    </div>
                    <div class="form-actions">
                        <a href="${pageContext.request.contextPath}/categories" class="btn-cancel">Hủy</a>
                        <button type="submit" class="btn-add">+ Thêm mới loại vật tư</button>
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
