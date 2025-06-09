<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!-- Sidebar -->
<div class="sidebar">
    <div class="sidebar-header">
        <div class="logo-container">
            <i class="fas fa-warehouse"></i>
            <a href="userPermission.jsp"></a>
            <span class="logo-text">WMS</span>
        </div>
        <div class="toggle-btn">
            <i class="fas fa-chevron-left"></i>
        </div>
    </div>
    <ul class="sidebar-menu">
        <li class="menu-item">
            <a href="adminDashboard.jsp" class="menu-link active">
                <i class="fas fa-tachometer-alt menu-icon"></i>
                <span class="menu-text">Dashboard</span>
            </a>
        </li>
        <li class="menu-item has-submenu">
            <a href="#" class="menu-link">
                <i class="fas fa-tags menu-icon"></i>
                <span class="menu-text">Category List</span>
                <i class="fas fa-chevron-down submenu-icon"></i>
            </a>
            <ul class="submenu">
                <li>
                    <a href="${pageContext.request.contextPath}/categories" class="submenu-link">
                        <i class="fas fa-list menu-icon"></i>
                        <span class="menu-text">Category List</span>
                    </a>
                </li>
                <li>
                    <a href="${pageContext.request.contextPath}/settinglist" class="submenu-link">
                        <i class="fas fa-users menu-icon"></i>
                        <span class="menu-text">User Management</span>
                    </a>
                </li>
                <li>
                    <a href="${pageContext.request.contextPath}/supplier-list" class="submenu-link">
                        <i class="fas fa-truck menu-icon"></i>
                        <span class="menu-text">Supplier Management</span>
                    </a>
                </li>
            </ul>
        </li>
        <li class="menu-item">
            <a href="#" class="menu-link">
                <i class="fas fa-boxes menu-icon"></i>
                <span class="menu-text">Inventory</span>
            </a>
        </li>
        <c:if test="${sessionScope.permissions.contains('delivery_view')}">
            <li class="menu-item">
                <a href="${pageContext.request.contextPath}/delivery" class="menu-link">
                    <i class="fas fa-truck menu-icon"></i>
                    <span class="menu-text">Deliveries</span>
                </a>
            </li>
        </c:if>
        <li class="menu-item has-submenu">
            <a href="#" class="menu-link">
                <i class="fas fa-users menu-icon"></i>
                <span class="menu-text">Users</span>
                <i class="fas fa-chevron-down submenu-icon"></i>
            </a>
            <ul class="submenu">
                <li>
                    <a href="${pageContext.request.contextPath}/UserDetailServlet?userId=${sessionScope.user.user_id}" class="submenu-link">
                        <i class="fas fa-user menu-icon"></i>
                        <span class="menu-text">User Information</span>
                    </a>
                </li>
                <li>
                    <a href="${pageContext.request.contextPath}/userPermission.jsp" class="submenu-link">
                        <i class="fas fa-user-lock menu-icon"></i>
                        <span class="menu-text">User Permissions</span>
                    </a>
                </li>
            </ul>
        </li>
        <li class="menu-item">
            <a href="#" class="menu-link">
                <i class="fas fa-cog menu-icon"></i>
                <span class="menu-text">Settings</span>
            </a>
        </li>
        <li class="menu-item">
            <a href="homepage.jsp" class="menu-link">
                <i class="fas fa-sign-out-alt menu-icon"></i>
                <span class="menu-text">Logout</span>
            </a>
        </li>
    </ul>
</div>

<!-- Sidebar Toggle Script -->
<script>
    document.addEventListener('DOMContentLoaded', function () {
        const sidebar = document.querySelector('.sidebar');
        const mainContent = document.querySelector('#main-content');
        const toggleBtn = document.querySelector('.toggle-btn');
        const menuItemsWithSubmenu = document.querySelectorAll('.has-submenu');

        toggleBtn.addEventListener('click', function () {
            sidebar.classList.toggle('collapsed');
            mainContent.classList.toggle('expanded');
        });

        // Set active menu item
        const menuLinks = document.querySelectorAll('.menu-link');
        menuLinks.forEach(link => {
            link.addEventListener('click', function () {
                menuLinks.forEach(l => l.classList.remove('active'));
                this.classList.add('active');
            });
        });

        // Handle submenu toggle
        menuItemsWithSubmenu.forEach(item => {
            const link = item.querySelector('.menu-link');
            const submenu = item.querySelector('.submenu');

            link.addEventListener('click', (e) => {
                e.preventDefault();
                item.classList.toggle('open');
                submenu.style.maxHeight = item.classList.contains('open') ? submenu.scrollHeight + "px" : "0";
            });
        });

        // Highlight active page
        const currentPath = window.location.pathname;
        menuLinks.forEach(link => {
            if (link.getAttribute('href') === currentPath) {
                link.classList.add('active');
            }
        });
    });
</script>