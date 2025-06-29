<%@page contentType="text/html" pageEncoding="UTF-8"%>
cósa<%@page import="java.util.*, model.Category, dao.CategoryDAO"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%
    String error = (String) session.getAttribute("error");
    String message = (String) session.getAttribute("message");
    if (error != null) {
        session.removeAttribute("error");
    }
    if (message != null) {
        session.removeAttribute("message");
    }
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Quản lý danh mục</title>
    <link rel="stylesheet" href="css/category.css">
    <link rel="stylesheet" href="css/footer.css">
    <link rel="stylesheet" href="css/sidebar.css">
    <link rel="stylesheet" href="css/dashboard.css">
    <link rel="stylesheet" href="css/supplier.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/css/all.min.css">
    <style>
        body, * {
            font-family: 'Times New Roman', Times, serif !important;
        }
    </style>
</head>
<body>
    <jsp:include page="sidebar.jsp" />
    <div id="main-content">
            
        <div class="dashboard-header">
            <div class="header-left">
                <h1>Quản lý danh mục</h1>
            </div>
            <div class="header-actions">
                <a href="categories?action=add" class="btn-add">+ Thêm danh mục mới</a>
            </div>
        </div>
        <form action="categories" method="get" class="filter-form">
            <select name="hiddenFilter" class="filter-select">
                <option value="all" ${param.hiddenFilter == null || param.hiddenFilter == 'all' ? 'selected' : ''}>Tất cả trạng thái</option>
                <option value="visible" ${param.hiddenFilter == 'visible' ? 'selected' : ''}>Đang hiển thị</option>
                <option value="hidden" ${param.hiddenFilter == 'hidden' ? 'selected' : ''}>Đã ẩn</option>
            </select>

            <select name="parentId" class="filter-select">
                <option value="">Tất cả danh mục vật tư</option>
                <c:forEach items="${parentCategories}" var="parent">
                    <option value="${parent.categoryId}" ${parent.categoryId == param.parentId ? 'selected' : ''}>
                        ${parent.name}
                    </option>
                </c:forEach>
            </select>

            <input type="text" name="keyword" placeholder="Tìm kiếm loại vật tư"
                   value="${keyword}" class="filter-input">

            <button type="submit" name="sortBy" value="name" class="sort-btn ${sortBy == 'name' ? 'active' : ''}">
                Sắp xếp theo tên
            </button>

            <button type="submit" name="sortBy" value="id" class="sort-btn ${sortBy == 'id' ? 'active' : ''}">
                Sắp xếp theo ID
            </button>

            <button type="submit" class="search-btn">Tìm kiếm</button>
        </form>
        <div class="table-card">
            <table class="category-table">
                <thead>
                    <tr>
                        <th>ID</th>
                        <th>Loại vật tư</th>
                        <th>Danh mục vật tư</th>
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
                                    </tr>
                                </c:when>
                                <c:otherwise>
                                    <tr>
                                        <td colspan="4" class="no-data">Không tìm thấy danh mục nào.</td>
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
                        <a href="categories?page=${currentPage - 1}${not empty keyword ? '&keyword=' : ''}${not empty keyword ? keyword : ''}${not empty param.parentId ? '&parentId=' : ''}${not empty param.parentId ? param.parentId : ''}${not empty sortBy ? '&sortBy=' : ''}${not empty sortBy ? sortBy : ''}"
                           class="page-button" aria-label="Trang trước">&laquo;</a>
                    </c:when>
                    <c:otherwise>
                        <button class="page-button" disabled aria-label="Trang trước">&laquo;</button>
                    </c:otherwise>
                </c:choose>

                <c:forEach begin="${startPage}" end="${endPage}" var="i">
                    <a href="categories?page=${i}${not empty keyword ? '&keyword=' : ''}${not empty keyword ? keyword : ''}${not empty param.parentId ? '&parentId=' : ''}${not empty param.parentId ? param.parentId : ''}${not empty sortBy ? '&sortBy=' : ''}${not empty sortBy ? sortBy : ''}"
                       class="page-button${i == currentPage ? ' active' : ''}">${i}</a>
                </c:forEach>

                <c:choose>
                    <c:when test="${currentPage < totalPages}">
                        <a href="categories?page=${currentPage + 1}${not empty keyword ? '&keyword=' : ''}${not empty keyword ? keyword : ''}${not empty param.parentId ? '&parentId=' : ''}${not empty param.parentId ? param.parentId : ''}${not empty sortBy ? '&sortBy=' : ''}${not empty sortBy ? sortBy : ''}"
                           class="page-button" aria-label="Trang sau">&raquo;</a>
                    </c:when>
                    <c:otherwise>
                        <button class="page-button" disabled aria-label="Trang sau">&raquo;</button>
                    </c:otherwise>
                </c:choose>
              </div>
            </div>
        </c:if>
        <jsp:include page="footer.jsp" />
    </div>
</body>
</html>