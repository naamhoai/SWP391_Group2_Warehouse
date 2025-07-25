<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="java.util.*, model.Category, dao.CategoryDAO"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Quản lý danh mục</title>

    <link rel="stylesheet" href="css/sidebar.css">
    <link rel="stylesheet" href="css/common.css">
    <link rel="stylesheet" href="css/category.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/css/all.min.css">
</head>
<body>
    <jsp:include page="sidebar.jsp" />
    <div id="main-content">
            
        <div class="dashboard-header">
            <div class="header-left">
                <h1>Quản lý danh mục</h1>
            </div>
            <div class="header-actions">
                <a href="categories?action=add" class="btn-add">+ Thêm mới loại vật tư</a>
            </div>
        </div>

        <!-- Thông báo thành công và lỗi -->
        <c:if test="${not empty sessionScope.message}">
            <div class="message success" style="margin: 20px 0; padding: 12px; background: #e6ffe6; color: #207520; border: 1px solid #b2e2b2; border-radius: 4px; font-weight: 500;">
                ${sessionScope.message}
                <c:remove var="message" scope="session"/>
            </div>
        </c:if>
        <c:if test="${not empty sessionScope.error}">
            <div class="message error">
                ${sessionScope.error}
                <c:remove var="error" scope="session"/>
            </div>
        </c:if>

        <form action="categories" method="get" class="filter-form">
            <input type="text" name="keyword" placeholder="Tìm kiếm loại vật tư"
                   value="${keyword}" class="filter-input">

            <select name="parentId" class="filter-select" onchange="this.form.submit()">
                <option value="">Tất cả danh mục vật tư</option>
                <c:forEach items="${parentCategories}" var="parent">
                    <option value="${parent.categoryId}" ${parent.categoryId == param.parentId ? 'selected' : ''}>
                        ${parent.name}
                    </option>
                </c:forEach>
            </select>

            <select name="hiddenFilter" class="filter-select" onchange="this.form.submit()">
                <option value="all" ${param.hiddenFilter == null || param.hiddenFilter == 'all' ? 'selected' : ''}>Tất cả trạng thái</option>
                <option value="visible" ${param.hiddenFilter == 'visible' ? 'selected' : ''}>Đang hiển thị</option>
                <option value="hidden" ${param.hiddenFilter == 'hidden' ? 'selected' : ''}>Đã ẩn</option>
            </select>

            <button type="submit" name="sortBy" value="id" class="sort-btn ${param.sortBy == 'id' ? 'active' : ''}" onclick="toggleSortOrder();">
                Sắp xếp theo ID
                <c:choose>
                    <c:when test="${param.sortBy == 'id' && param.sortOrder == 'desc'}">↓</c:when>
                    <c:otherwise>↑</c:otherwise>
                </c:choose>
            </button>
            <input type="hidden" id="sortOrder" name="sortOrder" value="${param.sortOrder != null ? param.sortOrder : 'asc'}" />

            <button type="submit" class="search-btn">Tìm kiếm</button>
        </form>
        <div class="table-card">
            <table class="category-table">
                <thead>
                    <tr>
                        <th>ID</th>
                        <th>Loại vật tư</th>
                        <th>Danh mục vật tư</th>
                        <th>Ngày tạo</th>
                        <th>Ngày cập nhật</th>
                        <th>Thao tác</th>
                    </tr>
                </thead>
                <tbody>
                    <c:choose>
                        <c:when test="${empty categories}">
                            <c:choose>
                                <c:when test="${not empty param.parentId}">
                                    <tr>
                                        <td></td>
                                        <td class="no-data">Chưa có Loại vật tư nào</td>
                                        <td>
                                            <c:forEach items="${parentCategories}" var="parent">
                                                <c:if test="${parent.categoryId == param.parentId}">
                                                    ${parent.name}
                                                </c:if>
                                            </c:forEach>
                                        </td>
                                        <td></td>
                                        <td></td>
                                        <td></td>
                                    </tr>
                                </c:when>
                                <c:otherwise>
                                    <tr>
                                        <td colspan="6" class="no-data">Không tìm thấy danh mục nào.</td>
                                    </tr>
                                </c:otherwise>
                            </c:choose>
                        </c:when>
                        <c:otherwise>
                            <c:forEach items="${categories}" var="category">
                                <c:if test="${not empty category.parentId}">
                                    <tr class="${category.hidden ? 'hidden-category' : ''}">
                                        <td>${category.categoryId}</td>
                                        <td>${category.name}</td>
                                        <td>${parentNames[category.parentId]}</td>
                                        <td><fmt:formatDate value="${category.createdAt}" pattern="dd/MM/yyyy"/></td>
                                        <td><fmt:formatDate value="${category.updatedAt}" pattern="dd/MM/yyyy"/></td>
                                        <td class="action-buttons">
                                            <a href="categories?action=edit&id=${category.categoryId}" class="btn-edit">Sửa</a>
                                            <c:choose>
                                                <c:when test="${category.hidden}">
                                                    <a href="categories?action=unhide&id=${category.categoryId}" class="btn-unhide" onclick="return confirm('Bạn có chắc muốn hiện lại danh mục này?');">Đã ẩn</a>
                                                </c:when>
                                                <c:otherwise>
                                                    <a href="categories?action=hide&id=${category.categoryId}" class="btn-hide" onclick="return confirm('Bạn có chắc muốn ẩn danh mục này?');">Ẩn</a>
                                                </c:otherwise>
                                            </c:choose>
                                        </td>
                                    </tr>
                                </c:if>
                            </c:forEach>
                        </c:otherwise>
                    </c:choose>
                </tbody>
            </table>
        </div>
        <c:if test="${totalPages > 1}">
            <div class="pagination">
              <div class="pagination-form">
                <c:choose>
                    <c:when test="${currentPage > 1}">
                        <a href="${pageContext.request.contextPath}/categories?page=${currentPage - 1}${param.keyword != null ? '&keyword=' : ''}${param.keyword != null ? param.keyword : ''}${param.parentId != null ? '&parentId=' : ''}${param.parentId != null ? param.parentId : ''}${param.sortBy != null ? '&sortBy=' : ''}${param.sortBy != null ? param.sortBy : ''}${param.hiddenFilter != null ? '&hiddenFilter=' : ''}${param.hiddenFilter != null ? param.hiddenFilter : ''}"
                           class="page-button" aria-label="Trang trước">&laquo;</a>
                    </c:when>
                    <c:otherwise>
                        <button class="page-button" disabled aria-label="Trang trước">&laquo;</button>
                    </c:otherwise>
                </c:choose>

                <c:forEach begin="${startPage}" end="${endPage}" var="i">
                    <a href="${pageContext.request.contextPath}/categories?page=${i}${param.keyword != null ? '&keyword=' : ''}${param.keyword != null ? param.keyword : ''}${param.parentId != null ? '&parentId=' : ''}${param.parentId != null ? param.parentId : ''}${param.sortBy != null ? '&sortBy=' : ''}${param.sortBy != null ? param.sortBy : ''}${param.hiddenFilter != null ? '&hiddenFilter=' : ''}${param.hiddenFilter != null ? param.hiddenFilter : ''}"
                       class="page-button${i == currentPage ? ' active' : ''}">${i}</a>
                </c:forEach>

                <c:choose>
                    <c:when test="${currentPage < totalPages}">
                        <a href="${pageContext.request.contextPath}/categories?page=${currentPage + 1}${param.keyword != null ? '&keyword=' : ''}${param.keyword != null ? param.keyword : ''}${param.parentId != null ? '&parentId=' : ''}${param.parentId != null ? param.parentId : ''}${param.sortBy != null ? '&sortBy=' : ''}${param.sortBy != null ? param.sortBy : ''}${param.hiddenFilter != null ? '&hiddenFilter=' : ''}${param.hiddenFilter != null ? param.hiddenFilter : ''}"
                           class="page-button" aria-label="Trang sau">&raquo;</a>
                    </c:when>
                    <c:otherwise>
                        <button class="page-button" disabled aria-label="Trang sau">&raquo;</button>
                    </c:otherwise>
                </c:choose>
              </div>
              <div class="pagination-info">
                  Tổng số danh mục: ${totalCategories}
              </div>
            </div>
        </c:if>
        
    </div>
</body>
<script>
function toggleSortOrder() {
    const sortOrderInput = document.getElementById('sortOrder');
    const currentSortBy = '${param.sortBy}';
    if (currentSortBy === 'id') {
        sortOrderInput.value = sortOrderInput.value === 'asc' ? 'desc' : 'asc';
    } else {
        sortOrderInput.value = 'asc';
    }
}
</script>
</html>