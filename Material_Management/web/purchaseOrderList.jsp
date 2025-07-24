<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Danh Sách Đơn Mua Vật Tư</title>
    <link rel="stylesheet" href="css/createPurchaseOrder.css">
    <link rel="stylesheet" href="css/sidebar.css">
    <link rel="stylesheet" href="css/purchaseOrderList.css">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" rel="stylesheet">
    
</head>
<body>
    <jsp:include page="sidebar.jsp" />
    <div class="purchase-order-list-container">
        <div class="purchase-order-list-header">
            <h1 class="purchase-order-list-title">Danh Sách Đơn Mua Vật Tư</h1>
            <a href="createPurchaseOrder" class="create-order-btn">
                <i class="fas fa-plus"></i> Tạo đơn mua mới
            </a>
        </div>
        
        <!-- Thông báo thành công/lỗi -->
        <c:if test="${not empty successMessage}">
            <div class="alert-success">
                <strong>Thành công!</strong> ${successMessage}
            </div>
        </c:if>
        <c:if test="${param.error == 'true'}">
            <div class="alert-error">
                <strong>Lỗi!</strong> ${param.message}
            </div>
        </c:if>
        <c:if test="${param.success == 'true' && not empty param.message}">
            <div class="alert-success-flex">
                <i class="fas fa-check-circle icon-margin-right"></i>
                <strong>Thành công!</strong> ${param.message}
            </div>
        </c:if>
        <c:if test="${param.msg == 'approved'}">
            <div class="alert-approved">
                Đơn mua đã được duyệt thành công!
            </div>
        </c:if>
        <c:if test="${param.msg == 'rejected'}">
            <div class="alert-rejected">
                Đơn mua đã bị từ chối!
            </div>
        </c:if>
        
        <!-- Phần tìm kiếm -->
        <div class="search-section">
            <form method="GET" action="purchaseOrderList">
                <div class="search-row">
                    <div>
                        <label>Ngày</label>
                        <select name="dateRange" id="dateRangeSelect" onchange="this.form.submit()">
                            <option value="all" <c:if test='${dateRange == "all" || empty dateRange}'>selected</c:if>>Tất cả</option>
                            <option value="today" <c:if test='${dateRange == "today"}'>selected</c:if>>Hôm nay</option>
                            <option value="1day" <c:if test='${dateRange == "1day"}'>selected</c:if>>1 ngày trước</option>
                            <option value="1week" <c:if test='${dateRange == "1week"}'>selected</c:if>>1 tuần trước</option>
                            <option value="1month" <c:if test='${dateRange == "1month"}'>selected</c:if>>1 tháng trước</option>
                        </select>
                    </div>
                    <div>
                        <label>Trạng thái</label>
                        <select name="status" onchange="this.form.submit()">
                            <option value="">Tất cả</option>
                            <option value="pending" <c:if test="${status == 'pending'}">selected</c:if>>Chờ duyệt</option>
                            <option value="approved" <c:if test="${status == 'approved'}">selected</c:if>>Đã duyệt</option>
                            <option value="rejected" <c:if test="${status == 'rejected'}">selected</c:if>>Từ chối</option>
                        </select>
                    </div>
                    <div>
                        <label>Nhà cung cấp</label>
                        <select name="supplier" onchange="this.form.submit()">
                            <option value="">Tất cả</option>
                            <c:forEach var="name" items="${supplierNamesInOrders}">
                                <option value="${name}" <c:if test='${supplier == name}'>selected</c:if>>${name}</option>
                            </c:forEach>
                        </select>
                    </div>
                    <div>
                        <label for="sortOrder" class="label-block">Sắp xếp ID</label>
                        <select name="sortOrder" id="sortOrder" class="sort-order-select" onchange="this.form.submit()">
                            <option value="desc" ${param.sortOrder == 'desc' || empty param.sortOrder ? 'selected' : ''}>Giảm dần</option>
                            <option value="asc" ${param.sortOrder == 'asc' ? 'selected' : ''}>Tăng dần</option>
                        </select>
                    </div>
                    <div>
                        <label>&nbsp;</label>
                        <button type="submit" class="btn btn-primary btn-search">Tìm kiếm</button>
                    </div>
                </div>
            </form>
        </div>

        <!-- Bảng danh sách -->
        <table>
            <thead>
                <tr>
                    <th>ID</th>
                    <th>Nhà cung cấp</th>
                    <th>Ngày tạo</th>
                    <th>Tổng tiền</th>
                    <th>Ghi chú</th>
                    <th>Trạng thái</th>
                    <th>Thao tác</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach var="order" items="${purchaseOrders}" varStatus="orderStatus">
                    <tr>
                        <td>${order.purchaseOrderId}</td>
                        <td>${order.supplierName}</td>
                        <td><fmt:formatDate value="${order.orderDate}" pattern="dd/MM/yyyy"/></td>
                        <td><fmt:formatNumber value="${order.totalAmount}" type="number" groupingUsed="true"/> VNĐ</td>
                        <td>
                            <c:choose>
                                <c:when test="${order.status eq 'Rejected' || order.status eq 'Từ chối'}">
                                    <span class="note-rejected">${order.rejectionReason}</span>
                                </c:when>
                                <c:when test="${order.status eq 'Approved' || order.status eq 'Đã duyệt'}">
                                    <span class="note-approved">${order.note}</span>
                                </c:when>
                                <c:otherwise>
                                    ${order.note}
                                </c:otherwise>
                            </c:choose>
                        </td>
                        <td>
                            <c:choose>
                                <c:when test="${order.status eq 'Completed'}">
                                    <span class="status-approved">Đã duyệt</span>
                                </c:when>
                                <c:when test="${order.status eq 'Pending'}">
                                    <span class="status-pending">Chờ duyệt</span>
                                </c:when>
                                <c:when test="${order.status eq 'Rejected'}">
                                    <span class="status-rejected">Từ chối</span>
                                </c:when>
                                <c:otherwise>
                                    <span>${order.status}</span>
                                </c:otherwise>
                            </c:choose>
                        </td>
                        <td>
                            <a class="btn btn-view" href="purchaseOrderDetail?id=${order.purchaseOrderId}">
                                Chi tiết
                            </a>
                            <c:choose>
                                <c:when test="${order.status eq 'Completed'}">
                                    <button class="btn btn-edit btn-edit-disabled" onclick="showEditWarning('${order.status}')">
                                        Sửa
                                    </button>
                                </c:when>
                                <c:otherwise>
                                    <a class="btn btn-edit btn-edit" href="editPurchaseOrder?orderId=${order.purchaseOrderId}">
                                        Sửa
                                    </a>
                                </c:otherwise>
                            </c:choose>
                            <a class="btn btn-history btn-history" href="purchaseOrderHistory?orderId=${order.purchaseOrderId}">
                                Lịch sử
                            </a>
                        </td>
                    </tr>
                </c:forEach>
                <c:if test="${empty purchaseOrders}">
                    <tr>
                        <td colspan="7" class="no-data">Không có dữ liệu</td>
                    </tr>
                </c:if>
            </tbody>
        </table>

        <!-- Phân trang -->
        <div class="pagination">
          <div class="pagination-form">
            <c:choose>
              <c:when test="${currentPage > 1}">
                <a href="purchaseOrderList?page=${currentPage - 1}&fromDate=${fromDate}&toDate=${toDate}&status=${status}&supplier=${supplier}&sortOrder=${param.sortOrder}" class="page-button" aria-label="Trang trước">&laquo;</a>
              </c:when>
              <c:otherwise>
                <button class="page-button" disabled aria-label="Trang trước">&laquo;</button>
              </c:otherwise>
            </c:choose>
            <c:forEach var="i" begin="1" end="${totalPages}">
              <a href="purchaseOrderList?page=${i}&fromDate=${fromDate}&toDate=${toDate}&status=${status}&supplier=${supplier}&sortOrder=${param.sortOrder}" class="page-button${i == currentPage ? ' active' : ''}">${i}</a>
            </c:forEach>
            <c:choose>
              <c:when test="${currentPage < totalPages}">
                <a href="purchaseOrderList?page=${currentPage + 1}&fromDate=${fromDate}&toDate=${toDate}&status=${status}&supplier=${supplier}&sortOrder=${param.sortOrder}" class="page-button" aria-label="Trang sau">&raquo;</a>
              </c:when>
              <c:otherwise>
                <button class="page-button" disabled aria-label="Trang sau">&raquo;</button>
              </c:otherwise>
            </c:choose>
          </div>
          <div class="pagination-info">
            Tổng số đơn mua: ${totalOrders}
          </div>
        </div>


    </div>

    <script>
        // Xử lý hiển thị/ẩn date picker
        function toggleDateInputs() {
            const dateRangeSelect = document.getElementById('dateRangeSelect');
            const dateInputs = document.getElementById('dateInputs');
            if (!dateInputs) return; // Fix lỗi null
            if (dateRangeSelect.value === 'custom') {
                dateInputs.classList.add('show');
            } else {
                dateInputs.classList.remove('show');
            }
        }
        
        // Khởi tạo khi trang load
        document.addEventListener('DOMContentLoaded', function() {
            toggleDateInputs();
        });
        
        // Xử lý duyệt đơn
        function approveOrder(orderId) {
            if (confirm('Bạn có chắc muốn duyệt đơn này?')) {
                fetch('purchaseOrderList?action=approve&orderId=' + orderId, {
                    method: 'POST'
                })
                .then(response => response.json())
                .then(data => {
                    if (data.success) {
                        alert('Đã duyệt đơn thành công');
                        location.reload();
                    } else {
                        alert('Có lỗi xảy ra: ' + data.message);
                    }
                })
                .catch(error => {
                    console.error('Error:', error);
                    alert('Có lỗi xảy ra khi duyệt đơn');
                });
            }
        }

        // Xử lý từ chối đơn
        function rejectOrder(orderId) {
            if (confirm('Bạn có chắc muốn từ chối đơn này?')) {
                fetch('purchaseOrderList?action=reject&orderId=' + orderId, {
                    method: 'POST'
                })
                .then(response => response.json())
                .then(data => {
                    if (data.success) {
                        alert('Đã từ chối đơn');
                        location.reload();
                    } else {
                        alert('Có lỗi xảy ra: ' + data.message);
                    }
                })
                .catch(error => {
                    console.error('Error:', error);
                    alert('Có lỗi xảy ra khi từ chối đơn');
                });
            }
        }

        // Hiển thị thông báo khi không thể sửa đơn
        function showEditWarning(status) {
            let message = '';
            if (status === 'Approved' || status === 'Đã duyệt') {
                message = 'Không thể sửa đơn đã được duyệt!';
            } else if (status === 'Rejected' || status === 'Từ chối') {
                message = 'Không thể sửa đơn đã bị từ chối!';
            } else {
                message = 'Không thể sửa đơn với trạng thái này!';
            }
            alert(message);
        }
    </script>
    <script>
window.onerror = function(message, source, lineno, colno, error) {
    var errorDiv = document.createElement('div');
    errorDiv.className = 'js-error-alert';
    errorDiv.innerText = 'Lỗi JS: ' + message + ' (tại dòng ' + lineno + ')';
    document.body.prepend(errorDiv);
    return false;
};
</script>
</body>
</html> 