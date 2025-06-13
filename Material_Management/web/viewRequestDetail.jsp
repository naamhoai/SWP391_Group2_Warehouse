<%-- 
    Document   : viewRequestDetail
    Created on : Jun 12, 2025, 5:01:36 PM
    Author     : kimoa
--%>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Chi tiết yêu cầu vật tư</title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    </head>
    <body>
        <div class="container mt-4">
            <h2>Chi tiết yêu cầu vật tư</h2>
            <div class="mb-3">
                <label><b>Mã yêu cầu:</b></label> ${request.requestId}<br>
                <label><b>Loại yêu cầu:</b></label> ${request.requestType}<br>
                <label><b>Người yêu cầu (ID):</b></label> ${request.userId}<br>
                <label><b>Lý do:</b></label> ${request.reason}<br>
                <label><b>Trạng thái:</b></label> ${request.requestStatus}<br>
                <label><b>Thời gian tạo:</b></label> ${request.createdAt}<br>
            </div>
            <h4>Danh sách vật tư</h4>
            <table class="table table-bordered">
                <thead>
                <tr>
                    <th>Danh mục</th>
                    <th>Loại vật tư</th>
                    <th>Tên vật tư</th>
                    <th>Số lượng</th>
                    <th>Đơn vị</th>
                    <th>Mô tả</th>
                </tr>
                </thead>
                <tbody>
                <c:forEach var="d" items="${details}">
                    <tr>
                        <td>${d.parentCategoryName}</td>
                        <td>${d.categoryName}</td>
                        <td>${d.materialName}</td>
                        <td>${d.quantity}</td>
                        <td>${d.unitName}</td>
                        <td>${d.description}</td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
            <c:if test="${request.requestStatus eq 'Pending'}">
                <form action="approveRequest" method="post" style="display:inline;">
                    <input type="hidden" name="requestId" value="${request.requestId}">
                    <button type="submit" class="btn btn-success">Phê duyệt</button>
                </form>
                <form action="rejectRequest" method="post" style="display:inline;">
                    <input type="hidden" name="requestId" value="${request.requestId}">
                    <button type="submit" class="btn btn-danger">Từ chối</button>
                </form>
            </c:if>
            <a href="director" class="btn btn-secondary mt-3">Quay lại</a>
        </div>
    </body>
</html>
