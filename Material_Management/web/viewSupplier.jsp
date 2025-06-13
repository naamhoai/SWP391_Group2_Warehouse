<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page import="model.Supplier" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Chi tiết nhà cung cấp</title>
    <link rel="stylesheet" href="css/supplier.css">
    <link rel="stylesheet" href="css/style.css">
    <style>
        .supplier-details {
            max-width: 800px;
            margin: 20px auto;
            padding: 20px;
            background: #fff;
            border-radius: 8px;
            box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
        }
        .detail-row {
            display: flex;
            margin-bottom: 15px;
            border-bottom: 1px solid #eee;
            padding-bottom: 10px;
        }
        .detail-label {
            width: 200px;
            font-weight: bold;
            color: #555;
        }
        .detail-value {
            flex: 1;
            color: #333;
        }
        .status-badge {
            display: inline-block;
            padding: 5px 10px;
            border-radius: 4px;
            font-size: 14px;
            font-weight: 500;
        }
        .status-active {
            background-color: #e6f4ea;
            color: #1e7e34;
        }
        .status-inactive {
            background-color: #feeced;
            color: #dc3545;
        }
        .timestamp {
            color: #666;
            font-size: 0.9em;
        }
        .back-button {
            display: inline-block;
            padding: 8px 16px;
            background-color: #6c757d;
            color: white;
            text-decoration: none;
            border-radius: 4px;
            margin-top: 20px;
        }
        .back-button:hover {
            background-color: #5a6268;
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
                        <span class="status-badge ${supplier.status == 'active' ? 'status-active' : 'status-inactive'}">
                            ${supplier.status == 'active' ? 'Hợp tác' : 'Không hợp tác'}
                        </span>
                    </div>
                </div>
                
                <div class="detail-row">
                    <div class="detail-label">Ngày tạo:</div>
                    <div class="detail-value timestamp">
                        <fmt:formatDate value="${supplier.createdAt}" pattern="dd/MM/yyyy HH:mm:ss"/>
                    </div>
                </div>
                
                <div class="detail-row">
                    <div class="detail-label">Cập nhật lần cuối:</div>
                    <div class="detail-value timestamp">
                        <fmt:formatDate value="${supplier.updatedAt}" pattern="dd/MM/yyyy HH:mm:ss"/>
                    </div>
                </div>
            </c:if>
            
            <a href="suppliers" class="back-button">Quay lại danh sách</a>
        </div>
    </div>
</body>
</html> 