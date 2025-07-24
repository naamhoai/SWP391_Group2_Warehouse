<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Danh sách vật tư theo nhà cung cấp</title>
    <link rel="stylesheet" href="css/materialSupplierList.css">
    <link rel="stylesheet" href="css/sidebar.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/css/all.min.css">
    <style>
        .filter-supplier-select {
            min-width: 360px !important;
            max-width: 440px !important;
            width: 100% !important;
        }
        .filter-supplier-select option {
            white-space: normal !important;
            word-wrap: break-word !important;
        }
    </style>
</head>
<body>
    <jsp:include page="sidebar.jsp" />
<div class="container">
    <h1>Danh sách vật tư theo nhà cung cấp</h1>
    <form class="filter-form" method="get" action="material-suppliers">
        <input type="text" name="keyword" placeholder="Tìm kiếm theo tên vật tư..." value="${keyword}" class="filter-input">
        <div class="filter-group">
            <label>Nhà cung cấp:</label>
            <select name="supplier_id" class="filter-supplier-select" onchange="this.form.submit()">
                <option value="">-- Chọn nhà cung cấp --</option>
                <c:forEach items="${activeSuppliers}" var="supplier">
                    <option value="${supplier.supplierId}" ${supplier.supplierId == supplier_id ? 'selected' : ''}>${supplier.supplierName}</option>
                </c:forEach>
            </select>
        </div>
        <div class="filter-group">
            <label>Trạng thái:</label>
            <select name="status" onchange="this.form.submit()">
                <option value="" ${status == null || status.isEmpty() ? 'selected' : ''}>Tất cả</option>
                <option value="available" ${status == 'available' ? 'selected' : ''}>Có sẵn</option>
            </select>
        </div>
        <div class="filter-group">
            <label>Hiển thị:</label>
            <select name="pageSize" onchange="this.form.submit()">
                <option value="5" ${pageSize == 5 ? 'selected' : ''}>5 items</option>
                <option value="10" ${pageSize == null || pageSize == 10 ? 'selected' : ''}>10 items</option>
                <option value="20" ${pageSize == 20 ? 'selected' : ''}>20 items</option>
                <option value="50" ${pageSize == 50 ? 'selected' : ''}>50 items</option>
            </select>
        </div>
        <button type="submit" class="search-btn">Tìm kiếm</button>
    </form>
    <table>
        <thead>
            <tr>
                <th>Tên vật tư</th>
                <th>Nhà cung cấp</th>
                <th>Trạng thái</th>
            </tr>
        </thead>
        <tbody>
            <c:choose>
                <c:when test="${not empty materialSupplierList}">
                    <c:forEach items="${materialSupplierList}" var="ms">
                        <tr>
                            <td>${ms.materialName}</td>
                            <td>${ms.supplierName}</td>
                            <td class="status-active">Có sẵn</td>
                        </tr>
                    </c:forEach>
                </c:when>
                <c:otherwise>
                    <tr><td colspan="3" class="no-data">Không có dữ liệu</td></tr>
                </c:otherwise>
            </c:choose>
        </tbody>
    </table>
    <c:if test="${totalPages > 1}">
        <div class="pagination">
            <div class="pagination-buttons">
                <c:choose>
                    <c:when test="${currentPage > 1}">
                        <a href="material-suppliers?page=${currentPage-1}&supplier_id=${supplier_id}&keyword=${keyword}&status=${status}&pageSize=${pageSize}" class="page-button" aria-label="Trang trước">&laquo;</a>
                    </c:when>
                    <c:otherwise>
                        <button class="page-button" disabled aria-label="Trang trước">&laquo;</button>
                    </c:otherwise>
                </c:choose>
                
                <c:forEach begin="1" end="${totalPages}" var="i">
                    <a href="material-suppliers?page=${i}&supplier_id=${supplier_id}&keyword=${keyword}&status=${status}&pageSize=${pageSize}" class="page-button${i == currentPage ? ' active' : ''}">${i}</a>
                </c:forEach>
                
                <c:choose>
                    <c:when test="${currentPage < totalPages}">
                        <a href="material-suppliers?page=${currentPage+1}&supplier_id=${supplier_id}&keyword=${keyword}&status=${status}&pageSize=${pageSize}" class="page-button" aria-label="Trang sau">&raquo;</a>
                    </c:when>
                    <c:otherwise>
                        <button class="page-button" disabled aria-label="Trang sau">&raquo;</button>
                    </c:otherwise>
                </c:choose>
            </div>
            <div class="pagination-info">
                Tổng số vật tư: ${total}
            </div>
        </div>
    </c:if>
</div>

<script src="js/sidebar.js"></script>
</body>
</html>