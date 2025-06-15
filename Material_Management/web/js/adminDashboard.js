document.addEventListener('DOMContentLoaded', function() {
    // Request Distribution Chart
    const requestDistributionChartCanvas = document.getElementById('requestDistributionChart');
    if (requestDistributionChartCanvas) {
        new Chart(requestDistributionChartCanvas, {
            type: 'pie',
            data: {
                labels: window.requestLabels || [],
                datasets: [{
                    data: window.requestData || [0, 0, 0],
                    backgroundColor: ['#3b82f6', '#10b981', '#f59e0b']
                }]
            },
            options: {
                responsive: true,
                plugins: {
                    legend: { position: 'top' },
                    title: { display: true, text: 'Phân bổ Yêu Cầu (Tháng 6/2025)' }
                }
            }
        });
    }

    // Cost Trend Chart
    const costTrendChartCanvas = document.getElementById('costTrendChart');
    if (costTrendChartCanvas) {
        new Chart(costTrendChartCanvas, {
            type: 'line',
            data: {
                labels: window.costLabels || [],
                datasets: [{
                    label: 'Chi Phí (Triệu VNĐ)',
                    data: window.costData || [0, 0, 0, 0],
                    borderColor: '#ef4444',
                    tension: 0.1,
                    fill: false
                }]
            },
            options: {
                responsive: true,
                scales: {
                    y: { beginAtZero: true, title: { display: true, text: 'Triệu VNĐ' } }
                },
                plugins: {
                    legend: { position: 'top' },
                    title: { display: true, text: 'Xu hướng Chi Phí (Tháng 6/2025)' }
                }
            }
        });
    }

    // Material Category Chart
    const materialCategoryChartCanvas = document.getElementById('materialCategoryChart');
    if (materialCategoryChartCanvas) {
        new Chart(materialCategoryChartCanvas, {
            type: 'doughnut',
            data: {
                labels: window.categoryLabels || [],
                datasets: [{
                    data: window.categoryData || [],
                    backgroundColor: [
                        '#4CAF50', '#2196F3', '#FFC107', '#FF5722', '#9C27B0', '#00BCD4', '#8BC34A', '#FFEB3B', '#795548', '#607D8B'
                    ]
                }]
            },
            options: {
                responsive: true,
                plugins: {
                    legend: { position: 'top' },
                    title: { display: true, text: 'Phân Bổ Vật Tư Theo Danh Mục' }
                }
            }
        });
    }

    // Inventory Trend Chart
    const inventoryTrendChartCanvas = document.getElementById('inventoryTrendChart');
    if (inventoryTrendChartCanvas) {
        new Chart(inventoryTrendChartCanvas, {
            type: 'line',
            data: {
                labels: window.inventoryTrendLabels || [],
                datasets: [{
                    label: 'Số Lượng Tồn Kho',
                    data: window.inventoryTrendData || [],
                    borderColor: '#9C27B0',
                    tension: 0.3,
                    fill: false
                }]
            },
            options: {
                responsive: true,
                scales: {
                    y: { beginAtZero: true, title: { display: true, text: 'Số Lượng' } }
                },
                plugins: {
                    legend: { position: 'top' },
                    title: { display: true, text: 'Xu Hướng Tồn Kho' }
                }
            }
        });
    }

    // Calendar Functionality (Fixed to current date: 15/06/2025)
    const monthNames = ["Tháng Một", "Tháng Hai", "Tháng Ba", "Tháng Tư", "Tháng Năm", "Tháng Sáu",
                        "Tháng Bảy", "Tháng Tám", "Tháng Chín", "Tháng Mười", "Tháng Mười Một", "Tháng Mười Hai"];
    const currentDate = new Date(2025, 5, 15); // Fixed to 15/06/2025

    const calendarTableBody = document.querySelector('#calendarTable tbody');
    const currentMonthSpan = document.getElementById('currentMonth');
    const currentYearSpan = document.getElementById('currentYear');

    function renderCalendar(date) {
        if (!calendarTableBody || !currentMonthSpan || !currentYearSpan) return;

        calendarTableBody.innerHTML = '';
        const year = date.getFullYear();
        const month = date.getMonth();
        const today = new Date(2025, 5, 15); // Fixed today as 15/06/2025

        currentMonthSpan.textContent = monthNames[month];
        currentYearSpan.textContent = year;

        const firstDayOfMonth = new Date(year, month, 1);
        const lastDayOfMonth = new Date(year, month + 1, 0);
        const numDaysInMonth = lastDayOfMonth.getDate();
        const startDay = firstDayOfMonth.getDay();

        let dateCounter = 1;
        let row;

        for (let i = 0; i < 6; i++) {
            row = document.createElement('tr');
            for (let j = 0; j < 7; j++) {
                const cell = document.createElement('td');
                if (i === 0 && j < startDay) {
                    const prevMonthLastDay = new Date(year, month, 0).getDate();
                    cell.textContent = prevMonthLastDay - (startDay - 1 - j);
                    cell.classList.add('inactive');
                } else if (dateCounter > numDaysInMonth) {
                    cell.textContent = dateCounter - numDaysInMonth;
                    cell.classList.add('inactive');
                    dateCounter++;
                } else {
                    cell.textContent = dateCounter;
                    if (dateCounter === today.getDate() && month === today.getMonth() && year === today.getFullYear()) {
                        cell.classList.add('today-highlight');
                    }
                    dateCounter++;
                }
                row.appendChild(cell);
            }
            calendarTableBody.appendChild(row);
            if (dateCounter > numDaysInMonth) break;
        }
    }

    // Initial render with fixed date
    renderCalendar(currentDate);
});