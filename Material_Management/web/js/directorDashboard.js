// Toggle Sidebar
const sidebar = document.getElementById('sidebar');
const toggleSidebar = document.getElementById('toggleSidebar');
const toggleSidebarMobile = document.getElementById('toggleSidebarMobile');

function toggleSidebarVisibility() {
    sidebar.classList.toggle('active');
    sidebar.classList.toggle('hidden');
}

toggleSidebar.addEventListener('click', toggleSidebarVisibility);
toggleSidebarMobile.addEventListener('click', toggleSidebarVisibility);

// Initialize sidebar as hidden
sidebar.classList.add('hidden');

// Dark Mode Toggle
const toggleDarkMode = document.getElementById('toggleDarkMode');
toggleDarkMode.addEventListener('click', () => {
    document.body.classList.toggle('dark-mode');
    const icon = toggleDarkMode.querySelector('i');
    icon.classList.toggle('fa-moon');
    icon.classList.toggle('fa-sun');
    localStorage.setItem('darkMode', document.body.classList.contains('dark-mode'));
});

// Load Dark Mode Preference
if (localStorage.getItem('darkMode') === 'true') {
    document.body.classList.add('dark-mode');
    toggleDarkMode.querySelector('i').classList.replace('fa-moon', 'fa-sun');
}

// Toast Notification
function showToast(message, bgColor = "#3b82f6") {
    Toastify({
        text: message,
        duration: 3000,
        gravity: "top",
        position: "right",
        backgroundColor: bgColor,
        stopOnFocus: true,
        className: "rounded-lg shadow-lg",
        style: {borderRadius: "0.5rem"}
    }).showToast();
}

// Approve/Reject Request
function approveRequest(id) {
    showToast(`Yêu cầu #${id} đã được phê duyệt`, "#10b981");
    // Backend API call to update request status
}

function rejectRequest(id) {
    const reason = prompt('Lý do từ chối:');
    if (reason) {
        showToast(`Yêu cầu #${id} đã bị từ chối: ${reason}`, "#ef4444");
        // Backend API call to update request status
    }
}

// Export to Excel
function exportToExcel(chartId) {
    showToast(`Đang xuất báo cáo ra Excel...`);
    // Backend call to generate Excel file
}

// Initialize Charts
document.addEventListener('DOMContentLoaded', function() {
    // Inventory Chart
    const inventoryCtx = document.getElementById('inventoryChart').getContext('2d');
    const inventoryChart = new Chart(inventoryCtx, {
        type: 'line',
        data: {
            labels: ['Thứ 2', 'Thứ 3', 'Thứ 4', 'Thứ 5', 'Thứ 6', 'Thứ 7', 'CN'],
            datasets: [
                {
                    label: 'Tồn kho',
                    data: [1200, 1150, 1100, 1050, 1000, 950, 900],
                    borderColor: '#3b82f6',
                    backgroundColor: 'rgba(59, 130, 246, 0.1)',
                    borderWidth: 2,
                    tension: 0.3,
                    fill: true
                },
                {
                    label: 'Nhập kho',
                    data: [200, 150, 100, 80, 50, 30, 10],
                    borderColor: '#10b981',
                    backgroundColor: 'rgba(16, 185, 129, 0.1)',
                    borderWidth: 2,
                    tension: 0.3,
                    fill: true
                },
                {
                    label: 'Xuất kho',
                    data: [50, 70, 90, 120, 150, 100, 80],
                    borderColor: '#ef4444',
                    backgroundColor: 'rgba(239, 68, 68, 0.1)',
                    borderWidth: 2,
                    tension: 0.3,
                    fill: true
                }
            ]
        },
        options: {
            responsive: true,
            plugins: {
                legend: {position: 'top', labels: {usePointStyle: true, padding: 20}},
                tooltip: {enabled: true, mode: 'index', intersect: false}
            },
            scales: {
                y: {beginAtZero: false, grid: {drawBorder: false}},
                x: {grid: {display: false}}
            }
        }
    });

    // Cost Chart
    const costCtx = document.getElementById('costChart').getContext('2d');
    new Chart(costCtx, {
        type: 'pie',
        data: {
            labels: ['Mua mới', 'Sửa chữa', 'Khác'],
            datasets: [{
                data: [60, 30, 10],
                backgroundColor: ['#3b82f6', '#10b981', '#f87171'],
                borderWidth: 0
            }]
        },
        options: {
            responsive: true,
            plugins: {legend: {position: 'bottom', labels: {usePointStyle: true}}}
        }
    });

    // Chart Filter
    document.querySelectorAll('.chart-filter').forEach(button => {
        button.addEventListener('click', () => {
            document.querySelectorAll('.chart-filter').forEach(btn => btn.classList.remove('active'));
            button.classList.add('active');
            const period = button.textContent;
            // Update inventory chart data based on period (Week/Month/Year)
            let newData;
            if (period === 'Tuần') {
                newData = {
                    labels: ['Thứ 2', 'Thứ 3', 'Thứ 4', 'Thứ 5', 'Thứ 6', 'Thứ 7', 'CN'],
                    datasets: inventoryChart.data.datasets
                };
            } else if (period === 'Tháng') {
                newData = {
                    labels: ['T1', 'T2', 'T3', 'T4'],
                    datasets: [
                        {...inventoryChart.data.datasets[0], data: [1200, 1100, 1000, 900]},
                        {...inventoryChart.data.datasets[1], data: [200, 150, 100, 50]},
                        {...inventoryChart.data.datasets[2], data: [50, 70, 90, 120]}
                    ]
                };
            } else {
                newData = {
                    labels: ['2023', '2024', '2025'],
                    datasets: [
                        {...inventoryChart.data.datasets[0], data: [1300, 1250, 1200]},
                        {...inventoryChart.data.datasets[1], data: [300, 250, 200]},
                        {...inventoryChart.data.datasets[2], data: [100, 80, 60]}
                    ]
                };
            }
            inventoryChart.data = newData;
            inventoryChart.update();
            showToast(`Đã cập nhật biểu đồ cho ${period}`);
        });
    });

    // Table Sorting
    document.querySelectorAll('th').forEach(th => {
        th.addEventListener('click', () => {
            const table = th.closest('table');
            const tbody = table.querySelector('tbody');
            const rows = Array.from(tbody.querySelectorAll('tr'));
            const columnIndex = th.cellIndex;
            const isNumeric = columnIndex === 0; // Only "Thời gian" is sortable as text for simplicity
            const isAsc = th.classList.toggle('asc');
            th.classList.toggle('desc', !isAsc);
            table.querySelectorAll('th').forEach(header => {
                if (header !== th)
                    header.classList.remove('asc', 'desc');
            });
            rows.sort((a, b) => {
                let aValue = a.cells[columnIndex].textContent;
                let bValue = b.cells[columnIndex].textContent;
                if (isNumeric) {
                    aValue = new Date(aValue).getTime();
                    bValue = new Date(bValue).getTime();
                    return isAsc ? aValue - bValue : bValue - aValue;
                }
                return isAsc ? aValue.localeCompare(bValue) : bValue.localeCompare(aValue);
            });
            tbody.innerHTML = '';
            rows.forEach(row => tbody.appendChild(row));
        });
    });
}); 