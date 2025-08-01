<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<!DOCTYPE html>
<html lang="vi">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Bảng Điều Khiển - Hệ Thống Quản Lý Kho</title>
        <link rel="stylesheet" href="css/adminDashboard.css">
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
                <div class="dashboard-filter-container" style="margin-top: 16px; margin-bottom: 24px;">
                    <form method="GET" action="adminDashboard" class="dashboard-filter-form" style="display: flex; gap: 12px; align-items: center; flex-wrap: wrap;">
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
                        <button type="submit" class="chart-form-button" style="height: 36px;">Xem</button>
                    </form>
                </div>
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
                            <h3>Đơn Hàng Chờ Giao</h3>
                            <p class="stat-number">${pendingDeliveries}</p>
                        </div>
                    </a>
                </div>
                <div class="stat-card">
                    <a href="InventoryServlet" style="text-decoration:none;color:inherit;">
                        <div class="stat-icon" style="background-color: #8e44ad;"><i class="fas fa-coins"></i></div>
                        <div class="stat-info">
                            <h3>Tổng Giá Trị Tồn Kho</h3>
                            <p class="stat-number"><fmt:formatNumber value="${totalInventoryValue}" type="number" maxFractionDigits="0"/> ₫</p>
                        </div>
                    </a>
                </div>
                <div class="stat-card">
                    <a href="suppliers" style="text-decoration:none;color:inherit;">
                        <div class="stat-icon" style="background-color: #e67e22;"><i class="fas fa-handshake"></i></div>
                        <div class="stat-info">
                            <h3>Tổng Nhà Cung Cấp</h3>
                            <p class="stat-number">${totalSuppliers}</p>
                            <div style="font-size: 13px; color: #555; margin-top: 4px;">
                                Đang hợp tác: <b>${activeSuppliers}</b> / Ngừng hợp tác: <b>${terminatedSuppliers}</b> / Chưa hợp tác: <b>${inactiveSuppliers}</b>
                            </div>
                        </div>
                    </a>
                </div>
                <div class="stat-card">
                    <a href="settinglist" style="text-decoration:none;color:inherit;">
                        <div class="stat-icon" style="background-color: #00bcd4;"><i class="fas fa-user"></i></div>
                        <div class="stat-info">
                            <h3>Tài Khoản Người Dùng</h3>
                            <p class="stat-number">${totalUsers}</p>
                            <div style="font-size: 13px; color: #555; margin-top: 4px;">
                                Hoạt động: <b>${activeUsers}</b> / Không hoạt động: <b>${inactiveUsers}</b>
                            </div>
                        </div>
                    </a>
                </div>
                <div class="stat-card">
                    <a href="unitConversionSeverlet" style="text-decoration:none;color:inherit;">
                        <div class="stat-icon" style="background-color: #ff9800;"><i class="fas fa-balance-scale"></i></div>
                        <div class="stat-info">
                            <h3>Tổng Đơn Vị Tính</h3>
                            <p class="stat-number">${totalUnits}</p>
                            
                        </div>
                    </a>
                </div>
            </div>
            <div class="charts-grid charts-grid-1">
                <div class="chart-card">
                    <h3>Phân Bổ Yêu Cầu</h3>
                    <canvas id="requestDistributionChart"></canvas>
                </div>
            </div>
            <div class="charts-grid charts-grid-1">
                <div class="chart-card">
                    <h3>Biểu Đồ Mua/Xuất Vật Tư Theo Tháng</h3>
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
                        <c:forEach var="order" items="${recentPurchases}">
                            <tr>
                                <td>${order.purchaseOrderId}</td>
                                <td>
                                    <c:forEach var="detail" items="${order.details}" varStatus="loop">
                                        ${detail.materialName}<c:if test="${!loop.last}">, </c:if>
                                    </c:forEach>
                                </td>
                                <td>Mua</td>
                                <td>
                                    <c:set var="totalQty" value="0" />
                                    <c:forEach var="detail" items="${order.details}">
                                        <c:set var="totalQty" value="${totalQty + detail.quantity}" />
                                    </c:forEach>
                                    ${totalQty}
                                </td>
                                <td><fmt:formatDate value="${order.orderDate}" pattern="dd/MM/yyyy HH:mm"/></td>
                            </tr>
                        </c:forEach>
                        <c:if test="${empty recentPurchases}">
                            <tr>
                                <td colspan="5" style="text-align: center;">Không có đơn mua nào.</td>
                            </tr>
                        </c:if>
                        <c:if test="${fn:length(recentPurchases) >= 1}">
                            <tr>
                                <td colspan="5" style="text-align: center; cursor: pointer; color: #3578e5; font-weight: bold;" onclick="window.location.href = 'recentTransactions'">... Xem tất cả giao dịch</td>
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

            var importExportMonthLabels = [<c:forEach var="label" items="${importExportMonthLabels}" varStatus="loop">"${label}"<c:if test="${!loop.last}">,</c:if></c:forEach>];
            var importByMonth = <c:out value="${importByMonthJson}" default="[]"/>;
            var exportByMonth = <c:out value="${exportByMonthJson}" default="[]"/>;
            
            // Debug: Kiểm tra dữ liệu
            console.log('importExportMonthLabels:', importExportMonthLabels);
            console.log('importByMonth:', importByMonth);
            console.log('exportByMonth:', exportByMonth);
            
            var ctxImportExport = document.getElementById('importExportLineChart');
            if (!ctxImportExport) {
                console.error('Không tìm thấy canvas importExportLineChart');
            } else {
                console.log('Canvas found, creating chart...');
                var importExportLineChart = new Chart(ctxImportExport.getContext('2d'), {
                type: 'bar',
                data: {
                    labels: importExportMonthLabels,
                    datasets: [
                        {
                            label: 'Số lượng mua',
                            data: importByMonth,
                            backgroundColor: '#2980b9',
                            borderColor: '#2471a3',
                            borderWidth: 1
                        },
                        {
                            label: 'Số lượng xuất',
                            data: exportByMonth,
                            backgroundColor: '#e74c3c',
                            borderColor: '#c0392b',
                            borderWidth: 1
                        }
                    ]
                },
                options: {
                    responsive: true,
                    maintainAspectRatio: false,
                    plugins: {
                        legend: {
                            display: true,
                            position: 'top',
                            labels: {
                                font: {
                                    size: 16
                                },
                                padding: 25
                            }
                        },
                        title: {
                            display: true,
                            text: 'Biểu Đồ Mua/Xuất Vật Tư Theo Tháng',
                            font: {
                                size: 20,
                                weight: 'bold'
                            },
                            padding: {
                                top: 15,
                                bottom: 25
                            }
                        }
                    },
                    scales: {
                        y: {
                            beginAtZero: true,
                            title: {
                                display: true,
                                text: 'Số lượng',
                                font: {
                                    size: 16,
                                    weight: 'bold'
                                }
                            },
                            ticks: {
                                font: {
                                    size: 14
                                }
                            }
                        },
                        x: {
                            title: {
                                display: true,
                                text: 'Tháng',
                                font: {
                                    size: 16,
                                    weight: 'bold'
                                }
                            },
                            ticks: {
                                font: {
                                    size: 14
                                }
                            }
                        }
                    }
                }
            });
            }
        </script>

        <script src="js/adminDashboard.js"></script>
    </body>
</html> 