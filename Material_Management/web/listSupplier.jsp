<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Danh sách nhà cung cấp</title>
    <link rel="stylesheet" href="css/sidebar.css">
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
    <jsp:include page="sidebar.jsp"/>
    <div class="main-content">
        <div class="page-content">
            <div class="content-header">
                <h1>Danh sách nhà cung cấp</h1>
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
        </div>
        <jsp:include page="footer.jsp"/>
    </div>
</body>
</html> 