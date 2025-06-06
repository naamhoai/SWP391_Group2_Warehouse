<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="vi">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Tổng quan Giám đốc - Hệ thống Quản lý Vật tư</title>

        <!-- Chart.js -->
        <script src="https://cdn.jsdelivr.net/npm/chart.js@4.4.3/dist/chart.umd.min.js"></script>
        <!-- Toastify -->
        <link href="https://cdn.jsdelivr.net/npm/toastify-js/src/toastify.min.css" rel="stylesheet">
        <script src="https://cdn.jsdelivr.net/npm/toastify-js"></script>
        <!-- Font Awesome -->
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
        <!-- Custom CSS -->
        <link rel="stylesheet" href="../css/directionDashboard.css">
        <link rel="stylesheet" href="../css/sidebar.css"/>
        <link rel="stylesheet" href="../css/footer.css"/>
        
        
    </head>
    <body>
        <!-- Sidebar -->
        <%@include file="../sidebar.jsp" %>

        <!-- Main Content -->
        <main>
            <!-- Header -->
            <header>
                <div class="header-left">
                    <button id="toggleSidebarMobile">
                        <i class="fas fa-bars"></i>
                    </button>
                    <div>
                        <h1>Tổng quan Giám đốc</h1>
                        <p>Thống kê và phê duyệt yêu cầu vật tư</p>
                    </div>
                </div>
                <div class="header-right">
                    <div class="notification">
                        <i class="fas fa-bell"></i>
                        <span class="badge">3</span>
                    </div>
                    <div class="user-info">
                        <img src="https://ui-avatars.com/api/?name=Giám đốc&background=3b82f6&color=fff" alt="Giám đốc">
                        <span>Giám đốc</span>
                    </div>
                    <button id="toggleDarkMode">
                        <i class="fas fa-moon"></i>
                    </button>
                </div>
            </header>

            <!-- Dashboard Stats -->
            <div class="stats-grid">
                <div class="card animate-fadeInUp">
                    <div class="card-content">
                        <div>
                            <p>Tổng vật tư</p>
                        </div>
                        <div class="icon-box">
                            <i class="fas fa-boxes"></i>
                        </div>
                    </div>
                    <div class="card-footer">
                        <a href="${pageContext.request.contextPath}/inventoryReport.jsp">Xem báo cáo</a>
                    </div>
                </div>
                <div class="card animate-fadeInUp delay-100">
                    <div class="card-content">
                        <div>
                            <p>Yêu cầu chờ duyệt</p>
                            <h3>5</h3>
                            <p class="trend-warning"><i class="fas fa-clock"></i>Đang chờ xử lý</p>
                        </div>
                        <div class="icon-box">
                            <i class="fas fa-clipboard-list"></i>
                        </div>
                    </div>
                    <div class="card-footer">
                        <a href="${pageContext.request.contextPath}/pendingRequests.jsp">Duyệt yêu cầu</a>
                    </div>
                </div>
                <div class="card animate-fadeInUp delay-200">
                    <div class="card-content">
                        <div>
                            <p>Chi phí kỳ gần nhất</p>
                            <p class="trend-info"><i class="fas fa-dollar-sign"></i>Mua & Sửa chữa</p>
                        </div>
                        <div class="icon-box">
                            <i class="fas fa-money-bill-wave"></i>
                        </div>
                    </div>
                    <div class="card-footer">
                        <a href="${pageContext.request.contextPath}/costReport.jsp">Xem báo cáo</a>
                    </div>
                </div>
                <div class="card animate-fadeInUp delay-300">
                    <div class="card-content">
                        <div>
                            <p>Tồn kho thấp</p>
                            <p class="trend-danger"><i class="fas fa-exclamation-circle"></i>Cần bổ sung</p>
                        </div>
                        <div class="icon-box">
                            <i class="fas fa-exclamation"></i>
                        </div>
                    </div>
                    <div class="card-footer">
                        <a href="${pageContext.request.contextPath}/lowInventoryReport.jsp">Xem chi tiết</a>
                    </div>
                </div>
            </div>

            <!-- Charts Row -->
            <div class="charts-grid">
                <div class="chart-container">
                    <div class="chart-header">
                        <div>
                            <h2>Xu hướng Xuất - Nhập - Tồn</h2>
                            <p>Theo dõi nhập/xuất kho theo thời gian</p>
                        </div>
                        <div class="chart-filters">
                            <button class="chart-filter active">Tuần</button>
                            <button class="chart-filter">Tháng</button>
                            <button class="chart-filter">Năm</button>
                        </div>
                    </div>
                    <canvas id="inventoryChart"></canvas>
                    <button onclick="exportToExcel('inventoryChart')" class="btn-primary">Xuất Excel</button>
                </div>
                <div class="chart-container">
                    <div class="chart-header">
                        <h2>Phân bố chi phí</h2>
                        <p>Chi phí mua và sửa chữa vật tư</p>
                    </div>
                    <canvas id="costChart"></canvas>
                    <button onclick="exportToExcel('costChart')" class="btn-primary">Xuất Excel</button>
                </div>
            </div>

            <!-- Request Management Table -->
            <div class="table-container">
                <div class="table-header">
                    <div>
                        <h2>Yêu cầu chờ duyệt</h2>
                        <p>Danh sách yêu cầu xuất/mua/sửa vật tư</p>
                    </div>
                    <a href="${pageContext.request.contextPath}/pendingRequests.jsp">Xem tất cả</a>
                </div>
                <div class="table-wrapper">
                    <table>
                        <thead>
                            <tr>
                                <th>Thời gian</th>
                                <th>Loại yêu cầu</th>
                                <th>Người yêu cầu</th>
                                <th>Nội dung</th>
                                <th>Hành động</th>
                            </tr>
                        </thead>
                        <tbody>
                            <tr>
                                <td>Yêu cầu xuất kho</td>
                                <td>
                                    <button onclick="approveRequest(1)" class="btn-primary">Phê duyệt</button>
                                    <button onclick="rejectRequest(1)" class="btn-danger">Từ chối</button>
                                </td>
                            </tr>
                            <tr>
                                <td>Đề nghị mua vật tư</td>
                                <td>
                                    <button onclick="approveRequest(2)" class="btn-primary">Phê duyệt</button>
                                    <button onclick="rejectRequest(2)" class="btn-danger">Từ chối</button>
                                </td>
                            </tr>
                            <tr>
                                <td>
                                    <button onclick="approveRequest(3)" class="btn-primary">Phê duyệt</button>
                                    <button onclick="rejectRequest(3)" class="btn-danger">Từ chối</button>
                                </td>
                            </tr>
                        </tbody>
                    </table>
                </div>
            </div>

            <!-- Request History Table -->
            <div class="table-container">
                <div class="table-header">
                    <div>
                        <h2>Lịch sử yêu cầu</h2>
                        <p>Lịch sử yêu cầu xuất/mua/sửa vật tư</p>
                    </div>
                    <a href="${pageContext.request.contextPath}/requestHistory.jsp">Xem tất cả</a>
                </div>
                <div class="table-wrapper">
                    <table>
                        <thead>
                            <tr>
                                <th>Thời gian</th>
                                <th>Loại yêu cầu</th>
                                <th>Phòng ban</th>
                                <th>Nội dung</th>
                                <th>Trạng thái</th>
                            </tr>
                        </thead>
                        <tbody>
                            <tr>
                                <td>Yêu cầu xuất kho</td>
                                <td>Phòng Kỹ thuật</td>
                                <td>20 ghế xoay</td>
                                <td><span class="badge badge-success">Đã phê duyệt</span></td>
                            </tr>
                            <tr>
                                <td>Đề nghị mua vật tư</td>
                                <td>Phòng Hành chính</td>
                                <td>10 bàn họp</td>
                                <td><span class="badge badge-danger">Bị từ chối</span></td>
                            </tr>
                        </tbody>
                    </table>
                </div>
            </div>
        </main>

        <!-- Footer -->
        <%@include file="../footer.jsp" %>

    </body>
</html>

