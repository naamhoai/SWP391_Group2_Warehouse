<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<fmt:setLocale value="vi_VN"/>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>Danh Sách Giao Dịch Gần Đây</title>
    <link rel="stylesheet" href="css/dashboard.css">
    <link rel="stylesheet" href="css/sidebar.css">
    <link rel="stylesheet" href="css/header.css"/>
    <link rel="stylesheet" href="css/footer.css"/>
</head>
<body>
    <%@include file="sidebar.jsp" %>
    <div id="main-content">
        <%@include file="header.jsp" %>
        <div class="dashboard-header">
            <h1>Danh Sách Giao Dịch Gần Đây</h1>
        </div>
        <div class="table-card">
            <table>
                <thead>
                    <tr>
                        <th>Mã Giao Dịch</th>
                        <th>Vật Tư</th>
                        <th>Loại</th>
                        <th>Số Lượng</th>
                        <th>Ngày</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="order" items="${allPurchases}">
                        <tr>
                            <td>${order.purchaseOrderId}</td>
                            <td>
                                <c:forEach var="detail" items="${order.details}" varStatus="loop">
                                    ${detail.materialName}<c:if test="${!loop.last}">, </c:if>
                                </c:forEach>
                            </td>
                            <td>Mua</td>
                            <td>
                                <c:set var="totalQty" value="0" />
                                <c:forEach var="detail" items="${order.details}">
                                    <c:set var="totalQty" value="${totalQty + detail.quantity}" />
                                </c:forEach>
                                ${totalQty}
                            </td>
                            <td><fmt:formatDate value="${order.orderDate}" pattern="dd/MM/yyyy HH:mm"/></td>
                        </tr>
                    </c:forEach>
                    <c:if test="${empty allPurchases}">
                        <tr>
                            <td colspan="5" style="text-align: center;">Không có đơn mua nào.</td>
                        </tr>
                    </c:if>
                </tbody>
            </table>
        </div>
        <jsp:include page="footer.jsp" />
    </div>
</body>
</html> 