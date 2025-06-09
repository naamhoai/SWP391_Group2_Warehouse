<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Create Material Detail</title>
    <link rel="stylesheet" href="css/createMaterialDetail.css">
</head>
<body>

    <div class="material-detail-container">
        <h1 class="material-detail-title">CREATE MATERIAL DETAIL</h1>

        <form action="CreateMaterialDetail" method="POST" enctype="multipart/form-data">
            <div class="form-group">
                <div class="input-group">
                    <label for="materialId">Enter ID:</label>
                    <input type="text" id="materialId" name="materialId" required>
                    <span class="error-message" id="materialId-error"></span>
                </div>

                <div class="input-group">
                    <label for="supplier">Supplier:</label>
                    <select id="supplier" name="supplier" required>
                        <option value="">Select Supplier</option>
                        <c:forEach items="${supplierList}" var="supplier">
                            <option value="${supplier.id}">${supplier.name}</option>
                        </c:forEach>
                    </select>
                </div>

                <div class="input-group">
                    <label for="imageUpload">Upload Image:</label>
                    <div class="image-upload">
                        <input type="file" id="imageUpload" name="imageUpload" accept="image/*">
                    </div>
                </div>

                <div class="input-group">
                    <label for="quantity">Quantity:</label>
                    <input type="number" id="quantity" name="quantity" min="0" required>
                </div>

                <div class="input-group">
                    <label for="name">Name:</label>
                    <input type="text" id="name" name="name" required>
                </div>

                <div class="input-group">
                    <label for="unit">Unit:</label>
                    <select id="unit" name="unit" required>
                        <option value="">Select Unit</option>
                        <c:forEach items="${unitList}" var="unit">
                            <option value="${unit.id}">${unit.name}</option>
                        </c:forEach>
                    </select>
                </div>

                <div class="input-group">
                    <label for="category">Category:</label>
                    <select id="category" name="category" required>
                        <option value="">Select Category</option>
                        <c:forEach items="${categoryList}" var="category">
                            <option value="${category.id}">${category.name}</option>
                        </c:forEach>
                    </select>
                </div>

                <div class="input-group">
                    <label for="status">Status:</label>
                    <select id="status" name="status" required>
                        <option value="">Select Status</option>
                        <option value="active">Active</option>
                        <option value="inactive">Inactive</option>
                    </select>
                </div>

                <div class="input-group description-group">
                    <label for="description">Description:</label>
                    <textarea id="description" name="description" rows="5"></textarea>
                </div>

                <div class="submit-btn">
                    <button type="submit">Submit</button>
                </div>
            </div>
        </form>
    </div>
</body>
</html>
