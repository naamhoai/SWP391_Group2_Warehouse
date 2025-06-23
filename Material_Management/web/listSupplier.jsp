<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Danh sách nhà cung cấp</title>
    <link rel="stylesheet" href="css/supplier.css">
    <link rel="stylesheet" href="css/footer.css">
    <style>
        .btn-view {
            background-color: #28a745;
            color: white;
            padding: 5px 10px;
            border-radius: 4px;
            text-decoration: none;
            margin-right: 5px;
        }
        .btn-view:hover {
            background-color: #218838;
        }
    </style>
</head>
<body>
    <div class="main-content" style="width:100vw; min-height:100vh; margin:0; padding:0;">
        <div class="page-content" style="max-width:100vw; margin:0;">
            <div class="content-header" style="display:flex; justify-content:space-between; align-items:center;">
                <div style="display:flex; align-items:center; gap:16px;">
                    <h1 style="margin:0;">Danh sách nhà cung cấp</h1>
                </div>
                <a href="${pageContext.request.contextPath}/suppliers?action=add" class="btn-new">+ Thêm mới</a>
            </div>

            <c:if test="${not empty sessionScope.error}">
                <div class="message error">
                    ${sessionScope.error}
                </div>
                <c:remove var="error" scope="session"/>
            </c:if>

            <c:if test="${not empty sessionScope.message}">
                <div class="message success">
                    ${sessionScope.message}
                </div>
                <c:remove var="message" scope="session"/>
            </c:if>

            <!-- Search and Filter Form -->
            <form action="${pageContext.request.contextPath}/suppliers" method="get" class="filter-form">
                <select name="status" class="filter-select">
                    <option value="">Tất cả trạng thái</option>
                    <option value="active" ${param.status == 'active' ? 'selected' : ''}>Hợp tác</option>
                    <option value="inactive" ${param.status == 'inactive' ? 'selected' : ''}>Không hợp tác</option>
                </select>

                <input type="text" name="keyword" placeholder="Tìm kiếm theo tên..."
                       value="${param.keyword != null ? param.keyword : ''}" class="filter-input">

                <button type="submit" name="sortBy" value="name" class="sort-btn ${param.sortBy == 'name' ? 'active' : ''}">
                    Sắp xếp theo tên
                </button>

                <button type="submit" name="sortBy" value="id" class="sort-btn ${param.sortBy == 'id' ? 'active' : ''}">
                    Sắp xếp theo ID
                </button>

                <button type="submit" class="search-btn">Tìm kiếm</button>
            </form>

            <!-- Suppliers Table -->
            <div class="table-container">
                <table class="supplier-table">
                    <thead>
                        <tr>
                            <th>ID</th>
                            <th>Tên nhà cung cấp</th>
                            <th>Người liên hệ</th>
                            <th>Số điện thoại</th>
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
                                        <td>${s.supplierPhone}</td>
                                        <td>${s.address}</td>
                                        <td>
                                            <span class="status-badge ${s.status == 'active' ? 'status-active' : 'status-inactive'}">
                                                ${s.status == 'active' ? 'Hợp tác' : 'Không hợp tác'}
                                            </span>
                                        </td>
                                        <td class="action-buttons">
                                            <a href="${pageContext.request.contextPath}/suppliers?action=view&id=${s.supplierId}" 
                                               class="btn-view">Xem</a>
                                            <a href="${pageContext.request.contextPath}/suppliers?action=edit&id=${s.supplierId}" 
                                               class="btn-edit">Sửa</a>
                                            <a href="${pageContext.request.contextPath}/material-suppliers?supplier_id=${s.supplierId}" 
                                               class="btn-view" style="background-color:#ffc107; color:#212529; margin-left:5px;">Vật tư</a>
                                        </td>
                                    </tr>
                                </c:forEach>
                            </c:when>
                            <c:otherwise>
                                <tr>
                                    <td colspan="7" class="no-data">Không tìm thấy nhà cung cấp nào</td>
                                </tr>
                            </c:otherwise>
                        </c:choose>
                    </tbody>
                </table>
            </div>

            <%
                // Tạo mảng số lượng items per page nếu chưa có
                java.util.List<Integer> itemsPerPageOptions = java.util.Arrays.asList(5, 10, 20, 50);
                pageContext.setAttribute("itemsPerPageOptions", itemsPerPageOptions);
                int itemsPerPage = 10;
                try {
                    String itemsPerPageStr = request.getParameter("itemsPerPage");
                    if (itemsPerPageStr != null) {
                        itemsPerPage = Integer.parseInt(itemsPerPageStr);
                    }
                } catch (Exception e) { itemsPerPage = 10; }
            %>
            <!-- Pagination giống materialSupplierList -->
            <c:if test="${totalPages > 1}">
                <div class="pagination">
                    <form method="get" action="${pageContext.request.contextPath}/suppliers" class="pagination-form">
                        <input type="hidden" name="keyword" value="${param.keyword}">
                        <input type="hidden" name="status" value="${param.status}">
                        <input type="hidden" name="sortBy" value="${param.sortBy}">
                        <input type="hidden" name="itemsPerPage" value="${param.itemsPerPage != null ? param.itemsPerPage : itemsPerPage}">
                        <button type="submit" name="page" value="${currentPage - 1}" class="page-button" ${currentPage <= 1 ? 'disabled' : ''}>
                            Previous
                        </button>
                        <c:if test="${startPage > 1}">
                            <button type="submit" name="page" value="1" class="page-button">1</button>
                            <c:if test="${startPage > 2}">
                                <span class="page-ellipsis">...</span>
                            </c:if>
                        </c:if>
                        <c:forEach begin="${startPage}" end="${endPage}" var="i">
                            <button type="submit" name="page" value="${i}" class="page-button ${i == currentPage ? 'active' : ''}">
                                ${i}
                            </button>
                        </c:forEach>
                        <c:if test="${endPage < totalPages}">
                            <c:if test="${endPage < totalPages - 1}">
                                <span class="page-ellipsis">...</span>
                            </c:if>
                            <button type="submit" name="page" value="${totalPages}" class="page-button">${totalPages}</button>
                        </c:if>
                        <button type="submit" name="page" value="${currentPage + 1}" class="page-button" ${currentPage >= totalPages ? 'disabled' : ''}>
                            Next
                        </button>
                    </form>
                    <form method="get" action="${pageContext.request.contextPath}/suppliers" style="margin-top:8px; text-align:center;">
                        <input type="hidden" name="keyword" value="${param.keyword}">
                        <input type="hidden" name="status" value="${param.status}">
                        <input type="hidden" name="sortBy" value="${param.sortBy}">
                        <label for="itemsPerPage" style="font-size:15px; color:#555;">Hiển thị:</label>
                        <select name="itemsPerPage" id="itemsPerPage" onchange="this.form.submit()" style="padding:4px 10px; border-radius:4px; margin-left:4px;">
                            <c:forEach items="${itemsPerPageOptions}" var="size">
                                <option value="${size}" <c:if test="${itemsPerPage == size}">selected</c:if>>${size} items</option>
                            </c:forEach>
                        </select>
                    </form>
                    <div class="pagination-info">
                        Page ${currentPage} of ${totalPages}
                    </div>
                </div>
            </c:if>
        </div>
        <div style="position:fixed; bottom:32px; left:32px; z-index:1000;">
            <a href="adminDashboard.jsp" class="btn-cancel" style="background:#4a90e2; color:#fff; font-weight:bold; padding:10px 28px; border-radius:4px; text-decoration:none; font-size:16px;">&larr; Về trang chủ</a>
        </div>
        <jsp:include page="footer.jsp"/>
    </div>
</body>
</html> 