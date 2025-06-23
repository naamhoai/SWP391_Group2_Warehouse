<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
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
        <%@include file="header_warehouse_staff.jsp" %>
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
</body>
</html> 