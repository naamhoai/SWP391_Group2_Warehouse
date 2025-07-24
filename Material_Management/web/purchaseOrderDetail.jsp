<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
    <title>Chi tiết đơn mua</title>
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" rel="stylesheet">
    <link rel="stylesheet" href="css/purchaseOrderDetail.css">
</head>
<body>
<div class="container">
    <h1>Chi tiết đơn mua</h1>
    <div class="info-box">
        <div class="info-title">Thông tin đơn mua</div>
        <div class="info-row">
            <div class="info-col">
                <span class="info-label">Mã đơn:</span><span class="info-value">${order.purchaseOrderId}</span><br/>
                <span class="info-label">Người tạo:</span><span class="info-value">${order.creatorName}</span><br/>
                <span class="info-label">Vai trò:</span><span class="info-value">${order.creatorRoleName}</span><br/>
                <span class="info-label">Ngày tạo:</span><span class="info-value"><fmt:formatDate value="${order.orderDate}" pattern="dd/MM/yyyy"/></span><br/>
                <span class="info-label">Nhà cung cấp:</span><span class="info-value">${order.supplierName}</span><br/>
                <span class="info-label">Người liên hệ:</span><span class="info-value">${order.contactPerson}</span><br/>
                <span class="info-label">Số điện thoại:</span><span class="info-value">${order.supplierPhone}</span><br/>
            </div>
            <div class="info-col">
                <span class="info-label">Trạng thái:</span>
                <c:choose>
                    <c:when test="${order.status eq 'Completed'}">
                        <span class="badge badge-approved">Đã duyệt</span>
                    </c:when>
                    <c:when test="${order.status eq 'Pending'}">
                        <span class="badge badge-pending">Chờ duyệt</span>
                    </c:when>
                    <c:when test="${order.status eq 'Rejected'}">
                        <span class="badge badge-rejected">Từ chối</span>
                    </c:when>
                    <c:otherwise>
                        <span class="badge">${order.status}</span>
                    </c:otherwise>
                </c:choose><br/>
                <span class="info-label">Tổng tiền:</span><span class="info-value"><fmt:formatNumber value="${order.totalAmount}" type="number" groupingUsed="true"/></span><br/>
                <span class="info-label">Ghi chú:</span>
                <c:choose>
                    <c:when test="${not empty order.note}">
                        <span class="note-highlight">
                            <i class="fas fa-sticky-note icon-blue"></i>
                            <span class="note-blue">${order.note}</span>
                        </span>
                    </c:when>
                    <c:otherwise>
                        <span class="note-highlight note-empty">Không có ghi chú</span>
                    </c:otherwise>
                </c:choose>
            </div>
        </div>
    </div>
    <div class="table-box">
        <div class="info-title">Chi tiết vật tư</div>
        <table>
            <thead>
                <tr>
                    <th>STT</th>
                    <th>Tên vật tư</th>
                    <th>Số lượng</th>
                    <th>Đơn vị</th>
                    <th>Đơn vị gốc</th>
                    <th>Đơn giá</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach var="detail" items="${details}" varStatus="stt">
                    <tr>
                        <td>${stt.index + 1}</td>
                        <td>${detail.materialName}</td>
                        <td>${detail.quantity}</td>
                        <td>${detail.unit}</td>
                        <td>${detail.convertedUnit}</td>
                        <td><fmt:formatNumber value="${detail.unitPrice}" type="number" groupingUsed="true"/> VNĐ</td>
                    </tr>
                </c:forEach>
                <c:if test="${empty details}">
                    <tr><td colspan="6" class="no-data">Không có dữ liệu</td></tr>
                </c:if>
            </tbody>
        </table>
    </div>
    <%-- Lấy roleName từ session --%>
    <%
        String roleName = null;
        if (session.getAttribute("Admin") != null) {
            roleName = ((model.User)session.getAttribute("Admin")).getRole().getRolename();
        } else if (session.getAttribute("user") != null) {
            roleName = ((model.User)session.getAttribute("user")).getRole().getRolename();
        } else if (session.getAttribute("staff") != null) {
            roleName = ((model.User)session.getAttribute("staff")).getRole().getRolename();
        }
        request.setAttribute("roleName", roleName);
    %>
    <c:if test="${order.status eq 'Pending' || order.status eq 'Chờ duyệt'}">
        <c:choose>
            <c:when test="${roleName eq 'Giám đốc' || roleName eq 'Director'}">
                <!-- Chỉ giám đốc mới thấy nút duyệt/từ chối -->
                <div class="reason-section">
                    <form id="approveRejectForm" method="post" action="purchaseOrderList">
                        <input type="hidden" name="orderId" value="${order.purchaseOrderId}" />
                        <label for="reason" class="reason-label">
                            <i class="fas fa-comment-alt icon-mr-8"></i>
                            Lý do (không bắt buộc khi duyệt, bắt buộc khi từ chối)
                        </label>
                        <textarea 
                            id="reason" 
                            name="reason" 
                            class="reason-textarea" 
                            placeholder="Nhập lý do phê duyệt hoặc từ chối đơn mua này..."
                            rows="3"></textarea>
                        <div class="action-buttons">
                            <button type="submit" name="action" value="approve" class="btn-approve">
                                <i class="fas fa-check icon-mr-8"></i>Duyệt
                            </button>
                            <button type="submit" name="action" value="reject" class="btn-reject">
                                <i class="fas fa-times icon-mr-8"></i>Từ chối
                            </button>
                        </div>
                    </form>
                </div>
                <script>
                document.getElementById('approveRejectForm').addEventListener('submit', function(e) {
                    var action = document.activeElement.value;
                    var reason = document.getElementById('reason').value.trim();
                    if(action === 'reject' && reason === '') {
                        e.preventDefault();
                        alert('Vui lòng nhập lý do khi từ chối!');
                    }
                });
                </script>
            </c:when>
            <c:otherwise>
                <!-- Người dùng khác chỉ thấy thông báo -->
                <div class="reason-section">
                    <div class="reason-header">
                        <i class="fas fa-clock icon-clock"></i>
                        Đơn này đang chờ giám đốc phê duyệt. Bạn chỉ có thể xem thông tin.
                    </div>
                </div>
            </c:otherwise>
        </c:choose>
    </c:if>
    <%-- Thông báo trạng thái phê duyệt --%>
    <c:choose>
        <c:when test="${order.status == 'Đã duyệt'}">
            <div class="alert-success">
                <i class="fas fa-check-circle"></i> Đơn này đã được duyệt. Bạn chỉ có thể xem thông tin.
            </div>
        </c:when>
        <c:when test="${order.status == 'Từ chối'}">
            <div class="alert-error">
                <i class="fas fa-times-circle"></i> Đơn này đã bị từ chối. Bạn chỉ có thể xem thông tin.
                <c:if test="${not empty order.rejectionReason}">
                    <br/><b>Lý do từ chối:</b> ${order.rejectionReason}
                </c:if>
            </div>
        </c:when>
    </c:choose>
    <c:if test="${not empty param.success}">
        <script>
            alert('${param.success}');
            window.location.href = 'purchaseOrderList.jsp';
        </script>
    </c:if>
    <button class="btn-back" onclick="window.history.back()">
        <i class="fas fa-arrow-left"></i> Quay lại
    </button>
</div>
</body>
</html> 