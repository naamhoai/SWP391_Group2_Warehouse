<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Warehouse Staff Dashboard</title>
        <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" rel="stylesheet">
        <link href="css/warehouseStaffDashboard.css" rel="stylesheet" type="text/css"/>
    </head>
    <body>
        <jsp:include page="header.jsp"/>
        
        <div class="main-content">
            <div class="dashboard-container">
                <!-- Stats Grid -->
                <div class="stats-grid">
                    <div class="stat-card orange">
                        <div class="stat-icon"><i class="fas fa-inbox"></i></div>
                        <div class="stat-info">
                            <div class="stat-title">Yêu Cầu Chờ Xử Lý</div>
                            <div class="stat-number">${pendingRequests}</div>
                            <div class="stat-desc">Cần bạn thực hiện ngay</div>
                        </div>
                    </div>
                    <div class="stat-card green">
                        <div class="stat-icon"><i class="fas fa-truck-loading"></i></div>
                        <div class="stat-info">
                            <div class="stat-title">Đơn Hàng Sắp Về</div>
                            <div class="stat-number">${incomingOrders}</div>
                            <div class="stat-desc">Cần chuẩn bị nhận hàng</div>
                        </div>
                    </div>
                    <div class="stat-card red">
                        <div class="stat-icon"><i class="fas fa-exclamation-triangle"></i></div>
                        <div class="stat-info">
                            <div class="stat-title">Vật Tư Tồn Kho Thấp</div>
                            <div class="stat-number">${lowStockItems}</div>
                            <div class="stat-desc">Cần được chú ý</div>
                        </div>
                    </div>
                    <div class="stat-card purple">
                        <div class="stat-icon"><i class="fas fa-exchange-alt"></i></div>
                        <div class="stat-info">
                            <div class="stat-title">Giao Dịch Hôm Nay</div>
                            <div class="stat-number">${dailyTransactions}</div>
                            <div class="stat-desc">Đã hoàn thành</div>
                        </div>
                    </div>
                </div>
                <!-- Charts Grid (Static Table) -->
                <div class="charts-grid">
                    <div class="chart-card">
                        <div class="chart-title">Tỷ Lệ Nhập/Xuất (Tuần này)</div>
                        <table class="simple-table">
                            <thead>
                                <tr><th>Loại</th><th>Số Lượng</th><th>Tỷ Lệ</th></tr>
                            </thead>
                            <tbody>
                                <tr>
                                    <td><i class="fas fa-arrow-down" style="color:#4CAF50"></i> Nhập Kho</td>
                                    <td>${inStock}</td>
                                    <td>${inStockPercentage}%</td>
                                </tr>
                                <tr>
                                    <td><i class="fas fa-arrow-up" style="color:#f44336"></i> Xuất Kho</td>
                                    <td>${outStock}</td>
                                    <td>${outStockPercentage}%</td>
                                </tr>
                            </tbody>
                        </table>
                    </div>
                    <div class="chart-card">
                        <div class="chart-title">Hoạt Động Kho Theo Giờ (Hôm Nay)</div>
                        <table class="simple-table">
                            <thead>
                                <tr><th>Khung Giờ</th><th>Số Giao Dịch</th></tr>
                            </thead>
                            <tbody>
                                <c:forEach items="${hourlyTransactionsList}" var="transaction">
                                    <tr>
                                        <td>${transaction.timeSlot}</td>
                                        <td>${transaction.count}</td>
                                    </tr>
                                </c:forEach>
                            </tbody>
                        </table>
                    </div>
                </div>
                <!-- Tables -->
                <div class="tables-grid">
                    <div class="table-card">
                        <div class="table-title">Danh Sách Yêu Cầu Cần Thực Hiện</div>
                        <div class="table-scroll">
                            <table class="simple-table">
                                <thead>
                                    <tr>
                                        <th>Mã YC</th>
                                        <th>Loại</th>
                                        <th>Người Yêu Cầu</th>
                                        <th>Ngày Yêu Cầu</th>
                                        <th>Hành Động</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <c:forEach items="${pendingRequestsList}" var="request">
                                        <tr>
                                            <td>#${request.id}</td>
                                            <td>${request.type}</td>
                                            <td>${request.requester}</td>
                                            <td>${request.requestDate}</td>
                                            <td><button class="btn blue">Xử Lý</button></td>
                                        </tr>
                                    </c:forEach>
                                </tbody>
                            </table>
                        </div>
                    </div>
                    <div class="table-card">
                        <div class="table-title">Danh Sách Vật Tư Tồn Kho Thấp</div>
                        <div class="table-scroll">
                            <table class="simple-table">
                                <thead>
                                    <tr>
                                        <th>Mã VT</th>
                                        <th>Tên Vật Tư</th>
                                        <th>Tồn Kho/Tối Thiểu</th>
                                        <th>Hành Động</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <c:forEach items="${lowStockItemsList}" var="item">
                                        <tr>
                                            <td>#${item.id}</td>
                                            <td>${item.name}</td>
                                            <td>${item.currentStock} / ${item.minimumStock}</td>
                                            <td><button class="btn orange">Y/C Mua</button></td>
                                        </tr>
                                    </c:forEach>
                                </tbody>
                            </table>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </body>
</html>