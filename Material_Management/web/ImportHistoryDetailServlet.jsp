<%-- 
    Document   : ImportHistoryDetailServlet
    Created on : Jul 26, 2025, 1:56:29 AM
    Author     : kien3
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Chi tiết phiếu nhập kho</title>
    <link rel="stylesheet" href="./css/importWarehouse.css">
    <style>
        .card {
            background: #fff;
            border-radius: 12px;
            box-shadow: 0 2px 10px rgba(0,0,0,0.08);
            padding: 24px 32px;
            margin-bottom: 24px;
        }
        .info-grid {
            display: grid;
            grid-template-columns: 1fr 1fr;
            gap: 12px 32px;
            margin-top: 12px;
        }
        .info-grid label {
            font-weight: 600;
            margin-bottom: 4px;
            display: block;
        }
        .table-vattu th, .table-vattu td {
            text-align: center;
            vertical-align: middle;
        }
        .btn-back {
            margin-top: 24px;
            padding: 8px 24px;
            border-radius: 6px;
            background: #64748b;
            color: #fff;
            border: none;
            font-weight: 600;
            cursor: pointer;
            text-decoration: none;
            display: inline-block;
            transition: background-color 0.3s ease;
        }
        
        .btn-back:hover {
            background: #475569;
        }
        .status-badge {
            padding: 4px 12px;
            border-radius: 12px;
            font-weight: 600;
            color: #fff;
            display: inline-block;
            background: #6ee7b7;
            color: #222;
        }
    </style>
</head>
<body>
<div class="container">
    <h2 style="margin-bottom: 24px;">Chi tiết phiếu nhập kho</h2>
    <div class="card">
        <h3 style="margin-bottom: 16px;">Thông tin phiếu nhập</h3>
        <div class="info-grid">
            <div><b>Mã phiếu:</b> ${importHistory.id}</div>
            <div><b>Ngày tạo:</b> <fmt:formatDate value="${importHistory.createdAt}" pattern="dd/MM/yyyy"/></div>
            <div><b>Tên dự án:</b> ${importHistory.projectName}</div>
            <div><b>Người nhận:</b> ${importHistory.receivedBy}</div>
            <div><b>Lý do:</b> ${importHistory.reason}</div>
            <div><b>Người giao:</b> ${importHistory.deliveredBy}</div>
            <div><b>SĐT người giao:</b> ${importHistory.deliveryPhone}</div>
        </div>
    </div>
    <div class="card">
        <h3 style="margin-bottom: 16px;">Danh sách vật tư</h3>
        <table class="table-vattu">
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
                <c:forEach var="d" items="${importDetailList}" varStatus="status">
                    <tr>
                        <td>${status.index + 1}</td>
                        <td>${d.materialName}</td>
                        <td>${d.quantity}</td>
                        <td>${d.unit}</td>
                        <td><span class="status-badge">${d.status}</span></td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>
    </div>
    <button onclick="history.back()" class="btn-back">&larr; Quay lại</button>
</div>
</body>
</html>
