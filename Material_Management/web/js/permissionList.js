
document.addEventListener('DOMContentLoaded', function() {
    // Theme toggle functionality
    const themeToggle = document.getElementById('themeToggle');
    if (themeToggle) {
        themeToggle.addEventListener('click', function() {
            document.body.classList.toggle('dark-theme');
            const isDark = document.body.classList.contains('dark-theme');
            themeToggle.innerHTML = isDark 
                ? '<i class="fas fa-sun"></i> Light'
                : '<i class="fas fa-moon"></i> Dark';
            localStorage.setItem('theme', isDark ? 'dark' : 'light');
        });

        // Load saved theme
        if (localStorage.getItem('theme') === 'dark') {
            document.body.classList.add('dark-theme');
            themeToggle.innerHTML = '<i class="fas fa-sun"></i> Light';
        }
    }

    // Search, filter and sort functionality
    const searchInput = document.getElementById('searchInput');
    const moduleFilter = document.getElementById('moduleFilter');
    const sortSelect = document.getElementById('sortSelect');

    if (searchInput && moduleFilter && sortSelect) {
        searchInput.addEventListener('input', filterPermissions);
        moduleFilter.addEventListener('change', filterPermissions);
        sortSelect.addEventListener('change', filterPermissions);
    }

    // Debug role edit buttons
    const editButtons = document.querySelectorAll('.role-card .action-btn');
    editButtons.forEach(button => {
        button.addEventListener('click', function() {
            console.log('Edit role clicked:', this.closest('.role-card').dataset.roleId);
        });
    });
});

function filterPermissions() {
    const searchTerm = document.getElementById('searchInput').value.toLowerCase();
    const moduleFilter = document.getElementById('moduleFilter').value;
    const sortOption = document.getElementById('sortSelect').value;
    const permissionCards = document.querySelectorAll('.permission-card');
    let visibleCards = 0;

    // Filter and sort cards
    const sortedCards = Array.from(permissionCards).sort((a, b) => {
        const moduleA = a.dataset.module;
        const moduleB = b.dataset.module;
        
        if (sortOption === 'module_asc') {
            return moduleA.localeCompare(moduleB);
        } else if (sortOption === 'module_desc') {
            return moduleB.localeCompare(moduleA);
        }
        return 0;
    });

    // Apply filters and update visibility
    sortedCards.forEach(card => {
        const module = card.dataset.module;
        const permissionItems = card.querySelectorAll('.permission-item');
        let hasVisibleItems = false;

        permissionItems.forEach(item => {
            const permissionName = item.dataset.permissionName.toLowerCase();
            const matchesSearch = permissionName.includes(searchTerm);
            const matchesModule = !moduleFilter || module === moduleFilter;
            
            item.style.display = matchesSearch && matchesModule ? 'flex' : 'none';
            if (matchesSearch && matchesModule) {
                hasVisibleItems = true;
            }
        });

        card.style.display = hasVisibleItems ? 'block' : 'none';
        if (hasVisibleItems) visibleCards++;
    });

    // Show no results message if needed
    const noResultsMsg = document.querySelector('.no-results');
    if (visibleCards === 0) {
        if (!noResultsMsg) {
            const msg = document.createElement('div');
            msg.className = 'no-results';
            msg.textContent = 'Không tìm thấy quyền nào phù hợp với điều kiện tìm kiếm';
            document.getElementById('permissionGrid').appendChild(msg);
        }
    } else if (noResultsMsg) {
        noResultsMsg.remove();
    }
}
