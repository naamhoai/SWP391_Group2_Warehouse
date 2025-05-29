<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Dashboard - Warehouse Management System</title>
        
        
        <link rel="stylesheet" href="css/footer.css">
        <link rel="stylesheet" href="css/dashboard.css">
        
    </head>
    <body>
        <!-- Include Sidebar -->
     

        <div id="main-content">
            <div class="dashboard-header">
                <h1>Dashboard</h1>
                <div class="date-filter">
                    <button class="active" onclick="updateStats('today')">Today</button>
                    <button onclick="updateStats('week')">Week</button>
                    <button onclick="updateStats('month')">Month</button>
                    <button onclick="updateStats('year')">Year</button>
                </div>
            </div>

            <div class="stats-grid">
                <div class="stat-card">
                    <div class="stat-icon" style="background-color: #6dbdf2;">
                        <i class="fas fa-box"></i>
                    </div>
                    <div class="stat-info">
                        <h3>Total Items</h3>
                        <p class="stat-number">${dashboardStats.totalItems}</p>
                        <p class="stat-change positive">
                            <i class="fas fa-arrow-up"></i> Updated
                        </p>
                    </div>
                </div>

                <div class="stat-card">
                    <div class="stat-icon" style="background-color: #2ecc71;">
                        <i class="fas fa-shopping-cart"></i>
                    </div>
                    <div class="stat-info">
                        <h3>Monthly Orders</h3>
                        <p class="stat-number">${dashboardStats.monthlyOrders}</p>
                        <p class="stat-change positive">
                            <i class="fas fa-arrow-up"></i> This Month
                        </p>
                    </div>
                </div>

                <div class="stat-card">
                    <div class="stat-icon" style="background-color: #e74c3c;">
                        <i class="fas fa-exclamation-triangle"></i>
                    </div>
                    <div class="stat-info">
                        <h3>Low Stock Items</h3>
                        <p class="stat-number">${dashboardStats.lowStockItems}</p>
                        <p class="stat-change negative">
                            <i class="fas fa-arrow-down"></i> Need Attention
                        </p>
                    </div>
                </div>

                <div class="stat-card">
                    <div class="stat-icon" style="background-color: #f1c40f;">
                        <i class="fas fa-truck"></i>
                    </div>
                    <div class="stat-info">
                        <h3>Pending Deliveries</h3>
                        <p class="stat-number">${dashboardStats.pendingDeliveries}</p>
                        <p class="stat-change positive">
                            <i class="fas fa-arrow-up"></i> In Progress
                        </p>
                    </div>
                </div>
            </div>

            <div class="charts-grid">
                <div class="chart-card">
                    <h3>Inventory Overview</h3>
                    <canvas id="inventoryChart"></canvas>
                </div>
                <div class="chart-card">
                    <h3>Monthly Orders</h3>
                    <canvas id="ordersChart"></canvas>
                </div>
            </div>

            <div class="table-card">
                <h3>Low Stock Items</h3>
                <table>
                    <thead>
                        <tr>
                            <th>Item</th>
                            <th>Current Stock</th>
                            <th>Minimum Stock</th>
                            <th>Status</th>
                        </tr>
                    </thead>
                    <tbody>
                        <tr>
                            <td>Item A</td>
                            <td>15</td>
                            <td>20</td>
                            <td><span class="status-badge warning">Low Stock</span></td>
                        </tr>
                        <tr>
                            <td>Item B</td>
                            <td>5</td>
                            <td>25</td>
                            <td><span class="status-badge danger">Critical</span></td>
                        </tr>
                        <tr>
                            <td>Item C</td>
                            <td>18</td>
                            <td>20</td>
                            <td><span class="status-badge warning">Low Stock</span></td>
                        </tr>
                    </tbody>
                </table>
            </div>
        </div>

        <!-- Include Footer -->
        <jsp:include page="footer.jsp" />
    </body>

    <script>
        // Parse the server-side data
        const inventoryStats = {
            inStock: parseInt('${inventoryStats.inStock}'),
            lowStock: parseInt('${inventoryStats.lowStock}'),
            outOfStock: parseInt('${inventoryStats.outOfStock}')
        };

        const monthlyOrders = JSON.parse('${monthlyOrders}');

        // Inventory Chart
        const inventoryCtx = document.getElementById('inventoryChart').getContext('2d');
        new Chart(inventoryCtx, {
            type: 'doughnut',
            data: {
                labels: ['In Stock', 'Low Stock', 'Out of Stock'],
                datasets: [{
                        data: [
                            inventoryStats.inStock,
                            inventoryStats.lowStock,
                            inventoryStats.outOfStock
                        ],
                        backgroundColor: ['#2ecc71', '#f1c40f', '#e74c3c']
                    }]
            },
            options: {
                responsive: true,
                maintainAspectRatio: false
            }
        });

        // Orders Chart
        const ordersCtx = document.getElementById('ordersChart').getContext('2d');
        new Chart(ordersCtx, {
            type: 'line',
            data: {
                labels: monthlyOrders.months,
                datasets: [{
                        label: 'Orders',
                        data: monthlyOrders.counts,
                        borderColor: '#6dbdf2',
                        tension: 0.1
                    }]
            },
            options: {
                responsive: true,
                maintainAspectRatio: false,
                scales: {
                    y: {
                        beginAtZero: true
                    }
                }
            }
        });

        // Function to update stats based on time filter
        function updateStats(timeFilter) {
            // Update active button
            document.querySelectorAll('.date-filter button').forEach(btn => {
                btn.classList.remove('active');
                if (btn.textContent.toLowerCase() === timeFilter) {
                    btn.classList.add('active');
                }
            });

            // Fetch updated stats
            fetch('dashboard?action=getDashboardStats&timeFilter=' + timeFilter)
                    .then(response => response.json())
                    .then(data => {
                        // Update stats
                        document.querySelector('.stat-card:nth-child(1) .stat-number').textContent = data.totalItems;
                        document.querySelector('.stat-card:nth-child(2) .stat-number').textContent = data.monthlyOrders;
                        document.querySelector('.stat-card:nth-child(3) .stat-number').textContent = data.lowStockItems;
                        document.querySelector('.stat-card:nth-child(4) .stat-number').textContent = data.pendingDeliveries;
                    })
                    .catch(error => console.error('Error:', error));
        }
    </script>
</body>
</html> 