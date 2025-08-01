<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html lang="vi">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Bảng Điều Khiển Nhân Viên - Hệ Thống Quản Lý Kho</title>
        <link rel="stylesheet" href="css/dashboard.css">
        <link rel="stylesheet" href="css/sidebar.css">
        <link rel="stylesheet" href="css/header.css"/>
        <link rel="stylesheet" href="css/footer.css"/>
        <link rel="stylesheet" href="css/staffDashboard.css"/>
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
        <!-- Sidebar -->
        <%@include file="sidebar.jsp" %>
        
        <!-- Main Content -->
        <div id="main-content">
            <!-- Header -->
            <div class="welcome-header">
                <div class="welcome-text">
                    Xin chào, 
                    <strong>${sessionScope.Admin.fullname}</strong> 
                    <c:if test="${not empty sessionScope.Admin.role and not empty sessionScope.Admin.role.rolename}">
                        (<span style="font-weight:normal;">${sessionScope.Admin.role.rolename}</span>)
                    </c:if>
                </div>
                <div class="user-info">
                    <div class="notification" id="notificationBell" style="cursor:pointer;position:relative;">
                        <i class="fas fa-bell" style="font-size:20px;color:#333;"></i>
                        <span class="badge" style="display:none;position:absolute;top:-4px;right:-4px;background:red;color:white;border-radius:50%;padding:1px 5px;font-size:11px;"></span>
                        <div id="notificationDropdown" style="display:none;position:absolute;right:0;top:30px;z-index:1000;background:#fff;border:1px solid #ccc;width:350px;box-shadow:0 2px 5px rgba(0,0,0,0.1);border-radius:8px;">
                            <div style="padding:12px;border-bottom:1px solid #eee;background:#f8f9fa;font-weight:600;">Thông báo mới</div>
                            <div style="max-height:400px;overflow-y:auto;">
                                <ul style="list-style:none;padding:0;margin:0;"></ul>
                            </div>
                        </div>
                    </div>

                    <div class="user-avatar">
                        ${sessionScope.Admin.fullname != null ? sessionScope.Admin.fullname.charAt(0) : 'N'}
                    </div>
                </div>
            </div>

            <!-- Dashboard Header -->
            <div class="dashboard-header">
                <div class="header-left">
                    <h1>Bảng Điều Khiển Nhân Viên</h1>
                </div>
                <div class="dashboard-filter-container" style="margin-top: 16px; margin-bottom: 24px;">
                    <form method="GET" action="staffDashboard" class="dashboard-filter-form" style="display: flex; gap: 12px; align-items: center; flex-wrap: wrap;">
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

            <!-- Stats Grid -->
            <%--
            <div class="stats-grid">
                <div class="stat-card">
                    <div class="stat-icon" style="background-color: #6dbdf2;">
                        <i class="fas fa-box"></i>
                    </div>
                    <div class="stat-info">
                        <h3>Tổng Vật Tư</h3>
                        <p class="stat-number">${totalItems != null ? totalItems : 0}</p>
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
                        <p class="stat-number">${monthlyOrders != null ? monthlyOrders : 0}</p>
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
                        <p class="stat-number">${lowStockItems != null ? lowStockItems : 0}</p>
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
                        <p class="stat-number">${pendingDeliveries != null ? pendingDeliveries : 0}</p>
                        <p class="stat-change positive">
                            <i class="fas fa-arrow-up"></i> Đang Xử Lý
</p>
                    </div>
                </div>
            </div>
            --%>

                         <!-- Charts Grid -->
             <div class="charts-grid charts-grid-1" style="width: 100%; max-width: none; margin: 0; padding: 0;">
                 <div class="chart-card full-width" style="min-height: 600px; padding: 30px; width: 100%; margin: 0;">
                     <h3>Biểu Đồ Mua/Xuất Vật Tư Theo Tháng</h3>
                     <canvas id="importExportLineChart" style="height: 500px !important; width: 100% !important;"></canvas>
                 </div>
             </div>

            <!-- Low Stock Items Table -->
            <%--
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
                        <c:if test="${empty lowStockItemsList}">
                            <tr>
                                <td colspan="4" style="text-align: center;">Không có vật tư nào sắp hết.</td>
                            </tr>
                        </c:if>
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
            --%>

            <!-- Recent Transactions Table -->
            <%--
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
                        <c:if test="${empty recentTransactions}">
                            <tr>
                                <td colspan="5" style="text-align: center;">Không có giao dịch gần đây nào.</td>
                            </tr>
                        </c:if>
                        <c:forEach var="transaction" items="${recentTransactions}">
                            <tr>
                                <td>${transaction.id}</td>
                                <td>${transaction.materialName}</td>
                                <td>${transaction.type}</td>
                                <td>${transaction.quantity}</td>
                                <td><fmt:formatDate value="${transaction.date}" pattern="dd/MM/yyyy HH:mm"/></td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
            </div>
            --%>

            <!-- Include Footer -->
            <jsp:include page="footer.jsp" />
        </div>

        <!-- Chart Data -->
        <script>
            var requestLabels = ["Mua Vật Tư", "Xuất Kho", "Sửa Chữa"];
            var requestData = [<c:out value="${requestStats.purchaseCount}" default="0"/>,
            <c:out value="${requestStats.outgoingCount}" default="0"/>,
            <c:out value="${requestStats.repairCount}" default="0"/>];
            var costLabels = ["01/06", "04/06", "08/06", "12/06"];
            var costData = [<c:out value="${costTrend[0]}" default="0"/>,
            <c:out value="${costTrend[1]}" default="0"/>,
            <c:out value="${costTrend[2]}" default="0"/>,
            <c:out value="${costTrend[3]}" default="0"/>];
var categoryLabels = [<c:forEach var="item" items="${categoryStats}" varStatus="loop">"${item.categoryName}"<c:if test="${!loop.last}">,</c:if></c:forEach>];
            var categoryData = [<c:forEach var="item" items="${categoryStats}" varStatus="loop">${item.itemCount}<c:if test="${!loop.last}">,</c:if></c:forEach>];
                    var inventoryTrendLabels = [<c:forEach var="label" items="${inventoryTrendLabels}" varStatus="loop">"${label}"<c:if test="${!loop.last}">,</c:if></c:forEach>];
            var inventoryTrendData = [<c:forEach var="value" items="${inventoryTrend}" varStatus="loop">${value}<c:if test="${!loop.last}">,</c:if></c:forEach>];
        </script>
        <script>
            const contextPath = "${pageContext.request.contextPath}";

            document.addEventListener('DOMContentLoaded', () => {
                const bell = document.getElementById('notificationBell');
                const dropdown = document.getElementById('notificationDropdown');
                const ul = dropdown?.querySelector('ul');
                const badge = document.querySelector('.badge');

                bell?.addEventListener('click', e => {
                    e.stopPropagation();
                    dropdown.style.display = dropdown.style.display === 'block' ? 'none' : 'block';
                });

                document.addEventListener('click', () => dropdown.style.display = 'none');
                dropdown?.addEventListener('click', e => e.stopPropagation());

                function updateNotifications() {
                    fetch('getNotifications')
                            .then(res => res.json())
                            .then(data => {
                                data.sort((a, b) => {
                                    if (a.read !== b.read)
                                        return a.read ? 1 : -1;
                                    return new Date(b.createdAt) - new Date(a.createdAt);
                                });
                                badge.style.display = data.length ? 'block' : 'none';
                                badge.textContent = data.length || '';
                                ul.innerHTML = data.length ? '' : '<li style="padding:8px;color:#666;">Không có thông báo</li>';

                                data.forEach(n => {
                                    const li = document.createElement('li');
                                    li.style = `padding:8px 0;border-bottom:1px solid #eee;${n.read ? 'opacity:0.7;' : ''}`;

                                    const a = document.createElement('a');
                                    let linkUrl = "#";
                                    
                                    // Xử lý link dựa trên loại thông báo
                                    if (n.notificationType === 'purchase_order' || (n.message && n.message.includes('đơn mua'))) {
                                        // Thông báo về purchase order
                                        if (n.message && n.message.includes('bị từ chối')) {
                                            // Nếu bị từ chối, link đến editPurchaseOrder
                                            linkUrl = 'editPurchaseOrder?id=' + n.requestId;
                                        } else {
                                            // Nếu được duyệt, link đến purchaseOrderDetail
                                            linkUrl = 'purchaseOrderDetail?id=' + n.requestId;
                                        }
                                    } else {
                                        // Thông báo về request
                                        if (n.message && n.message.includes('bị từ chối')) {
                                            linkUrl = 'editRequest?requestId=' + n.requestId;
                                        } else {
                                            linkUrl = n.link || "#";
                                        }
                                    }
                                    
                                    a.href = linkUrl;
                                    a.style = 'color:#007bff;text-decoration:none;display:block;';
                                    a.onclick = e => {
                                        e.preventDefault();
                                        let markReadUrl = '';
                                        
                                        // Xử lý URL mark as read dựa trên loại thông báo
                                        if (n.notificationType === 'purchase_order' || (n.message && n.message.includes('đơn mua'))) {
                                            // Thông báo về purchase order
                                            markReadUrl = `markNotificationRead?notificationId=${n.id}&purchaseOrderId=${n.requestId}`;
                                        } else {
                                            // Thông báo về request
                                            markReadUrl = `markNotificationRead?notificationId=${n.id}&requestId=${n.requestId}`;
                                        }
                                        
                                        fetch(markReadUrl)
                                                .then(() => location.href = a.href);
                                    };

                                    const message = document.createElement('div');
                                    message.textContent = n.message;
                                    message.style = 'font-weight:500;margin-bottom:4px';

                                    const bottom = document.createElement('div');
                                    bottom.style = 'display:flex;justify-content:space-between;align-items:center';

                                    const time = document.createElement('span');
                                    time.textContent = (n.createdAt && n.createdAt !== "0") ? n.createdAt : "";
                                    time.style = 'font-size:12px;color:gray';

                                    if (!n.read) {
                                        const newBadge = document.createElement('span');
                                        newBadge.textContent = 'Mới';
                                        newBadge.style = 'background:#007bff;color:white;padding:2px 6px;border-radius:10px;font-size:10px;';
                                        bottom.appendChild(newBadge);
                                    }

                                    bottom.appendChild(time);
                                    a.append(message, bottom);
                                    li.appendChild(a);
                                    ul.appendChild(li);
                                });
                            })
                            .catch(err => console.error("Lỗi khi cập nhật thông báo:", err));
                }

                updateNotifications();
                setInterval(updateNotifications, 5000);
            });
        </script>

        <script>
            var importExportMonthLabels = [<c:forEach var="label" items="${importExportMonthLabels}" varStatus="loop">"${label}"<c:if test="${!loop.last}">,</c:if></c:forEach>];
            var importByMonth = <c:out value="${importByMonthJson}" default="[]"/>;
            var exportByMonth = <c:out value="${exportByMonthJson}" default="[]"/>;
            var ctxImportExport = document.getElementById('importExportLineChart').getContext('2d');
            var importExportLineChart = new Chart(ctxImportExport, {
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
        </script>
        <script src="js/adminDashboard.js"></script>
    </body>
</html>