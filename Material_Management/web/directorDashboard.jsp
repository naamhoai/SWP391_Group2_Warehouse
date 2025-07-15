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

            <!-- Dashboard filter -->
            <div class="dashboard-header">
                <h1>Tổng Quan</h1>
                <div class="date-filter">
                    <form action="dashboard" method="get" class="filter-form">
                        <button type="submit" name="timeFilter" value="today" class="filter-btn ${timeFilter == 'today' ? 'active' : ''}">Hôm Nay</button>
                        <button type="submit" name="timeFilter" value="week" class="filter-btn ${timeFilter == 'week' ? 'active' : ''}">Tuần</button>
                        <button type="submit" name="timeFilter" value="month" class="filter-btn ${timeFilter == 'month' ? 'active' : ''}">Tháng</button>
                        <button type="submit" name="timeFilter" value="year" class="filter-btn ${timeFilter == 'year' ? 'active' : ''}">Năm</button>
                    </form>
                </div>
            </div>

            <!-- Dashboard stats -->
            <div class="stats-grid">
                <div class="stat-card">
                    <div class="stat-icon" style="background-color: #6dbdf2;"><i class="fas fa-box"></i></div>
                    <div class="stat-info">
                        <h3>Tổng Vật Tư</h3>
                        <p class="stat-number">${totalItems}</p>
                        <p class="stat-change positive"><i class="fas fa-arrow-up"></i> Đã Cập Nhật</p>
                    </div>
                </div>

                <div class="stat-card">
                    <div class="stat-icon" style="background-color: #2ecc71;"><i class="fas fa-shopping-cart"></i></div>
                    <div class="stat-info">
                        <h3>Yêu Cầu</h3>
                        <p class="stat-number">${monthlyOrders}</p>
                        <p class="stat-change positive"><i class="fas fa-arrow-up"></i>
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
                    <div class="stat-icon" style="background-color: #e74c3c;"><i class="fas fa-exclamation-triangle"></i></div>
                    <div class="stat-info">
                        <h3>Vật Tư Sắp Hết</h3>
                        <p class="stat-number">${lowStockItems}</p>
                        <p class="stat-change negative"><i class="fas fa-arrow-down"></i> Cần Chú Ý</p>
                    </div>
                </div>

                <div class="stat-card">
                    <div class="stat-icon" style="background-color: #f1c40f;"><i class="fas fa-truck"></i></div>
                    <div class="stat-info">
                        <h3>Đang Giao</h3>
                        <p class="stat-number">${pendingDeliveries}</p>
                        <p class="stat-change positive"><i class="fas fa-arrow-up"></i> Đang Xử Lý</p>
                    </div>
                </div>
            </div>

            <!-- Table low stock -->
            <div class="table-card">
                <h3>Vật Tư Sắp Hết</h3>
                <table>
                    <thead>
                        <tr><th>Vật Tư</th><th>Tồn Kho</th><th>Tồn Tối Thiểu</th><th>Trạng Thái</th></tr>
                    </thead>
                    <tbody>
                        <c:forEach items="${lowStockItemsList}" var="item">
                            <tr>
                                <td>${item.name}</td>
                                <td>${item.quantity}</td>
                                <td>${item.minStock}</td>
                                <td><span class="status-badge ${item.status == 'Critical' ? 'danger' : 'warning'}">${item.status}</span></td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
            </div>

            <!-- Inventory summary -->
            <div class="inventory-summary">
                <h3>Tổng Quan Kho</h3>
                <div class="inventory-stats">
                    <div class="inventory-stat"><span class="label">Còn Hàng:</span><span class="value">${inventoryStats.inStock}</span></div>
                    <div class="inventory-stat"><span class="label">Sắp Hết:</span><span class="value">${inventoryStats.lowStock}</span></div>
                    <div class="inventory-stat"><span class="label">Hết Hàng:</span><span class="value">${inventoryStats.outOfStock}</span></div>
                </div>
            </div>

            <!-- Charts -->
            <div class="charts-grid">
                <div class="chart-card"><h3>Phân Bổ Vật Tư Theo Danh Mục</h3><canvas id="materialCategoryChart"></canvas></div>
                <div class="chart-card"><h3>Xu Hướng Tồn Kho</h3><canvas id="inventoryTrendChart"></canvas></div>
            </div>

            <!-- Recent transactions -->
            <div class="table-card">
                <h3>Các Giao Dịch Gần Đây</h3>
                <table>
                    <thead>
                        <tr><th>Mã Giao Dịch</th><th>Vật Tư</th><th>Loại</th><th>Số Lượng</th><th>Ngày</th></tr>
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
                            <tr><td colspan="5" style="text-align: center;">Không có giao dịch gần đây nào.</td></tr>
                        </c:if>
                    </tbody>
                </table>
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
        </script>

    </body>
</html>
