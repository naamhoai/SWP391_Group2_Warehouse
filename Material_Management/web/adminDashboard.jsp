<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="vi">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Bảng Điều Khiển - Hệ Thống Quản Lý Kho</title>
        <link rel="stylesheet" href="css/dashboard.css">
        <link rel="stylesheet" href="css/sidebar.css">
        <link rel="stylesheet" href="css/header.css"/>
        <link rel="stylesheet" href="css/footer.css"/>
        <!-- Font Awesome -->
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/css/all.min.css">
        <!-- Google Fonts - Roboto -->
        <link href="https://fonts.googleapis.com/css2?family=Roboto:wght@400;500;700&display=swap" rel="stylesheet">
        <!-- Chart.js CDN -->
        <script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
        <!-- Flatpickr CSS -->
        <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/flatpickr/dist/flatpickr.min.css">
        <!-- Flatpickr JS -->
        <script src="https://cdn.jsdelivr.net/npm/flatpickr"></script>
        <!-- Flatpickr Vietnamese -->
        <script src="https://cdn.jsdelivr.net/npm/flatpickr/dist/l10n/vi.js"></script>
    </head>
    <body>
        <!-- Include Sidebar -->
        <jsp:include page="sidebar.jsp" />

        <!-- Main Content -->
        <div id="main-content">
            <%@include file="header.jsp" %>

            <!-- Dashboard Header -->
            <div class="dashboard-header">
                <div class="header-left">
                    <h1>Bảng Điều Khiển</h1>
                </div>
                <div class="date-filter">
                    <form action="adminDashboard" method="get" class="filter-form">
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

            <!-- Stats Grid -->
            <div class="stats-grid">
                <!-- Tổng Vật Tư clickable -->
                <a href="materialDetailList.jsp" class="stat-card-link">
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
                </a>

                
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
                <!-- Tổng Giá Trị Tồn Kho -->
                <div class="stat-card">
                    <div class="stat-icon" style="background-color: #8e44ad;">
                        <i class="fas fa-coins"></i>
                    </div>
                    <div class="stat-info">
                        <h3>Tổng Giá Trị Tồn Kho</h3>
                        <p class="stat-number">
                            <fmt:formatNumber value="${totalInventoryValue}" type="currency" currencySymbol="₫"/>
                        </p>
                        <p class="stat-change positive">
                            <i class="fas fa-arrow-up"></i> Đã Cập Nhật
                        </p>
                    </div>
                </div>
            </div>

            <!-- Charts Grid -->
            <div class="charts-grid">
                <div class="charts-row">
                    <div class="chart-card">
                        <h3>Phân Bổ Yêu Cầu</h3>
                        <canvas id="requestDistributionChart"></canvas>
                    </div>
                    <div class="chart-card">
                        <h3>Xu Hướng Tồn Kho</h3>
                        <canvas id="inventoryTrendChart"></canvas>
                    </div>
                </div>
                <div class="charts-row">
                    <div class="chart-card">
                        <h3>Top 5 Vật Tư Xuất Nhiều Nhất</h3>
                        <table>
                            <thead>
                                <tr>
                                    <th>Vật Tư</th>
                                    <th>Số Lượng Xuất</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:forEach var="item" items="${topExportedItems}">
                                    <tr>
                                        <td>${item.materialName}</td>
                                        <td>${item.quantity}</td>
                                    </tr>
                                </c:forEach>
                            </tbody>
                        </table>
                    </div>
                    <div class="chart-card">
                        <h3>Top 5 Vật Tư Nhập Nhiều Nhất</h3>
                        <table>
                            <thead>
                                <tr>
                                    <th>Vật Tư</th>
                                    <th>Số Lượng Nhập</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:forEach var="item" items="${topImportedItems}">
                                    <tr>
                                        <td>${item.materialName}</td>
                                        <td>${item.quantity}</td>
                                    </tr>
                                </c:forEach>
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>
            <style>
                .charts-grid {
                    display: flex;
                    flex-direction: column;
                    gap: 24px;
                }
                .charts-row {
                    display: flex;
                    flex-direction: row;
                    gap: 24px;
                }
                .chart-card {
                    flex: 1;
                    background: #fff;
                    border-radius: 16px;
                    padding: 24px;
                    box-shadow: 0 2px 8px rgba(0,0,0,0.03);
                    min-width: 0;
                }
                .stat-card-link {
                    text-decoration: none;
                    color: inherit;
                    display: block;
                }
                .stat-card-link .stat-card {
                    transition: box-shadow 0.2s, transform 0.2s;
                    cursor: pointer;
                }
                .stat-card-link .stat-card:hover {
                    box-shadow: 0 4px 16px rgba(53,120,229,0.10);
                    transform: translateY(-2px) scale(1.02);
                }
            </style>

            <!-- Low Stock Items Table -->
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

            <!-- Recent Transactions Table -->
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

        <!-- Chart Data -->
        <script>
            var requestLabels = ["Mua Vật Tư", "Xuất Kho"];
            var requestData = [<c:out value="${requestStats.purchaseCount}" default="0"/>,
            <c:out value="${requestStats.outgoingCount}" default="0"/>];
            var costLabels = ["01/06", "04/06", "08/06", "12/06"];
            var costData = [<c:out value="${costTrend[0]}" default="0"/>,
            <c:out value="${costTrend[1]}" default="0"/>,
            <c:out value="${costTrend[2]}" default="0"/>,
            <c:out value="${costTrend[3]}" default="0"/>];
            var categoryLabels = [<c:forEach var="item" items="${categoryStats}" varStatus="loop">"${item.categoryName}"<c:if test="${!loop.last}">,</c:if></c:forEach>];
            var categoryData = [<c:forEach var="item" items="${categoryStats}" varStatus="loop">${item.itemCount}<c:if test="${!loop.last}">,</c:if></c:forEach>];
            var inventoryTrendLabels = [<c:forEach var="label" items="${inventoryTrendLabels}" varStatus="loop">"${label}"<c:if test="${!loop.last}">,</c:if></c:forEach>];
            var inventoryTrendData = [<c:forEach var="value" items="${inventoryTrend}" varStatus="loop">${value}<c:if test="${!loop.last}">,</c:if></c:forEach>];

            // Vẽ biểu đồ phân bổ yêu cầu
            var ctx = document.getElementById('requestDistributionChart').getContext('2d');
            var requestDistributionChart = new Chart(ctx, {
                type: 'bar',
                data: {
                    labels: requestLabels,
                    datasets: [{
                        label: 'Phân bổ Yêu Cầu',
                        data: requestData,
                        backgroundColor: [
                            '#3578e5', // Mua Vật Tư
                            '#2ecc71'  // Xuất Kho
                        ],
                        borderWidth: 1
                    }]
                },
                options: {
                    responsive: true,
                    plugins: {
                        legend: {
                            display: false
                        }
                    },
                    scales: {
                        y: {
                            beginAtZero: true,
                            title: {
                                display: true,
                                text: 'Số lượng'
                            }
                        }
                    }
                }
            });

            // Vẽ biểu đồ xu hướng tồn kho
            var ctxInventory = document.getElementById('inventoryTrendChart').getContext('2d');
            var inventoryTrendChart = new Chart(ctxInventory, {
                type: 'line',
                data: {
                    labels: inventoryTrendLabels,
                    datasets: [{
                        label: 'Tổng tồn kho',
                        data: inventoryTrendData,
                        borderColor: '#3578e5',
                        backgroundColor: 'rgba(53,120,229,0.1)',
                        fill: true,
                        tension: 0.3
                    }]
                },
                options: {
                    responsive: true,
                    plugins: {
                        legend: {
                            display: true
                        }
                    },
                    scales: {
                        y: {
                            beginAtZero: true,
                            title: {
                                display: true,
                                text: 'Số lượng tồn kho'
                            }
                        }
                    }
                }
            });
        </script>

        <script src="js/adminDashboard.js"></script>
        <script src="js/sidebar.js"></script>
    </body>
</html> 