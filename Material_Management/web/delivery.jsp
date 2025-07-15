<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html>
    <head>
        <title>Quản lý giao hàng</title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
        <link href="https://fonts.googleapis.com/css2?family=Roboto:wght@400;500;600&display=swap" rel="stylesheet">
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
        <link rel="stylesheet" href="css/delivery.css">
        <link rel="stylesheet" href="css/sidebar.css"/>
    </head>
    <body>
        
        <%@include file="sidebar.jsp" %>

        <header class="navbar navbar-expand-lg navbar-dark fixed-top" style="background-color: #3b82f6;">
            <div class="container-fluid">
                <button class="navbar-toggler" type="button" id="sidebarToggle">
                    <span class="navbar-toggler-icon"></span>
                </button>
                <a class="navbar-brand" href="#">Quản Lý Vận Chuyển</a>
            </div>
        </header>

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
                        <select class="form-select" name="statusFilter" style="width: 180px; display: inline-block;" onchange="filterByStatus()">
                            <option value="">Tất cả trạng thái</option>
                            <option value="Chờ giao">Chờ giao</option>
                            <option value="Đã giao">Đã giao</option>
                            <option value="Đã hủy">Đã hủy</option>
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
                                <th>Ngày giao</th>
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
                                            <c:when test="${not empty delivery.deliveryDate}">
                                    <fmt:formatDate value="${delivery.deliveryDate}" pattern="dd/MM/yyyy HH:mm"/>
                                </c:when>
                                <c:otherwise>
                                    -
                                </c:otherwise>
                            </c:choose>
                            </td>
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
                                <a href="exportFormDetail?exportId=${f.exportId}" class="export-list-btn">
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
            </div>
        </div>
    </body>
</html>
