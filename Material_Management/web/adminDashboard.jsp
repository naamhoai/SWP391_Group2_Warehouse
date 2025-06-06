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
        <link rel="stylesheet" href="css/adminDashboard.css">
        <link rel="stylesheet" href="css/sidebar.css">
        <!-- Font Awesome -->
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/css/all.min.css">
    </head>
    <body>
        <!-- Include Sidebar -->
        <jsp:include page="sidebar.jsp" />

        <div id="main-content">
            <div class="dashboard-header">
                <h1>Dashboard</h1>
                <div class="date-filter">
                    <form action="dashboard" method="get" class="filter-form">
                        <button type="submit" name="timeFilter" value="today" 
                                class="filter-btn ${timeFilter == 'today' ? 'active' : ''}">Today</button>
                        <button type="submit" name="timeFilter" value="week" 
                                class="filter-btn ${timeFilter == 'week' ? 'active' : ''}">Week</button>
                        <button type="submit" name="timeFilter" value="month" 
                                class="filter-btn ${timeFilter == 'month' ? 'active' : ''}">Month</button>
                        <button type="submit" name="timeFilter" value="year" 
                                class="filter-btn ${timeFilter == 'year' ? 'active' : ''}">Year</button>
                    </form>
                </div>
            </div>

            <div class="stats-grid">
                <div class="stat-card">
                    <div class="stat-icon" style="background-color: #6dbdf2;">
                        <i class="fas fa-box"></i>
                    </div>
                    <div class="stat-info">
                        <h3>Total Items</h3>
                        <p class="stat-number">${totalItems}</p>
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
                        <h3>Orders</h3>
                        <p class="stat-number">${monthlyOrders}</p>
                        <p class="stat-change positive">
                            <i class="fas fa-arrow-up"></i> 
                            <c:choose>
                                <c:when test="${timeFilter == 'today'}">Today</c:when>
                                <c:when test="${timeFilter == 'week'}">This Week</c:when>
                                <c:when test="${timeFilter == 'year'}">This Year</c:when>
                                <c:otherwise>This Month</c:otherwise>
                            </c:choose>
                        </p>
                    </div>
                </div>

                <div class="stat-card">
                    <div class="stat-icon" style="background-color: #e74c3c;">
                        <i class="fas fa-exclamation-triangle"></i>
                    </div>
                    <div class="stat-info">
                        <h3>Low Stock Items</h3>
                        <p class="stat-number">${lowStockItems}</p>
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
                        <p class="stat-number">${pendingDeliveries}</p>
                        <p class="stat-change positive">
                            <i class="fas fa-arrow-up"></i> In Progress
                        </p>
                    </div>
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
                        <c:forEach items="${lowStockItemsList}" var="item">
                            <tr>
                                <td>${item.name}</td>
                                <td>${item.quantity}</td>
                                <td>${item.minStock}</td>
                                <td>
                                    <span class="status-badge ${item.status == 'Critical' ? 'danger' : 'warning'}">
                                        ${item.status}
                                    </span>
                                </td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
            </div>

            <div class="inventory-summary">
                <h3>Inventory Overview</h3>
                <div class="inventory-stats">
                    <div class="inventory-stat">
                        <span class="label">In Stock:</span>
                        <span class="value">${inventoryStats.inStock}</span>
                    </div>
                    <div class="inventory-stat">
                        <span class="label">Low Stock:</span>
                        <span class="value">${inventoryStats.lowStock}</span>
                    </div>
                    <div class="inventory-stat">
                        <span class="label">Out of Stock:</span>
                        <span class="value">${inventoryStats.outOfStock}</span>
                    </div>
                </div>
            </div>
        </div>

        <!-- Include Footer -->
        <jsp:include page="footer.jsp" />
    </body>
</html>