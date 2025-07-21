<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

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
        <%@include file="sidebar.jsp" %>

        <div id="main-content">
            <%@include file="header.jsp" %>
            <div class="dashboard-header">
                <h1>Báo cáo tổng quan hệ thống</h1>
            </div>
            <div class="stats-grid">
                <div class="stat-card">
                    <a href="MaterialListServlet" style="text-decoration:none;color:inherit;">
                        <div class="stat-icon" style="background-color: #2563eb;"><i class="fas fa-box"></i></div>
                        <div class="stat-info">
                            <h3>Tổng Vật Tư</h3>
                            <p class="stat-number">${totalItems}</p>
                        </div>
                    </a>
                </div>
                <div class="stat-card">
                    <a href="delivery" style="text-decoration:none;color:inherit;">
                        <div class="stat-icon" style="background-color: #f1c40f;"><i class="fas fa-truck"></i></div>
                        <div class="stat-info">
                            <h3>Đơn Hàng Đã Giao</h3>
                            <p class="stat-number">${pendingDeliveries}</p>
                        </div>
                    </a>
                </div>
                <div class="stat-card">
                    <div class="stat-icon" style="background-color: #8e44ad;"><i class="fas fa-coins"></i></div>
                    <div class="stat-info">
                        <h3>Tổng Giá Trị Tồn Kho</h3>
                        <p class="stat-number"><fmt:formatNumber value="${totalInventoryValue}" type="number" maxFractionDigits="0"/> ₫</p>
                    </div>
                </div>
            </div>
            <div class="charts-grid charts-grid-2">
                <div class="chart-card">
                    <h3>Xu Hướng Tồn Kho</h3>
                    <canvas id="inventoryTrendChart"></canvas>
                </div>
                <div class="chart-card">
                    <h3>Phân Bổ Yêu Cầu</h3>
                    <canvas id="requestDistributionChart"></canvas>
                </div>
            </div>
            <div class="charts-grid charts-grid-1">
                <div class="chart-card full-width">
                    <h3>Biểu Đồ Mua/Xuất Vật Tư Theo Tháng</h3>
                    <div class="chart-form-container">
                        <form method="GET" action="adminDashboard" class="chart-form">
                            <div class="chart-form-group">
                                <label for="startMonth" class="chart-form-label">Từ tháng:</label>
                                <select id="startMonth" name="startMonth" class="chart-form-select">
                                    <c:forEach var="month" begin="1" end="12">
                                        <option value="${month < 10 ? '0' : ''}${month}" ${startMonth == month ? 'selected' : ''}>Tháng ${month}</option>
                                    </c:forEach>
                                </select>
                                <select id="startYear" name="startYear" class="chart-form-select">
                                    <c:forEach var="year" begin="2020" end="2030">
                                        <option value="${year}" ${startYear == year ? 'selected' : ''}>${year}</option>
                                    </c:forEach>
                                </select>
                            </div>
                            
                            <div class="chart-form-group">
                                <label for="endMonth" class="chart-form-label">Đến tháng:</label>
                                <select id="endMonth" name="endMonth" class="chart-form-select">
                                    <c:forEach var="month" begin="1" end="12">
                                        <option value="${month < 10 ? '0' : ''}${month}" ${endMonth == month ? 'selected' : ''}>Tháng ${month}</option>
                                    </c:forEach>
                                </select>
                                <select id="endYear" name="endYear" class="chart-form-select">
                                    <c:forEach var="year" begin="2020" end="2030">
                                        <option value="${year}" ${endYear == year ? 'selected' : ''}>${year}</option>
                                    </c:forEach>
                                </select>
                            </div>
                            
                            <button type="submit" class="chart-form-button">Xem</button>
                        </form>
                    </div>
                    <canvas id="importExportLineChart"></canvas>
                </div>
            </div>
            <div class="table-card">
                <h3>Top 5 Vật Tư Xuất Nhiều Nhất</h3>
                <table>
                    <thead><tr><th>Vật Tư</th><th>Số Lượng Xuất</th></tr></thead>
                    <tbody>
                        <c:forEach var="item" items="${topExportedItems}">
                            <tr><td>${item.materialName}</td><td>${item.quantity}</td></tr>
                        </c:forEach>
                    </tbody>
                </table>
            </div>
            <div class="table-card">
                <h3>Các Giao Dịch Gần Đây</h3>
                <table>
                    <thead><tr><th>Mã Giao Dịch</th><th>Vật Tư</th><th>Loại</th><th>Số Lượng</th><th>Ngày</th></tr></thead>
                    <tbody>
                        <c:forEach var="transaction" items="${recentTransactions}" varStatus="loop">
                            <tr>
                                <td>${transaction.id}</td>
                                <td>${transaction.materialName}</td>
                                <td>${transaction.type}</td>
                                <td>${transaction.quantity}</td>
                                <td><fmt:formatDate value="${transaction.date}" pattern="dd/MM/yyyy HH:mm"/></td>
                            </tr>
                        </c:forEach>
                        <c:if test="${totalTransactions > 5}">
                            <tr>
                                <td colspan="5" style="text-align: center; cursor: pointer; color: #3578e5; font-weight: bold;" onclick="window.location.href='recentTransactions'">... Xem tất cả giao dịch</td>
                            </tr>
                        </c:if>
                    </tbody>
                </table>
            </div>
            <jsp:include page="footer.jsp" />
        </div>
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

            var ctx = document.getElementById('requestDistributionChart').getContext('2d');
            var requestDistributionChart = new Chart(ctx, {
                type: 'bar',
                data: {
                    labels: requestLabels,
                    datasets: [{
                        label: 'Phân bổ Yêu Cầu',
                        data: requestData,
                        backgroundColor: [
                            '#3578e5',
                            '#2ecc71'
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

            var importExportMonthLabels = [<c:forEach var="label" items="${importExportMonthLabels}" varStatus="loop">"${label}"<c:if test="${!loop.last}">,</c:if></c:forEach>];
            var importByMonth = [<c:forEach var="v" items="${importByMonth}" varStatus="loop">${v}<c:if test="${!loop.last}">,</c:if></c:forEach>];
            var exportByMonth = [<c:forEach var="v" items="${exportByMonth}" varStatus="loop">${v}<c:if test="${!loop.last}">,</c:if></c:forEach>];
            var ctxImportExport = document.getElementById('importExportLineChart').getContext('2d');
            var importExportLineChart = new Chart(ctxImportExport, {
                type: 'bar',
                data: {
                    labels: importExportMonthLabels,
                    datasets: [
                        {
                            label: 'Số lượng nhập',
                            data: importByMonth,
                            backgroundColor: '#e74c3c',
                            borderColor: '#c0392b',
                            borderWidth: 1
                        },
                        {
                            label: 'Số lượng xuất',
                            data: exportByMonth,
                            backgroundColor: '#2ecc71',
                            borderColor: '#27ae60',
                            borderWidth: 1
                        }
                    ]
                },
                options: {
                    responsive: true,
                    plugins: {
                        legend: {
                            display: true,
                            position: 'top'
                        }
                    },
                    scales: {
                        y: {
                            beginAtZero: true,
                            title: {
                                display: true,
                                text: 'Số lượng'
                            }
                        },
                        x: {
                            title: {
                                display: true,
                                text: 'Tháng'
                            }
                        }
                    }
                }
            });
        </script>

        <script src="js/adminDashboard.js"></script>
    </body>
</html> 