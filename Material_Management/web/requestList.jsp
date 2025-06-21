<%-- requestList.jsp --%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Danh sách yêu cầu</title>
        <link rel="stylesheet" href="css/requestList.css">
        <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/css/all.min.css" rel="stylesheet">
        <style>
            .no-results {
                text-align: center;
                padding: 20px;
                color: #777;
                font-style: italic;
            }
        </style>
    </head>
    <body>
        <div class="request-list-container">
            <c:choose>
                <c:when test="${sessionScope.roleId == 1}">
                    <a class="btn-back" href="adminDashboard.jsp"><i class="fas fa-arrow-left"></i> Quay lại</a>
                </c:when>
                <c:when test="${sessionScope.roleId == 2}">
                    <a class="btn-back" href="director"><i class="fas fa-arrow-left"></i> Quay lại</a>
                </c:when>
                <c:when test="${sessionScope.roleId == 3}">
                    <a class="btn-back" href="warehouseEmployeeDashboard"><i class="fas fa-arrow-left"></i> Quay lại</a>
                </c:when>
                <c:when test="${sessionScope.roleId == 4}">
                    <a class="btn-back" href="staffDashboard"><i class="fas fa-arrow-left"></i> Quay lại</a>
                </c:when>
                <c:otherwise>
                    <a class="btn-back" href="homepage.jsp"><i class="fas fa-arrow-left"></i> Quay lại</a>
                </c:otherwise>
            </c:choose>
            <h2>Danh sách yêu cầu</h2>

            <!-- Bộ lọc -->
            <form method="get" action="RequestListServlet" id="filterForm" accept-charset="UTF-8">
                <div class="filter-row">
                    <div class="filter-col">
                        <label class="filter-label">Trạng thái:</label>
                        <select class="filter-select" name="status" id="statusSelect">
                            <option value="">Tất cả trạng thái</option>
                            <option value="Pending" ${param.status eq 'Pending' ? 'selected' : ''}>Chờ duyệt</option>
                            <option value="Approved" ${param.status eq 'Approved' ? 'selected' : ''}>Đã duyệt</option>
                            <option value="Rejected" ${param.status eq 'Rejected' ? 'selected' : ''}>Từ chối</option>
                        </select>
                    </div>
                    <div class="filter-col">
                        <label class="filter-label">Loại yêu cầu:</label>
                        <select class="filter-select" name="requestType" id="requestTypeSelect">
                            <option value="">Tất cả loại yêu cầu</option>
                            <option value="Mua Vật Tư" ${param.requestType eq 'Mua Vật Tư' ? 'selected' : ''}>Mua Vật Tư</option>
                            <option value="Xuất Kho" ${param.requestType eq 'Xuất Kho' ? 'selected' : ''}>Xuất Kho</option>
                        </select>
                    </div>
                    <div class="filter-col">
                        <label class="filter-label">Ngày bắt đầu:</label>
                        <input type="date" class="filter-input" name="startDate" id="startDateInput" value="${param.startDate}">
                    </div>
                    <div class="filter-col">
                        <label class="filter-label">Ngày kết thúc:</label>
                        <input type="date" class="filter-input" name="endDate" id="endDateInput" value="${param.endDate}">
                    </div>
                </div>
                <div class="filter-row filter-actions">
                    <div class="filter-col filter-col-actions">
                        <button type="submit" class="btn-primary">
                            <i class="fas fa-search"></i> Lọc
                        </button>
                        <button type="button" class="btn-sort" onclick="toggleSort()">
                            <i class="fas fa-sort"></i> Sắp xếp theo ID 
                            <span id="sortDirection">${param.sort eq 'desc' ? '(Z-A ↓)' : '(A-Z ↑)'}</span>
                        </button>
                        <button type="button" class="btn-outline" onclick="resetForm()">
                            <i class="fas fa-undo"></i> Đặt lại
                        </button>
                    </div>
                </div>
                <input type="hidden" name="sort" value="${param.sort eq 'desc' ? 'desc' : 'asc'}" id="sortInput">
                <input type="hidden" name="page" value="${param.page}" id="pageInput">
            </form>

            <!-- Bảng danh sách -->
            <div class="table-responsive">
                <c:if test="${empty requests}">
                    <p class="no-results">Không tìm thấy yêu cầu nào phù hợp.</p>
                </c:if>
                <c:if test="${not empty requests}">
                    <table class="request-table">
                        <thead>
                            <tr>
                                <th>ID</th>
                                <th>Loại yêu cầu</th>
                                <th>Người tạo</th>
                                <th>Ngày tạo</th>
                                <th>Trạng thái</th>
                                <th>Lý do</th>
                                <th>Ghi chú</th>
                                <th>Thao tác</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach items="${requests}" var="r">
                                <tr>
                                    <td>${r.requestId}</td>
                                    <td>${r.requestType}</td>
                                    <td>
                                        ${r.creatorName}
                                        <small class="text-muted">(${r.creatorRole})</small>
                                    </td>
                                    <td>${r.createdAt}</td>
                                    <td>
                                        <span class="status-badge ${r.requestStatus}">
                                            ${r.requestStatus}
                                        </span>
                                    </td>
                                    <td>${r.reason}</td>
                                    <td>${r.directorNote}</td>
                                    <td>
                                        <div class="action-buttons">
                                            <a href="viewRequestDetail?requestId=${r.requestId}" class="btn-info btn-sm">Chi tiết</a>
                                            <a href="editRequest?requestId=${r.requestId}" class="btn-info btn-sm"><i class="fas fa-edit"></i> Chỉnh sửa</a>
                                            <c:if test="${r.requestStatus == 'Pending' && sessionScope.user.role == 'Director'}">
                                                <a href="ApproveRequestServlet?id=${r.requestId}" class="btn-success btn-sm">Duyệt</a>
                                                <a href="RejectRequestServlet?id=${r.requestId}" class="btn-danger btn-sm">Từ chối</a>
                                            </c:if>
                                        </div>
                                    </td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </c:if>
            </div>

            <!-- Phân trang -->
            <div class="pagination-container">
                <c:if test="${totalPages > 0}">
                    <c:url var="pageUrl" value="RequestListServlet">
                        <c:param name="status" value="${param.status}" />
                        <c:param name="requestType" value="${param.requestType}" />
                        <c:param name="startDate" value="${param.startDate}" />
                        <c:param name="endDate" value="${param.endDate}" />
                        <c:param name="sort" value="${param.sort}" />
                    </c:url>

                    <ul class="pagination-list">
                        <!-- Previous Page -->
                        <li class="pagination-item ${currentPage == 1 ? 'disabled' : ''}">
                            <a class="pagination-link" href="${currentPage > 1 ? pageUrl.concat('&page=').concat(currentPage - 1) : '#'}" aria-label="Previous">&laquo;</a>
                        </li>

                        <!-- Page Numbers -->
                        <c:set var="maxPagesToShow" value="5" />
                        <c:set var="startPage" value="${currentPage - 2}" />
                        <c:set var="endPage" value="${currentPage + 2}" />

                        <c:if test="${startPage < 1}">
                            <c:set var="endPage" value="${endPage + (1 - startPage)}" />
                            <c:set var="startPage" value="1" />
                        </c:if>
                        <c:if test="${endPage > totalPages}">
                            <c:set var="startPage" value="${startPage - (endPage - totalPages)}" />
                            <c:set var="endPage" value="${totalPages}" />
                        </c:if>
                        <c:if test="${startPage < 1}">
                            <c:set var="startPage" value="1" />
                        </c:if>

                        <c:if test="${startPage > 1}">
                            <li class="pagination-item"><a class="pagination-link" href="${pageUrl.concat('&page=1')}">1</a></li>
                            <c:if test="${startPage > 2}">
                                <li class="pagination-item disabled"><span class="pagination-link">...</span></li>
                            </c:if>
                        </c:if>
                        
                        <c:forEach begin="${startPage}" end="${endPage}" var="i">
                            <li class="pagination-item ${currentPage == i ? 'active' : ''}">
                                <a class="pagination-link" href="${pageUrl.concat('&page=').concat(i)}">${i}</a>
                            </li>
                        </c:forEach>

                        <c:if test="${endPage < totalPages}">
                            <c:if test="${endPage < totalPages - 1}">
                                <li class="pagination-item disabled"><span class="pagination-link">...</span></li>
                            </c:if>
                            <li class="pagination-item"><a class="pagination-link" href="${pageUrl.concat('&page=').concat(totalPages)}">${totalPages}</a></li>
                        </c:if>

                        <!-- Next Page -->
                        <li class="pagination-item ${currentPage == totalPages ? 'disabled' : ''}">
                            <a class="pagination-link" href="${currentPage < totalPages ? pageUrl.concat('&page=').concat(currentPage + 1) : '#'}" aria-label="Next">&raquo;</a>
                        </li>
                    </ul>
                </c:if>
            </div>

            <script>
                function resetForm() {
                    document.getElementById('statusSelect').value = '';
                    document.getElementById('requestTypeSelect').value = '';
                    document.getElementById('startDateInput').value = '';
                    document.getElementById('endDateInput').value = '';
                    document.getElementById('sortInput').value = 'asc';
                    document.getElementById('pageInput').value = '1';

                    document.getElementById('filterForm').submit();
                }

                function toggleSort() {
                    const currentSort = document.getElementById('sortInput').value;
                    const newSort = currentSort === 'asc' ? 'desc' : 'asc';
                    document.getElementById('sortInput').value = newSort;
                    document.getElementById('filterForm').submit();
                }
            </script>
        </div>
    </body>
</html>