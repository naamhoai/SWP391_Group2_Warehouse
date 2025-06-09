<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Material Request Form</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body>
    <div class="container mt-4">
        <!-- Display notifications -->
        <c:if test="${not empty message}">
            <div class="alert alert-success" role="alert">
                ${message}
            </div>
        </c:if>
        <c:if test="${not empty error}">
            <div class="alert alert-danger" role="alert">
                ${error}
            </div>
        </c:if>

        <h2 class="mb-4">Material Request Form</h2>
        <form action="${pageContext.request.contextPath}/SubmitRequestServlet" method="post" class="needs-validation" novalidate>
            <!-- Employee and Department Info -->
            <div class="row mb-3">
                <div class="col-md-6">
                    <label for="employee_name" class="form-label">Staff required:</label>
                    <input type="text" class="form-control" id="employee_name" name="employee_name" value="${user.fullname}" readonly>
                </div>
                <div class="col-md-6">
                    <label for="request_type" class="form-label">Request Type:</label>
                    <select class="form-select" id="request_type" name="request_type" required>
                        <c:forEach var="requestType" items="${requestTypes}">
                            <option value="${requestType.id}">${requestType.name}</option>
                        </c:forEach>
                    </select>
                </div>
            </div>

            <!-- Material Table Section -->
            <div class="table-responsive mb-3">
                <table class="table table-bordered">
                    <thead>
                        <tr>
                            <th>Material type</th>
                            <th>Material name</th>
                            <th>Quantity</th>
                            <th>Unit</th>
                            <th>Condition (new/used/damaged)</th>
                            <th>Reason</th>
                        </tr>
                    </thead>
                    <tbody id="materialTableBody">
                        <tr>
                            <td>
                                <select class="form-select" name="material_type[]" required>
                                    <c:forEach var="category" items="${categories}">
                                        <option value="${category.categoryId}">${category.name}</option>
                                    </c:forEach>
                                </select>
                            </td>
                            <td><input type="text" class="form-control" name="material_name[]" required></td>
                            <td><input type="number" class="form-control" name="quantity[]" required></td>
                            <td>
                                <select class="form-select" name="unit[]" required>
                                    <c:forEach var="unit" items="${units}">
                                        <option value="${unit.conversionId}">${unit.unitName}</option>
                                    </c:forEach>
                                </select>
                            </td>
                            <td>
                                <select class="form-select" name="material_condition[]" required>
                                    <c:forEach var="condition" items="${materialConditions}">
                                        <option value="${condition.id}">${condition.name}</option>
                                    </c:forEach>
                                </select>
                            </td>
                            <td><input type="text" class="form-control" name="reason[]" required></td>
                        </tr>
                    </tbody>
                </table>
                <button type="button" id="addItemBtn" class="btn btn-success">Add Item</button>
            </div>

            <!-- Submit Button -->
            <button type="submit" class="btn btn-primary">Submit Application</button>
        </form>

        <!-- Request List Section -->
        <h2 class="mt-5 mb-4">Request List</h2>
        <div class="table-responsive">
            <table class="table table-striped">
                <thead>
                    <tr>
                        <th>Request ID</th>
                        <th>Employee Name</th>
                        <th>Request Type</th>
                        <th>Material Condition</th>
                        <th>Created Date</th>
                        <th>Status</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="request" items="${requests}">
                        <tr>
                            <td>${request.requestId}</td>
                            <td>${request.user.fullname}</td>
                            <td>${request.requestType.name}</td>
                            <td>${request.materialCondition}</td>
                            <td>${request.createdAt}</td>
                            <td>
                                <span class="badge ${request.status == 'pending' ? 'bg-warning' : request.status == 'approved' ? 'bg-success' : 'bg-danger'}">
                                    ${request.status}
                                </span>
                            </td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </div>
    </div>

    <script>
        // JavaScript for adding new rows to the material table
        document.getElementById('addItemBtn').addEventListener('click', function() {
            var tableBody = document.getElementById('materialTableBody');
            var newRow = tableBody.rows[0].cloneNode(true); // Clone the first row
            var inputs = newRow.getElementsByTagName('input');
            for (var i = 0; i < inputs.length; i++) {
                inputs[i].value = ''; // Clear the values in the cloned row
            }
            tableBody.appendChild(newRow); // Append the new row
        });
    </script>
</body>
</html>
