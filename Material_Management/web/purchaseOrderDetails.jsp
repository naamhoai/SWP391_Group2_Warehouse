<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <title>Purchase Order Details</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            margin: 20px;
        }
        .container {
            max-width: 1200px;
            margin: 0 auto;
        }
        .order-info {
            background-color: #f8f9fa;
            padding: 20px;
            border-radius: 5px;
            margin-bottom: 20px;
        }
        .order-info p {
            margin: 5px 0;
        }
        table {
            width: 100%;
            border-collapse: collapse;
            margin-top: 20px;
        }
        th, td {
            padding: 12px;
            text-align: left;
            border: 1px solid #ddd;
        }
        th {
            background-color: #f5f5f5;
        }
        .total-amount {
            text-align: right;
            font-weight: bold;
            margin-top: 20px;
            font-size: 1.2em;
        }
        .back-button {
            display: inline-block;
            padding: 10px 20px;
            background-color: #6c757d;
            color: white;
            text-decoration: none;
            border-radius: 5px;
            margin-bottom: 20px;
        }
        .back-button:hover {
            background-color: #5a6268;
        }
    </style>
</head>
<body>
    <div class="container">
        <a href="pendingRequests" class="back-button">← Back to Pending Requests</a>
        
        <h2>Purchase Order Details</h2>
        
        <div class="order-info">
            <p><strong>Order ID:</strong> ${purchaseOrder.purchaseOrderId}</p>
            <p><strong>Supplier ID:</strong> ${purchaseOrder.supplierId}</p>
            <p><strong>Created By:</strong> ${purchaseOrder.userId}</p>
            <p><strong>Order Date:</strong> ${purchaseOrder.orderDate}</p>
        </div>

        <h3>Order Items</h3>
        <table>
            <thead>
                <tr>
                    <th>Category</th>
                    <th>Material</th>
                    <th>Quantity</th>
                    <th>Unit</th>
                    <th>Unit Price</th>
                    <th>Total Price</th>
                    <th>Description</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach items="${orderDetails}" var="detail">
                    <tr>
                        <td>${detail.category}</td>
                        <td>${detail.materialName}</td>
                        <td>${detail.quantity}</td>
                        <td>${detail.unit}</td>
                        <td>${detail.unitPrice}</td>
                        <td>${detail.totalPrice}</td>
                        <td>${detail.describe}</td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>
        
        <div class="total-amount">
            Total Amount: ${purchaseOrder.totalAmount}
        </div>
    </div>
</body>
</html> 