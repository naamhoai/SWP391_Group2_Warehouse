document.addEventListener('DOMContentLoaded', function() {

    const sidebar = document.querySelector('.sidebar');
    const mainContent = document.querySelector('#main-content');
    const menuItemsWithSubmenu = document.querySelectorAll('.has-submenu');
    const menuLinks = document.querySelectorAll('.menu-link');

    function toggleSidebar() {
        if (sidebar && mainContent) {
            const isCollapsed = !sidebar.classList.contains('collapsed'); // Đảo ngược trạng thái
            sidebar.classList.toggle('collapsed', isCollapsed);
            mainContent.classList.toggle('expanded', isCollapsed);
            localStorage.setItem('sidebarCollapsed', isCollapsed);

            // Update submenu visibility
            menuItemsWithSubmenu.forEach(item => {
                const submenu = item.querySelector('.submenu');
                if (submenu) {
                    submenu.style.maxHeight = isCollapsed ? '0' : submenu.scrollHeight + 'px';
                    if (isCollapsed) item.classList.remove('open');
                }
            });
        }
    }

    function setActiveMenuItem() {
        const currentPath = window.location.pathname.split('/').pop();
        menuLinks.forEach(link => {
            link.classList.remove('active');
            const linkPath = link.getAttribute('href').split('/').pop();
            if (currentPath === linkPath) {
                link.classList.add('active');
                let parentSubmenu = link.closest('.has-submenu');
                if (parentSubmenu && !sidebar.classList.contains('collapsed')) {
                    parentSubmenu.classList.add('open');
                    let submenu = parentSubmenu.querySelector('.submenu');
                    if (submenu) {
                        submenu.style.maxHeight = submenu.scrollHeight + 'px';
                    }
                }
            }
        });

        if (currentPath === '' || currentPath === 'adminDashboard.jsp') {
            const adminDashboardLink = document.querySelector('a[href="adminDashboard.jsp"]');
            if (adminDashboardLink) {
                adminDashboardLink.classList.add('active');
            }
        }
    }

    setActiveMenuItem();
    menuLinks.forEach(link => {
        link.addEventListener('click', function(e) {
            if (!this.getAttribute('href') || this.getAttribute('href') === '#') {
                e.preventDefault();
            }
            menuLinks.forEach(l => l.classList.remove('active'));
            this.classList.add('active');

            menuItemsWithSubmenu.forEach(item => {
                if (item !== this.closest('.has-submenu')) {
                    item.classList.remove('open');
                    let submenu = item.querySelector('.submenu');
                    if (submenu) {
                        submenu.style.maxHeight = '0';
                    }
                }
            });
        });
    });

    menuItemsWithSubmenu.forEach(item => {
        const link = item.querySelector('.menu-link');
        const submenu = item.querySelector('.submenu');

        if (link && submenu) {
            link.addEventListener('click', (e) => {
                if (!sidebar.classList.contains('collapsed')) {
                    e.preventDefault();
                    item.classList.toggle('open');
                    submenu.style.maxHeight = item.classList.contains('open') ? submenu.scrollHeight + 'px' : '0';
                }
            });
        }
    });

    window.addEventListener('resize', function() {
        if (window.innerWidth <= 768 && sidebar) {
            sidebar.classList.add('collapsed');
            if (mainContent) {
                mainContent.classList.add('expanded');
            }
            menuItemsWithSubmenu.forEach(item => {
                let submenu = item.querySelector('.submenu');
                if (submenu) {
                    submenu.style.maxHeight = '0';
                    item.classList.remove('open');
                }
            });
        } else if (sidebar && !localStorage.getItem('sidebarCollapsed')) {
            sidebar.classList.remove('collapsed');
            if (mainContent) {
                mainContent.classList.remove('expanded');
            }
            menuItemsWithSubmenu.forEach(item => {
                if (item.classList.contains('open')) {
                    let submenu = item.querySelector('.submenu');
                    if (submenu) {
                        submenu.style.maxHeight = submenu.scrollHeight + 'px';
                    }
                }
            });
        }
    });

    const savedState = localStorage.getItem('sidebarCollapsed');
    if (savedState === 'true' && sidebar && mainContent) {
        sidebar.classList.add('collapsed');
        mainContent.classList.add('expanded');
        menuItemsWithSubmenu.forEach(item => {
            let submenu = item.querySelector('.submenu');
            if (submenu) {
                submenu.style.maxHeight = '0';
            }
        });
    } else if (sidebar && mainContent) {
        sidebar.classList.remove('collapsed');
        mainContent.classList.remove('expanded');
    }
});