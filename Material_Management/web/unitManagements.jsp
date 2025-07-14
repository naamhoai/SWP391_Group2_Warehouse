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
            <div class="header">
                <div class="header-content">
                    <h1 class="page-title">
                        <i class="fas fa-ruler"></i> Quản Lý Đơn Vị Tính
                        <span class="unit-count">(${list.size()})</span>
                    </h1>
                    <p class="subtitle">Danh mục các đơn vị đo lường trong hệ thống</p>
                </div>
                <div class="header-actions">
                    <a href="createUnit.jsp" class="btn-add">
                        <i class="fas fa-plus"></i> Thêm mới đơn vị
                    </a>
                    <a href="UnitChangeHistoryServlet" class="btn-add">
                        <i class="fas fa-history"></i> Lịch sử chỉnh sửa
                    </a>
                    <a href="unitEditseverlet" class="btn-add">
                        <i class="fas fa-pencil-alt"></i> Chỉnh sửa tỉ lệ đơn vị
                    </a>
                </div>
            </div>

                        <h2 style="color: red">${requestScope.messUpdate}</h2>
            <form action="unitConversionSeverlet" method="get">
                <div class="search-section">
                    <div class="search-box">
                     
                        <input type="text" name="search" value="${search}" placeholder="Tìm kiếm theo tên đơn vị...">
                        <button type="submit">Tìm kiếm</button>
                        <button><a href="unitConversionSeverlet">Làm mới</a></button>
                    </div>
                     
                </div>
            </form>

            <!-- Bảng đơn vị -->
            <div class="table-container">
                <table class="units-table">
                    <thead>
                        <tr>
                            <th>STT</th>
                            <th>Tên đơn vị</th>
                            <th>Tên đơn vị lưu kho</th>
                            <th>Trạng thái</th>
                            <th>Tỉ lệ quy đổi</th>
                            <th>Hành động</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="u" items="${list}" varStatus="status">
                            <tr>
                                <td>${u.conversionid}</td>
                                <td>${u.units.unit_name}</td>
                                <td>${u.units.unit_namePr}</td>
                                <td>${u.status}</td>
                                <td>${u.conversionfactor}</td>
                                <td>
                                   
                                    <a href="unitConversionSeverlet?action=${u.status == 'Hoạt động' ? 'Không hoạt động' : 'Hoạt động'}&cvid=${u.conversionid}" 
                                       class="btn-toggle" title="Đổi trạng thái">

                                        <i class="fas fa-toggle-${u.status == 'Hoạt động' ? 'on' : 'off'}"></i>
                                    </a>
                                </td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
            </div>
            <div class="pagination">
                <c:forEach begin="1" end="${pages}" var="p">
                    <form method="get" action="unitConversionSeverlet" style="display:inline;">
                        <input type="hidden" name="page" value="${p}" />
                        <input type="hidden" name="search" value="${search}" />
                        <button type="submit" class="${p == currentPage ? 'active-page' : ''}">
                            ${p}
                        </button>
                    </form>
                </c:forEach>
            </div>
        </div>
    </body>
</html>
