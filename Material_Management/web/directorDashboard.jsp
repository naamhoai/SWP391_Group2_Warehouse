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
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/css/all.min.css">
        <script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
    </head>
    <body>
        <jsp:include page="sidebar.jsp" />

        <div id="main-content">
            <!-- Welcome header -->
            <div class="welcome-header">
                <div class="welcome-text">
                    Xin chào, <strong>${user.fullname}</strong>
                    <c:if test="${not empty user and not empty user.role and not empty user.role.rolename}">
                        (<span style="font-weight:normal;">${user.role.rolename}</span>)
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
                        ${sessionScope.user.fullname != null ? sessionScope.user.fullname.charAt(0) : 'A'}
                    </div>
                </div>
            </div>

            <div class="dashboard-header">
                <h1>Bảng Điều Khiển Giám Đốc</h1>
            </div>
            <div class="stats-grid">
                <div class="stat-card">
                    <div class="stat-icon" style="background-color: #2563eb;"><i class="fas fa-box"></i></div>
                    <div class="stat-info">
                        <h3>Tổng Vật Tư</h3>
                        <p class="stat-number">${totalItems}</p>
                    </div>
                </div>
                <div class="stat-card">
                    <div class="stat-icon" style="background-color: #2ecc71;"><i class="fas fa-coins"></i></div>
                    <div class="stat-info">
                        <h3>Giá Trị Mua Tháng Này</h3>
                        <p class="stat-number"><fmt:formatNumber value="${totalPurchaseValueThisMonth}" type="number" maxFractionDigits="0"/> ₫</p>
                    </div>
                </div>
                <div class="stat-card">
                    <div class="stat-icon" style="background-color: #f1c40f;"><i class="fas fa-file-alt"></i></div>
                    <div class="stat-info">
                        <h3>Yêu Cầu Mua Tháng Này</h3>
                        <p class="stat-number">${totalPurchaseRequestsThisMonth}</p>
                    </div>
                </div>
                <div class="stat-card">
                    <div class="stat-icon" style="background-color: #8e44ad;"><i class="fas fa-check-circle"></i></div>
                    <div class="stat-info">
                        <h3>Đơn Đã Duyệt Tháng Này</h3>
                        <p class="stat-number">${totalApprovedRequestsThisMonth}</p>
                    </div>
                </div>
            </div>
            <div class="charts-grid" style="grid-template-columns: 1fr;">
                <div class="chart-card">
                    <h3>Biểu Đồ Số Lượng Yêu Cầu Mua Theo Tháng</h3>
                    <canvas id="requestMonthChart"></canvas>
                </div>
            </div>
            <div class="charts-grid" style="grid-template-columns: 1fr;">
                <div class="chart-card">
                    <h3>Biểu Đồ Giá Trị Mua Hàng Theo Tháng</h3>
                    <canvas id="purchaseValueChart"></canvas>
                </div>
            </div>

            <!-- Bảng thống kê - 2 bảng trên 1 dòng -->
            <div class="stats-tables-row">
                <!-- Bảng thống kê số lượng yêu cầu mua theo tháng -->
                <div class="stats-table-container">
                    <div class="table-card">
                        <h3><i class="fas fa-chart-bar"></i> Bảng Thống Kê Số Lượng Yêu Cầu Mua Theo Tháng</h3>
                        <div class="table-responsive">
                            <table class="stats-table">
                                <thead>
                                    <tr>
                                        <th>Tháng</th>
                                        <th>Số lượng yêu cầu mua</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <c:forEach var="item" items="${requestCountByMonth}">
                                        <tr>
                                            <td>${item.month}</td>
                                            <td>${item.count}</td>
                                        </tr>
                                    </c:forEach>
                                    <c:if test="${empty requestCountByMonth}">
                                        <tr>
                                            <td colspan="2" class="no-data">Không có dữ liệu</td>
                                        </tr>
                                    </c:if>
                                </tbody>
                            </table>
                        </div>
                    </div>
                </div>

                <!-- Bảng thống kê giá trị mua hàng theo tháng -->
                <div class="stats-table-container">
                    <div class="table-card">
                        <h3><i class="fas fa-chart-line"></i> Bảng Thống Kê Giá Trị Mua Hàng Theo Tháng</h3>
                        <div class="table-responsive">
                            <table class="stats-table">
                                <thead>
                                    <tr>
                                        <th>Tháng</th>
                                        <th>Giá trị mua hàng (VNĐ)</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <c:forEach var="item" items="${purchaseValueByMonth}">
                                        <tr>
                                            <td>${item.month}</td>
                                            <td><fmt:formatNumber value="${item.value}" type="currency" currencySymbol="₫"/></td>
                                        </tr>
                                    </c:forEach>
                                    <c:if test="${empty purchaseValueByMonth}">
                                        <tr>
                                            <td colspan="2" class="no-data">Không có dữ liệu</td>
                                        </tr>
                                    </c:if>
                                </tbody>
                            </table>
                        </div>
                    </div>
                </div>
            </div>

            <jsp:include page="footer.jsp" />
        </div>
        <script src="js/directorDashboard.js"></script>
        <script>
            var contextPath = "${pageContext.request.contextPath}";

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
                            .then(r => r.json())
                            .then(data => {
                                data.sort((a, b) => {
                                    if (a.read !== b.read)
                                        return a.read ? 1 : -1;
                                    return new Date(b.createdAt) - new Date(a.createdAt);
                                });
                                badge.style.display = data.length ? 'block' : 'none';
                                badge.textContent = data.length;
                                ul.innerHTML = data.length ? '' : '<li style="padding:8px;color:#666">Không có thông báo</li>';

                                data.forEach(n => {
                                    const li = document.createElement('li');
                                    li.style.padding = '8px';
                                    li.style.borderBottom = '1px solid #eee';
                                    if (n.read)
                                        li.style.opacity = '0.7';

                                    const a = document.createElement('a');
                                    a.href = contextPath + '/viewRequestDetail?requestId=' + n.requestId;
                                    a.style = 'display:block;text-decoration:none;color:#007bff;';
                                    a.onclick = e => {
                                        e.preventDefault();
                                        fetch(`markNotificationRead?notificationId=${n.id}&requestId=${n.requestId}`)
                                                .then(() => location.href = a.href);
                                    };

                                    const message = document.createElement('div');
                                    message.textContent = n.message;
                                    message.style = 'font-weight:500;margin-bottom:4px';

                                    const bottom = document.createElement('div');
bottom.style = 'display:flex;justify-content:space-between;align-items:center';

                                    const time = document.createElement('span');
                                    time.textContent = n.createdAt || "";
                                    time.style = 'font-size:12px;color:gray';

                                    if (!n.read) {
                                        const newBadge = document.createElement('span');
                                        newBadge.textContent = 'Mới';
                                        newBadge.style = 'background:#007bff;color:white;padding:2px 6px;border-radius:10px;font-size:10px;';
                                        bottom.appendChild(newBadge);
                                    }

                                    bottom.appendChild(time);
                                    a.appendChild(message);
                                    a.appendChild(bottom);
                                    li.appendChild(a);
                                    ul.appendChild(li);
                                });
                            });
                }

                updateNotifications();
                setInterval(updateNotifications, 5000);
            });
            // Biểu đồ số lượng yêu cầu mua theo tháng
            var requestMonthLabels = [<c:forEach var="label" items="${requestMonthLabels}" varStatus="loop">"${label}"<c:if test="${!loop.last}">,</c:if></c:forEach>];
            var requestMonthValues = [<c:forEach var="v" items="${requestMonthValues}" varStatus="loop">${v}<c:if test="${!loop.last}">,</c:if></c:forEach>];
            var ctxRequestMonth = document.getElementById('requestMonthChart').getContext('2d');
            var requestMonthChart = new Chart(ctxRequestMonth, {
                type: 'bar',
                data: {
                    labels: requestMonthLabels,
                    datasets: [{
                        label: 'Số lượng yêu cầu mua',
                        data: requestMonthValues,
                        backgroundColor: '#2563eb',
                        borderWidth: 1
                    }]
                },
                options: {
                    responsive: true,
                    plugins: { legend: { display: false } },
                    scales: {
                        y: {
                            beginAtZero: true,
                            title: { display: true, text: 'Số lượng' }
                        }
                    }
                }
            });
            // Biểu đồ giá trị mua hàng theo tháng
            var purchaseMonthLabels = [<c:forEach var="label" items="${purchaseMonthLabels}" varStatus="loop">"${label}"<c:if test="${!loop.last}">,</c:if></c:forEach>];
            var purchaseMonthValues = [<c:forEach var="v" items="${purchaseMonthValues}" varStatus="loop">${v}<c:if test="${!loop.last}">,</c:if></c:forEach>];
            var ctxPurchaseValue = document.getElementById('purchaseValueChart').getContext('2d');
            var purchaseValueChart = new Chart(ctxPurchaseValue, {
                type: 'line',
                data: {
                    labels: purchaseMonthLabels,
                    datasets: [{
                        label: 'Giá trị mua hàng (₫)',
                        data: purchaseMonthValues,
                        borderColor: '#2ecc71',
                        backgroundColor: 'rgba(46,204,113,0.1)',
                        fill: true,
                        tension: 0.3
                    }]
                },
                options: {
                    responsive: true,
                    plugins: { legend: { display: true } },
                    scales: {
                        y: {
                            beginAtZero: true,
                            title: { display: true, text: 'Giá trị (₫)' }
                        }
                    }
                }
            });
        </script>

    </body>
</html>
