<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
    <title>Chi tiết đơn mua</title>
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" rel="stylesheet">
    <style>
        body { font-family: 'Segoe UI', Arial, sans-serif; background: #f8f9fa; }
        .container { background: #fff; margin: 30px auto; padding: 30px; border-radius: 14px; max-width: 1100px; box-shadow: 0 2px 8px #ccc; }
        h1 { color: #1976d2; font-size: 32px; font-weight: bold; margin-bottom: 24px; }
        .info-box { border: 1.5px solid #e3e3e3; border-radius: 10px; padding: 24px 32px; margin-bottom: 32px; background: #fff; }
        .info-title { font-size: 20px; font-weight: bold; margin-bottom: 18px; border-bottom: 2px solid #1976d2; padding-bottom: 8px; }
        .info-row { display: flex; flex-wrap: wrap; }
        .info-col { flex: 1; min-width: 320px; margin-bottom: 10px; }
        .info-label { color: #1976d2; font-weight: 600; }
        .info-value { color: #222; font-weight: 500; margin-left: 6px; }
        .badge { display: inline-block; padding: 4px 18px; border-radius: 16px; font-size: 15px; font-weight: 600; color: #fff; }
        .badge-approved { background: #4caf50; }
        .badge-pending { background: #ffb300; color: #333; }
        .badge-rejected { background: #f44336; }
        .table-box { background: #fff; border-radius: 10px; box-shadow: 0 1px 4px #e0e0e0; padding: 18px 0 0 0; margin-bottom: 24px; }
        table { width: 100%; border-collapse: separate; border-spacing: 0; border-radius: 10px; overflow: hidden; }
        th, td { border: 1px solid #e0e0e0; padding: 10px 8px; text-align: center; background: #fff; }
        th { background: #f1f1f1; font-weight: bold; }
        tr:nth-child(even) td { background: #f8fafd; }
        .badge-status { background: #e3fcec; color: #388e3c; border-radius: 12px; padding: 2px 14px; font-size: 14px; font-weight: 600; }
        .btn-back { background: #757575; color: #fff; border: none; border-radius: 6px; padding: 10px 28px; font-size: 16px; font-weight: 500; cursor: pointer; margin-top: 18px; }
        .btn-back:hover { background: #333; }
        
        /* CSS cho phần lý do */
        .reason-section { 
            background: #f8f9fa; 
            border: 1px solid #e3e3e3; 
            border-radius: 12px; 
            padding: 24px; 
            margin: 24px 0; 
            box-shadow: 0 2px 8px rgba(0,0,0,0.05); 
        }
        .reason-label { 
            display: block; 
            color: #1976d2; 
            font-weight: 600; 
            font-size: 16px; 
            margin-bottom: 12px; 
            padding-bottom: 8px; 
            border-bottom: 2px solid #e3f2fd; 
        }
        .reason-textarea { 
            width: 100%; 
            max-width: 600px; 
            min-height: 80px; 
            padding: 12px 16px; 
            border: 2px solid #e3e3e3; 
            border-radius: 8px; 
            font-family: inherit; 
            font-size: 14px; 
            line-height: 1.5; 
            resize: vertical; 
            transition: border-color 0.3s ease, box-shadow 0.3s ease; 
        }
        .reason-textarea:focus { 
            outline: none; 
            border-color: #1976d2; 
            box-shadow: 0 0 0 3px rgba(25, 118, 210, 0.1); 
        }
        .reason-textarea::placeholder { 
            color: #999; 
            font-style: italic; 
        }
        .action-buttons { 
            margin-top: 20px; 
            display: flex; 
            gap: 12px; 
            flex-wrap: wrap; 
        }
        .btn-approve { 
            background: linear-gradient(135deg, #4caf50, #45a049); 
            color: #fff; 
            border: none; 
            border-radius: 8px; 
            padding: 12px 32px; 
            font-size: 16px; 
            font-weight: 600; 
            cursor: pointer; 
            transition: all 0.3s ease; 
            box-shadow: 0 2px 4px rgba(76, 175, 80, 0.3); 
        }
        .btn-approve:hover { 
            background: linear-gradient(135deg, #45a049, #3d8b40); 
            transform: translateY(-1px); 
            box-shadow: 0 4px 8px rgba(76, 175, 80, 0.4); 
        }
        .btn-reject { 
            background: linear-gradient(135deg, #f44336, #d32f2f); 
            color: #fff; 
            border: none; 
            border-radius: 8px; 
            padding: 12px 32px; 
            font-size: 16px; 
            font-weight: 600; 
            cursor: pointer; 
            transition: all 0.3s ease; 
            box-shadow: 0 2px 4px rgba(244, 67, 54, 0.3); 
        }
        .btn-reject:hover { 
            background: linear-gradient(135deg, #d32f2f, #c62828); 
            transform: translateY(-1px); 
            box-shadow: 0 4px 8px rgba(244, 67, 54, 0.4); 
        }
        .note-highlight {
            display: inline-block;
            background: #e3f2fd;
            color: #1976d2;
            border-radius: 8px;
            padding: 6px 16px;
            margin-left: 8px;
            font-size: 15px;
            font-style: italic;
            min-width: 80px;
            box-shadow: 0 1px 4px #e0e0e0;
        }
        .note-empty {
            color: #888;
            background: #f5f5f5;
            font-style: italic;
        }
    </style>
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
                            <i class="fas fa-sticky-note" style="color:#1976d2;margin-right:6px;"></i>
                            <span style="color:#1976d2;font-weight:500;">${order.note}</span>
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
                        <td><fmt:formatNumber value="${detail.unitPrice}" type="number" groupingUsed="true"/> VNĐ</td>
                    </tr>
                </c:forEach>
                <c:if test="${empty details}">

                    <tr><td colspan="6" style="text-align:center;">Không có dữ liệu</td></tr>

                    <tr><td colspan="5" class="no-data">Không có dữ liệu</td></tr>

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
                            <i class="fas fa-comment-alt" style="margin-right: 8px;"></i>
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
                                <i class="fas fa-check" style="margin-right: 8px;"></i>Duyệt
                            </button>
                            <button type="submit" name="action" value="reject" class="btn-reject">
                                <i class="fas fa-times" style="margin-right: 8px;"></i>Từ chối
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
                <div class="reason-section" style="background: #fff3cd; border-color: #ffc107;">
                    <div style="display: flex; align-items: center; color: #856404; font-weight: 600; font-size: 16px;">
                        <i class="fas fa-clock" style="margin-right: 12px; font-size: 20px;"></i>
                        Đơn này đang chờ giám đốc phê duyệt. Bạn chỉ có thể xem thông tin.
                    </div>
                </div>
            </c:otherwise>
        </c:choose>
    </c:if>
    <%-- Thông báo trạng thái phê duyệt --%>
    <c:choose>
        <c:when test="${order.status == 'Đã duyệt'}">
            <div style="background: #d4edda; border: 1px solid #c3e6cb; color: #155724; padding: 10px; margin-bottom: 20px; border-radius: 5px;">
                <i class="fas fa-check-circle"></i> Đơn này đã được duyệt. Bạn chỉ có thể xem thông tin.
            </div>
        </c:when>
        <c:when test="${order.status == 'Từ chối'}">
            <div style="background: #f8d7da; border: 1px solid #f5c6cb; color: #721c24; padding: 10px; margin-bottom: 20px; border-radius: 5px;">
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
    <button class="btn-back" onclick="window.history.back()" style="display: flex; align-items: center; gap: 8px;">
        <i class="fas fa-arrow-left"></i> Quay lại
    </button>
</div>
</body>
</html> 