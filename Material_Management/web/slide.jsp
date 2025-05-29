<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!-- Sidebar -->
<div class="sidebar">
    <div class="sidebar-header">
        <div class="logo-container">
            <i class="fas fa-warehouse"></i>
            <span class="logo-text">WMS</span>
        </div>
        <div class="toggle-btn">
            <i class="fas fa-chevron-left"></i>
        </div>
    </div>
    <ul class="sidebar-menu">
        <li class="menu-item">
            <a href="dashboard.jsp" class="menu-link active">
                <i class="fas fa-tachometer-alt menu-icon"></i>
                <span class="menu-text">Dashboard</span>
            </a>
        </li>
        <li class="menu-item">
            <a href="#" class="menu-link">
                <i class="fas fa-boxes menu-icon"></i>
                <span class="menu-text">Inventory</span>
            </a>
        </li>
        <li class="menu-item">
            <a href="#" class="menu-link">
                <i class="fas fa-shopping-cart menu-icon"></i>
                <span class="menu-text">Orders</span>
            </a>
        </li>
        <li class="menu-item">
            <a href="#" class="menu-link">
                <i class="fas fa-truck menu-icon"></i>
                <span class="menu-text">Deliveries</span>
            </a>
        </li>
        <li class="menu-item">
            <a href="#" class="menu-link">
                <i class="fas fa-users menu-icon"></i>
                <span class="menu-text">Users</span>
            </a>
        </li>
        <li class="menu-item">
            <a href="#" class="menu-link">
                <i class="fas fa-cog menu-icon"></i>
                <span class="menu-text">Settings</span>
            </a>
        </li>
        <li class="menu-item">
            <a href="logout" class="menu-link">
                <i class="fas fa-sign-out-alt menu-icon"></i>
                <span class="menu-text">Logout</span>
            </a>
        </li>
    </ul>
</div>

<!-- Sidebar Toggle Script -->
<script>
    document.addEventListener('DOMContentLoaded', function() {
        const sidebar = document.querySelector('.sidebar');
        const mainContent = document.querySelector('#main-content');
        const toggleBtn = document.querySelector('.toggle-btn');
        
        toggleBtn.addEventListener('click', function() {
            sidebar.classList.toggle('collapsed');
            mainContent.classList.toggle('expanded');
        });
        
        // Set active menu item
        const menuLinks = document.querySelectorAll('.menu-link');
        menuLinks.forEach(link => {
            link.addEventListener('click', function() {
                menuLinks.forEach(l => l.classList.remove('active'));
                this.classList.add('active');
            });
        });
    });
</script> 