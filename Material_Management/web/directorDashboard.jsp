<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html lang="vi">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Tổng Quan - Hệ Thống Quản Lý Kho</title>
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/footer.css">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/directorDashboard.css">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/sidebar.css"/>
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/header.css"/>

        <!-- Font Awesome -->
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/css/all.min.css">

        <!-- Chart.js CDN -->
        <script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
    </head>
    <body>
        <!-- Include Sidebar -->
        <jsp:include page="sidebar.jsp" />

        <!-- Main Content -->
        <div id="main-content">
            <!-- Header -->
            <div class="welcome-header">
                <div class="welcome-text">

                    Xin chào, 
                    <strong>${user.fullname}</strong> 
                    <c:if test="${not empty user and not empty user.role and not empty user.role.rolename}">
                        (<span style="font-weight:normal;">${user.role.rolename}</span>)
                    </c:if>
                </div>



                <div class="user-info">
                    <div class="notification" id="notificationBell" style="position:relative;cursor:pointer;">
                        <i class="fas fa-bell notification-icon"></i>
                        <c:if test="${not empty notifications}">
                            <span class="badge">${fn:length(notifications)}</span>
                        </c:if>
                        <div id="notificationDropdown" style="display:none;position:absolute;right:0;top:100%;z-index:1000;background:#fff;border:1px solid #ccc;width:350px;box-shadow:0 2px 5px rgba(0,0,0,0.1);">

                            <ul style="list-style:none;padding:10px;margin:0;max-height:420px;overflow-y:auto;">
                                <c:if test="${empty notifications}">
                                    <li style="padding:8px 0;color:#666;">Không có thông báo</li>
                                    </c:if>
                                    <c:forEach var="noti" items="${notifications}">
                                    <li style="padding:8px 0;border-bottom:1px solid #eee;${noti.read ? 'opacity:0.7;' : ''}">
                                        <a href="markNotificationRead?notificationId=${noti.id}&requestId=${noti.requestId}" 
                                           style="color:#007bff;text-decoration:none;display:block;">
                                            <div style="display:flex;justify-content:space-between;align-items:center;">
                                                <span>${noti.message}</span>
                                                <c:if test="${!noti.read}">
                                                    <span style="background:#007bff;color:white;padding:2px 6px;border-radius:10px;font-size:10px;">Mới</span>
                                                </c:if>
                                            </div>
                                            <span style="font-size:12px;color:gray;">(${noti.createdAt})</span>
                                        </a>
                                    </li>
                                </c:forEach>
                            </ul>
                        </div>
                    </div>                    
                    <div class="user-avatar">
                        ${sessionScope.user.fullname != null ? sessionScope.user.fullname.charAt(0) : 'A'}
                    </div>
                </div>
            </div>  

            <div class="dashboard-header">
                <h1>Tổng Quan</h1>
                <div class="date-filter">
                    <form action="dashboard" method="get" class="filter-form">
                        <button type="submit" name="timeFilter" value="today" 
                                class="filter-btn ${timeFilter == 'today' ? 'active' : ''}">Hôm Nay</button>
                        <button type="submit" name="timeFilter" value="week" 
                                class="filter-btn ${timeFilter == 'week' ? 'active' : ''}">Tuần</button>
                        <button type="submit" name="timeFilter" value="month" 
                                class="filter-btn ${timeFilter == 'month' ? 'active' : ''}">Tháng</button>
                        <button type="submit" name="timeFilter" value="year" 
                                class="filter-btn ${timeFilter == 'year' ? 'active' : ''}">Năm</button>
                    </form>
                </div>
            </div>

            <div class="stats-grid">
                <div class="stat-card">
                    <div class="stat-icon" style="background-color: #6dbdf2;">
                        <i class="fas fa-box"></i>
                    </div>
                    <div class="stat-info">
                        <h3>Tổng Vật Tư</h3>
                        <p class="stat-number">${totalItems}</p>
                        <p class="stat-change positive">
                            <i class="fas fa-arrow-up"></i> Đã Cập Nhật
                        </p>
                    </div>
                </div>

                <div class="stat-card">
                    <div class="stat-icon" style="background-color: #2ecc71;">
                        <i class="fas fa-shopping-cart"></i>
                    </div>
                    <div class="stat-info">
                        <h3>Yêu Cầu</h3>
                        <p class="stat-number">${monthlyOrders}</p>
                        <p class="stat-change positive">
                            <i class="fas fa-arrow-up"></i> 
                            <c:choose>
                                <c:when test="${timeFilter == 'today'}">Hôm Nay</c:when>
                                <c:when test="${timeFilter == 'week'}">Tuần Này</c:when>
                                <c:when test="${timeFilter == 'year'}">Năm Nay</c:when>
                                <c:otherwise>Tháng Này</c:otherwise>
                            </c:choose>
                        </p>
                    </div>
                </div>

                <div class="stat-card">
                    <div class="stat-icon" style="background-color: #e74c3c;">
                        <i class="fas fa-exclamation-triangle"></i>
                    </div>
                    <div class="stat-info">
                        <h3>Vật Tư Sắp Hết</h3>
                        <p class="stat-number">${lowStockItems}</p>
                        <p class="stat-change negative">
                            <i class="fas fa-arrow-down"></i> Cần Chú Ý
                        </p>
                    </div>
                </div>

                <div class="stat-card">
                    <div class="stat-icon" style="background-color: #f1c40f;">
                        <i class="fas fa-truck"></i>
                    </div>
                    <div class="stat-info">
                        <h3>Đang Giao</h3>
                        <p class="stat-number">${pendingDeliveries}</p>
                        <p class="stat-change positive">
                            <i class="fas fa-arrow-up"></i> Đang Xử Lý
                        </p>
                    </div>
                </div>
            </div>

            <div class="table-card">
                <h3>Vật Tư Sắp Hết</h3>
                <table>
                    <thead>
                        <tr>
                            <th>Vật Tư</th>
                            <th>Tồn Kho</th>
                            <th>Tồn Tối Thiểu</th>
                            <th>Trạng Thái</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach items="${lowStockItemsList}" var="item">
                            <tr>
                                <td>${item.name}</td>
                                <td>${item.quantity}</td>
                                <td>${item.minStock}</td>
                                <td>
                                    <span class="status-badge ${item.status == 'Critical' ? 'danger' : 'warning'}">
                                        ${item.status}
                                    </span>
                                </td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
            </div>

            <div class="inventory-summary">
                <h3>Tổng Quan Kho</h3>
                <div class="inventory-stats">
                    <div class="inventory-stat">
                        <span class="label">Còn Hàng:</span>
                        <span class="value">${inventoryStats.inStock}</span>
                    </div>
                    <div class="inventory-stat">
                        <span class="label">Sắp Hết:</span>
                        <span class="value">${inventoryStats.lowStock}</span>
                    </div>
                    <div class="inventory-stat">
                        <span class="label">Hết Hàng:</span>
                        <span class="value">${inventoryStats.outOfStock}</span>
                    </div>
                </div>
            </div>

            <!-- Biểu Đồ -->
            <div class="charts-grid">
                <div class="chart-card">
                    <h3>Phân Bổ Vật Tư Theo Danh Mục</h3>
                    <canvas id="materialCategoryChart"></canvas>
                </div>
                <div class="chart-card">
                    <h3>Xu Hướng Tồn Kho</h3>
                    <canvas id="inventoryTrendChart"></canvas>
                </div>
            </div>

            <!-- Bảng Dữ Liệu -->
            <div class="table-card">
                <h3>Các Giao Dịch Gần Đây</h3>
                <table>
                    <thead>
                        <tr>
                            <th>Mã Giao Dịch</th>
                            <th>Vật Tư</th>
                            <th>Loại</th>
                            <th>Số Lượng</th>
                            <th>Ngày</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="transaction" items="${recentTransactions}">
                            <tr>
                                <td>${transaction.id}</td>
                                <td>${transaction.materialName}</td>
                                <td>${transaction.type}</td>
                                <td>${transaction.quantity}</td>
                                <td><fmt:formatDate value="${transaction.date}" pattern="dd/MM/yyyy HH:mm"/></td>
                            </tr>
                        </c:forEach>
                        <c:if test="${empty recentTransactions}">
                            <tr>
                                <td colspan="5" style="text-align: center;">Không có giao dịch gần đây nào.</td>
                            </tr>
                        </c:if>
                    </tbody>
                </table>
            </div>
            <!-- Include Footer -->
            <jsp:include page="footer.jsp" />
        </div>

        <!-- Hidden data for charts -->
        <div id="chartData"
             data-category-labels='[<c:forEach var="item" items="${categoryStats}" varStatus="loop">"${item.categoryName}"<c:if test="${!loop.last}">,</c:if></c:forEach>]'
             data-category-data='[<c:forEach var="item" items="${categoryStats}" varStatus="loop">${item.itemCount}<c:if test="${!loop.last}">,</c:if></c:forEach>]'
             data-inventory-labels='[<c:forEach var="label" items="${inventoryTrendLabels}" varStatus="loop">"${label}"<c:if test="${!loop.last}">,</c:if></c:forEach>]'
             data-inventory-data='[<c:forEach var="value" items="${inventoryTrend}" varStatus="loop">${value}<c:if test="${!loop.last}">,</c:if></c:forEach>]'
                     data-request-labels='["Mua Vật Tư", "Xuất Kho", "Sửa Chữa"]'
                     data-request-data='[${requestStats.purchaseCount}, ${requestStats.outgoingCount}, ${requestStats.repairCount}]'
             data-cost-labels='["01/06", "04/06", "08/06", "12/06"]'
             data-cost-data='[${costTrend[0]}, ${costTrend[1]}, ${costTrend[2]}, ${costTrend[3]}]'>
        </div>

        <script src="../js/directorDashboard.js"></script>
        <script>
            document.addEventListener('DOMContentLoaded', function () {
                var bell = document.getElementById('notificationBell');
                var dropdown = document.getElementById('notificationDropdown');
                if (bell && dropdown) {
                    bell.addEventListener('click', function (event) {
                        event.stopPropagation();
                        dropdown.style.display = (dropdown.style.display === 'none' || dropdown.style.display === '') ? 'block' : 'none';
                    });
                    // Ẩn dropdown khi click ra ngoài
                    document.addEventListener('click', function () {
                        dropdown.style.display = 'none';
                    });
                    // Ngăn dropdown bị ẩn khi click vào chính nó
                    dropdown.addEventListener('click', function (event) {
                        event.stopPropagation();
                    });
                }
            });
        </script>

    </body>
</html>