<script>
        // Biểu đồ vòng tròn: Phân bổ Yêu Cầu
        const requestDistributionChart = new Chart(document.getElementById('requestDistributionChart'), {
        type: 'pie',
        data: {
        labels: ['Mua Vật Tư', 'Xuất Kho', 'Sửa Chữa'],
                datasets: [{
                data: [
                        ${requestStats.purchaseCount},
                        ${requestStats.outgoingCount},
                        ${requestStats.repairCount}
                ],
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

        // Biểu đồ đường: Xu hướng Chi Phí
        const costTrendChart = new Chart(document.getElementById('costTrendChart'), {
        type: 'line',
        data: {
        labels: ['01/06', '04/06', '08/06', '12/06'],
                datasets: [{
                label: 'Chi Phí (Triệu VNĐ)',
                        data: [
                                ${costTrend[0]},
                                ${costTrend[1]},
                                ${costTrend[2]},
                                ${costTrend[3]}
                        ],
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
</script>

