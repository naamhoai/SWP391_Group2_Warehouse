<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <title>Purchase Material</title>
    <style>
        body {
            font-family: Arial, Helvetica, sans-serif;
            background: #f5f6fa;
        }
        .container {
            max-width: 900px;
            margin: 40px auto 30px auto;
            background: #fff;
            border-radius: 10px;
            box-shadow: 0 2px 16px rgba(0,0,0,0.08);
            padding: 32px 32px 24px 32px;
        }
        h2 {
            text-align: center;
            font-weight: bold;
            margin-bottom: 28px;
            letter-spacing: 1px;
        }
        .form-row {
            display: flex;
            align-items: center;
            justify-content: center;
            margin-bottom: 18px;
        }
        .form-label {
            min-width: 110px;
            text-align: right;
            margin-right: 10px;
            font-weight: 500;
        }
        .form-input {
            width: 220px;
            padding: 7px 10px;
            border: 1.5px solid #bdbdbd;
            border-radius: 5px;
            font-size: 15px;
            background: #f9f9f9;
        }
        .form-input[readonly] {
            background: #eaeaea;
            color: #888;
        }
        .table-wrap {
            overflow-x: auto;
            margin-bottom: 18px;
        }
        table.custom-table {
            width: 100%;
            border-collapse: collapse;
            background: #fff;
        }
        table.custom-table th, table.custom-table td {
            border: 2px solid #1a237e;
            text-align: center;
            vertical-align: middle;
            padding: 8px 6px;
        }
        table.custom-table th {
            background: #e3eafc;
            font-weight: bold;
        }
        .btn {
            padding: 8px 22px;
            border-radius: 5px;
            border: 1.5px solid #1a237e;
            font-size: 15px;
            font-weight: 500;
            cursor: pointer;
            margin-right: 10px;
            transition: background 0.2s, color 0.2s;
        }
        .btn-green {
            background: #38d600;
            color: #fff;
        }
        .btn-green:hover {
            background: #2bb300;
        }
        .btn-exit {
            background: #bdbdbd;
            color: #222;
        }
        .btn-exit:hover {
            background: #757575;
            color: #fff;
        }
        .btn-danger {
            background: #e74c3c;
            color: #fff;
            border: 1.5px solid #c0392b;
        }
        .btn-danger:hover {
            background: #c0392b;
        }
        @media (max-width: 600px) {
            .container { padding: 10px; }
            .form-row { flex-direction: column; align-items: flex-start; }
            .form-label { text-align: left; margin-bottom: 4px; }
        }
    </style>
</head>
<body>
<div class="container">
    <h2>Purchase Material</h2>
    <form action="CreatePurchaseOrderServlet" method="post" id="purchaseOrderForm">
        <div class="form-row">
            <label for="supplierId" class="form-label">Supplier:</label>
            <select name="supplierId" id="supplierId" class="form-input" required>
                <option value="">Select Supplier</option>
                <c:forEach items="${suppliers}" var="supplier">
                    <option value="${supplier.id}">${supplier.name}</option>
                </c:forEach>
            </select>
        </div>
        <div class="form-row">
            <label for="importer" class="form-label">Importer:</label>
            <input type="text" name="importer" id="importer" class="form-input" value="${sessionScope.userName}" readonly>
        </div>
        <div class="table-wrap">
            <table class="custom-table" id="itemsTable">
                <thead>
                <tr>
                    <th>Category</th>
                    <th>Material</th>
                    <th>Quantity</th>
                    <th>Unit</th>
                    <th>Describe</th>
                    <th></th>
                </tr>
                </thead>
                <tbody id="itemsBody">
                <!-- Dynamic rows here -->
                </tbody>
            </table>
        </div>
        <div style="margin-bottom: 22px;">
            <button type="button" class="btn btn-green" onclick="addItemRow()">Add Item</button>
        </div>
        <div style="text-align:center;">
            <button type="submit" class="btn btn-green">Import material</button>
            <button type="button" class="btn btn-exit" onclick="window.location.href='homepage.jsp'">Exit</button>
        </div>
    </form>
</div>
<script>
    let rowCount = 0;
    function addItemRow() {
        const tbody = document.getElementById('itemsBody');
        const row = document.createElement('tr');
        row.innerHTML = `
            <td><input type="text" name="items[${rowCount}].category" class="form-input" required></td>
            <td><input type="text" name="items[${rowCount}].material" class="form-input" required></td>
            <td><input type="number" name="items[${rowCount}].quantity" class="form-input" min="1" required></td>
            <td><input type="text" name="items[${rowCount}].unit" class="form-input" required></td>
            <td><input type="text" name="items[${rowCount}].describe" class="form-input"></td>
            <td><button type="button" class="btn btn-danger" onclick="removeRow(this)">Remove</button></td>
        `;
        tbody.appendChild(row);
        rowCount++;
    }
    function removeRow(btn) {
        btn.closest('tr').remove();
    }
    // Add one row by default
    window.onload = addItemRow;
</script>
</body>
</html>
