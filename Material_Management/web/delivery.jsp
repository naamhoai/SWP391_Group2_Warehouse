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
        <header class="navbar navbar-expand-lg navbar-dark fixed-top" style="background-color: #3b82f6;">
            <div class="container-fluid">
                <button class="navbar-toggler" type="button" id="sidebarToggle"><span class="navbar-toggler-icon"></span></button>
                <a class="navbar-brand" href="#">Quản Lý Vận Chuyển</a>
                <div class="ms-auto">
                    <a href="adminDashboard.jsp" class="btn btn-outline-light ms-2">Quay lại</a>
                </div>
            </div>
        </header>

        <!-- Main Content -->
        <div class="content" id="mainContent">
            <div class="container">
                <!-- Control Panel -->
                <div class="control-panel">
                    <div class="control-left">
                        <button class="btn btn-primary" onclick="showAddEditForm()">
                            <i class="fas fa-plus"></i> Chỉnh sửa
                        </button>
                    </div>
                    
                    <div class="control-center">
                        <div class="search-box">
                            <input type="text" placeholder="Tìm kiếm..." class="search-input">
                            <button class="search-btn">
                                <i class="fas fa-search"></i>
                            </button>
                        </div>
                    </div>

                    <div class="control-right">
                        <select class="form-select status-select" name="statusFilter" onchange="filterByStatus()">
                            <option value="">Trạng thái</option>
                            <option value="In Transit">Đang giao</option>
                            <option value="Delivered">Đã giao</option>
                            <option value="Cancelled">Đã hủy</option>
                        </select>
                    </div>
                </div>

                <!-- Delivery List -->
                <div class="table-container">
                    <table>
                        <thead>
                            <tr>
                                <th>Mã giao hàng</th>
                                <th>Công ty</th>
                                <th>Địa chỉ</th>
                                <th>Ngày</th>
                                <th>Trạng thái</th>
                                <th>Thao tác</th>
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
                                            <button class="edit-btn" onclick="showAddEditForm(${delivery.id}, '${delivery.exportId}', '${delivery.receiverName}', '${delivery.deliveryAddress}', '${delivery.status}', '${delivery.deliveryDate}', '${delivery.description}')">
                                                <i class="fas fa-edit"></i> Sửa
                                            </button>
                                        </c:if>
                                        <c:if test="${sessionScope.permissions.contains('delivery_delete')}">
                                            <button class="delete-btn" onclick="deleteDelivery(${delivery.id})">
                                                <i class="fas fa-trash"></i> Xóa
                                            </button>
                                        </c:if>
                                        <c:if test="${sessionScope.permissions.contains('delivery_edit')}">
                                            <button class="post-btn">
                                                <i class="fas fa-paper-plane"></i> Gửi
                                            </button>
                                        </c:if>
                                        <c:if test="${sessionScope.permissions.contains('delivery_edit')}">
                                            <button class="force-btn">
                                                <i class="fas fa-exclamation-triangle"></i> Cưỡng chế
                                            </button>
                                        </c:if>
                                    </td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </div>

                <!-- Add/Edit Form -->
                <div id="deliveryForm" class="form-container" style="display: none;">
                    <h2 id="formTitle">Thêm/Sửa giao hàng</h2>
                    <form action="delivery" method="POST">
                        <input type="hidden" name="action" id="formAction" value="add">
                        <input type="hidden" name="deliveryId" id="deliveryId">

                        <div class="form-group">
                            <label for="exportId">Mã xuất kho:</label>
                            <select id="exportId" name="exportId" required>
                                <c:forEach var="export" items="${exportForms}">
                                    <option value="${export.export_id}">${export.export_id} - ${export.material_name}</option>
                                </c:forEach>
                            </select>
                        </div>

                        <div class="form-group">
                            <label for="receiverName">Công ty:</label>
                            <input type="text" id="receiverName" name="receiverName" required>
                        </div>

                        <div class="form-group">
                            <label for="deliveryAddress">Địa chỉ:</label>
                            <textarea id="deliveryAddress" name="deliveryAddress" required></textarea>
                        </div>

                        <div class="form-group">
                            <label for="deliveryDate">Ngày giao:</label>
                            <input type="date" id="deliveryDate" name="deliveryDate" required>
                        </div>

                        <div class="form-group">
                            <label for="status">Trạng thái:</label>
                            <select id="status" name="status" required>
                                <option value="Pending">Chờ xử lý</option>
                                <option value="Delivered">Đã giao</option>
                                <option value="Cancelled">Đã hủy</option>
                            </select>
                        </div>

                        <div class="form-group">
                            <label for="description">Mô tả:</label>
                            <textarea id="description" name="description"></textarea>
                        </div>

                        <div class="form-buttons">
                            <button type="submit" class="btn btn-success">
                                <i class="fas fa-save"></i> Lưu
                            </button>
                            <button type="button" class="btn btn-danger" onclick="hideForm()">
                                <i class="fas fa-times"></i> Hủy
                            </button>
                        </div>
                    </form>
                </div>
            </div>
        </div>

        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
        <script src="js/delivery.js"></script>

    </body>
</html>