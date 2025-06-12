<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Delivery Management</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdn.jsdelivr.net/npm/boxicons@2.0.7/css/boxicons.min.css" rel="stylesheet">
    <link href="css/deliveryManagement.css" rel="stylesheet">
</head>
<body>
    <div class="container-fluid">
        <div class="row">
            <!-- Sidebar -->
            <nav class="col-md-3 col-lg-2 d-md-block bg-light sidebar">
                <div class="position-sticky pt-3">
                    <ul class="nav flex-column">
                        <li class="nav-item">
                            <a class="nav-link" href="dashboard">
                                <i class='bx bxs-dashboard'></i>
                                Dashboard
                            </a>
                        </li>
                        <li class="nav-item">
                            <a class="nav-link" href="materialManagement">
                                <i class='bx bxs-box'></i>
                                Materials
                            </a>
                        </li>
                        <li class="nav-item">
                            <a class="nav-link" href="purchaseManagement">
                                <i class='bx bxs-cart'></i>
                                Purchases
                            </a>
                        </li>
                        <li class="nav-item">
                            <a class="nav-link active" href="deliveryManagement">
                                <i class='bx bxs-truck'></i>
                                Deliveries
                            </a>
                        </li>
                    </ul>
                </div>
            </nav>

            <!-- Main content -->
            <main class="col-md-9 ms-sm-auto col-lg-10 px-md-4">
                <div class="d-flex justify-content-between flex-wrap flex-md-nowrap align-items-center pt-3 pb-2 mb-3 border-bottom">
                    <h1 class="h2">Delivery Management</h1>
                    <button class="btn btn-primary" data-bs-toggle="modal" data-bs-target="#addDeliveryModal">
                        <i class='bx bx-plus'></i> New Delivery
                    </button>
                </div>

                <!-- Filters -->
                <div class="row mb-3">
                    <div class="col-md-3">
                        <select class="form-select" id="statusFilter">
                            <option value="">All Status</option>
                            <option value="Pending">Pending</option>
                            <option value="In Transit">In Transit</option>
                            <option value="Delivered">Delivered</option>
                            <option value="Cancelled">Cancelled</option>
                        </select>
                    </div>
                    <div class="col-md-3">
                        <input type="date" class="form-control" id="dateFilter" placeholder="Filter by date">
                    </div>
                    <div class="col-md-4">
                        <input type="text" class="form-control" id="searchInput" placeholder="Search deliveries...">
                    </div>
                </div>

                <!-- Deliveries Table -->
                <div class="table-responsive">
                    <table class="table table-striped table-hover">
                        <thead>
                            <tr>
                                <th>ID</th>
                                <th>Export ID</th>
                                <th>Receiver</th>
                                <th>Address</th>
                                <th>Delivery Date</th>
                                <th>Status</th>
                                <th>Actions</th>
                            </tr>
                        </thead>
                        <tbody id="deliveriesTableBody">
                            <c:forEach var="delivery" items="${deliveries}">
                                <tr>
                                    <td>${delivery.id}</td>
                                    <td>${delivery.exportId}</td>
                                    <td>${delivery.receiverName}</td>
                                    <td>${delivery.deliveryAddress}</td>
                                    <td>${delivery.deliveryDate}</td>
                                    <td>
                                        <span class="badge bg-${delivery.status == 'Delivered' ? 'success' : 
                                                              delivery.status == 'In Transit' ? 'primary' : 
                                                              delivery.status == 'Cancelled' ? 'danger' : 'warning'}">
                                            ${delivery.status}
                                        </span>
                                    </td>
                                    <td>
                                        <button class="btn btn-sm btn-info" onclick="viewDelivery(${delivery.id})">
                                            <i class='bx bx-show'></i>
                                        </button>
                                        <button class="btn btn-sm btn-warning" onclick="editDelivery(${delivery.id})">
                                            <i class='bx bx-edit'></i>
                                        </button>
                                        <button class="btn btn-sm btn-danger" onclick="deleteDelivery(${delivery.id})">
                                            <i class='bx bx-trash'></i>
                                        </button>
                                    </td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </div>
            </main>
        </div>
    </div>

    <!-- Add Delivery Modal -->
    <div class="modal fade" id="addDeliveryModal" tabindex="-1">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title">Add New Delivery</h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                </div>
                <div class="modal-body">
                    <form id="addDeliveryForm">
                        <div class="mb-3">
                            <label class="form-label">Export ID</label>
                            <input type="number" class="form-control" name="exportId" required>
                        </div>
                        <div class="mb-3">
                            <label class="form-label">Receiver Name</label>
                            <input type="text" class="form-control" name="receiverName" required>
                        </div>
                        <div class="mb-3">
                            <label class="form-label">Delivery Address</label>
                            <textarea class="form-control" name="deliveryAddress" required></textarea>
                        </div>
                        <div class="mb-3">
                            <label class="form-label">Delivery Date</label>
                            <input type="datetime-local" class="form-control" name="deliveryDate" required>
                        </div>
                        <div class="mb-3">
                            <label class="form-label">Status</label>
                            <select class="form-select" name="status" required>
                                <option value="Pending">Pending</option>
                                <option value="In Transit">In Transit</option>
                                <option value="Delivered">Delivered</option>
                                <option value="Cancelled">Cancelled</option>
                            </select>
                        </div>
                        <div class="mb-3">
                            <label class="form-label">Description</label>
                            <textarea class="form-control" name="description"></textarea>
                        </div>
                    </form>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cancel</button>
                    <button type="button" class="btn btn-primary" onclick="submitDelivery()">Add Delivery</button>
                </div>
            </div>
        </div>
    </div>

    <!-- Edit Delivery Modal -->
    <div class="modal fade" id="editDeliveryModal" tabindex="-1">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title">Edit Delivery</h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                </div>
                <div class="modal-body">
                    <form id="editDeliveryForm">
                        <input type="hidden" name="id">
                        <div class="mb-3">
                            <label class="form-label">Export ID</label>
                            <input type="number" class="form-control" name="exportId" required>
                        </div>
                        <div class="mb-3">
                            <label class="form-label">Receiver Name</label>
                            <input type="text" class="form-control" name="receiverName" required>
                        </div>
                        <div class="mb-3">
                            <label class="form-label">Delivery Address</label>
                            <textarea class="form-control" name="deliveryAddress" required></textarea>
                        </div>
                        <div class="mb-3">
                            <label class="form-label">Delivery Date</label>
                            <input type="datetime-local" class="form-control" name="deliveryDate" required>
                        </div>
                        <div class="mb-3">
                            <label class="form-label">Status</label>
                            <select class="form-select" name="status" required>
                                <option value="Pending">Pending</option>
                                <option value="In Transit">In Transit</option>
                                <option value="Delivered">Delivered</option>
                                <option value="Cancelled">Cancelled</option>
                            </select>
                        </div>
                        <div class="mb-3">
                            <label class="form-label">Description</label>
                            <textarea class="form-control" name="description"></textarea>
                        </div>
                    </form>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cancel</button>
                    <button type="button" class="btn btn-primary" onclick="updateDelivery()">Save Changes</button>
                </div>
            </div>
        </div>
    </div>

    <!-- View Delivery Modal -->
    <div class="modal fade" id="viewDeliveryModal" tabindex="-1">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title">Delivery Details</h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                </div>
                <div class="modal-body">
                    <div class="delivery-details">
                        <p><strong>ID:</strong> <span id="viewId"></span></p>
                        <p><strong>Export ID:</strong> <span id="viewExportId"></span></p>
                        <p><strong>Receiver Name:</strong> <span id="viewReceiverName"></span></p>
                        <p><strong>Delivery Address:</strong> <span id="viewDeliveryAddress"></span></p>
                        <p><strong>Delivery Date:</strong> <span id="viewDeliveryDate"></span></p>
                        <p><strong>Status:</strong> <span id="viewStatus"></span></p>
                        <p><strong>Description:</strong> <span id="viewDescription"></span></p>
                    </div>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Close</button>
                </div>
            </div>
        </div>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
    <script src="js/deliveryManagement.js"></script>
</body>
</html> 