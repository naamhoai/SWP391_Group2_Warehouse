document.addEventListener('DOMContentLoaded', function() {
    // Get DOM elements
    const sidebar = document.querySelector('.sidebar');
    const mainContent = document.querySelector('#main-content');
    const logoContainer = document.getElementById('sidebarToggle');
    const menuItemsWithSubmenu = document.querySelectorAll('.has-submenu');
    const menuLinks = document.querySelectorAll('.menu-link');

    // Ensure sidebar is open by default on page load
    if (sidebar) {
        sidebar.classList.remove('collapsed');
        if (mainContent) {
            mainContent.classList.remove('expanded');
        }
    }

    // Restore sidebar state from localStorage
    const sidebarCollapsed = localStorage.getItem('sidebarCollapsed'); // Get raw value
    if (sidebarCollapsed === 'true' && sidebar) { // Only collapse if explicitly true in localStorage
        sidebar.classList.add('collapsed');
        if (mainContent) {
            mainContent.classList.add('expanded');
        }
    }

    // Function to toggle sidebar
    function toggleSidebar() {
        if (sidebar) {
            sidebar.classList.toggle('collapsed');
            if (mainContent) {
                mainContent.classList.toggle('expanded');
            }
            // Save state to localStorage
            localStorage.setItem('sidebarCollapsed', sidebar.classList.contains('collapsed'));
        }
    }

    // Add click event to logo container
    if (logoContainer) {
        logoContainer.addEventListener('click', function(e) {
            e.preventDefault();
            e.stopPropagation();
            toggleSidebar();
        });
    }

    // Function to set active menu item based on current URL
    function setActiveMenuItem() {
        const currentPath = window.location.pathname.split('/').pop(); // Get current file name
        menuLinks.forEach(link => {
            link.classList.remove('active');
            const linkPath = link.getAttribute('href').split('/').pop();
            if (currentPath === linkPath) {
                link.classList.add('active');
                // Open parent submenu if it's a submenu item
                let parentSubmenu = link.closest('.has-submenu');
                if (parentSubmenu && !sidebar.classList.contains('collapsed')) {
                    parentSubmenu.classList.add('open');
                    let submenu = parentSubmenu.querySelector('.submenu');
                    if (submenu) {
                        submenu.style.maxHeight = submenu.scrollHeight + "px";
                    }
                }
            }
        });

        // Handle case for homepage or adminDashboard if no specific path matches
        if (currentPath === '' || currentPath === 'adminDashboard.jsp') {
            // Default to adminDashboard if it's homepage or no path
            const adminDashboardLink = document.querySelector('a[href="adminDashboard.jsp"]');
            if (adminDashboardLink) {
                adminDashboardLink.classList.add('active');
            }
        }
    }

    // Initial active state setup
    setActiveMenuItem();

    // Handle menu items active state on click
    menuLinks.forEach(link => {
        link.addEventListener('click', function(e) {
            // Don't prevent default for links that should navigate
            if (!this.getAttribute('href') || this.getAttribute('href') === '#') {
                e.preventDefault();
            }
            // Remove active from all and add to clicked link
            menuLinks.forEach(l => l.classList.remove('active'));
            this.classList.add('active');

            // Close other submenus and open current one
            menuItemsWithSubmenu.forEach(item => {
                if (item !== this.closest('.has-submenu')) {
                    item.classList.remove('open');
                    let submenu = item.querySelector('.submenu');
                    if (submenu) {
                        submenu.style.maxHeight = "0";
                    }
                }
            });
        });
    });

    // Handle submenu toggle
    menuItemsWithSubmenu.forEach(item => {
        const link = item.querySelector('.menu-link');
        const submenu = item.querySelector('.submenu');

        if (link && submenu) {
            link.addEventListener('click', (e) => {
                // Only toggle if sidebar is not collapsed, or if it's the main submenu link clicked in collapsed mode (if desired)
                if (!sidebar.classList.contains('collapsed')) {
                    e.preventDefault();
                    item.classList.toggle('open');
                    submenu.style.maxHeight = item.classList.contains('open') ? submenu.scrollHeight + "px" : "0";
                }
            });
        }
    });

    // Add window resize handler
    window.addEventListener('resize', function() {
        if (window.innerWidth <= 768 && sidebar) {
            sidebar.classList.add('collapsed');
            if (mainContent) {
                mainContent.classList.add('expanded');
            }
        }
        // Re-evaluate submenu heights on resize in case content changes
        menuItemsWithSubmenu.forEach(item => {
            if (item.classList.contains('open')) {
                let submenu = item.querySelector('.submenu');
                if (submenu) {
                    submenu.style.maxHeight = submenu.scrollHeight + "px";
                }
            }
        });
    });
}); 