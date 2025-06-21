document.addEventListener('DOMContentLoaded', function () {
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
                    legend: {position: 'top'},
                    title: {display: true, text: 'Phân bổ Yêu Cầu'}
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
                    y: {beginAtZero: true, title: {display: true, text: 'Triệu VNĐ'}}
                },
                plugins: {
                    legend: {position: 'top'},
                    title: {display: true, text: 'Xu hướng Chi Phí'}
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
                    legend: {position: 'top'},
                    title: {display: true, text: 'Phân Bổ Vật Tư Theo Danh Mục'}
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
                    y: {beginAtZero: true, title: {display: true, text: 'Số Lượng'}}
                },
                plugins: {
                    legend: {position: 'top'},
                    title: {display: true, text: 'Xu Hướng Tồn Kho'}
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

        // Tên các tháng bằng tiếng Việt
        const monthNames = [
            "Tháng 1", "Tháng 2", "Tháng 3", "Tháng 4", "Tháng 5", "Tháng 6",
            "Tháng 7", "Tháng 8", "Tháng 9", "Tháng 10", "Tháng 11", "Tháng 12"
        ];

        // Hiển thị tháng và năm hiện tại
        document.getElementById('currentMonth').textContent = monthNames[currentMonth];
        document.getElementById('currentYear').textContent = currentYear;

        // Tạo lịch
        const firstDay = new Date(currentYear, currentMonth, 1);
        const lastDay = new Date(currentYear, currentMonth + 1, 0);
        const startingDay = firstDay.getDay();
        const monthLength = lastDay.getDate();

        // Xóa nội dung cũ
        const calendarBody = document.querySelector('#calendarTable tbody');
        calendarBody.innerHTML = '';

        // Tạo các ô ngày
        let date = 1;
        for (let i = 0; i < 6; i++) {
            const row = document.createElement('tr');
            
            for (let j = 0; j < 7; j++) {
                const cell = document.createElement('td');
                
                if (i === 0 && j < startingDay) {
                    // Ô trống trước ngày 1
                    cell.textContent = '';
                } else if (date > monthLength) {
                    // Ô trống sau ngày cuối
                    cell.textContent = '';
                } else {
                    // Ngày trong tháng
                    cell.textContent = date;
                    
                    // Đánh dấu ngày hiện tại
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
            
            // Dừng nếu đã hết ngày trong tháng
            if (date > monthLength) {
                break;
            }
        }
    }

    // Gọi hàm tạo lịch khi trang load
    generateCalendar();
});