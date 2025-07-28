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
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" rel="stylesheet">
    
</head>
<body>
    <jsp:include page="sidebar.jsp" />
    <div class="purchase-order-list-container">
        <div style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 30px;">
            <h1 class="purchase-order-list-title">Danh Sách Đơn Mua Vật Tư</h1>
            <a href="createPurchaseOrder" class="create-order-btn">
                <i class="fas fa-plus"></i> Tạo đơn mua mới
            </a>
        </div>
        
        <!-- Thông báo thành công/lỗi -->
        <c:if test="${not empty successMessage}">
            <div style="background: #d4edda; border: 1px solid #c3e6cb; color: #155724; padding: 10px; margin-bottom: 20px; border-radius: 5px;">
                <strong>Thành công!</strong> ${successMessage}
            </div>
        </c:if>
        <c:if test="${param.error == 'true'}">
            <div style="background: #f8d7da; border: 1px solid #f5c6cb; color: #721c24; padding: 10px; margin-bottom: 20px; border-radius: 5px;">
                <strong>Lỗi!</strong> ${param.message}
            </div>
        </c:if>
        <c:if test="${param.success == 'true' && not empty param.message}">
            <div style="background:#d4edda; color:#155724; border:1px solid #c3e6cb; padding:15px; margin-bottom:20px; border-radius:8px; display:flex; align-items:center;">
                <i class="fas fa-check-circle" style="margin-right:8px;"></i>
                <strong>Thành công!</strong> ${param.message}
            </div>
        </c:if>
        <c:if test="${param.msg == 'approved'}">
            <div style="background:#d4edda;color:#155724;padding:12px 24px;border-radius:8px;margin-bottom:18px;font-weight:600;">
                Đơn mua đã được duyệt thành công!
            </div>
        </c:if>
        <c:if test="${param.msg == 'rejected'}">
            <div style="background:#f8d7da;color:#721c24;padding:12px 24px;border-radius:8px;margin-bottom:18px;font-weight:600;">
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
                        <label for="sortOrder" style="display:block;">Sắp xếp ID</label>
                        <select name="sortOrder" id="sortOrder" style="width:100%;padding:7px 12px;border:1.5px solid #e3e3e3;border-radius:6px;font-size:15px;background:#f8fafd;margin-bottom:2px;" onchange="this.form.submit()">
                            <option value="desc" ${param.sortOrder == 'desc' || empty param.sortOrder ? 'selected' : ''}>Giảm dần</option>
                            <option value="asc" ${param.sortOrder == 'asc' ? 'selected' : ''}>Tăng dần</option>
                        </select>
                    </div>
                    <div>
                        <label>&nbsp;</label>
                        <button type="submit" class="btn btn-primary" style="display:none;">Tìm kiếm</button>
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
                                    <span style="color:#d32f2f;font-weight:600;">${order.rejectionReason}</span>
                                </c:when>
                                <c:when test="${order.status eq 'Approved' || order.status eq 'Đã duyệt'}">
                                    <span style="color:#388e3c;font-weight:600;">${order.note}</span>
                                </c:when>
                                <c:otherwise>
                                    ${order.note}
                                </c:otherwise>
                            </c:choose>
                        </td>
                        <td>
                            <c:choose>
                                <c:when test="${order.status eq 'Completed'}">
                                    <span style="background:#d4f5e9;color:#2e7d32;padding:4px 12px;border-radius:8px;font-weight:600;">Đã duyệt</span>
                                </c:when>
                                <c:when test="${order.status eq 'Pending'}">
                                    <span style="background:#fff8e1;color:#fbc02d;padding:4px 12px;border-radius:8px;font-weight:600;">Chờ duyệt</span>
                                </c:when>
                                <c:when test="${order.status eq 'Rejected'}">
                                    <span style="background:#ffebee;color:#d32f2f;padding:4px 12px;border-radius:8px;font-weight:600;">Từ chối</span>
                                </c:when>
                                <c:otherwise>
                                    <span>${order.status}</span>
                                </c:otherwise>
                            </c:choose>
                        </td>
                        <td>
                            <a class="btn btn-primary" style="padding: 5px 10px; font-size: 12px; margin-left:0;" href="purchaseOrderDetail?id=${order.purchaseOrderId}">
                                <i class="fas fa-eye"></i> Chi tiết
                            </a>
                            <c:choose>
                                <c:when test="${order.status eq 'Rejected'}">
                                    <a class="btn btn-warning" style="padding: 5px 10px; font-size: 12px; margin-left:4px; color:#fff;" href="editPurchaseOrder?orderId=${order.purchaseOrderId}">
                                        <i class="fas fa-edit"></i> Sửa
                                    </a>
                                </c:when>
                                <c:otherwise>
                                    <button class="btn btn-warning" style="padding: 5px 10px; font-size: 12px; margin-left:4px; color:#fff;" onclick="showEditWarning('${order.status}')">
                                        <i class="fas fa-edit"></i> Sửa
                                    </button>
                                </c:otherwise>
                            </c:choose>
                            
                        </td>
                    </tr>
                </c:forEach>
                <c:if test="${empty purchaseOrders}">
                    <tr>
                        <td colspan="7" style="text-align: center; padding: 20px;">Không có dữ liệu</td>
                    </tr>
                </c:if>
            </tbody>
        </table>

        <!-- Phân trang -->
        <div class="pagination" style="flex-direction: column; align-items: center; gap: 8px; margin-top: 32px;">
          <div class="pagination-form" style="justify-content: center;">
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
          <div class="pagination-info" style="margin-top: 8px; color: #6b7280; font-size: 16px; font-weight: 500;">
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
            // Chuyển đổi trạng thái từ tiếng Anh sang tiếng Việt
            let statusInVietnamese = status;
            switch(status) {
                case 'Pending':
                    statusInVietnamese = 'Chờ duyệt';
                    break;
                case 'Approved':
                    statusInVietnamese = 'Đã duyệt';
                    break;
                case 'Rejected':
                    statusInVietnamese = 'Từ chối';
                    break;
                case 'Completed':
                    statusInVietnamese = 'Hoàn thành';
                    break;
                case 'Cancelled':
                    statusInVietnamese = 'Đã hủy';
                    break;
                default:
                    statusInVietnamese = status; // Giữ nguyên nếu không tìm thấy mapping
            }
            alert('Chỉ có thể chỉnh sửa đơn hàng bị từ chối! Trạng thái hiện tại: ' + statusInVietnamese);
        }
    </script>
    <script>
window.onerror = function(message, source, lineno, colno, error) {
    var errorDiv = document.createElement('div');
    errorDiv.style.background = '#ffebee';
    errorDiv.style.color = '#b71c1c';
    errorDiv.style.padding = '12px 18px';
    errorDiv.style.margin = '12px 0 12px auto';
    errorDiv.style.border = '1px solid #f44336';
    errorDiv.style.borderRadius = '8px';
    errorDiv.style.fontWeight = 'bold';
    errorDiv.style.fontSize = '15px';
    errorDiv.style.width = '420px';
    errorDiv.style.maxWidth = '90vw';
    errorDiv.style.textAlign = 'right';
    errorDiv.style.boxShadow = '0 2px 8px rgba(244,67,54,0.08)';
    errorDiv.innerText = 'Lỗi JS: ' + message + ' (tại dòng ' + lineno + ')';
    document.body.prepend(errorDiv);
    return false;
};
</script>
</body>
</html> 