// Khai báo biến toàn cục để lưu Chart instances
let requestDistributionChart = null;
let costTrendChart = null;
let materialCategoryChart = null;
let inventoryTrendChart = null;
let importExportLineChart = null;

document.addEventListener('DOMContentLoaded', function () {
    // Request Distribution Chart
    const requestDistributionChartCanvas = document.getElementById('requestDistributionChart');
    if (requestDistributionChartCanvas) {
        if (requestDistributionChart) requestDistributionChart.destroy();
        requestDistributionChart = new Chart(requestDistributionChartCanvas, {
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
                    title: { display: true, text: 'Phân bổ Yêu Cầu' }
                }
            }
        });
    }

    // Cost Trend Chart
    const costTrendChartCanvas = document.getElementById('costTrendChart');
    if (costTrendChartCanvas) {
        if (costTrendChart) costTrendChart.destroy();
        costTrendChart = new Chart(costTrendChartCanvas, {
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
                    y: {
                        beginAtZero: true,
                        title: { display: true, text: 'Triệu VNĐ' }
                    }
                },
                plugins: {
                    legend: { position: 'top' },
                    title: { display: true, text: 'Xu hướng Chi Phí' }
                }
            }
        });
    }

    // Material Category Chart
    const materialCategoryChartCanvas = document.getElementById('materialCategoryChart');
    if (materialCategoryChartCanvas) {
        if (materialCategoryChart) materialCategoryChart.destroy();
        materialCategoryChart = new Chart(materialCategoryChartCanvas, {
            type: 'doughnut',
            data: {
                labels: window.categoryLabels || [],
                datasets: [{
                    data: window.categoryData || [],
                    backgroundColor: [
                        '#4CAF50', '#2196F3', '#FFC107', '#FF5722', '#9C27B0',
                        '#00BCD4', '#8BC34A', '#FFEB3B', '#795548', '#607D8B'
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
        if (inventoryTrendChart) inventoryTrendChart.destroy();
        inventoryTrendChart = new Chart(inventoryTrendChartCanvas, {
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
                    y: {
                        beginAtZero: true,
                        title: { display: true, text: 'Số Lượng' }
                    }
                },
                plugins: {
                    legend: { position: 'top' },
                    title: { display: true, text: 'Xu Hướng Tồn Kho' }
                }
            }
        });
    }

    // Import/Export Line Chart
    const importExportLineChartCanvas = document.getElementById('importExportLineChart');
    if (importExportLineChartCanvas) {
        // Gán dữ liệu mẫu để test
        window.importByMonth = [20, 30, 40, 50, 60];
        window.exportByMonth = [15, 25, 35, 45, 55];

        console.log('importByMonth:', window.importByMonth);
        console.log('exportByMonth:', window.exportByMonth);

        if (importExportLineChart) importExportLineChart.destroy();
        importExportLineChart = new Chart(importExportLineChartCanvas, {
            type: 'bar',
            data: {
                labels: window.importExportMonthLabels || [],
                datasets: [
                    {
                        label: 'Số lượng nhập',
                        data: window.importByMonth || [],
                        backgroundColor: '#e74c3c',
                        borderColor: '#c0392b',
                        borderWidth: 1
                    },
                    {
                        label: 'Số lượng xuất',
                        data: window.exportByMonth || [],
                        backgroundColor: '#2ecc71',
                        borderColor: '#27ae60',
                        borderWidth: 1
                    }
                ]
            },
            options: {
                responsive: true,
                plugins: {
                    legend: {
                        display: true,
                        position: 'top'
                    }
                },
                scales: {
                    y: {
                        beginAtZero: true,
                        min: 0,
                        max: 100,
                        title: {
                            display: true,
                            text: 'Số lượng'
                        },
                        ticks: {
                            stepSize: 10
                        }
                    },
                    x: {
                        title: {
                            display: true,
                            text: 'Tháng'
                        }
                    }
                }
            }
        });
    }

    // Calendar Functionality
    function generateCalendar() {
        const today = new Date();
        const currentMonth = today.getMonth();
        const currentYear = today.getFullYear();
        const currentDate = today.getDate();

        const monthNames = [
            "Tháng 1", "Tháng 2", "Tháng 3", "Tháng 4", "Tháng 5", "Tháng 6",
            "Tháng 7", "Tháng 8", "Tháng 9", "Tháng 10", "Tháng 11", "Tháng 12"
        ];

        document.getElementById('currentMonth').textContent = monthNames[currentMonth];
        document.getElementById('currentYear').textContent = currentYear;

        const firstDay = new Date(currentYear, currentMonth, 1);
        const lastDay = new Date(currentYear, currentMonth + 1, 0);
        const startingDay = firstDay.getDay();
        const monthLength = lastDay.getDate();

        const calendarBody = document.querySelector('#calendarTable tbody');
        calendarBody.innerHTML = '';

        let date = 1;
        for (let i = 0; i < 6; i++) {
            const row = document.createElement('tr');

            for (let j = 0; j < 7; j++) {
                const cell = document.createElement('td');

                if (i === 0 && j < startingDay) {
                    cell.textContent = '';
                } else if (date > monthLength) {
                    cell.textContent = '';
                } else {
                    cell.textContent = date;

                    if (date === currentDate &&
                        currentMonth === today.getMonth() &&
                        currentYear === today.getFullYear()) {
                        cell.classList.add('today');
                        cell.style.fontWeight = 'bold';
                        cell.style.backgroundColor = '#3b82f6';
                        cell.style.color = 'white';
                    }

                    date++;
                }

                row.appendChild(cell);
            }

            calendarBody.appendChild(row);

            if (date > monthLength) break;
        }
    }

    generateCalendar();
});
