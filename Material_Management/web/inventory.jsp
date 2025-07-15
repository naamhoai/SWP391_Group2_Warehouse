<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html>
<head>
    <title>Quản lý tồn kho</title>
    <link rel="stylesheet" href="css/inventory.css">
    <link rel="stylesheet" href="css/sidebar.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.2/css/all.min.css">
</head>
<body>
    <jsp:include page="sidebar.jsp" />
    <div class="main-content">
        <div class="header-section">
            <h1 class="page-title">Quản Lý Tồn Kho</h1>
        </div>
        <div class="content-card">
            <form class="filter-form" method="get" action="inventory">
                <div class="filter-row">
                    <div class="filter-group">
                        <i class="fa fa-filter"></i>
                        <select name="categoryId" onchange="this.form.submit()">
                            <option value="">Tất cả danh mục</option>
                            <c:forEach var="cat" items="${categoryList}">
                                <c:if test="${cat.parentId != null}">
                                    <option value="${cat.categoryId}" ${cat.categoryId == categoryId ? 'selected' : ''}>${cat.name}</option>
                                </c:if>
                            </c:forEach>
                        </select>
                    </div>
                    <div class="filter-group">
                        <i class="fa fa-truck"></i>
                        <select name="supplierId" onchange="this.form.submit()">
                            <option value="">Tất cả nhà cung cấp</option>
                            <c:forEach var="sup" items="${supplierList}">
                                <option value="${sup.supplierId}" ${sup.supplierId == supplierId ? 'selected' : ''}>${sup.supplierName}</option>
                            </c:forEach>
                        </select>
                    </div>
                    <div class="filter-group">
                        <i class="fa fa-tags"></i>
                        <select name="condition" onchange="this.form.submit()">
                            <option value="">Tất cả tình trạng</option>
                            <option value="Mới" ${condition == 'Mới' ? 'selected' : ''}>Mới</option>
                            <option value="Cũ" ${condition == 'Cũ' ? 'selected' : ''}>Cũ</option>
                        </select>
                    </div>
                    <div class="filter-group">
                        <i class="fa fa-list-ol"></i>
                        <select name="pageSize" onchange="this.form.submit()">
                            <c:forEach var="size" items="${fn:split('10,20,50',',')}">
                                <option value="${size}" ${size == pageSize ? 'selected' : ''}>Hiển thị ${size}</option>
                            </c:forEach>
                        </select>
                    </div>
                    <button type="submit" name="reset" value="true" class="btn btn-secondary">Đặt lại</button>
                </div>
                <div class="filter-row">
                    <div class="filter-group search-group">
                        <i class="fa fa-search"></i>
                        <input type="text" name="search" placeholder="Tìm kiếm tên sản phẩm..." value="${search != null ? search : ''}" />
                        <button type="submit" class="btn btn-secondary">Tìm</button>
                    </div>
                    <a href="inventory-history.jsp" class="btn btn-primary"><i class="fa fa-history"></i> Lịch sử</a>
                </div>
            </form>
        </div>
        <div class="content-card">
            <div class="table-responsive">
                <table class="data-table">
                    <thead>
                    <tr>
                        <th>Mã VT</th>
                        <th>Tên sản phẩm</th>
                        <th>Danh mục</th>
                        <th>Nhà cung cấp</th>
                        <th>Tình trạng vật tư</th>
                        <th>Số lượng</th>
                        <th>Đơn vị</th>
                        <th>Giá</th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:choose>
                        <c:when test="${not empty inventoryList}">
                            <c:forEach var="item" items="${inventoryList}">
                                <tr>
                                    <td>${item.materialId}</td>
                                    <td>${item.materialName}</td>
                                    <td>${item.categoryName}</td>
                                    <td>${item.supplierName}</td>
                                    <td>
                                        <span class="status-badge ${item.materialCondition eq 'Mới' ? 'status-active' : 'status-inactive'}">
                                            ${item.materialCondition}
                                        </span>
                                    </td>
                                    <td>${item.quantityOnHand}</td>
                                    <td>${item.unitName}</td>
                                    <td>
                                        <fmt:formatNumber value="${item.price}" type="number"/>
                                        ₫
                                    </td>
                                </tr>
                            </c:forEach>
                        </c:when>
                        <c:otherwise>
                            <tr><td colspan="8" class="no-data">Không có dữ liệu tồn kho phù hợp.</td></tr>
                        </c:otherwise>
                    </c:choose>
                    </tbody>
                </table>
            </div>
            <c:if test="${totalRecords > pageSize}">
                <div class="pagination-container">
                    <div class="pagination">
                        <!-- Nút về trang đầu -->
                        <c:if test="${page > 1}">
                            <a href="inventory?page=1&pageSize=${pageSize}
                                <c:if test='${categoryId != null}'> &amp;categoryId=${categoryId}</c:if>
                                <c:if test='${supplierId != null}'> &amp;supplierId=${supplierId}</c:if>
                                <c:if test='${search != null && search != ""}'> &amp;search=${search}</c:if>
                                <c:if test='${condition != null && condition != ""}'> &amp;condition=${condition}</c:if>" class="page-link" title="Trang đầu">&laquo;</a>
                        </c:if>
                        <!-- Dải số phân trang -->
                        <c:set var="startPage" value="${page - 2 > 0 ? page - 2 : 1}" />
                        <c:set var="endPage" value="${page + 2 < totalPages ? page + 2 : totalPages}" />
                        <c:forEach var="i" begin="${startPage}" end="${endPage}">
                            <a href="inventory?page=${i}&amp;pageSize=${pageSize}
                                <c:if test='${categoryId != null}'> &amp;categoryId=${categoryId}</c:if>
                                <c:if test='${supplierId != null}'> &amp;supplierId=${supplierId}</c:if>
                                <c:if test='${search != null && search != ""}'> &amp;search=${search}</c:if>
                                <c:if test='${condition != null && condition != ""}'> &amp;condition=${condition}</c:if>" class="page-link ${i == page ? 'active' : ''}">${i}</a>
                        </c:forEach>
                        <!-- Nút về trang cuối -->
                        <c:if test="${page < totalPages}">
                            <a href="inventory?page=${totalPages}&amp;pageSize=${pageSize}
                                <c:if test='${categoryId != null}'> &amp;categoryId=${categoryId}</c:if>
                                <c:if test='${supplierId != null}'> &amp;supplierId=${supplierId}</c:if>
                                <c:if test='${search != null && search != ""}'> &amp;search=${search}</c:if>
                                <c:if test='${condition != null && condition != ""}'> &amp;condition=${condition}</c:if>" class="page-link" title="Trang cuối">&raquo;</a>
                        </c:if>
                    </div>
                </div>
            </c:if>
        </div>
    </div>
</body>
</html> 