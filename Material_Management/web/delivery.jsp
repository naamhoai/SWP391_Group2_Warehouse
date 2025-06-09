<%@ page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Quản lý giao hàng</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://fonts.googleapis.com/css2?family=Roboto:wght@400;500;600&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    <link rel="stylesheet" href="css/delivery.css">
</head>
<body>

    <!-- Header -->
    <header class="navbar navbar-expand-lg navbar-dark bg-dark fixed-top">
        <div class="container-fluid">
            <button class="navbar-toggler" type="button" id="sidebarToggle"><span class="navbar-toggler-icon"></span></button>
            <a class="navbar-brand" href="#">Quản lý kho</a>
            <div class="ms-auto">
                <span class="text-white">Xin chào, ${sessionScope.user.full_name}</span>
                <a href="/logout" class="btn btn-outline-light ms-2">Đăng xuất</a>
            </div>
        </div>
    </header>

    <!-- Main Content -->
    <div class="content" id="mainContent">
        <div class="container">
            <!-- Control Panel (Separate from Table) -->
            <div class="control-panel">
                <button class="btn btn-primary" onclick="showAddEditForm()">Edit</button>
                <select class="form-select" name="statusFilter" onchange="filterByStatus()">
                    <option value="">Status</option>
                    <option value="Pending">Pending</option>
                    <option value="In Transit">In Transit</option>
                    <option value="Delivered">Delivered</option>
                    <option value="Cancelled">Cancelled</option>
                </select>
                
            </div>

            <!-- Delivery List -->
            <div class="table-container">
                <table>
                    <thead>
                        <tr>
                            <th>Ship Code</th>
                            <th>Company</th>
                            <th>Address</th>
                            <th>Day</th>
                            <th>Status</th>
                            <th>Action</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="delivery" items="${deliveries}">
                            <tr>
                                <td>${delivery.id}</td>
                                <td>${delivery.receiverName}</td>
                                <td>${delivery.deliveryAddress}</td>
                                <td>${delivery.deliveryDate != null ? delivery.deliveryDate.toLocalDateTime().format(java.time.format.DateTimeFormatter.ofPattern("EEEE")) : ''}</td>
                                <td>${delivery.status}</td>
                                <td class="action-buttons">
                                    <c:if test="${sessionScope.permissions.contains('delivery_edit')}">
                                        <button class="edit-btn" onclick="showAddEditForm(${delivery.id}, '${delivery.exportId}', '${delivery.receiverName}', '${delivery.deliveryAddress}', '${delivery.status}', '${delivery.deliveryDate}', '${delivery.description}')">Edit</button>
                                    </c:if>
                                    <c:if test="${sessionScope.permissions.contains('delivery_delete')}">
                                        <button class="delete-btn" onclick="deleteDelivery(${delivery.id})">Delete</button>
                                    </c:if>
                                    <c:if test="${sessionScope.permissions.contains('delivery_edit')}">
                                        <button class="post-btn">Post</button>
                                    </c:if>
                                    <c:if test="${sessionScope.permissions.contains('delivery_edit')}">
                                        <button class="force-btn">Force</button>
                                    </c:if>
                                </td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
            </div>

            <!-- Add/Edit Form (Hidden by default) -->
            <div id="deliveryForm" class="form-container" style="display: none;">
                <h2 id="formTitle">Add/Edit Delivery</h2>
                <form action="delivery" method="POST">
                    <input type="hidden" name="action" id="formAction" value="add">
                    <input type="hidden" name="deliveryId" id="deliveryId">

                    <div class="form-group">
                        <label for="exportId">Export ID:</label>
                        <select id="exportId" name="exportId" required>
                            <c:forEach var="export" items="${exportForms}">
                                <option value="${export.export_id}">${export.export_id} - ${export.material_name}</option>
                            </c:forEach>
                        </select>
                    </div>

                    <div class="form-group">
                        <label for="receiverName">Company:</label>
                        <input type="text" id="receiverName" name="receiverName" required>
                    </div>

                    <div class="form-group">
                        <label for="deliveryAddress">Address:</label>
                        <textarea id="deliveryAddress" name="deliveryAddress" required></textarea>
                    </div>

                    <div class="form-group">
                        <label for="deliveryDate">Day:</label>
                        <input type="date" id="deliveryDate" name="deliveryDate" required>
                    </div>

                    <div class="form-group">
                        <label for="status">Status:</label>
                        <select id="status" name="status" required>
                            <option value="Pending">Pending</option>
                            <option value="In Transit">In Transit</option>
                            <option value="Delivered">Delivered</option>
                            <option value="Cancelled">Cancelled</option>
                        </select>
                    </div>

                    <div class="form-group">
                        <label for="description">Description:</label>
                        <textarea id="description" name="description"></textarea>
                    </div>

                    <div class="form-buttons">
                        <button type="submit" class="btn btn-success">Save</button>
                        <button type="button" class="btn btn-danger" onclick="hideForm()">Cancel</button>
                    </div>
                </form>
            </div>
        </div>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
    <script src="js/delivery.js"></script>
    
</body>
</html>