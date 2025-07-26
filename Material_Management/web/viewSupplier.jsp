<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page import="model.Supplier" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta http-equiv="Cache-Control" content="no-cache, no-store, must-revalidate">
    <meta http-equiv="Pragma" content="no-cache">
    <meta http-equiv="Expires" content="0">
    <title>Chi tiết nhà cung cấp</title>
    <link rel="stylesheet" href="css/sidebar.css">
    <link rel="stylesheet" href="css/common.css">
    <link rel="stylesheet" href="css/supplier.css?v=1.2">
    <style>
        /* Force override any conflicting styles */
        body .container {
            max-width: 980px !important;
            margin: 40px auto !important;
            padding: 0 20px !important;
            width: 980px !important;
        }
        body .supplier-details {
            padding: 50px !important;
            max-width: 980px !important;
        }
        body .detail-row {
            margin-bottom: 12px !important;
            padding: 12px 0 !important;
        }
        body .detail-label {
            font-size: 16px !important;
            font-weight: 600 !important;
        }
        body .detail-value {
            font-size: 16px !important;
        }
    </style>
</head>
<body>
    <div class="container">
        <div class="supplier-details">
            <h2>Chi tiết nhà cung cấp</h2>
            
            <c:if test="${not empty sessionScope.error}">
                <div class="alert alert-danger">
                    ${sessionScope.error}
                    <% session.removeAttribute("error"); %>
                </div>
            </c:if>

            <c:if test="${not empty supplier}">
                <div class="detail-row">
                    <div class="detail-label">ID:</div>
                    <div class="detail-value">${supplier.supplierId}</div>
                </div>
                
                <div class="detail-row">
                    <div class="detail-label">Tên nhà cung cấp:</div>
                    <div class="detail-value">${supplier.supplierName}</div>
                </div>
                
                <div class="detail-row">
                    <div class="detail-label">Người liên hệ:</div>
                    <div class="detail-value">${supplier.contactPerson}</div>
                </div>
                
                <div class="detail-row">
                    <div class="detail-label">Số điện thoại:</div>
                    <div class="detail-value">${supplier.supplierPhone}</div>
                </div>
                
                <div class="detail-row">
                    <div class="detail-label">Địa chỉ:</div>
                    <div class="detail-value">${supplier.address}</div>
                </div>
                
                <div class="detail-row">
                    <div class="detail-label">Trạng thái:</div>
                    <div class="detail-value">
                        <span class="status-badge 
                            ${supplier.status == 'active' ? 'status-active' : (supplier.status == 'inactive' ? 'status-inactive' : (supplier.status == 'terminated' ? 'status-inactive' : ''))}">
                            ${supplier.status == 'active' ? 'Hợp tác' : (supplier.status == 'inactive' ? 'Chưa hợp tác' : (supplier.status == 'terminated' ? 'Ngừng hợp tác' : 'Không xác định'))}
                        </span>
                    </div>
                </div>
                
                <c:choose>
                    <c:when test="${supplier.status == 'active'}">
                        <c:if test="${not empty firstPurchaseDate}">
                        <div class="detail-row">
                            <div class="detail-label">Thời gian bắt đầu hợp tác:</div>
                            <div class="detail-value timestamp">
                                <fmt:formatDate value="${firstPurchaseDate}" pattern="dd/MM/yyyy"/>
                            </div>
                        </div>
                        </c:if>
                    </c:when>
                    <c:when test="${supplier.status == 'terminated'}">
                        <c:if test="${not empty latestPurchaseDate}">
                        <div class="detail-row">
                            <div class="detail-label">Thời gian hoạt động gần nhất:</div>
                            <div class="detail-value timestamp">
                                <fmt:formatDate value="${latestPurchaseDate}" pattern="dd/MM/yyyy"/>
                            </div>
                        </div>
                        </c:if>
                        <c:if test="${not empty endCooperationDate}">
                        <div class="detail-row">
                            <div class="detail-label">Thời gian kết thúc hợp tác:</div>
                            <div class="detail-value timestamp">
                                <fmt:formatDate value="${endCooperationDate}" pattern="dd/MM/yyyy"/>
                            </div>
                        </div>
                        </c:if>
                    </c:when>
                </c:choose>
                <c:if test="${(cooperationYears > 0) || (cooperationRemainMonths > 0)}">
                <div class="detail-row">
                    <div class="detail-label">Thời gian hợp tác:</div>
                    <div class="detail-value timestamp">
                        <c:if test="${cooperationYears > 0 && cooperationRemainMonths > 0}">
                            ${cooperationYears} năm ${cooperationRemainMonths} tháng
                        </c:if>
                        <c:if test="${cooperationYears > 0 && cooperationRemainMonths == 0}">
                            ${cooperationYears} năm
                        </c:if>
                        <c:if test="${cooperationYears == 0 && cooperationRemainMonths > 0}">
                            ${cooperationRemainMonths} tháng
                        </c:if>
                    </div>
                </div>
                </c:if>
                <c:if test="${not empty supplier.statusReason}">
                    <div class="detail-row">
                        <div class="detail-label">Lý do:</div>
                        <div class="detail-value">${supplier.statusReason}</div>
                    </div>
                </c:if>
            </c:if>
            
            <div style="margin-top: 24px; display: flex; gap: 12px;"> 
                <a href="suppliers" class="back-button">Quay lại danh sách</a>
                <a href="suppliers?action=edit&id=${supplier.supplierId}" class="back-button" style="background-color: #007bff;">Sửa thông tin</a>
            </div>
        </div>
    </div>
</body>
</html> 