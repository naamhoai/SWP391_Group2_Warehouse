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
                        ${user.fullname != null ? user.fullname.charAt(0) : 'A'}
                    </div>
                </div>
            </div>
            <div class="dashboard-header">
                <h1>Báo cáo tổng hợp giám đốc</h1>
            </div>
            <div class="stats-grid" style="display: grid; grid-template-columns: repeat(2, 1fr); gap: 28px; margin-bottom: 32px;">
                <a href="exportFormHistory" style="text-decoration:none;color:inherit;">
                    <div class="stat-card">
                        <div class="stat-icon" style="background-color: #2563eb;"><i class="fas fa-list"></i></div>
                        <div class="stat-info">
                            <h3>Tổng số đơn xuất kho</h3>
                            <p class="stat-number">${totalExportForms}</p>
                        </div>
                    </div>
                </a>
                <a href="purchaseOrderList" style="text-decoration:none;color:inherit;">
                    <div class="stat-card">
                        <div class="stat-icon" style="background-color: #00b894;"><i class="fas fa-shopping-cart"></i></div>
                        <div class="stat-info">
                            <h3>Tổng số đơn mua hàng</h3>
                            <p class="stat-number">${totalPurchaseOrders}</p>
                        </div>
                    </div>
                </a>
            </div>
            <div class="stats-grid" style="display: grid; grid-template-columns: repeat(2, 1fr); gap: 28px; margin-bottom: 32px;">
                <a href="MaterialListServlet" style="text-decoration:none;color:inherit;">
                    <div class="stat-card">
                        <div class="stat-icon" style="background-color: #f1c40f;"><i class="fas fa-box"></i></div>
                        <div class="stat-info">
                            <h3>Tổng vật tư</h3>
                            <p class="stat-number">${totalItems}</p>
                        </div>
                    </div>
                </a>
                <a href="purchaseOrderList" style="text-decoration:none;color:inherit;">
                    <div class="stat-card">
                        <div class="stat-icon" style="background-color: #8e44ad;"><i class="fas fa-coins"></i></div>
                        <div class="stat-info">
                            <h3>Tổng giá trị đã mua vật tư</h3>
                            <fmt:formatNumber value="${totalPurchaseValue}" type="number" groupingUsed="true" maxFractionDigits="0" var="formattedTotal"/>
                            <p class="stat-number">${fn:replace(formattedTotal, ',', '.')} VND</p>
                        </div>
                    </div>
                </a>
            </div>
            <%-- 2 phần dưới --%>
            <div style="margin-bottom: 32px;">
                <form method="get" action="director" class="chart-filter-form" style="margin-bottom: 18px; display: flex; gap: 12px; align-items: center; background: #f8f9fa; padding: 16px; border-radius: 8px; box-shadow: 0 2px 4px rgba(0,0,0,0.1);">
                    <input type="hidden" name="chartType" value="combined"/>
                    <label for="startMonth" style="font-weight: 500; color: #333;">Từ tháng:</label>
                    <select id="startMonth" name="startMonth" style="padding: 8px 12px; border: 1px solid #ddd; border-radius: 4px; background: white; min-width: 80px;">
                        <c:forEach var="month" begin="1" end="12">
                            <option value="${month < 10 ? '0' : ''}${month}" ${param.startMonth == (month < 10 ? '0' : '') + month || (empty param.startMonth && month == 1) ? 'selected' : ''}>Tháng ${month}</option>
                        </c:forEach>
                    </select>
                    <select id="startYear" name="startYear" style="padding: 8px 12px; border: 1px solid #ddd; border-radius: 4px; background: white; min-width: 80px;">
                        <c:forEach var="year" begin="2020" end="2030">
                            <option value="${year}" ${param.startYear == year || (empty param.startYear && year == 2025) ? 'selected' : ''}>${year}</option>
                        </c:forEach>
                    </select>
                    <label for="endMonth" style="font-weight: 500; color: #333;">Đến tháng:</label>
                    <select id="endMonth" name="endMonth" style="padding: 8px 12px; border: 1px solid #ddd; border-radius: 4px; background: white; min-width: 80px;">
                        <c:forEach var="month" begin="1" end="12">
                            <option value="${month < 10 ? '0' : ''}${month}" ${param.endMonth == (month < 10 ? '0' : '') + month || (empty param.endMonth && month == 7) ? 'selected' : ''}>Tháng ${month}</option>
                        </c:forEach>
                    </select>
                    <select id="endYear" name="endYear" style="padding: 8px 12px; border: 1px solid #ddd; border-radius: 4px; background: white; min-width: 80px;">
                        <c:forEach var="year" begin="2020" end="2030">
                            <option value="${year}" ${param.endYear == year || (empty param.endYear && year == 2025) ? 'selected' : ''}>${year}</option>
                        </c:forEach>
                    </select>
                    <button type="submit" style="background: linear-gradient(135deg, #667eea 0%, #764ba2 100%); color: white; border: none; padding: 10px 20px; border-radius: 6px; font-weight: 500; cursor: pointer; transition: all 0.3s ease; box-shadow: 0 2px 4px rgba(0,0,0,0.2);">
                        <i class="fas fa-search" style="margin-right: 6px;"></i>Xem
                    </button>
                </form>
            <!-- BẢNG MUA/XUẤT THEO THÁNG GIỐNG ADMIN -->
            <div class="table-card">
                <h3>Bảng Mua/Xuất Vật Tư Theo Tháng</h3>
                <table>
                    <thead><tr><th>Tháng</th><th>Số Lượng Mua</th><th>Số Lượng Xuất</th></tr></thead>
                    <tbody>
                        <c:choose>
                            <c:when test="${not empty exportMonthLabels or not empty purchaseMonthLabels}">
                                <c:if test="${not empty exportMonthLabels}">
                                    <c:forEach var="i" begin="0" end="${exportMonthLabels.size() - 1}">
                                        <tr>
                                            <td>${exportMonthLabels[i]}</td>
                                            <td>
                                                <c:choose>
                                                    <c:when test="${i < purchaseMonthLabels.size() && purchaseMonthLabels[i] == exportMonthLabels[i]}">
                                                        ${purchaseMonthValues[i]}
                                                    </c:when>
                                                    <c:otherwise>0</c:otherwise>
                                                </c:choose>
                                            </td>
                                            <td>${exportMonthValues[i]}</td>
                                        </tr>
                                    </c:forEach>
                                </c:if>
                                <c:if test="${not empty purchaseMonthLabels}">
                                    <c:forEach var="i" begin="0" end="${purchaseMonthLabels.size() - 1}">
                                        <c:set var="found" value="false"/>
                                        <c:if test="${not empty exportMonthLabels}">
                                            <c:forEach var="j" begin="0" end="${exportMonthLabels.size() - 1}">
                                                <c:if test="${purchaseMonthLabels[i] == exportMonthLabels[j]}">
                                                    <c:set var="found" value="true"/>
                                                </c:if>
                                            </c:forEach>
                                        </c:if>
                                        <c:if test="${not found}">
                                            <tr>
                                                <td>${purchaseMonthLabels[i]}</td>
                                                <td>${purchaseMonthValues[i]}</td>
                                                <td>0</td>
                                            </tr>
                                        </c:if>
                                    </c:forEach>
                                </c:if>
                            </c:when>
                            <c:otherwise>
                                <tr>
                                    <td colspan="3" style="text-align: center;">Không có dữ liệu.</td>
                                </tr>
                            </c:otherwise>
                        </c:choose>
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
                                    // Xử lý link dựa trên loại thông báo
                                    let link = '';
                                    let markReadUrl = '';
                                    
                                    if (n.notificationType === 'purchase_order' || (n.message && n.message.includes('đơn mua'))) {
                                        // Thông báo về purchase order
                                        link = contextPath + '/purchaseOrderDetail?id=' + n.requestId;
                                        markReadUrl = `markNotificationRead?notificationId=${n.id}&purchaseOrderId=${n.requestId}`;
                                    } else {
                                        // Thông báo về request
                                        link = contextPath + '/viewRequestDetail?requestId=' + n.requestId;
                                        markReadUrl = `markNotificationRead?notificationId=${n.id}&requestId=${n.requestId}`;
                                    }
                                    
                                    a.href = link;
                                    a.style = 'display:block;text-decoration:none;color:#007bff;';
                                    a.onclick = e => {
                                        e.preventDefault();
                                        fetch(markReadUrl)
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

