<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>Lịch Sử Giao Dịch Vật Tư</title>
    <link rel="stylesheet" href="css/inventoryHistory.css"> 
    <link rel="stylesheet" href="css/sidebar.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/css/all.min.css">
    <link href="https://fonts.googleapis.com/css2?family=Roboto:wght@400;500;700&display=swap" rel="stylesheet">
</head>
<body>
    <jsp:include page="sidebar.jsp" />

    <div class="main-content">
        <div class="header-section">
            <h1 class="page-title">Lịch Sử Giao Dịch Vật Tư</h1>
        </div>
        <div class="content-card">
            <form method="get" action="InventoryHistoryServlet" id="filterForm">
                <div class="filter-row">
                    <div class="filter-group">
                        <i class="fas fa-search"></i>
                        <input type="text" name="keyword" placeholder="Tên vật tư, người thực hiện..." value="${fn:escapeXml(keyword)}"/>
                    </div>
                    <div class="filter-group">
                        <i class="fas fa-calendar-alt"></i>
                        <label for="fromDate" style="margin-right: 6px; font-weight: 500; color: #374151;">Từ ngày</label>
                        <input type="date" id="fromDate" name="fromDate" placeholder="Từ ngày" value="${fromDate}"/>
                    </div>
                    <div class="filter-group">
                        <i class="fas fa-calendar-alt"></i>
                        <label for="toDate" style="margin-right: 6px; font-weight: 500; color: #374151;">Đến ngày</label>
                        <input type="date" id="toDate" name="toDate" placeholder="Đến ngày" value="${toDate}"/>
                    </div>
                    <div class="filter-group">
                        <i class="fas fa-exchange-alt"></i>
                        <select name="transactionType" onchange="this.form.submit()">
                            <option value="All" ${param.transactionType == 'All' || empty param.transactionType ? 'selected' : ''}>Tất cả giao dịch</option>
                            <option value="Nhập kho" ${param.transactionType == 'Nhập kho' ? 'selected' : ''}>Nhập kho</option>
                            <option value="Xuất kho" ${param.transactionType == 'Xuất kho' ? 'selected' : ''}>Xuất kho</option>
                        </select>
                    </div>
                    <button type="submit" class="btn btn-primary">Tìm kiếm</button>
                    <button type="button" class="btn btn-secondary" onclick="resetFilters()">Đặt lại</button>
                </div>
            </form>
            <script>
                function resetFilters() {
                    document.getElementById('filterForm').querySelectorAll('input, select').forEach(el => {
                        if (el.tagName === 'SELECT') el.selectedIndex = 0;
                        else el.value = '';
                    });
                    document.getElementById('filterForm').submit();
                }
            </script>
        </div>

        <div class="content-card">
            <div class="table-responsive">
                <table class="data-table">
                    <colgroup>
                        <col class="col-type" />
                        <col class="col-date" />
                        <col class="col-id" />
                        <col class="col-name" />
                        <col class="col-status" />
                        <col class="col-quantity" />
                        <col class="col-actor" />
                        <col class="col-ref" />
                        <col style="width: 90px;" /> 
                    </colgroup>
                    <thead>
                        <tr>
                            <th>Loại Giao Dịch</th>
                            <th>Ngày Giao Dịch</th>
                            <th>Mã Vật Tư</th>
                            <th>Tên Vật Tư</th>
                            <th>Tình Trạng</th>
                            <th>Số Lượng</th>
                            <th>Người Thực Hiện</th>
                            <th>Mã Phiếu</th>
                            <th>Thao tác</th>
                        </tr>
                    </thead>
                    <tbody>
                    <c:choose>
                        <c:when test="${not empty historyList}">
                            <c:forEach var="h" items="${historyList}">
                                <tr>
                                    <td>
                                        <c:choose>
                                            <c:when test='${h.transactionType == "Nhập kho"}'>
                                                <span class="badge badge-info">Nhập kho</span>
                                            </c:when>
                                            <c:when test='${h.transactionType == "Xuất kho"}'>
                                                <span class="badge badge-danger">Xuất kho</span>
                                            </c:when>
                                        </c:choose>
                                    </td>
                                    <td><fmt:formatDate value="${h.transactionDate}" pattern="dd-MM-yyyy HH:mm"/></td>
                                    <td>${h.materialId}</td>
                                    <td>${h.materialName}</td>
                                    <td>${h.materialCondition}</td>
                                    <td><fmt:formatNumber value="${h.quantity}" pattern="#,##0" /></td>
                                    <td>${h.actorName}</td>
                                    <td>
                                        <c:choose>
                                            <c:when test="${h.referenceId >= 2000000}">${h.referenceId - 2000000}</c:when>
                                            <c:when test="${h.referenceId >= 1000000}">${h.referenceId - 1000000}</c:when>
                                            <c:otherwise>${h.referenceId}</c:otherwise>
                                        </c:choose>
                                    </td>
                                    <td>
                                        <c:choose>
                                            <%-- Nhập thủ công (IH) --%>
                                            <c:when test="${h.referenceId >= 2000000}">
                                                <a href="ImportHistoryDetailServlet?id=${h.referenceId - 2000000}" class="btn-action btn-view" title="Xem chi tiết phiếu nhập">
                                                    <i class="fas fa-eye"></i>
                                                </a>
                                            </c:when>
                                            <%-- Xuất kho (Export Form) --%>
                                            <c:when test="${h.referenceId >= 1000000}">
                                                <a href="exportFormDetail?exportId=${h.referenceId - 1000000}" class="btn-action btn-view" title="Xem chi tiết phiếu xuất">
                                                    <i class="fas fa-eye"></i>
                                                </a>
                                            </c:when>
                                            <%-- Đơn mua hàng (PO) --%>
                                            <c:otherwise>
                                                <a href="purchaseOrderDetail?id=${h.referenceId}" class="btn-action btn-view" title="Xem chi tiết đơn mua hàng">
                                                    <i class="fas fa-eye"></i>
                                                </a>
                                            </c:otherwise>
                                        </c:choose>
                                    </td>
                                </tr>
                            </c:forEach>
                        </c:when>
                        <c:otherwise>
                            <tr><td colspan="9" class="no-data">Không có dữ liệu lịch sử nào khớp với tiêu chí tìm kiếm.</td></tr>
                        </c:otherwise>
                    </c:choose>
                    </tbody>
                </table>
            </div>
        </div>
        <div class="pagination-container">
            <div class="pagination-info">
                Hiển thị ${fn:length(historyList)} của ${totalRecords} kết quả.
            </div>
            <c:if test="${totalPages > 1}">
                <nav class="pagination">
                    <c:if test="${currentPage > 1}">
                        <a href="InventoryHistoryServlet?page=${currentPage - 1}&keyword=${fn:escapeXml(keyword)}&fromDate=${fromDate}&toDate=${toDate}&transactionType=${transactionType}" class="page-link">«</a>
                    </c:if>
                    <c:forEach begin="${startPage}" end="${endPage}" var="i">
                        <a href="InventoryHistoryServlet?page=${i}&keyword=${fn:escapeXml(keyword)}&fromDate=${fromDate}&toDate=${toDate}&transactionType=${transactionType}" class="page-link ${currentPage == i ? 'active' : ''}">${i}</a>
                    </c:forEach>
                    <c:if test="${currentPage < totalPages}">
                        <a href="InventoryHistoryServlet?page=${currentPage + 1}&keyword=${fn:escapeXml(keyword)}&fromDate=${fromDate}&toDate=${toDate}&transactionType=${transactionType}" class="page-link">»</a>
                    </c:if>
                </nav>
            </c:if>
        </div>
    </div>
</body>
</html>