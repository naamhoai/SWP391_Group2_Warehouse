document.addEventListener('DOMContentLoaded', function () {
    const sidebar = document.querySelector('.sidebar');
    const mainContent = document.querySelector('#main-content');
    const sidebarToggle = document.getElementById('sidebarToggle');
    const menuItemsWithSubmenu = document.querySelectorAll('.has-submenu');

    // Function to toggle sidebar
    function toggleSidebar() {
        sidebar.classList.toggle('collapsed');
        mainContent.classList.toggle('expanded');
    }

    // Toggle sidebar when clicking the logo container
    sidebarToggle.addEventListener('click', toggleSidebar);

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
            if (!sidebar.classList.contains('collapsed')) {
                e.preventDefault();
                item.classList.toggle('open');
                submenu.style.maxHeight = item.classList.contains('open') ? submenu.scrollHeight + "px" : "0";
            }
        });
    });
}); 