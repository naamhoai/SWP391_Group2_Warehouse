<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>


<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>Quản Lý Danh Sách Vật Tư</title>
    <link rel="stylesheet" href="css/materiallist.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/css/all.min.css">
</head>
<body>
    <div id="main-content">
        <div class="container">
            <div class="top-bar">
                <h1 class="page-title">Quản Lý Danh Sách Vật Tư</h1>
                <a href="CreateMaterialDetail" class="add-button">+ Thêm Vật Tư Mới</a>
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

            <form method="get" action="MaterialListServlet" id="filterForm">
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
                            <input type="text" name="search" list="materialSuggestions" placeholder="Tìm theo tên vật tư" value="${not empty searchQuery ? searchQuery : ''}"/>
                            <datalist id="materialSuggestions">
                                <c:forEach var="mat" items="${allMaterialNames}">
                                    <option value="${mat}"/>
                                </c:forEach>
                            </datalist>
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
                                            <form method="post" action="MaterialListServlet" onsubmit="return confirm('Bạn có chắc chắn muốn xóa vật tư #${material.materialId} - ${material.name}? Thao tác này không thể hoàn tác.')" style="display: inline;">
                                                <input type="hidden" name="action" value="delete">
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

            <c:if test="${totalPages > 1}">
                <div class="pagination">
                    <c:if test="${currentPage > 1}">
                        <c:url var="prevUrl" value="MaterialListServlet">
                            <c:if test="${not empty searchQuery}"><c:param name="search" value="${searchQuery}"/></c:if>
                            <c:if test="${categoryFilter ne 'All'}"><c:param name="category" value="${categoryFilter}"/></c:if>
                            <c:if test="${supplierFilter ne 'All'}"><c:param name="supplier" value="${supplierFilter}"/></c:if>
                            <c:param name="itemsPerPage" value="${itemsPerPage}"/>
                            <c:param name="sort" value="${sortField}"/>
                            <c:param name="dir" value="${sortDir}"/>
                            <c:param name="page" value="${currentPage - 1}"/>
                        </c:url>
                        <a href="${prevUrl}" class="page-button"><i class="fas fa-arrow-left"></i></a>
                    </c:if>
                    <c:if test="${currentPage <= 1}">
                        <span class="page-button disabled"><i class="fas fa-arrow-left"></i></span>
                    </c:if>

                    <c:if test="${startPage > 1}">
                        <c:url var="firstUrl" value="MaterialListServlet">
                            <c:if test="${not empty searchQuery}"><c:param name="search" value="${searchQuery}"/></c:if>
                            <c:if test="${categoryFilter ne 'All'}"><c:param name="category" value="${categoryFilter}"/></c:if>
                            <c:if test="${supplierFilter ne 'All'}"><c:param name="supplier" value="${supplierFilter}"/></c:if>
                            <c:param name="itemsPerPage" value="${itemsPerPage}"/>
                            <c:param name="sort" value="${sortField}"/>
                            <c:param name="dir" value="${sortDir}"/>
                            <c:param name="page" value="1"/>
                        </c:url>
                        <a href="${firstUrl}" class="page-button">1</a>
                        <c:if test="${startPage > 2}"><span class="page-ellipsis">...</span></c:if>
                    </c:if>

                    <c:forEach begin="${startPage}" end="${endPage}" var="i">
                        <c:url var="pageUrl" value="MaterialListServlet">
                            <c:if test="${not empty searchQuery}"><c:param name="search" value="${searchQuery}"/></c:if>
                            <c:if test="${categoryFilter ne 'All'}"><c:param name="category" value="${categoryFilter}"/></c:if>
                            <c:if test="${supplierFilter ne 'All'}"><c:param name="supplier" value="${supplierFilter}"/></c:if>
                            <c:param name="itemsPerPage" value="${itemsPerPage}"/>
                            <c:param name="sort" value="${sortField}"/>
                            <c:param name="dir" value="${sortDir}"/>
                            <c:param name="page" value="${i}"/>
                        </c:url>
                        <a href="${pageUrl}" class="page-button ${i == currentPage ? 'active' : ''}">${i}</a>
                    </c:forEach>

                    <c:if test="${endPage < totalPages}">
                        <c:if test="${endPage < totalPages - 1}"><span class="page-ellipsis">...</span></c:if>
                        <c:url var="lastUrl" value="MaterialListServlet">
                            <c:if test="${not empty searchQuery}"><c:param name="search" value="${searchQuery}"/></c:if>
                            <c:if test="${categoryFilter ne 'All'}"><c:param name="category" value="${categoryFilter}"/></c:if>
                            <c:if test="${supplierFilter ne 'All'}"><c:param name="supplier" value="${supplierFilter}"/></c:if>
                            <c:param name="itemsPerPage" value="${itemsPerPage}"/>
                            <c:param name="sort" value="${sortField}"/>
                            <c:param name="dir" value="${sortDir}"/>
                            <c:param name="page" value="${totalPages}"/>
                        </c:url>
                        <a href="${lastUrl}" class="page-button">${totalPages}</a>
                    </c:if>

                    <c:if test="${currentPage < totalPages}">
                        <c:url var="nextUrl" value="MaterialListServlet">
                            <c:if test="${not empty searchQuery}"><c:param name="search" value="${searchQuery}"/></c:if>
                            <c:if test="${categoryFilter ne 'All'}"><c:param name="category" value="${categoryFilter}"/></c:if>
                            <c:if test="${supplierFilter ne 'All'}"><c:param name="supplier" value="${supplierFilter}"/></c:if>
                            <c:param name="itemsPerPage" value="${itemsPerPage}"/>
                            <c:param name="sort" value="${sortField}"/>
                            <c:param name="dir" value="${sortDir}"/>
                            <c:param name="page" value="${currentPage + 1}"/>
                        </c:url>
                        <a href="${nextUrl}" class="page-button"><i class="fas fa-arrow-right"></i></a>
                    </c:if>
                    <c:if test="${currentPage >= totalPages}">
                        <span class="page-button disabled"><i class="fas fa-arrow-right"></i></span>
                    </c:if>
                </div>
                <div class="pagination-info">
                    Trang ${currentPage} / ${totalPages}
                </div>
            </c:if>
        </div>
        <div class="material-back-btn-wrapper" style="text-align: left; margin-top: 18px;">
            <a href="#" class="back-link" onclick="history.back(); return false;">
                <i class="fas fa-arrow-left"></i> Quay lại
            </a>
        </div>
    </div>
</body>
</html>