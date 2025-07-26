<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<html lang="vi">
    <head>
        <meta charset="UTF-8">
        <title>Danh sách nhập kho</title>
        <link rel="stylesheet" href="./css/importWarehouse.css">
        <style>
            .status-badge {
                padding: 4px 12px;
                border-radius: 12px;
                font-weight: 600;
                color: #fff;
                display: inline-block;
            }
            .status-badge.ÄÃ£\ duyá»t {
                background: #6ee7b7;
                color: #222;
            }
            .status-badge.Tá»«\ chá»i {
                background: #fecaca;
                color: #b91c1c;
            }
            .status-badge.Chá»\ duyá»t {
                background: #fef08a;
                color: #b45309;
            }
            .btn-action {
                margin: 0 2px;
                padding: 6px 16px;
                border-radius: 6px;
                border: none;
                font-weight: 600;
                cursor: pointer;
            }
            .btn-detail {
                background: #22c55e;
                color: #fff;
            }
            .btn-edit {
                background: #60a5fa;
                color: #fff;
            }
            .btn-log {
                background: #818cf8;
                color: #fff;
            }
            table {
                width: 100%;
                border-collapse: collapse;
                margin-top: 24px;
            }
            th, td {
                padding: 12px 8px;
                text-align: center;
            }
            th {
                background: #f1f5f9;
            }
            tr:nth-child(even) {
                background: #f9fafb;
            }
        </style>
    </head>
    <body>
        <div class="container">
            <div style="display: flex; align-items: center; justify-content: space-between; margin-bottom: 24px;">
                <h2 style="margin: 0;">Danh sách nhập kho</h2>
                <a href="${pageContext.request.contextPath}/ImportWarehouseServlet" class="btn-action btn-edit">Trở lại</a>

            </div>
                <h2>${requestScope.mess}</h2>
            <form action="${pageContext.request.contextPath}/importHistoryList" method="post">
                <input type="text" name="projectName" placeholder="Tên dự án" value="${param.projectName}" style="padding: 8px 12px; border-radius: 6px; border: 1px solid #ccc; margin-right: 8px;">
                <input type="date" name="createdDate" placeholder="Ngày tạo" value="${param.createdDate}" style="padding: 8px 12px; border-radius: 6px; border: 1px solid #ccc; margin-right: 8px;">
                <button type="submit" class="btn-action btn-detail">Tìm</button>
                <a href="${pageContext.request.contextPath}/importHistoryList" class="btn-action btn-edit">làm mới</a>
            </form>
            <table>
                <thead>
                    <tr>
                        <th>ID</th>
                        <th>Tên dự án</th>
                        <th>Ngày tạo</th>
                        <th>Lý do nhập lại</th>
                        <th>Hành động</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="h" items="${requestScope.importHistoryList}">
                        <tr>
                            <td>${h.id}</td>
                            <td>${h.projectName}</td>
                            <td><fmt:formatDate value="${h.createdAt}" pattern="dd/MM/yyyy"/></td>
                            <td>${h.reason}</td>
                            <td>
                                <a href="${pageContext.request.contextPath}/ImportHistoryDetailServlet?id=${h.id}" class="btn-action btn-detail">Chi tiết</a>
                            </td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
            
            <c:if test="${totalPages > 1}">
                <div style="margin-top: 16px; display: flex; justify-content: center;">
                    <c:forEach var="i" begin="1" end="${totalPages}">
                        <a href="${pageContext.request.contextPath}/importHistoryList?page=${i}&projectName=${projectName}&createdDate=${createdDate}" 
                           class="btn-action"
                           style="background: ${i == currentPage ? '#2563eb' : '#e5e7eb'}; color: ${i == currentPage ? '#fff' : '#222'}; margin: 0 2px;">
                            ${i}
                        </a>
                    </c:forEach>
                </div>
            </c:if>
        </div>
    </body>
</html> 