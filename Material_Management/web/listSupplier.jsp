<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <meta http-equiv="Cache-Control" content="no-cache, no-store, must-revalidate">
        <meta http-equiv="Pragma" content="no-cache">
        <meta http-equiv="Expires" content="0">
        <title>Danh sách nhà cung cấp</title>
        <link rel="stylesheet" href="css/supplier.css?v=1.3">
        <link rel="stylesheet" href="css/sidebar.css">
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/css/all.min.css">
    </head>
    <body>
        <jsp:include page="sidebar.jsp" />
        <div id="main-content">
            <div class="dashboard-header">
                <div class="header-left">
                    <h1>Danh sách nhà cung cấp</h1>
                </div>
                <div class="header-actions">
                    <a href="${pageContext.request.contextPath}/suppliers?action=add" class="btn-add">+ Thêm mới</a>
                </div>
            </div>
            
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
            
            <form action="${pageContext.request.contextPath}/suppliers" method="get" class="filter-form">
                <input type="text" name="keyword" placeholder="Tìm kiếm theo tên..."
                       value="${param.keyword != null ? param.keyword : ''}" class="filter-input">
                <select name="status" class="filter-select" onchange="this.form.submit()">
                    <option value="">Tất cả trạng thái</option>
                    <option value="active" ${param.status == 'active' ? 'selected' : ''}>Hợp tác</option>
                    <option value="inactive" ${param.status == 'inactive' ? 'selected' : ''}>Chưa hợp tác</option>
                    <option value="terminated" ${param.status == 'terminated' ? 'selected' : ''}>Ngừng hợp tác</option>
                </select>

                <label for="itemsPerPage" style="font-size:15px; color:#555; margin-left:10px;">Hiển thị:</label>
                <select name="itemsPerPage" id="itemsPerPage" onchange="this.form.submit()" style="padding:4px 10px; border-radius:4px; margin-left:4px;">
                    <c:forEach items="${itemsPerPageOptions}" var="size">
                        <option value="${size}" ${itemsPerPage == size ? 'selected' : ''}>${size} items</option>
                    </c:forEach>
                </select>

                <button type="submit" name="sortBy" value="id" class="sort-btn ${param.sortBy == 'id' ? 'active' : ''}"
                    onclick="toggleSortOrder();">
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
                <table class="supplier-table">
                    <thead>
                        <tr>
                            <th>ID</th>
                            <th>Tên nhà cung cấp</th>
                            <th>Người liên hệ</th>
                            <th>Địa chỉ</th>
                            <th>Trạng thái</th>
                            <th>Thao tác</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:choose>
                            <c:when test="${not empty requestScope.suppliers}">
                                <c:forEach items="${requestScope.suppliers}" var="s">
                                    <tr>
                                        <td>${s.supplierId}</td>
                                        <td>${s.supplierName}</td>
                                        <td>${s.contactPerson}</td>
                                        <td>${s.address}</td>
                                        <td>
                                            <c:choose>
                                                <c:when test="${s.status == 'active'}">
                                                    <span class="status-badge status-active">Hợp tác</span>
                                                </c:when>
                                                <c:when test="${s.status == 'inactive'}">
                                                    <span class="status-badge status-inactive">Chưa hợp tác</span>
                                                </c:when>
                                                <c:when test="${s.status == 'terminated'}">
                                                    <span class="status-badge status-terminated">Ngừng hợp tác</span>
                                                </c:when>
                                                <c:otherwise>
                                                    <span class="status-badge">Không xác định</span>
                                                </c:otherwise>
                                            </c:choose>
                                        </td>
                                        <td class="action-buttons">
                                            <a href="${pageContext.request.contextPath}/suppliers?action=view&id=${s.supplierId}" class="btn-view">Chi tiết</a>
                                            <a href="${pageContext.request.contextPath}/suppliers?action=edit&id=${s.supplierId}" class="btn-edit">Sửa</a>
                                            <a href="${pageContext.request.contextPath}/material-suppliers?supplier_id=${s.supplierId}" class="btn-vattu">Vật tư</a>
                                        </td>
                                    </tr>
                                </c:forEach>
                            </c:when>
                            <c:otherwise>
                                <tr>
                                    <td colspan="6" class="no-data">Không tìm thấy nhà cung cấp nào</td>
                                </tr>
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
                                <a href="${pageContext.request.contextPath}/suppliers?page=${currentPage - 1}&keyword=${param.keyword}&status=${param.status}&sortBy=${param.sortBy}&sortOrder=${param.sortOrder}&itemsPerPage=${param.itemsPerPage}"
                                   class="page-button" aria-label="Trang trước">&laquo;</a>
                            </c:when>
                            <c:otherwise>
                                <button class="page-button" disabled aria-label="Trang trước">&laquo;</button>
                            </c:otherwise>
                        </c:choose>

                        <c:forEach begin="${startPage}" end="${endPage}" var="i">
                            <a href="${pageContext.request.contextPath}/suppliers?page=${i}&keyword=${param.keyword}&status=${param.status}&sortBy=${param.sortBy}&sortOrder=${param.sortOrder}&itemsPerPage=${param.itemsPerPage}"
                               class="page-button${i == currentPage ? ' active' : ''}">${i}</a>
                        </c:forEach>

                        <c:choose>
                            <c:when test="${currentPage < totalPages}">
                                <a href="${pageContext.request.contextPath}/suppliers?page=${currentPage + 1}&keyword=${param.keyword}&status=${param.status}&sortBy=${param.sortBy}&sortOrder=${param.sortOrder}&itemsPerPage=${param.itemsPerPage}"
                                   class="page-button" aria-label="Trang sau">&raquo;</a>
                            </c:when>
                            <c:otherwise>
                                <button class="page-button" disabled aria-label="Trang sau">&raquo;</button>
                            </c:otherwise>
                        </c:choose>
                    </div>
                    <div class="pagination-info">
                        Tổng số nhà cung cấp: ${totalSuppliers}
                    </div>
                </div>
            </c:if>
        </div>
        
        <script>
            function toggleSortOrder() {
                const sortOrderInput = document.getElementById('sortOrder');
                const currentSortBy = '${param.sortBy}';
                
                if (currentSortBy === 'id') {
                    // Nếu đang sort theo ID, toggle order
                    sortOrderInput.value = sortOrderInput.value === 'asc' ? 'desc' : 'asc';
                } else {
                    // Nếu chưa sort theo ID, bắt đầu với asc
                    sortOrderInput.value = 'asc';
                }
            }
        </script>
    </body>
</html> 