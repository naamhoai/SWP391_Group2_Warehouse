<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html>
<html lang="vi">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Chi tiết yêu cầu vật tư</title>
        <link rel="stylesheet" href="css/requestHistory.css">
        <link rel="stylesheet" href="css/header.css">
        <link rel="stylesheet" href="css/sidebar.css">
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">
    </head>
    <body>
        <div class="sidebar">
            <jsp:include page="sidebar.jsp" />
        </div>
        <div class="main-content">

            <div class="container request-history-detail-container">
                <div class="page-header">
                    <c:if test="${not empty param.success}">
                        <div class="alert success">
                            <i class="fas fa-check-circle"></i> ${param.success}
                        </div>
                    </c:if>
                    <h1><i class="fas fa-file-alt"></i> Chi tiết yêu cầu vật tư</h1>

                </div>

                <c:if test="${request != null}">
                    <div class="history-info">
                        <div class="info-section">
                            <h3>Thông tin yêu cầu</h3>
                            <div class="info-grid-2col">
                                <div class="info-item">
                                    <label>Mã yêu cầu:</label>
                                    <span>${request.requestId}</span>
                                </div>
                                <div class="info-item">
                                    <label>Trạng thái:</label>
                                    <span class="status-badge status-${request.requestStatus}">${request.requestStatus}</span>
                                </div>
                                <div class="info-item">
                                    <label>Thời gian tạo:</label>
                                    <span><fmt:formatDate value="${request.createdAt}" pattern="dd/MM/yyyy HH:mm:ss"/></span>
                                </div>
                                <div class="info-item">
                                    <label>Người yêu cầu:</label>
                                    <span>${requesterName}</span>
                                </div>
                                <div class="info-item">
                                    <label>Tên dự án:</label>
                                    <span>${request.recipientName}</span>
                                </div>
                                <div class="info-item">
                                    <label>Địa chỉ giao hàng:</label>
                                    <span>${request.deliveryAddress}</span>
                                </div>
                                <div class="info-item">
                                    <label>Người liên hệ:</label>
                                    <span>${request.contactPerson}</span>
                                </div>
                                <div class="info-item">
                                    <label>SĐT người liên hệ:</label>
                                    <span>${request.contactPhone}</span>
                                </div>
                                <div class="info-item">
                                    <label>Lý do:</label>
                                    <span>${request.reason}</span>
                                </div>
                                <c:if test="${not empty request.directorNote}">
                                    <div class="info-item">
                                        <label>Lý do của giám đốc:</label>
                                        <span>${request.directorNote}</span>
                                    </div>
                                </c:if>
                            </div>
                        </div>
                    </div>

                    <div class="details-section">

                        <h3>Danh sách vật tư</h3>
                        <c:if test="${not empty details}">
                            <div class="table-container">
                                <table class="details-table">
                                    <thead>
                                        <tr>
                                            <th>STT</th>
                                            <th>Tên vật tư</th>
                                            <th>Số lượng</th>
                                            <th>Đơn vị</th>
                                            <th>Tình trạng</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <c:forEach var="d" items="${details}" varStatus="status">
                                            <tr>
                                                <td>${status.index + 1}</td>
                                                <td>${d.materialName}</td>
                                                <td>${d.quantity}</td>
                                                <td>${d.unitName}</td>
                                                <td>
                                                    <span class="condition-badge condition-${d.materialCondition}">
                                                        ${d.materialCondition}
                                                    </span>
                                                </td>
                                            </tr>
                                        </c:forEach>
                                    </tbody>
                                </table>
                            </div>
                        </c:if>
                        <c:if test="${empty details}">
                            <div class="no-data">
                                <i class="fas fa-info-circle"></i>
                                <p>Không có chi tiết vật tư cho yêu cầu này.</p>
                            </div>
                        </c:if>
                    </div>

                    <c:if test="${request.requestStatus eq 'Chờ duyệt' && sessionScope.roleId == 2}">
                        <div class="details-section">
                            <h3>Phê duyệt yêu cầu</h3>
                            <form action="approveOrRejectRequest" method="post" class="approve-reject-form">
                                <input type="hidden" name="requestId" value="${request.requestId}">
                                <div class="form-group">
                                    <label for="directorNote" class="form-label"><b>Lý do (bắt buộc):</b></label>
                                    <textarea name="directorNote" id="directorNote" required class="input-reason" placeholder="Nhập lý do phê duyệt hoặc từ chối..."></textarea>
                                </div>
                                <div class="action-btn-group">
                                    <button type="submit" name="action" value="approve" class="btn-approve">
                                        <i class="fas fa-check"></i> Phê duyệt
                                    </button>
                                    <button type="submit" name="action" value="reject" class="btn-reject">
                                        <i class="fas fa-times"></i> Từ chối
                                    </button>
                                </div>
                            </form>
                        </div>
                    </c:if>

                    <c:if test="${request.requestStatus eq 'Đã duyệt' && sessionScope.roleId == 3}">
                        <div class="details-section">
                            <h3>Hành động</h3>
                            <div class="action-btn-group">
                                <a href="createExportForm?requestId=${request.requestId}" class="btn-create-order">
                                    <i class="fas fa-plus"></i> Tạo đơn xuất kho
                                </a>
                            </div>
                        </div>
                    </c:if>
                </c:if>

                <a href="javascript:history.back()" class="btn-back">
                    <i class="fas fa-arrow-left"></i> Quay lại
                </a>



            </div>
        </div>
        <script>
            
            document.addEventListener('DOMContentLoaded', function () {
                const successAlert = document.querySelector('.alert.success');
                if (successAlert) {
                    setTimeout(() => {
                        successAlert.style.opacity = '0';
                        setTimeout(() => {
                            successAlert.remove();
                        }, 300);
                    }, 5000);
                }
            });
        </script>
    </body>
</html>