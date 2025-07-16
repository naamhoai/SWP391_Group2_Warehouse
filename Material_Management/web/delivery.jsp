<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html>
    <head>
        <title>Quản lý giao hàng</title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
        <link href="https://fonts.googleapis.com/css2?family=Inter:wght@400;500;600&family=Poppins:wght@600;700&display=swap" rel="stylesheet">
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
        <link rel="stylesheet" href="css/delivery.css">
        <link rel="stylesheet" href="css/sidebar.css"/>
    </head>
    <body>

        <%@include file="sidebar.jsp" %>
        
        <h2>Quản Lý Vận Chuyển</h2>

        <div class="content" id="mainContent" style="padding-top: 80px;">
            <div class="container">
                <c:if test="${not empty param.success}">
                    <div class="alert alert-success alert-dismissible fade show" role="alert">
                        ${param.success}
                        <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
                    </div>
                </c:if>
                <c:if test="${not empty param.error}">
                    <div class="alert alert-danger alert-dismissible fade show" role="alert">
                        ${param.error}
                        <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
                    </div>
                </c:if>

                <div class="d-flex justify-content-between align-items-center mb-4">
                    <div>
                        <select class="form-select" name="statusFilter" style="width: 200px; display: inline-block;" onchange="filterByStatus()">
                            <option value="" ${empty param.status ? 'selected' : ''}>Tất cả trạng thái</option>
                            <option value="Chờ giao" ${param.status == 'Chờ giao' ? 'selected' : ''}>Chờ giao</option>
                            <option value="Đã giao" ${param.status == 'Đã giao' ? 'selected' : ''}>Đã giao</option>
                            <option value="Đã hủy" ${param.status == 'Đã hủy' ? 'selected' : ''}>Đã hủy</option>
                        </select>
                    </div>
                </div>

                <div class="table-responsive">
                    <table class="table table-bordered align-middle">
                        <thead class="table-light">
                            <tr>
                                <th>Mã giao hàng</th>
                                <th>Người nhận</th>
                                <th>Địa chỉ</th>
                                <th>Trạng thái</th>
                                <th>Mô tả</th>
                                <th>Thao tác</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="delivery" items="${deliveries}">
                                <tr>
                                    <td>${delivery.id}</td>
                                    <td>${delivery.recipientName}</td>
                                    <td>${delivery.deliveryAddress}</td>
                                    <td>
                                        <c:choose>
                                            <c:when test="${delivery.status eq 'Chờ giao'}">
                                                <span class="badge bg-warning text-dark">Chờ giao</span>
                                            </c:when>
                                            <c:when test="${delivery.status eq 'Đã giao'}">
                                                <span class="badge bg-success">Đã giao</span>
                                            </c:when>
                                            <c:when test="${delivery.status eq 'Đã hủy'}">
                                                <span class="badge bg-danger">Đã hủy</span>
                                            </c:when>
                                            <c:otherwise>
                                                <span class="badge bg-secondary">${delivery.status}</span>
                                            </c:otherwise>
                                        </c:choose>
                                    </td>
                                    <td>${delivery.description}</td>
                                    <td>
                                        <a href="exportFormDetail?exportId=${delivery.exportId}" class="export-list-btn">
                                            <i class="fa fa-eye"></i> Xem chi tiết
                                        </a>
                                    </td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                    <c:if test="${empty deliveries}">
                        <div class="alert alert-warning text-center mt-3">Không có dữ liệu giao hàng.</div>
                    </c:if>

                    <script>
                        function filterByStatus() {
                            var status = document.getElementsByName('statusFilter')[0].value;
                            var url = 'delivery?page=1';
                            if (status) {
                                url += '&status=' + encodeURIComponent(status);
                            }
                            window.location.href = url;
                        }
                    </script>

                    <%
                        String statusParam = "";
                        String status = request.getParameter("status");
                        if (status != null && !status.isEmpty()) {
                            statusParam = "&status=" + status;
                        }
                        request.setAttribute("statusParam", statusParam);
                    %>
                    <div style="display: flex; justify-content: center; gap: 8px; margin-top: 24px;">
                        <c:if test="${currentPage > 1}">
                            <a href="delivery?page=${currentPage - 1}${statusParam}" class="btn btn-secondary btn-sm">Trang trước</a>
                        </c:if>
                        <c:forEach begin="1" end="${totalPages}" var="i">
                            <c:choose>
                                <c:when test="${i == currentPage}">
                                    <a href="delivery?page=${i}${statusParam}" class="btn btn-primary btn-sm" style="color:#fff;">${i}</a>
                                </c:when>
                                <c:otherwise>
                                    <a href="delivery?page=${i}${statusParam}" class="btn btn-outline-primary btn-sm">${i}</a>
                                </c:otherwise>
                            </c:choose>
                        </c:forEach>
                        <c:if test="${currentPage < totalPages}">
                            <a href="delivery?page=${currentPage + 1}${statusParam}" class="btn btn-secondary btn-sm">Trang sau</a>
                        </c:if>
                    </div>
                </div>
            </div>
    </body>
</html>
