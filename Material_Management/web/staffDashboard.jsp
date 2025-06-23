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
        <div class="sidebar">
            <div class="sidebar-header">
                <div class="logo-container" id="sidebarToggle">
                    <i class="fas fa-bars menu-icon"></i>
                    <span class="logo-text">Danh Mục</span>
                </div>
            </div>
            <ul class="sidebar-menu">
                <li class="menu-item">
                    <a href="${pageContext.request.contextPath}/UserDetailServlet?userId=${sessionScope.userId}" class="menu-link">
                        <i class="fas fa-tachometer-alt menu-icon"></i>
                        <span class="menu-text">Thông tin người dùng</span>
                    </a>
                </li>
                <li class="menu-item">
                    <a href="${pageContext.request.contextPath}/MaterialListServlet" class="menu-link">
                        <i class="fas fa-clipboard-list menu-icon"></i>
                        <span class="menu-text">Danh Sách Vật Tư</span>
                    </a>
                </li>
                <li class="menu-item">
                    <a href="${pageContext.request.contextPath}/suppliers" class="menu-link">
                        <i class="fas fa-shopping-cart menu-icon"></i>
                        <span class="menu-text">Nhà Cung Cấp</span>
                    </a>
                </li>
                <li class="menu-item">
                    <a href="${pageContext.request.contextPath}/createRequest" class="menu-link">
                        <i class="fas fa-warehouse menu-icon"></i>
                        <span class="menu-text">Tạo yêu cầu</span>
                    </a>
                </li>
                <li class="menu-item">
                    <a href="${pageContext.request.contextPath}/RequestListServlet" class="menu-link">
                        <i class="fas fa-truck menu-icon"></i>
                        <span class="menu-text">Lịch sử yêu cầu</span>
                    </a>
                </li>
                <li class="menu-item">
                    <a href="homepage.jsp" class="menu-link">
                        <i class="fas fa-sign-out-alt menu-icon"></i>
                        <span class="menu-text">Đăng Xuất</span>
                    </a>
                </li>
            </ul>
        </div>
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
                    <div class="notification relative" id="notificationBell" style="cursor:pointer;">
                        <i class="fas fa-bell text-gray-500 hover:text-primary-600 cursor-pointer text-xl"></i>
                        <c:if test="${not empty notifications}">
                            <span class="absolute -top-2 -right-2 bg-red-500 text-white text-xs rounded-full w-5 h-5 flex items-center justify-center">
                                ${fn:length(notifications)}
                            </span>
                        </c:if>
                        <div id="notificationDropdown" style="display:none;position:absolute;right:0;top:30px;z-index:1000;background:#fff;border:1px solid #ccc;width:350px;box-shadow:0 2px 5px rgba(0,0,0,0.1);border-radius:8px;">
                            <div style="padding:15px;border-bottom:1px solid #eee;background:#f8f9fa;border-radius:8px 8px 0 0;">
                                <h3 style="margin:0;font-size:16px;color:#333;font-weight:600;">Yêu cầu bị từ chối</h3>
                            </div>
                            <div style="max-height:400px;overflow-y:auto;">
                                <ul style="list-style:none;padding:0;margin:0;">
                                    <c:if test="${empty notifications}">
                                        <li style="padding:15px;color:#666;text-align:center;">
                                            <i class="fas fa-check-circle" style="font-size:24px;color:#28a745;margin-bottom:8px;"></i>
                                            <p style="margin:0;">Không có yêu cầu nào bị từ chối</p>
                                        </li>
                                    </c:if>
                                    <c:forEach var="noti" items="${notifications}">
                                        <li style="padding:15px;border-bottom:1px solid #eee;transition:all 0.3s ease;cursor:pointer;"
                                            onclick="window.location.href = 'editRequest?requestId=${noti.requestId}'">
                                            <div style="margin-bottom:8px;">
                                                <div style="display:flex;align-items:center;margin-bottom:5px;">
                                                    <i class="fas fa-times-circle" style="color:#dc3545;margin-right:8px;"></i>
                                                    <span style="flex:1;font-weight:600;color:#000;">${noti.message}</span>
                                                </div>
                                                <div style="font-size:12px;color:#000;font-weight:500;">
                                                    <i class="far fa-clock" style="margin-right:4px;"></i>
                                                    ${noti.createdAt}
                                                </div>
                                            </div>
                                        </li>
                                    </c:forEach>
                                </ul>
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
                <div class="date-filter">
                    <form action="staffDashboard" method="get" class="filter-form">
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
            <%--
            <div class="charts-grid">
                <div class="chart-card">
                    <h3>Phân Bổ Yêu Cầu</h3>
                    <canvas id="requestDistributionChart"></canvas>
                </div>
                <div class="chart-card">
                    <h3>Xu Hướng Chi Phí</h3>
                    <canvas id="costTrendChart"></canvas>
                </div>
                <div class="chart-card">
                    <h3>Phân Bổ Vật Tư Theo Danh Mục</h3>
                    <canvas id="materialCategoryChart"></canvas>
                </div>
                <div class="chart-card">
                    <h3>Xu Hướng Tồn Kho</h3>
                    <canvas id="inventoryTrendChart"></canvas>
                </div>
                <div class="calendar-card">
                    <h3>Lịch</h3>
                    <div class="calendar-container">
                        <div class="calendar-header">
                            <span class="month-year">
                                <span class="month" id="currentMonth"></span>
                                <span class="year" id="currentYear"></span>
                            </span>
                        </div>
                        <table class="calendar-table" id="calendarTable">
                            <thead>
                                <tr>
                                    <th>Su</th>
                                    <th>Mo</th>
                                    <th>Tu</th>
                                    <th>We</th>
                                    <th>Th</th>
                                    <th>Fr</th>
                                    <th>Sa</th>
                                </tr>
                            </thead>
                            <tbody>
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>
            --%>

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
        <%--
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
        --%>
        <script>
            document.getElementById("notificationBell").addEventListener("click", function (e) {
                e.stopPropagation();
                const dropdown = document.getElementById("notificationDropdown");
                dropdown.style.display = dropdown.style.display === "none" ? "block" : "none";
            });

            document.addEventListener("click", function () {
                const dropdown = document.getElementById("notificationDropdown");
                if (dropdown)
                    dropdown.style.display = "none";
            });
        </script>

        <script src="js/adminDashboard.js"></script>
        <script src="js/sidebar.js"></script>
    </body>
</html>