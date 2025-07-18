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
                    <c:forEach var="transaction" items="${recentTransactions}">
                        <tr>
                            <td>${transaction.id}</td>
                            <td>${transaction.materialName}</td>
                            <td>${transaction.type}</td>
                            <td>${transaction.quantity}</td>
                            <td><fmt:formatDate value="${transaction.date}" pattern="dd/MM/yyyy HH:mm"/></td>
                        </tr>
                    </c:forEach>
                    <c:if test="${empty recentTransactions}">
                        <tr><td colspan="5" style="text-align: center;">Không có giao dịch nào.</td></tr>
                    </c:if>
                </tbody>
            </table>
        </div>
        <jsp:include page="footer.jsp" />
    </div>
</body>
</html> 