<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<!DOCTYPE html>
<html lang="vi">
    <head>
        <meta charset="UTF-8">
        <title>Quản Lý Danh Sách Vật Tư</title>
        <link rel="stylesheet" href="css/materiallist.css">
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/css/all.min.css">
        <link rel="stylesheet" href="css/sidebar.css">
        <script src="https://kit.fontawesome.com/4b7b2b6e8b.js" crossorigin="anonymous"></script>
        <script src="js/sidebar.js"></script>
    </head>
    <body>
        <jsp:include page="sidebar.jsp" />
        <div class="main-content">
            <div class="header-section">
                <h1 class="page-title">Quản Lý Danh Sách Vật Tư</h1>
                <div class="header-buttons">
                    <a href="CreateMaterialDetail" class="btn btn-primary"><i class="fas fa-plus"></i> Thêm Vật Tư Mới</a>
                    <button type="submit" form="bulkActionForm" name="action" value="toggleStatus" class="btn btn-secondary" id="bulkToggleBtn">
                        <i class="fas fa-toggle-on"></i>  Chỉnh trạng thái
                    </button>
                </div>
            </div>

            <c:if test="${not empty param.actionStatus}">
                <div class="alert ${param.actionStatus.contains('Success') ? 'alert-success' : 'alert-danger'}">
                    <c:choose>
                        <c:when test="${param.actionStatus == 'addSuccess'}">Thêm vật tư mới thành công!</c:when>
                        <c:when test="${param.actionStatus == 'updateSuccess'}">Cập nhật vật tư thành công!</c:when>
                        <c:when test="${param.actionStatus == 'deleteSuccess'}">Đã cập nhật trạng thái vật tư thành công!</c:when>
                        <c:otherwise>Có lỗi xảy ra. Vui lòng thử lại.</c:otherwise>
                    </c:choose>
                </div>
            </c:if>
            <c:if test="${showLowStock}">
                <div class="content-card">
                    <h3>Vật tư sắp hết: <span style="color:red;font-weight:bold;">${lowStockItems}</span></h3>
                </div>
            </c:if>
            <c:if test="${not empty errorMessages}">
                <div class="alert alert-danger">
                    <ul>
                        <c:forEach var="err" items="${errorMessages}">
                            <li>${err}</li>
                        </c:forEach>
                    </ul>
                </div>
            </c:if>

            <div class="content-card">
                <form method="get" action="MaterialListServlet" id="filterForm" class="filter-form">
                    <div class="filter-row">
                        <div class="filter-group">
                            <i class="fas fa-filter"></i>
                            <select name="category" onchange="this.form.submit()">
                                <option value="" <c:if test="${empty categoryId}">selected</c:if>>Tất cả danh mục</option>
                                <c:forEach items="${categories}" var="cat">
                                    <c:if test="${cat.parentId != null}">
                                        <option value="${cat.categoryId}" <c:if test="${cat.categoryId == categoryId}">selected</c:if>>${cat.name}</option>
                                    </c:if>
                                </c:forEach>
                            </select>
                        </div>
                        <div class="filter-group">
                            <i class="fas fa-balance-scale"></i>
                            <select name="unit" onchange="this.form.submit()">
                                <option value="All">Tất cả đơn vị tính</option>
                                <c:forEach items="${units}" var="unit">
                                    <option value="${unit.unit_id}" <c:if test="${unit.unit_id eq unitFilter}">selected</c:if>>${unit.unit_name}</option>
                                </c:forEach>
                            </select>
                        </div>
                        <div class="filter-group">
                            <i class="fas fa-toggle-on"></i>
                            <select name="status" onchange="this.form.submit()">
                                <option value="All" <c:if test="${statusFilter == null || statusFilter == 'All'}">selected</c:if>>Tất cả trạng thái</option>
                                <option value="active" <c:if test="${statusFilter == 'active'}">selected</c:if>>Đang kinh doanh</option>
                                <option value="inactive" <c:if test="${statusFilter == 'inactive'}">selected</c:if>>Ngừng kinh doanh</option>
                                </select>
                        </div>
                        <div class="filter-group">
                            <i class="fas fa-list-ol"></i>
                            <select name="itemsPerPage" onchange="this.form.submit()">
                                <c:forEach items="${itemsPerPageOptions}" var="size">
                                    <option value="${size}" <c:if test="${itemsPerPage == size}">selected</c:if>>Hiển thị ${size}</option>
                                </c:forEach>
                            </select>
                        </div>
                        <button type="button" class="btn btn-secondary" onclick="resetFilters()">Đặt lại</button>
                    </div>
                    <div class="filter-row">
                        <div class="filter-group search-group">
                            <i class="fas fa-search"></i>
                            <input type="text" name="search" placeholder="Tìm kiếm theo tên vật tư..." value="${not empty searchQuery ? searchQuery : ''}"/>
                            <button type="submit" class="btn btn-secondary">Tìm</button>
                        </div>
                        <a href="materialDetailHistory" class="btn btn-secondary" style="align-self:center;"><i class="fas fa-history"></i> Lịch sử sửa đổi</a>
                    </div>
                </form>
            </div>

            <div class="content-card">
                <form method="post" action="MaterialListServlet" id="bulkActionForm">
                    <div class="table-responsive">
                        <table class="data-table">
                            <thead>
                                <tr>
                                    <th><input type="checkbox" id="selectAll"></th>
                                    <th>Mã VT</th>
                                    <th>Tên vật tư</th>
                                    <th>Loại vật tư</th>
                                    <th>Đơn vị tính</th>
                                    <th>Trạng thái</th>
                                    <th>Chi tiết</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:choose>
                                    <c:when test="${empty materials}">
                                        <tr><td colspan="7" class="no-data">Không tìm thấy vật tư nào.</td></tr>
                                    </c:when>
                                    <c:otherwise>
                                        <c:forEach items="${materials}" var="material" varStatus="status">
                                            <tr>
                                                <td><input type="checkbox" name="materialIds" value="${material.materialId}" class="material-checkbox"></td>
                                                <td>${material.materialId}</td>
                                                <td>${material.name}</td>
                                                <td>${material.categoryName}</td>
                                                <td>${material.unitName}</td>
                                                <td>
                                                    <span class="status-badge status-${material.status == 'active' ? 'active' : 'inactive'}">
                                                        <c:choose>
                                                            <c:when test="${material.status == 'active'}">
                                                                Đang kinh doanh
                                                            </c:when>
                                                            <c:otherwise>
                                                                Ngừng kinh doanh
                                                            </c:otherwise>
                                                        </c:choose>
                                                    </span>
                                                </td>
                                                <td class="action-buttons">
                                                    <a href="viewMaterialDetail?id=${material.materialId}" class="btn-action btn-view tooltip-parent">
                                                        <i class="fas fa-eye"></i>
                                                        <span class="custom-tooltip"></span>
                                                    </a>
                                                </td>
                                            </tr>
                                        </c:forEach>
                                    </c:otherwise>
                                </c:choose>
                            </tbody>
                        </table>
                    </div>
                </form>
                <c:if test="${totalPages > 0}">
                    <div class="pagination-container">
                        <div class="pagination-info">
                            Hiển thị <strong>${materials.size()}</strong> trên <strong>${totalMaterials}</strong> kết quả
                        </div>
                        <div class="pagination">
                            <!-- Nút về trang đầu -->
                            <c:if test="${currentPage > 1}">
                                <c:url var="firstUrl" value="MaterialListServlet">
                                    <c:param name="search" value="${searchQuery}"/>
                                    <c:param name="category" value="${categoryFilter}"/>
                                    <c:param name="supplier" value="${supplierFilter}"/>
                                    <c:param name="page" value="1"/>
                                </c:url>
                                <a href="${firstUrl}" class="page-link" title="Trang đầu">&laquo;</a>
                            </c:if>

                            <c:forEach begin="${startPage}" end="${endPage}" var="i">
                                <c:url var="pageUrl" value="MaterialListServlet">
                                    <c:param name="search" value="${searchQuery}"/>
                                    <c:param name="category" value="${categoryFilter}"/>
                                    <c:param name="supplier" value="${supplierFilter}"/>
                                    <c:param name="page" value="${i}"/>
                                </c:url>
                                <a href="${pageUrl}" class="page-link ${i == currentPage ? 'active' : ''}">${i}</a>
                            </c:forEach>

                            <!-- Nút về trang cuối -->
                            <c:if test="${currentPage < totalPages}">
                                <c:url var="lastUrl" value="MaterialListServlet">
                                    <c:param name="search" value="${searchQuery}"/>
                                    <c:param name="category" value="${categoryFilter}"/>
                                    <c:param name="supplier" value="${supplierFilter}"/>
                                    <c:param name="page" value="${totalPages}"/>
                                </c:url>
                                <a href="${lastUrl}" class="page-link" title="Trang cuối">&raquo;</a>
                            </c:if>
                        </div>
                    </div>
                </c:if>
            </div>
        </div>
        <script>
            document.getElementById('bulkToggleBtn').addEventListener('click', function (e) {
                var checkboxes = document.querySelectorAll('.material-checkbox:checked');
                if (checkboxes.length === 0) {
                    e.preventDefault();
                    alert('Vui lòng chọn ít nhất một vật tư để chuyển trạng thái!');
                }
            });
            document.getElementById('selectAll').addEventListener('change', function (e) {
                var checkboxes = document.querySelectorAll('.material-checkbox');
                checkboxes.forEach(function (checkbox) {
                    checkbox.checked = e.target.checked;
                });
            });

            function resetFilters() {
                var form = document.getElementById('filterForm');
                // Reset các select về option đầu tiên
                var selects = form.querySelectorAll('select');
                selects.forEach(function(select) {
                    select.selectedIndex = 0;
                });
                // Reset input search
                var searchInput = form.querySelector('input[name="search"]');
                if (searchInput) searchInput.value = '';
                // Submit lại form
                form.submit();
            }
        </script>
    </body>
</html>