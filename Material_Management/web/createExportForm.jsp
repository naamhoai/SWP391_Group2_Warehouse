
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Tạo đơn xuất kho từ yêu cầu</title>
        <link rel="stylesheet" href="css/requestForm.css">
        <link rel="stylesheet" href="css/sidebar.css">
        <link rel="stylesheet" href="css/footer.css"/>
    </head>
    <body>
        <div class="main-layout">
            <div class="sidebar">
                <%@include file="sidebar.jsp" %>
            </div>
            <div class="main-content">
                <div class="request-form-container">

                    <h2>Tạo đơn xuất kho</h2>
                    <c:if test="${not empty message}">
                        <div class="alert alert-success">${message}</div>
                    </c:if>
                    <c:if test="${not empty error}">
                        <div class="alert alert-danger">${error}</div>
                    </c:if>
                    <c:if test="${not empty partialExportWarning}">
                        <script>
                            alert('${partialExportWarning}');
                        </script>
                    </c:if>
                    <form action="createExportForm" method="post">
                        <input type="hidden" name="requestId" value="${param.requestId}" />
                        <c:if test="${not empty partialExportWarning}">
                            <input type="hidden" name="confirmPartialExport" value="true" />
                        </c:if>

                        <div class="form-section section-request-info">
                            <h3>Thông tin yêu cầu</h3>
                            <div><b>Tên dự án:</b> ${request.recipientName}</div>
                            <div><b>Địa chỉ giao hàng:</b> ${request.deliveryAddress}</div>
                            <div><b>Người liên hệ:</b> ${request.contactPerson}</div>
                            <div><b>SĐT liên hệ:</b> ${request.contactPhone}</div>
                            <div><b>Lý do:</b> ${request.reason}</div>
                        </div>

                        <div class="form-section section-material-list">
                            <h3>Danh sách vật tư xuất kho</h3>
                            <table class="request-items-table">
                                <thead>
                                    <tr>
                                        <th>Tên vật tư</th>
                                        <th>Số lượng yêu cầu</th>
                                        <th>Số lượng tồn kho</th>
                                        <th>Đơn vị</th>
                                        <th>Tình trạng</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <c:forEach var="d" items="${details}">
                                        <tr>
                                            <td>${d.materialName}</td>
                                            <td>${d.quantity}</td>
                                            <td>
                                                <c:choose>
                                                    <c:when test="${not empty inventoryMap[d.materialId]}">
                                                        ${inventoryMap[d.materialId]}
                                                    </c:when>
                                                    <c:otherwise>0</c:otherwise>
                                                </c:choose>
                                            </td>
                                            <td>${d.unitName}</td>
                                            <td>${d.materialCondition}</td>
                                        </tr>
                                    </c:forEach>
                                </tbody>
                            </table>
                        </div>

                        <div class="form-section">
                            <label for="deliveryType"><b>Đơn vị vận chuyển:</b></label>
                            <input type="text" name="deliveryType" id="deliveryType" class="input-text" required placeholder="Nhập đơn vị vận chuyển" value="${param.deliveryType}" />
                        </div>

                        <div class="form-section">
                            <label for="description">Ghi chú cho vận chuyển (nếu có):</label>
                            <textarea name="description" id="description" class="input-textarea" rows="2"><c:out value="${param.description}" /></textarea>
                        </div>
                        <div class="form-actions">
                            <button type="submit" class="btn-submit">Tạo đơn xuất kho</button>
                            <button type="button" class="btn-back" onclick="window.history.back()">Quay lại</button>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </body>
</html>
