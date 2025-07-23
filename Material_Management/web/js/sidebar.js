document.addEventListener('DOMContentLoaded', function () {
    const sidebar = document.querySelector('.sidebar');
    const mainContent = document.querySelector('#main-content');
    const toggleButton = document.querySelector('#toggleSidebar'); // gợi ý thêm nút để toggle
    const menuItemsWithSubmenu = document.querySelectorAll('.has-submenu');
    const menuLinks = document.querySelectorAll('.menu-link');

    // Toggle sidebar (collapse/expand)
    function toggleSidebar() {
        if (sidebar && mainContent) {
            const isCollapsed = sidebar.classList.toggle('collapsed');
            mainContent.classList.toggle('expanded', isCollapsed);
            localStorage.setItem('sidebarCollapsed', isCollapsed);
        }
    }

    // Set active menu item based on current URL
    function setActiveMenuItem() {
        const currentPath = window.location.pathname.split('/').pop();
        menuLinks.forEach(link => {
            link.classList.remove('active');
            const linkPath = link.getAttribute('href').split('/').pop();
            if (currentPath === linkPath) {
                link.classList.add('active');
                const parentSubmenu = link.closest('.has-submenu');
                if (parentSubmenu && !sidebar.classList.contains('collapsed')) {
                    parentSubmenu.classList.add('open');
                    const submenu = parentSubmenu.querySelector('.submenu');
                    if (submenu) submenu.style.maxHeight = submenu.scrollHeight + 'px';
                }
            }
        });
    }

    // Restore state from localStorage
    const savedState = localStorage.getItem('sidebarCollapsed');
    if (savedState === 'true') {
        sidebar.classList.add('collapsed');
        mainContent.classList.add('expanded');
    } else {
        sidebar.classList.remove('collapsed');
        mainContent.classList.remove('expanded');
    }

    // Menu click logic
    menuLinks.forEach(link => {
        link.addEventListener('click', function (e) {
            if (!this.getAttribute('href') || this.getAttribute('href') === '#') {
                e.preventDefault();
            }
            menuLinks.forEach(l => l.classList.remove('active'));
            this.classList.add('active');

            menuItemsWithSubmenu.forEach(item => {
                if (item !== this.closest('.has-submenu')) {
                    item.classList.remove('open');
                    const submenu = item.querySelector('.submenu');
                    if (submenu) submenu.style.maxHeight = '0';
                }
            });
        });
    });

    // Toggle submenu
    menuItemsWithSubmenu.forEach(item => {
        const link = item.querySelector('.menu-link');
        const submenu = item.querySelector('.submenu');

        if (link && submenu) {
            link.addEventListener('click', e => {
                if (!sidebar.classList.contains('collapsed')) {
                    e.preventDefault();
                    const isOpen = item.classList.toggle('open');
                    submenu.style.maxHeight = isOpen ? submenu.scrollHeight + 'px' : '0';
                }
            });
        }
    });

    // Optional: Toggle button for user to collapse manually
    if (toggleButton) {
        toggleButton.addEventListener('click', toggleSidebar);
    }

    setActiveMenuItem();
});
