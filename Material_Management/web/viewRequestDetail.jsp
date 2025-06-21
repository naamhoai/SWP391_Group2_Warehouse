
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Chi tiết yêu cầu vật tư</title>
        <link rel="stylesheet" href="css/viewRequestDetail.css">
        
    </head>
    <body>
        <div class="request-detail-container">
            <h2>Chi tiết yêu cầu vật tư</h2>
            <div class="request-info">
                <label><b>Mã yêu cầu:</b></label> ${request.requestId}<br>
                <label><b>Loại yêu cầu:</b></label> ${request.requestType}<br>
                <label><b>Người yêu cầu:</b></label> ${requesterName}<br>
                <label><b>Lý do:</b></label> ${request.reason}<br>
                <label><b>Trạng thái:</b></label> ${request.requestStatus}<br>
                <label><b>Thời gian tạo:</b></label> ${request.createdAt}<br>
                <c:if test="${request.requestStatus eq 'Approved' or request.requestStatus eq 'Rejected'}">
                    <c:if test="${not empty request.directorNote}">
                        <label><b>Ghi chú của giám đốc:</b></label> ${request.directorNote}<br>
                    </c:if>
                </c:if>
            </div>
            <h4>Danh sách vật tư</h4>
            <table class="request-detail-table">
                <thead>
                    <tr>
                        <th>Tên vật tư</th>
                        <th>Số lượng</th>
                        <th>Đơn vị</th>
                        <th>Tình trạng</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="d" items="${details}">
                        <tr>
                            <td>${d.materialName}</td>
                            <td>${d.quantity}</td>
                            <td>${d.unitName}</td>
                            <td>${d.materialCondition}</td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>

            <!-- Phần xử lý cho giám đốc -->
            <c:if test="${request.requestStatus eq 'Pending' && sessionScope.roleId == 2}">
                <form action="approveOrRejectRequest" method="post" class="approve-reject-form">
                    <input type="hidden" name="requestId" value="${request.requestId}">
                    <label for="directorNote" class="form-label"><b>Lý do (bắt buộc):</b></label>
                    <textarea name="directorNote" id="directorNote" required class="input-reason" placeholder="Nhập lý do phê duyệt hoặc từ chối..."></textarea>
                    <div class="action-btn-group">
                        <button type="submit" name="action" value="approve" class="btn-approve">Phê duyệt</button>
                        <button type="submit" name="action" value="reject" class="btn-reject">Từ chối</button>
                    </div>
                </form>
            </c:if>

            <!-- Phần xử lý cho nhân viên kho -->
            <c:if test="${request.requestStatus eq 'Approved' && sessionScope.roleId == 3}">
                <div class="mt-3">
                    <a href="createPurchaseOrder?requestId=${request.requestId}" class="btn-create-order">
                        <i class="fas fa-plus"></i> Tạo đơn nhập hàng
                    </a>
                </div>
            </c:if>

            
            <!-- Nút quay lại -->
            <div class="mt-3">
                <c:choose>
                    <c:when test="${sessionScope.roleId == 2}">
                        <a href="director" class="btn-back">Quay lại</a>
                    </c:when>
                    <c:when test="${sessionScope.roleId == 3}">
                        <a href="warehouseEmployeeDashboard" class="btn-back">Quay lại</a>
                    </c:when>
                    <c:otherwise>
                        <a href="javascript:history.back()" class="btn-back">Quay lại</a>
                    </c:otherwise>
                </c:choose>
            </div>
        </div>
    </body>
</html>