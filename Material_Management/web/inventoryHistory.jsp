<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>Lịch sử giao dịch vật tư</title>
    <link rel="stylesheet" href="css/inventoryHistory.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/css/all.min.css">
    <link rel="stylesheet" href="css/sidebar.css">
    <script src="js/sidebar.js"></script>
</head>
<body>
<jsp:include page="sidebar.jsp" />
<div class="main-content">
    <div class="header-section">
        <h1 class="page-title">Lịch sử giao dịch vật tư</h1>
    </div>
    <div class="content-card">
        <form method="get" action="InventoryHistoryServlet" id="filterForm" class="filter-form">
            <div class="filter-row">
                <div class="filter-group" style="position: relative; display: flex; align-items: center;">
                    <i class="fas fa-search"></i>
                    <input type="text" name="keyword" id="keywordInput" placeholder="Tên vật tư hoặc người thực hiện..." value="${param.keyword != null ? param.keyword : ''}" style="padding-right: 24px;" />
                    <button type="button" id="clearKeywordBtn" style="position: absolute; right: 4px; background: none; border: none; cursor: pointer; font-size: 16px; color: #888;">&times;</button>
                </div>
                <div class="filter-group">
                    <label for="fromDate" style="margin-right: 4px; font-weight: 500;">Từ ngày</label>
                    <input type="date" name="fromDate" id="fromDate" value="${param.fromDate != null ? param.fromDate : ''}" />
                </div>
                <div class="filter-group">
                    <label for="toDate" style="margin-right: 4px; font-weight: 500;">Đến ngày</label>
                    <input type="date" name="toDate" id="toDate" value="${param.toDate != null ? param.toDate : ''}" />
                </div>
                <div class="filter-group">
                    <select name="transactionType" onchange="this.form.submit()">
                        <option value="" ${empty param.transactionType ? 'selected' : ''}>Tất cả giao dịch</option>
                        <option value="Xuất kho" ${param.transactionType == 'Xuất kho' ? 'selected' : ''}>Xuất kho</option>
                        <option value="Mua hàng" ${param.transactionType == 'Mua hàng' ? 'selected' : ''}>Mua hàng</option>
                        <option value="Nhập kho" ${param.transactionType == 'Nhập kho' ? 'selected' : ''}>Nhập kho</option>
                    </select>
                </div>
                <div class="filter-group">
                    <select name="pageSize" onchange="this.form.submit()">
                        <option value="10" ${param.pageSize == '10' || param.pageSize == null ? 'selected' : ''}>Hiển thị 10</option>
                        <option value="20" ${param.pageSize == '20' ? 'selected' : ''}>Hiển thị 20</option>
                        <option value="50" ${param.pageSize == '50' ? 'selected' : ''}>Hiển thị 50</option>
                    </select>
                </div>
                <button type="submit" class="btn btn-secondary">Tìm kiếm</button>
                <button type="button" class="btn btn-light" id="resetFilterBtn" style="margin-left: 8px; border: 1px solid #ccc; color: #333; background: #f8f9fa;">Đặt lại</button>
            </div>
        </form>
    </div>
    <div class="content-card table-responsive">
        <table class="data-table">
            <thead>
            <tr>
                <th>Loại giao dịch</th>
                <th>Mã đơn</th>
                <th>Ngày thực hiện</th>
                <th>Tên vật tư</th>
                <th>Trạng thái vật tư</th>
                <th>Số lượng</th>
                <th>ID vật tư</th>
                <th>Tên người thực hiện</th>
            </tr>
            </thead>
            <tbody>
            <c:forEach var="row" items="${historyList}">
                <tr>
                    <td>${row.transactionType}</td>
                    <td>${row.referenceId}</td>
                    <td><fmt:formatDate value="${row.transactionDate}" pattern="yyyy-MM-dd HH:mm"/></td>
                    <td>${row.materialName}</td>
                    <td>${row.materialCondition}</td>
                    <td>${row.quantity != null ? (row.quantity < 0 ? -row.quantity : row.quantity) : ''}</td>
                    <td>${row.materialId}</td>
                    <td>${row.actorName}</td>
                </tr>
            </c:forEach>
            <c:if test="${empty historyList}">
                <tr><td colspan="8" class="no-data">Không có dữ liệu phù hợp.</td></tr>
            </c:if>
            </tbody>
        </table>
    </div>
    <!-- PHÂN TRANG -->
    <div class="pagination-section" style="align-items: flex-end;">
        <c:if test="${totalPages >= 1}">
            <ul class="pagination">
                <c:if test="${currentPage > 1}">
                    <c:url var="firstUrl" value="InventoryHistoryServlet">
                        <c:param name="page" value="1"/>
                        <c:param name="pageSize" value="${pageSize}"/>
                        <c:if test='${not empty param.keyword}'><c:param name="keyword" value="${param.keyword}"/></c:if>
                        <c:if test='${not empty param.transactionType}'><c:param name="transactionType" value="${param.transactionType}"/></c:if>
                        <c:if test='${not empty param.fromDate}'><c:param name="fromDate" value="${param.fromDate}"/></c:if>
                        <c:if test='${not empty param.toDate}'><c:param name="toDate" value="${param.toDate}"/></c:if>
                    </c:url>
                    <li class="page-item"><a class="page-link" href="${firstUrl}" title="Trang đầu">&laquo;</a></li>
                </c:if>
                <c:forEach var="i" begin="${startPage}" end="${endPage}">
                    <c:url var="pageUrl" value="InventoryHistoryServlet">
                        <c:param name="page" value="${i}"/>
                        <c:param name="pageSize" value="${pageSize}"/>
                        <c:if test='${not empty param.keyword}'><c:param name="keyword" value="${param.keyword}"/></c:if>
                        <c:if test='${not empty param.transactionType}'><c:param name="transactionType" value="${param.transactionType}"/></c:if>
                        <c:if test='${not empty param.fromDate}'><c:param name="fromDate" value="${param.fromDate}"/></c:if>
                        <c:if test='${not empty param.toDate}'><c:param name="toDate" value="${param.toDate}"/></c:if>
                    </c:url>
                    <li class="page-item ${i == currentPage ? 'active' : ''}"><a class="page-link" href="${pageUrl}">${i}</a></li>
                </c:forEach>
                <c:if test="${currentPage < totalPages}">
                    <c:url var="lastUrl" value="InventoryHistoryServlet">
                        <c:param name="page" value="${totalPages}"/>
                        <c:param name="pageSize" value="${pageSize}"/>
                        <c:if test='${not empty param.keyword}'><c:param name="keyword" value="${param.keyword}"/></c:if>
                        <c:if test='${not empty param.transactionType}'><c:param name="transactionType" value="${param.transactionType}"/></c:if>
                        <c:if test='${not empty param.fromDate}'><c:param name="fromDate" value="${param.fromDate}"/></c:if>
                        <c:if test='${not empty param.toDate}'><c:param name="toDate" value="${param.toDate}"/></c:if>
                    </c:url>
                    <li class="page-item"><a class="page-link" href="${lastUrl}" title="Trang cuối">&raquo;</a></li>
                </c:if>
            </ul>
        </c:if>
    </div>
</div>
<script>
    const keywordInput = document.getElementById('keywordInput');
    const clearKeywordBtn = document.getElementById('clearKeywordBtn');
    function updateClearKeywordBtn() {
        clearKeywordBtn.style.display = keywordInput.value ? 'block' : 'none';
    }
    updateClearKeywordBtn();
    clearKeywordBtn.onclick = function() {
        keywordInput.value = '';
        keywordInput.form.submit();
    };
    keywordInput.addEventListener('input', updateClearKeywordBtn);

    const resetBtn = document.getElementById('resetFilterBtn');
    resetBtn.onclick = function() {
        document.getElementById('keywordInput').value = '';
        resetBtn.form = document.getElementById('filterForm');
        resetBtn.form.submit();
    };
</script>
</body>
</html> 