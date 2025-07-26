<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>Quản Lý Đơn Vị Tính</title>
    <link rel="stylesheet" href="css/sidebar.css">
    <link rel="stylesheet" href="./css/unitMmanagement.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">
</head>
<body>
<jsp:include page="sidebar.jsp"/>
<div class="container">
    <div class="header-flex">
        <a href="createUnit.jsp" class="btn-primary btn-add"><i class="fas fa-plus-circle"></i> Thêm đơn vị mới</a>
        <h1 class="page-title"><i class="fas fa-ruler"></i> Quản Lý Đơn Vị Tính</h1>
        <form action="unitConversionSeverlet" method="get" class="search-form">
            <input type="text" name="search" placeholder="Tìm kiếm tên đơn vị" value="${param.search != null ? param.search : ''}">
            <button type="submit" class="btn-primary btn-search"><i class="fas fa-search"></i> Tìm kiếm</button>
            <button type="button" class="btn-primary btn-refresh" onclick="window.location.href='unitConversionSeverlet'" style="margin-left: 8px;"><i class="fas fa-sync-alt"></i> Làm mới</button>
        </form>
    </div>
    <h2 style="color: red">${requestScope.messUpdate}</h2>
    <div class="table-container">
        <table class="units-table">
            <thead>
            <tr>
                <th>STT</th>
                <th>Tên đơn vị</th>
                <th>Trạng thái</th>
                <th>Hành động</th>
            </tr>
            </thead>
            <tbody>
            <c:forEach var="u" items="${list}" varStatus="status">
                <tr>
                    <td>${u.unit_id}</td>
                    <td>${u.unit_name}</td>
                    <td>${u.status}</td>
                    <td>
                        <form action="unitConversionSeverlet" method="post" style="display:inline;">
                            <input type="hidden" name="action" value="toggleStatus">
                            <input type="hidden" name="unitId" value="${u.unit_id}">
                            <button type="submit" class="btn-toggle" title="Đổi trạng thái">
                                <i class="fas fa-toggle-${u.status == 'Hoạt động' ? 'on' : 'off'}"></i>
                            </button>
                        </form>
                    </td>
                </tr>
            </c:forEach>
            </tbody>
        </table>
    </div>
    <!-- PHÂN TRANG -->
    <div style="text-align:center; margin-top:20px;">
        <c:if test="${totalPages > 1}">
            <c:forEach var="i" begin="1" end="${totalPages}">
                <form action="unitConversionSeverlet" method="get" style="display:inline;">
                    <input type="hidden" name="page" value="${i}"/>
                    <c:if test="${param.search != null && param.search ne ''}">
                        <input type="hidden" name="search" value="${param.search}"/>
                    </c:if>
                    <button type="submit" style="margin:2px; padding:6px 12px; border-radius:4px; border:1px solid #ccc; background:${i == currentPage ? '#007bff' : '#fff'}; color:${i == currentPage ? '#fff' : '#333'}; font-weight:${i == currentPage ? 'bold' : 'normal'}; cursor:pointer;">
                        ${i}
                    </button>
                </form>
            </c:forEach>
        </c:if>
    </div>
</div>
</body>
</html>
