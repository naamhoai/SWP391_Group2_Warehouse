<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="vi">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Bảng Điều Khiển - Hệ Thống Quản Lý Kho</title>
        <link rel="stylesheet" href="css/footer.css">
        <link rel="stylesheet" href="css/adminDashboard.css">
        <link rel="stylesheet" href="css/sidebar.css">
        <link rel="stylesheet" href="css/header.css"/>
        <!-- Font Awesome -->
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/css/all.min.css">
        <!-- Google Fonts - Roboto -->
        <link href="https://fonts.googleapis.com/css2?family=Roboto:wght@400;500;700&display=swap" rel="stylesheet">
        <!-- Chart.js CDN -->
        <script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
        <script src="js/sidebar.js"></script>
    </head>
    <body>
        <!-- Include Sidebar -->
        <jsp:include page="sidebar.jsp" />
        
        <!-- Header -->
        <div id="main-content">
            <%@include file="header.jsp" %>
            <div class="dashboard-header">
                <h1>Bảng Điều Khiển</h1>
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

            <!-- New: Charts Grid -->
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

            <!-- New: Data Table -->
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
                        <%-- Example data, replace with actual JSTL loop --%>
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

        <script src="js/adminDashboard.js"></script>

    </body>
</html>