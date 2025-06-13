<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <title>Pending Purchase Requests</title>
    <style>
        table {
            width: 100%;
            border-collapse: collapse;
            margin-top: 20px;
        }
        th, td {
            padding: 10px;
            text-align: left;
            border: 1px solid #ddd;
        }
        th {
            background-color: #f5f5f5;
        }
        .action-buttons {
            display: flex;
            gap: 10px;
        }
        .view-details {
            color: #007bff;
            text-decoration: none;
        }
        .view-details:hover {
            text-decoration: underline;
        }
        .approve-btn {
            background-color: #28a745;
            color: white;
            border: none;
            padding: 5px 10px;
            cursor: pointer;
        }
        .reject-btn {
            background-color: #dc3545;
            color: white;
            border: none;
            padding: 5px 10px;
            cursor: pointer;
        }
    </style>
</head>
<body>
    <h2>Pending Purchase Requests</h2>
    <table>
        <thead>
        <tr>
            <th>Request ID</th>
            <th>Supplier</th>
            <th>Reason</th>
            <th>Status</th>
            <th>Details</th>
            <th>Action</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach items="${requests}" var="request">
            <tr>
                <td>${request.requestId}</td>
                <td>${request.supplierName}</td>
                <td>${request.reason}</td>
                <td>${request.requestStatus}</td>
                <td>
                    <a href="viewPurchaseOrderDetails?requestId=${request.requestId}" class="view-details">View Details</a>
                </td>
                <td class="action-buttons">
                    <form action="approveRequest" method="post" style="margin: 0;">
                        <input type="hidden" name="requestId" value="${request.requestId}">
                        <input type="submit" value="Approve" class="approve-btn">
                    </form>
                    <form action="rejectRequest" method="post" style="margin: 0;">
                        <input type="hidden" name="requestId" value="${request.requestId}">
                        <input type="submit" value="Reject" class="reject-btn">
                    </form>
                </td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
</body>
</html>
