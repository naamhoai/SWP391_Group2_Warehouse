document.addEventListener('DOMContentLoaded', function() {
    // Handle messages
    const errorMessage = document.querySelector('meta[name="error-message"]')?.content;
    const successMessage = document.querySelector('meta[name="success-message"]')?.content;

    if (errorMessage) {
        Toastify({
            text: errorMessage,
            duration: 3000,
            backgroundColor: "#e74c3c",
            position: "top-right"
        }).showToast();
    }

    if (successMessage) {
        Toastify({
            text: successMessage,
            duration: 3000,
            backgroundColor: "#27ae60",
            position: "top-right"
        }).showToast();
    }

    // Theme toggle
    const themeToggle = document.getElementById('themeToggle');
    const body = document.body;
    themeToggle.addEventListener('click', function() {
        body.classList.toggle('dark-mode');
        const isDark = body.classList.contains('dark-mode');
        themeToggle.innerHTML = isDark 
            ? '<i class="fas fa-sun"></i> Light'
            : '<i class="fas fa-moon"></i> Dark';
        localStorage.setItem('theme', isDark ? 'dark' : 'light');
    });

    // Load saved theme
    if (localStorage.getItem('theme') === 'dark') {
        body.classList.add('dark-mode');
        themeToggle.innerHTML = '<i class="fas fa-sun"></i> Light';
    }

    // Form submission handling
    const permissionForm = document.getElementById('permissionForm');
    if (permissionForm) {
        permissionForm.addEventListener('submit', function(e) {
            const checkboxes = this.querySelectorAll('input[type="checkbox"]:checked');
            if (checkboxes.length === 0) {
                e.preventDefault();
                Toastify({
                    text: 'Vui lòng chọn ít nhất một quyền',
                    duration: 3000,
                    backgroundColor: '#e74c3c',
                    position: 'top-right'
                }).showToast();
                return;
            }
            if (!confirm('Bạn có chắc chắn muốn lưu các quyền này?')) {
                e.preventDefault();
                return;
            }
            document.getElementById('loadingOverlay').style.display = 'flex';
        });
    }
});
