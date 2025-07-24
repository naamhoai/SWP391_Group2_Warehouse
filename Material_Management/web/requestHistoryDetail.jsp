<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html>
<html lang="vi">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Chi tiết lịch sử yêu cầu</title>
        <link rel="stylesheet" href="css/requestHistory.css">
        <link rel="stylesheet" href="css/header.css">
        <link rel="stylesheet" href="css/sidebar.css">
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">
    </head>
    <body>
        <div class="sidebar">
            <%@include file="sidebar.jsp" %>
        </div>
        <div class="main-content">
            <div class="container request-history-detail-container">

                <div class="header-card">
                    <h1><i class="fas fa-file-alt"></i> Chi tiết yêu cầu xuất kho</h1>

                </div>
                

                <c:if test="${history != null}">
                    <div class="history-info">
                        <div class="info-section">
                            <h3>Thông tin yêu cầu</h3>
                            <div class="info-grid-2col">
                                <div class="info-item">
                                    <label>Mã yêu cầu:</label>
                                    <span>${history.requestId}</span>
                                </div>
                                <div class="info-item">
                                    <label>Trạng thái:</label>
                                    <span class="status-badge status-${history.newStatus}">${history.newStatus}</span>
                                </div>
                                <div class="info-item">
                                    <label>Thời gian tạo:</label>
                                    <span><fmt:formatDate value="${history.createdAt}" pattern="dd/MM/yyyy HH:mm:ss"/></span>
                                </div>
                                <div class="info-item">
                                    <label>Người yêu cầu:</label>
                                    <span>${history.creatorName}</span>
                                </div>
                                <div class="info-item">
                                    <label>Tên dự án:</label>
                                    <span>${history.recipientName}</span>
                                </div>
                                <div class="info-item">
                                    <label>Địa chỉ giao hàng:</label>
                                    <span>${history.deliveryAddress}</span>
                                </div>
                                <div class="info-item">
                                    <label>Người liên hệ:</label>
                                    <span>${history.contactPerson}</span>
                                </div>
                                <div class="info-item">
                                    <label>SĐT người liên hệ:</label>
                                    <span>${history.contactPhone}</span>
                                </div>
                                <div class="info-item">
                                    <label>Lý do của nhân viên:</label>
                                    <span>${history.changeReason}</span>
                                </div>
                                <c:if test="${not empty history.directorNote}">
                                    <div class="info-item">
                                        <label>Lý do của giám đốc:</label>
                                        <span>${history.directorNote}</span>
                                    </div>
                                </c:if>
                                <div class="info-item">
                                    <label>Thời gian thay đổi:</label>
                                    <span><fmt:formatDate value="${history.changeTime}" pattern="dd/MM/yyyy HH:mm:ss"/></span>
                                </div>
                            </div>
                        </div>
                    </div>

                    <div class="details-section">
                        <h3>Chi tiết vật tư
                            <span style="font-size:14px;font-weight:400;color:#1976d2;margin-left:18px;">
                                (Thời gian thay đổi: <fmt:formatDate value="${history.changeTime}" pattern="dd/MM/yyyy HH:mm:ss"/>)
                            </span>
                        </h3>
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
                                        <c:forEach var="detail" items="${details}" varStatus="status">
                                            <tr>
                                                <td>${status.index + 1}</td>
                                                <td>${detail.materialName}</td>
                                                <td>${detail.quantity}</td>
                                                <td>${detail.unitName}</td>
                                                <td>
                                                    <span class="condition-badge condition-${detail.materialCondition}">
                                                        ${detail.materialCondition}
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
                                <p>Không có chi tiết vật tư cho lịch sử này.</p>
                            </div>
                        </c:if>
                    </div>
                </c:if>

                <a href="javascript:history.back()" class="btn-back">
                    <i class="fas fa-arrow-left"></i> Quay lại
                </a>

            </div>

        </div>
    </body>
</html> 