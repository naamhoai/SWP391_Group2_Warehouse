<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<!DOCTYPE html>
<html lang="vi">
    <head>
        <meta charset="UTF-8">
        <title>Lịch sử xuất nhập tồn</title>
        <link rel="stylesheet" href="css/inventoryHistory.css">
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/css/all.min.css">
        <link rel="stylesheet" href="css/sidebar.css">
        <script src="js/sidebar.js"></script>
    </head>
    <body>
        <jsp:include page="sidebar.jsp" />
        <div class="main-content">
            <div class="header-section">
                <h1 class="page-title">Lịch sử xuất nhập tồn</h1>
            </div>
            <div class="content-card">
                <form method="get" action="InventoryHistoryServlet" id="filterForm" class="filter-form">
                    <div class="filter-row">
                        <div class="filter-group">
                            <i class="fas fa-list-ol"></i>
                            <select name="pageSize" onchange="this.form.submit()">
                                <option value="10" ${param.pageSize == '10' || param.pageSize == null ? 'selected' : ''}>Hiển thị 10</option>
                                <option value="20" ${param.pageSize == '20' ? 'selected' : ''}>Hiển thị 20</option>
                                <option value="50" ${param.pageSize == '50' ? 'selected' : ''}>Hiển thị 50</option>
                            </select>
                        </div>
                        <div class="filter-group" style="position: relative; display: flex; align-items: center;">
                            <i class="fas fa-box"></i>
                            <input type="text" name="material" id="materialInput" placeholder="Tên vật tư..." value="${param.material != null ? param.material : ''}" style="padding-right: 24px;" />
                            <button type="button" id="clearMaterialBtn" style="position: absolute; right: 4px; background: none; border: none; cursor: pointer; font-size: 16px; color: #888;">&times;</button>
                        </div>
                        <div class="filter-group" style="position: relative; display: flex; align-items: center;">
                            <i class="fas fa-user"></i>
                            <input type="text" name="operator" id="operatorInput" placeholder="Người thực hiện..." value="${param.operator != null ? param.operator : ''}" style="padding-right: 24px;" />
                            <button type="button" id="clearOperatorBtn" style="position: absolute; right: 4px; background: none; border: none; cursor: pointer; font-size: 16px; color: #888;">&times;</button>
                        </div>
                        <div class="filter-group">
                            <label for="from" style="margin-right: 4px; font-weight: 500;">Từ ngày</label>
                            <i class="fas fa-calendar-alt"></i>
                            <input type="date" name="from" id="from" value="${param.from != null ? param.from : ''}" />
                        </div>
                        <div class="filter-group">
                            <label for="to" style="margin-right: 4px; font-weight: 500;">Đến ngày</label>
                            <i class="fas fa-calendar-alt"></i>
                            <input type="date" name="to" id="to" value="${param.to != null ? param.to : ''}" />
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
                            <th>Loại GD</th>
                            <th>Ngày</th>
                            <th>Mã phiếu</th>
                            <th>Vật tư</th>
                            <th>Số lượng</th>
                            <th>Đơn vị</th>
                            <th>Người thực hiện</th>
                            <th>Trạng thái</th>
                            <th>Ghi chú</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="row" items="${historyList}">
                            <tr>
                                <td>
                                    <c:choose>
                                        <c:when test="${row.type eq 'import'}">
                                            <span class="status-badge status-active"><i class="fas fa-arrow-down"></i> Nhập</span>
                                        </c:when>
                                        <c:when test="${row.type eq 'export'}">
                                            <span class="status-badge status-inactive"><i class="fas fa-arrow-up"></i> Xuất</span>
                                        </c:when>
                                        <c:when test="${row.type eq 'purchase'}">
                                            <span class="status-badge" style="background:#fef08a;color:#b45309"><i class="fas fa-shopping-cart"></i> Mua</span>
                                        </c:when>
                                    </c:choose>
                                </td>
                                <td><fmt:formatDate value="${row.date}" pattern="yyyy-MM-dd"/></td>
                                <td>${row.code}</td>
                                <td>${row.materialName}</td>
                                <td>${row.quantity}</td>
                                <td>${row.unit}</td>
                                <td>${row.operator}</td>
                                <td>${row.status}</td>
                                <td>${row.note}</td>
                            </tr>
                        </c:forEach>
                        <c:if test="${empty historyList}">
                            <tr><td colspan="9" class="no-data">Không có dữ liệu phù hợp.</td></tr>
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
                                <c:if test='${not empty param.material}'><c:param name="material" value="${param.material}"/></c:if>
                                <c:if test='${not empty param.operator}'><c:param name="operator" value="${param.operator}"/></c:if>
                                <c:if test='${not empty param.from}'><c:param name="from" value="${param.from}"/></c:if>
                                <c:if test='${not empty param.to}'><c:param name="to" value="${param.to}"/></c:if>
                            </c:url>
                            <li class="page-item"><a class="page-link" href="${firstUrl}" title="Trang đầu">&laquo;</a></li>
                        </c:if>
                        <c:forEach var="i" begin="${startPage}" end="${endPage}">
                            <c:url var="pageUrl" value="InventoryHistoryServlet">
                                <c:param name="page" value="${i}"/>
                                <c:param name="pageSize" value="${pageSize}"/>
                                <c:if test='${not empty param.material}'><c:param name="material" value="${param.material}"/></c:if>
                                <c:if test='${not empty param.operator}'><c:param name="operator" value="${param.operator}"/></c:if>
                                <c:if test='${not empty param.from}'><c:param name="from" value="${param.from}"/></c:if>
                                <c:if test='${not empty param.to}'><c:param name="to" value="${param.to}"/></c:if>
                            </c:url>
                            <li class="page-item ${i == currentPage ? 'active' : ''}"><a class="page-link" href="${pageUrl}">${i}</a></li>
                        </c:forEach>
                        <c:if test="${currentPage < totalPages}">
                            <c:url var="lastUrl" value="InventoryHistoryServlet">
                                <c:param name="page" value="${totalPages}"/>
                                <c:param name="pageSize" value="${pageSize}"/>
                                <c:if test='${not empty param.material}'><c:param name="material" value="${param.material}"/></c:if>
                                <c:if test='${not empty param.operator}'><c:param name="operator" value="${param.operator}"/></c:if>
                                <c:if test='${not empty param.from}'><c:param name="from" value="${param.from}"/></c:if>
                                <c:if test='${not empty param.to}'><c:param name="to" value="${param.to}"/></c:if>
                            </c:url>
                            <li class="page-item"><a class="page-link" href="${lastUrl}" title="Trang cuối">&raquo;</a></li>
                        </c:if>
                    </ul>
                    <div class="pagination-info" style="display:none;"></div>
                </c:if>
            </div>
        </div>

 
        <script>
            const operatorInput = document.getElementById('operatorInput');
            const clearOperatorBtn = document.getElementById('clearOperatorBtn');
            function updateClearOperatorBtn() {
                clearOperatorBtn.style.display = operatorInput.value ? 'block' : 'none';
            }
            updateClearOperatorBtn();
            clearOperatorBtn.onclick = function() {
                operatorInput.value = '';
                operatorInput.form.submit();
            };
            operatorInput.addEventListener('input', updateClearOperatorBtn);
            const materialInput = document.getElementById('materialInput');
            const clearMaterialBtn = document.getElementById('clearMaterialBtn');
            function updateClearMaterialBtn() {
                clearMaterialBtn.style.display = materialInput.value ? 'block' : 'none';
            }
            updateClearMaterialBtn();
            clearMaterialBtn.onclick = function() {
                materialInput.value = '';
                materialInput.form.submit();
            };
            materialInput.addEventListener('input', updateClearMaterialBtn);
            const resetBtn = document.getElementById('resetFilterBtn');
            resetBtn.onclick = function() {
                document.getElementById('materialInput').value = '';
                document.getElementById('operatorInput').value = '';
                document.getElementById('from').value = '';
                document.getElementById('to').value = '';
                resetBtn.form = document.getElementById('filterForm');
                resetBtn.form.submit();
            };
        </script>
    </body>
</html> 