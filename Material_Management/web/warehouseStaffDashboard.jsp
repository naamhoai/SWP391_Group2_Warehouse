<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ page import="dao.NotificationDAO, dao.PurchaseOrderDAO, dao.ExportFormDAO, model.Notification, java.util.List" %>

<!DOCTYPE html>
<html lang="vi">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Dashboard Nhân Viên Kho</title>
        <link rel="stylesheet" href="css/warehouseStaffDashboard.css">
        <link rel="stylesheet" href="css/sidebar.css">
        <link rel="stylesheet" href="css/header.css"/>
        <link rel="stylesheet" href="css/footer.css"/>
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/css/all.min.css">
        <link href="https://fonts.googleapis.com/css2?family=Roboto:wght@400;500;700&display=swap" rel="stylesheet">
    </head>
    <body>
        <jsp:include page="sidebar_warehouse_staff.jsp" />
        <div id="main-content">
            <div class="header">
                <div class="header-left">
                    <span class="logo">Xin chào nhân viên kho</span>
                </div>
                <div class="header-right">
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
                    <span class="avatar-circle">
                        <c:choose>
                            <c:when test="${not empty sessionScope.user.fullname}">
                                ${fn:substring(sessionScope.user.fullname, 0, 1)}
                            </c:when>
                            <c:otherwise>N</c:otherwise>
                        </c:choose>
                    </span>
                </div>
            </div> 
            <div class="dashboard-header">
                <div class="header-left">
                    <h1>Bảng Điều Khiển Nhân Viên Kho</h1>
                </div>
            </div>
            <div class="stats-grid">
                <div class="stat-card">
                    <div class="stat-icon" style="background-color: #6dbdf2;">
                        <i class="fas fa-box"></i>
                    </div>
                    <div class="stat-info">
                        <h3>Tổng Vật Tư</h3>
                        <p class="stat-number"></p>
                    </div>
                </div>
                <div class="stat-card">
                    <div class="stat-icon" style="background-color: #10b981;">
                        <i class="fas fa-warehouse"></i>
                    </div>
                    <div class="stat-info">
                        <h3>Vật Tư Tồn Kho</h3>
                        <p class="stat-number"></p>
                    </div>
                </div>
                <div class="stat-card">
                    <div class="stat-icon" style="background-color: #e67e22;">
                        <i class="fas fa-exclamation-triangle"></i>
                    </div>
                    <div class="stat-info">
                        <h3>Cảnh Báo Sắp Hết</h3>
                        <p class="stat-number"></p>
                    </div>
                </div>
            </div>
            <div class="table-card">
                <h3>Yêu Cầu Xuất/ Nhập Kho Đã Phê Duyệt</h3>
                <table>
                    <thead>
                        <tr>
                            <th>Mã Yêu Cầu</th>
                            <th>Loại Yêu Cầu</th>
                            <th>Ngày</th>
                            <th>Người Phê Duyệt</th>
                            <th>Trạng Thái</th>
                        </tr>
                    </thead>
                    <tbody>

                    </tbody>
                </table>
            </div>
        </div>
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
                                    li.style = `padding:8px;border-bottom:1px solid #eee;${n.read ? 'opacity:0.7;' : ''}`;

                                    const a = document.createElement('a');
                                    a.href = contextPath + '/viewRequestDetail?requestId=' + n.requestId;
                                    a.style = 'display:block;text-decoration:none;color:#007bff;';
                                    a.onclick = e => {
                                        e.preventDefault();
                                        fetch(`markNotificationRead?notificationId=${n.id}&requestId=${n.requestId}`)
                                                .then(() => location.href = a.href);
                                    };

                                    const msg = document.createElement('div');
                                    msg.textContent = n.message;
                                    msg.style = 'font-weight:500;margin-bottom:4px';

                                    const bottom = document.createElement('div');
                                    bottom.style = 'display:flex;justify-content:space-between;align-items:center';

                                    const time = document.createElement('span');
                                    time.textContent = n.createdAt || "";
                                    time.style = 'font-size:12px;color:gray';

                                    if (!n.read) {
                                        const badgeNew = document.createElement('span');
                                        badgeNew.textContent = 'Mới';
                                        badgeNew.style = 'background:#007bff;color:white;padding:2px 6px;border-radius:10px;font-size:10px;';
                                        bottom.appendChild(badgeNew);
                                    }

                                    bottom.appendChild(time);
                                    a.append(msg, bottom);
                                    li.appendChild(a);
                                    ul.appendChild(li);
                                });
                            })
                            .catch(err => console.error("Lỗi khi lấy thông báo:", err));
                }

                updateNotifications();
                setInterval(updateNotifications, 5000);
            });
        </script>

    </body>

</html> 
