<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="dao.MaterialDAO" %>
<%@ page import="model.Material" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Arrays" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<%
    request.setCharacterEncoding("UTF-8");

    // Lấy các tham số lọc, sắp xếp, phân trang từ request
    String searchQuery = request.getParameter("search");
    String categoryFilter = request.getParameter("category") == null ? "All" : request.getParameter("category");
    String supplierFilter = request.getParameter("supplier") == null ? "All" : request.getParameter("supplier");
    String sortField = request.getParameter("sort") == null ? "material_id" : request.getParameter("sort");
    String sortDir = request.getParameter("dir") == null ? "desc" : request.getParameter("dir");

    int currentPage = 1;
    int itemsPerPage = 10;
    try {
        String pageStr = request.getParameter("page");
        if (pageStr != null && !pageStr.isEmpty()) currentPage = Integer.parseInt(pageStr);
        String itemsPerPageStr = request.getParameter("itemsPerPage");
        if (itemsPerPageStr != null && !itemsPerPageStr.isEmpty()) itemsPerPage = Integer.parseInt(itemsPerPageStr);
    } catch (NumberFormatException e) {
        // Giữ giá trị mặc định nếu có lỗi
    }

    // Khởi tạo DAO và gọi các phương thức dành riêng cho trang quản trị
    MaterialDAO dao = new MaterialDAO();
    int totalItems = dao.getTotalMaterialsForAdmin(searchQuery, categoryFilter, supplierFilter);
    int totalPages = (int) Math.ceil((double) totalItems / itemsPerPage);

    if (currentPage > totalPages && totalPages > 0) currentPage = totalPages;
    if (currentPage < 1) currentPage = 1;

    List<Material> materials = dao.getMaterialsForAdmin(searchQuery, categoryFilter, supplierFilter, currentPage, itemsPerPage, sortField, sortDir);
    List<String> categories = dao.getAllCategories();
    List<String> suppliers = dao.getAllSuppliers();
    List<Integer> itemsPerPageOptions = Arrays.asList(5, 10, 20, 50);

    // Đặt các biến vào pageContext
    pageContext.setAttribute("materials", materials);
    pageContext.setAttribute("categories", categories);
    pageContext.setAttribute("suppliers", suppliers);
    pageContext.setAttribute("itemsPerPageOptions", itemsPerPageOptions);
    pageContext.setAttribute("currentPage", currentPage);
    pageContext.setAttribute("totalPages", totalPages);
    pageContext.setAttribute("itemsPerPage", itemsPerPage);
    pageContext.setAttribute("searchQuery", searchQuery);
    pageContext.setAttribute("categoryFilter", categoryFilter);
    pageContext.setAttribute("supplierFilter", supplierFilter);
    pageContext.setAttribute("sortField", sortField);
    pageContext.setAttribute("sortDir", sortDir);

    int startPage = Math.max(1, currentPage - 2);
    int endPage = Math.min(totalPages, currentPage + 2);
    pageContext.setAttribute("startPage", startPage);
    pageContext.setAttribute("endPage", endPage);
%>

<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>Quản Lý Danh Sách Vật Tư</title>
    <link rel="stylesheet" href="css/materiallist.css">
    <link rel="stylesheet" href="css/sidebar.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/css/all.min.css">
</head>
<body>
    <jsp:include page="sidebar.jsp" />

    <div id="main-content">
        <div class="container">
            <div class="top-bar">
                <h1 class="page-title">Quản Lý Danh Sách Vật Tư</h1>
                <a href="addMaterialDetail.jsp" class="add-button">+ Thêm Vật Tư Mới</a>
            </div>
            
            <c:if test="${not empty param.status}">
                <div class="alert ${param.status.contains('Success') ? 'alert-success' : 'alert-danger'}" 
                     style="padding: 10px; margin-bottom: 15px; border-radius: 4px; border: 1px solid; color: ${param.status.contains('Success') ? 'green' : 'red'};">
                    <c:choose>
                        <c:when test="${param.status == 'addSuccess'}">Thêm vật tư mới thành công!</c:when>
                        <c:when test="${param.status == 'updateSuccess'}">Cập nhật vật tư thành công!</c:when>
                        <c:when test="${param.status == 'deleteSuccess'}">Xóa vật tư thành công!</c:when>
                        <c:otherwise>Có lỗi xảy ra. Vui lòng thử lại.</c:otherwise>
                    </c:choose>
                </div>
            </c:if>

            <form method="get" action="materialDetailList.jsp" id="filterForm">
                <div class="filters">
                    <div class="filter-item">
                        <label>Danh mục</label>
                        <select name="category" onchange="this.form.submit()">
                            <option value="All">Tất cả danh mục</option>
                            <c:forEach items="${categories}" var="cat">
                                <option value="${cat}" <c:if test="${cat eq categoryFilter}">selected</c:if>>${cat}</option>
                            </c:forEach>
                        </select>
                    </div>

                    <div class="filter-item">
                        <label>Nhà cung cấp</label>
                        <select name="supplier" onchange="this.form.submit()">
                            <option value="All">Tất cả nhà cung cấp</option>
                            <c:forEach items="${suppliers}" var="sup">
                                <option value="${sup}" <c:if test="${sup eq supplierFilter}">selected</c:if>>${sup}</option>
                            </c:forEach>
                        </select>
                    </div>

                    <div class="filter-item">
                        <label>Hiển thị</label>
                        <select name="itemsPerPage" onchange="this.form.submit()">
                            <c:forEach items="${itemsPerPageOptions}" var="size">
                                <option value="${size}" <c:if test="${itemsPerPage == size}">selected</c:if>>${size} mục</option>
                            </c:forEach>
                        </select>
                    </div>

                    <div class="filter-item">
                        <label>Tìm kiếm</label>
                        <div class="search-group">
                            <input type="text" name="search" placeholder="Tìm theo tên vật tư" value="${not empty searchQuery ? searchQuery : ''}"/>
                            <button type="submit" class="search-btn">Tìm</button>
                        </div>
                    </div>
                </div>
            </form>

            <table class="product-table">
                <thead>
                    <tr>
                        <th>Mã VT</th>
                        <th>Hình ảnh</th>
                        <th>Tên vật tư</th>
                        <th>Danh mục</th>
                        <th>Nhà cung cấp</th>
                        <th>Đơn vị</th>
                        <th>Giá</th>
                        <th style="width: 15%;">Hành động</th>
                    </tr>
                </thead>
                <tbody>
                    <c:choose>
                        <c:when test="${empty materials}">
                            <tr><td colspan="8" class="no-data">Không tìm thấy vật tư nào.</td></tr>
                        </c:when>
                        <c:otherwise>
                            <c:forEach items="${materials}" var="material">
                                <tr>
                                    <td>${material.materialId}</td>
                                    <td>
                                        <img src="image/${not empty material.imageUrl ? material.imageUrl : 'default.png'}" alt="${material.name}" class="product-image"/>
                                    </td>
                                    <td>
                                        ${material.name}
                                    </td>
                                    <td>${material.categoryName}</td>
                                    <td>${material.supplierName}</td>
                                    <td>${material.unit}</td>
                                    <td>
                                        <fmt:setLocale value="vi_VN"/>
                                        <fmt:formatNumber value="${material.price}" type="currency"/>
                                    </td>
                                    <td>
                                        <div class="action-buttons">
                                            <a href="editMaterialDetail?id=${material.materialId}" class="table-button edit-button">Sửa</a>
                                            <form method="post" action="deleteMaterial" onsubmit="return confirm('Bạn có chắc chắn muốn xóa vật tư #${material.materialId} - ${material.name}? Thao tác này không thể hoàn tác.')" style="display: inline;">
                                                <input type="hidden" name="materialId" value="${material.materialId}">
                                                <button type="submit" class="table-button delete-button">Xóa</button>
                                            </form>
                                        </div>
                                    </td>
                                </tr>
                            </c:forEach>
                        </c:otherwise>
                    </c:choose>
                </tbody>
            </table>

            <%-- Phân trang --%>
            <c:if test="${totalPages > 1}">
                <div class="pagination">
                    <c:url var="baseUrl" value="materialDetailList.jsp">
                        <c:if test="${not empty searchQuery}"><c:param name="search" value="${searchQuery}"/></c:if>
                        <c:if test="${categoryFilter ne 'All'}"><c:param name="category" value="${categoryFilter}"/></c:if>
                        <c:if test="${supplierFilter ne 'All'}"><c:param name="supplier" value="${supplierFilter}"/></c:if>
                        <c:param name="itemsPerPage" value="${itemsPerPage}"/>
                        <c:param name="sort" value="${sortField}"/>
                        <c:param name="dir" value="${sortDir}"/>
                    </c:url>
                    
                    <a href="${currentPage > 1 ? baseUrl.concat('&page=').concat(currentPage - 1) : '#'}" class="page-button ${currentPage <= 1 ? 'disabled' : ''}">Lùi</a>
                    
                    <c:if test="${startPage > 1}">
                        <a href="${baseUrl}&page=1" class="page-button">1</a>
                        <c:if test="${startPage > 2}"><span class="page-ellipsis">...</span></c:if>
                    </c:if>

                    <c:forEach begin="${startPage}" end="${endPage}" var="i">
                        <a href="${baseUrl}&page=${i}" class="page-button ${i == currentPage ? 'active' : ''}">${i}</a>
                    </c:forEach>

                    <c:if test="${endPage < totalPages}">
                        <c:if test="${endPage < totalPages - 1}"><span class="page-ellipsis">...</span></c:if>
                        <a href="${baseUrl}&page=${totalPages}" class="page-button">${totalPages}</a>
                    </c:if>

                    <a href="${currentPage < totalPages ? baseUrl.concat('&page=').concat(currentPage + 1) : '#'}" class="page-button ${currentPage >= totalPages ? 'disabled' : ''}">Tiến</a>
                </div>
                 <div class="pagination-info">
                    Trang ${currentPage} / ${totalPages}
                </div>
            </c:if>
        </div>
    </div>
</body>
</html>