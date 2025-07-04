<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/css/all.min.css">

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
            <a href="adminDashboard.jsp" class="menu-link">
                <i class="fas fa-tachometer-alt menu-icon"></i>
                <span class="menu-text">Bảng Điều Khiển</span>
            </a>
        </li>

        <li class="menu-item">
            <a href="${pageContext.request.contextPath}/categories" class="menu-link">
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
            <a href="#" class="menu-link">
                <i class="fas fa-boxes"></i>
                <span class="menu-text">Kho Hàng</span>
            </a>
        </li>
        <li class="menu-item">
            <a href="delivery" class="menu-link">
                <i class="fas fa-truck menu-icon"></i>
                <span class="menu-text">Giao Hàng</span>
            </a>
        </li>
        <li class="menu-item has-submenu">
            <a href="adminDashboard.jsp" class="menu-link">
                <i class="fas fa-users menu-icon"></i>
                <span class="menu-text">Người Dùng</span>
                <i class="fas fa-submenu-icon"></i>
            </a>
            <ul class="submenu">
                <li>
                    <a href="${pageContext.request.contextPath}/UserDetailServlet?userId=${sessionScope.userId}" class="submenu-link">
                        <i class="fas fa-user menu-icon"></i>
                        <span class="menu-text">Thông Tin</span>
                    </a>
                </li>

                <li>
                    <a href="${pageContext.request.contextPath}/permissionList" class="submenu-link">
                        <i class="fas fa-gavel"></i>
                        <span class="menu-text">Danh Sách Quyền</span>
                    </a>
                </li>
                <li>
                    <a href="${pageContext.request.contextPath}/settinglist" class="submenu-link">
                        <i class="fas fa-user-circle"></i>
                        <span class="menu-text">Quản lý người dùng</span>
                    </a>
                </li>

            </ul>

        </li>
        <li>
            <a href="${pageContext.request.contextPath}/unitConversionSeverlet" class="menu-link">
                <i class="fas fa-balance-scale"></i>
                <span class="menu-text">Quản lý đơn vị</span>
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