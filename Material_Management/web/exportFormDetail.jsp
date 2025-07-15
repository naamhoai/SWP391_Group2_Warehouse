<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="vi">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Chi tiết phiếu xuất kho</title>
        <link rel="stylesheet" href="css/requestHistory.css">
        <link rel="stylesheet" href="css/sidebar.css">
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">
    </head>
    <body>
        <div class="sidebar">
            <jsp:include page="sidebar.jsp" />
        </div>
        <div class="container request-history-detail-container">
            <div class="content">
                <c:if test="${param.success eq 'true'}">
                    <div class="alert success">
                        <i class="fas fa-check-circle"></i> Xuất kho thành công!
                    </div>
                </c:if>
                <div class="page-header">
                    <h1><i class="fas fa-truck"></i> Chi tiết phiếu xuất kho</h1>
                </div>
                <c:if test="${not empty error}">
                    <div style="color:red;">${error}</div>
                </c:if>
                <c:if test="${not empty exportForm}">
                    <div class="history-info">
                        <div class="info-section">
                            <h3>Thông tin phiếu xuất kho</h3>
                            <div class="info-grid-2col">
                                <div class="info-item"><label>Mã phiếu:</label><span>${exportForm.exportId}</span></div>
                                <div class="info-item"><label>Người tạo:</label><span>${exportForm.userName}</span></div>
                                <div class="info-item"><label>Ngày xuất:</label><span><fmt:formatDate value="${exportForm.exportDate}" pattern="dd/MM/yyyy HH:mm:ss"/></span></div>
                                <div class="info-item"><label>Trạng thái:</label><span>${exportForm.status}</span></div>
                                <c:if test="${not empty exportForm.description}">
                                    <div class="info-item"><label>Ghi chú:</label><span>${exportForm.description}</span></div>
                                </c:if>
                            </div>
                        </div>
                    </div>
                    <div class="details-section">
                        <h3>Danh sách vật tư xuất kho</h3>
                        <div class="table-container">
                            <table class="details-table">
                                <thead>
                                    <tr>
                                        <th>STT</th>
                                        <th>Mã vật tư</th>
                                        <th>Tên vật tư</th>
                                        <th>Đơn vị</th>
                                        <th>Số lượng</th>
                                        <th>Tình trạng</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <c:forEach var="m" items="${exportMaterials}" varStatus="status">
                                        <tr>
                                            <td>${status.index + 1}</td>
                                            <td>${m.materialId}</td>
                                            <td>${m.materialName}</td>
                                            <td>${m.unitName}</td>
                                            <td>${m.quantity}</td>
                                            <td><span class="condition-badge condition-${m.materialCondition}">${m.materialCondition}</span></td>
                                        </tr>
                                    </c:forEach>
                                </tbody>
                            </table>
                        </div>
                    </div>
                    <div class="details-section">
                        <h3>Thông tin vận chuyển</h3>
                        <c:choose>
                            <c:when test="${not empty delivery}">
                                <div class="info-section">
                                    <div class="info-grid-2col">
                                        <div class="info-item"><label>Tên dự án:</label><span>${delivery.recipientName}</span></div>
                                        <div class="info-item"><label>Người nhận:</label><span>${delivery.contactPerson}</span></div>
                                        <div class="info-item"><label>Số điện thoại:</label><span>${delivery.contactPhone}</span></div>
                                        <div class="info-item"><label>Địa chỉ giao:</label><span>${delivery.deliveryAddress}</span></div>
                                        <div class="info-item"><label>Trạng thái giao:</label><span>${delivery.status}</span></div>
                                        <div class="info-item"><label>Ngày giao:</label><span><fmt:formatDate value="${delivery.deliveryDate}" pattern="dd/MM/yyyy HH:mm:ss"/></span></div>
                                        <div class="info-item"><label>Ghi chú:</label><span>${delivery.description}</span></div>
                                    </div>
                                </div>
                            </c:when>
                            <c:otherwise>
                                <div class="no-data"><i class="fas fa-info-circle"></i> Chưa có thông tin vận chuyển.</div>
                            </c:otherwise>
                        </c:choose>
                    </div>
                </c:if>
            </div>
            <a href="exportFormHistory" class="btn-back">
                <i class="fas fa-arrow-left"></i> Quay lại lịch sử xuất kho
            </a>
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