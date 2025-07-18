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
            <jsp:include page="footer.jsp" />
        </div>
        <script src="js/directorDashboard.js"></script>
        <script>
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
