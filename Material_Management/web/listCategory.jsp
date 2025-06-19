<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="java.util.*, model.Category, dao.CategoryDAO"%>
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
    <link rel="stylesheet" href="css/sidebar.css">
    <link rel="stylesheet" href="css/category.css">
    <link rel="stylesheet" href="css/footer.css">
</head>
<body>
    <jsp:include page="sidebar.jsp" />
    <div class="main-content">
        <div class="page-content">
            <div class="content-header">
                <h1>Quản lý danh mục</h1>
                <div class="header-actions">
                    <a href="categories?action=add" class="btn-add">+ Thêm danh mục mới</a>
                    <a href="MaterialListServlet" class="btn-new">Danh sách vật liệu</a>
                </div>
            </div>

            <% if (error != null) { %>
                <div class="message error">
                    <%= error %>
                </div>
            <% } %>
            
            <% if (message != null) { %>
                <div class="message success">
                    <%= message %>
                </div>
            <% } %>

            <form action="categories" method="get" class="filter-form">
                <select name="parentId" class="filter-select">
                    <option value="">Tất cả danh mục vật tư</option>
                    <c:forEach items="${parentCategories}" var="parent">
                        <option value="${parent.categoryId}" ${parent.categoryId == param.parentId ? 'selected' : ''}>
                            ${parent.name}
                        </option>
                    </c:forEach>
                </select>

                <input type="text" name="keyword" placeholder="Tìm theo tên"
                       value="${keyword}" class="filter-input">

                <button type="submit" name="sortBy" value="name" class="sort-btn ${sortBy == 'name' ? 'active' : ''}">
                    Sắp xếp theo tên
                </button>

                <button type="submit" name="sortBy" value="id" class="sort-btn ${sortBy == 'id' ? 'active' : ''}">
                    Sắp xếp theo ID
                </button>

                <button type="submit" class="search-btn">Tìm kiếm</button>
            </form>

            <div class="table-container">
                <table class="category-table">
                    <thead>
                        <tr>
                            <th>ID</th>
                            <th>Tên danh mục</th>
                            <th>Danh mục vật tư</th>
                            <th>Thao tác</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:choose>
                            <c:when test="${empty categories}">
                                <tr>
                                    <td colspan="4" class="no-data">Không tìm thấy danh mục nào.</td>
                                </tr>
                            </c:when>
                            <c:otherwise>
                                <c:forEach items="${categories}" var="category">
                                    <c:if test="${not empty category.parentId}">
                                        <tr>
                                            <td>${category.categoryId}</td>
                                            <td>${category.name}</td>
                                            <td>${parentNames[category.parentId]}</td>
                                            <td class="action-buttons">
                                                <a href="categories?action=edit&id=${category.categoryId}" class="btn-edit">Sửa</a>
                                                <a href="categories?action=delete&id=${category.categoryId}"
                                                   class="btn-delete"
                                                   onclick="return confirm('Bạn có chắc muốn xóa danh mục này?');">Xóa</a>
                                            </td>
                                        </tr>
                                    </c:if>
                                </c:forEach>
                            </c:otherwise>
                        </c:choose>
                    </tbody>
                </table>
            </div>

            <!-- Phân trang -->
            <c:if test="${totalPages > 1}">
                <div class="pagination">
                    <!-- Nút Previous -->
                    <c:choose>
                        <c:when test="${currentPage > 1}">
                            <a href="categories?page=${currentPage - 1}${not empty keyword ? '&keyword=' : ''}${not empty keyword ? keyword : ''}${not empty param.parentId ? '&parentId=' : ''}${not empty param.parentId ? param.parentId : ''}${not empty sortBy ? '&sortBy=' : ''}${not empty sortBy ? sortBy : ''}">&laquo;</a>
                        </c:when>
                        <c:otherwise>
                            <a class="disabled">&laquo;</a>
                        </c:otherwise>
                    </c:choose>

                    <!-- Các số trang -->
                    <c:forEach begin="${startPage}" end="${endPage}" var="i">
                        <a href="categories?page=${i}${not empty keyword ? '&keyword=' : ''}${not empty keyword ? keyword : ''}${not empty param.parentId ? '&parentId=' : ''}${not empty param.parentId ? param.parentId : ''}${not empty sortBy ? '&sortBy=' : ''}${not empty sortBy ? sortBy : ''}"
                           class="${i == currentPage ? 'active' : ''}">${i}</a>
                    </c:forEach>

                    <!-- Nút Next -->
                    <c:choose>
                        <c:when test="${currentPage < totalPages}">
                            <a href="categories?page=${currentPage + 1}${not empty keyword ? '&keyword=' : ''}${not empty keyword ? keyword : ''}${not empty param.parentId ? '&parentId=' : ''}${not empty param.parentId ? param.parentId : ''}${not empty sortBy ? '&sortBy=' : ''}${not empty sortBy ? sortBy : ''}">&raquo;</a>
                        </c:when>
                        <c:otherwise>
                            <a class="disabled">&raquo;</a>
                        </c:otherwise>
                    </c:choose>
                </div>
            </c:if>
        </div>
        <jsp:include page="footer.jsp" />
    </div>
</body>
</html>