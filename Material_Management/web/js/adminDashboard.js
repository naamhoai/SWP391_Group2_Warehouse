document.addEventListener('DOMContentLoaded', function() {
    const chartDataElement = document.getElementById('chartData');

    // Request Distribution Chart
    const requestLabels = JSON.parse(chartDataElement.dataset.requestLabels);
    const requestData = JSON.parse(chartDataElement.dataset.requestData);
    const requestDistributionChart = new Chart(document.getElementById('requestDistributionChart'), {
        type: 'pie',
        data: {
            labels: requestLabels,
            datasets: [{
                data: requestData,
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

    // Cost Trend Chart
    const costLabels = JSON.parse(chartDataElement.dataset.costLabels);
    const costData = JSON.parse(chartDataElement.dataset.costData);
    const costTrendChart = new Chart(document.getElementById('costTrendChart'), {
        type: 'line',
        data: {
            labels: costLabels,
            datasets: [{
                label: 'Chi Phí (Triệu VNĐ)',
                data: costData,
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

    // Material Category Chart
    const categoryLabels = JSON.parse(chartDataElement.dataset.categoryLabels);
    const categoryData = JSON.parse(chartDataElement.dataset.categoryData);
    const materialCategoryChart = new Chart(document.getElementById('materialCategoryChart'), {
        type: 'doughnut',
        data: {
            labels: categoryLabels,
            datasets: [{
                data: categoryData,
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

    // Inventory Trend Chart
    const inventoryTrendLabels = JSON.parse(chartDataElement.dataset.inventoryLabels);
    const inventoryTrendData = JSON.parse(chartDataElement.dataset.inventoryData);
    const inventoryTrendChart = new Chart(document.getElementById('inventoryTrendChart'), {
        type: 'line',
        data: {
            labels: inventoryTrendLabels,
            datasets: [{
                label: 'Số Lượng Tồn Kho',
                data: inventoryTrendData,
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
}); 