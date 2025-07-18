<%-- requestList.jsp --%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Danh sách yêu cầu</title>
        <link rel="stylesheet" href="css/requestList.css">
        <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/css/all.min.css" rel="stylesheet">
        <link rel="stylesheet" href="css/sidebar.css">
        <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/css/all.min.css">

        <link href="https://fonts.googleapis.com/css2?family=Roboto:wght@400;500;700&display=swap" rel="stylesheet">
        <script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
        <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/flatpickr/dist/flatpickr.min.css">
        <script src="https://cdn.jsdelivr.net/npm/flatpickr"></script>
        <script src="https://cdn.jsdelivr.net/npm/flatpickr/dist/l10n/vi.js"></script>

    <body>
        <div class="main-layout">
            <div class="sidebar">
                <%@include file="sidebar.jsp" %>
            </div>
            <div class="main-content">
                <div class="request-list-container">

                    <h2>Danh sách yêu cầu xuất vật tư</h2>

                    <!-- Bộ lọc -->
                    <form method="get" action="RequestListServlet" id="filterForm" accept-charset="UTF-8">
                        <div class="filter-row">
                            <div class="filter-col">
                                <label class="filter-label">Trạng thái:</label>
                                <select class="filter-select" name="status" id="statusSelect" onchange="this.form.submit()">
                                    <option value="">Tất cả trạng thái</option>
                                    <option value="Chờ duyệt" ${param.status eq 'Chờ duyệt' ? 'selected' : ''}>Chờ duyệt</option>
                                    <option value="Đã duyệt" ${param.status eq 'Đã duyệt' ? 'selected' : ''}>Đã duyệt</option>
                                    <option value="Từ chối" ${param.status eq 'Từ chối' ? 'selected' : ''}>Từ chối</option>
                                </select>
                            </div>
                            <div class="filter-col">
                                <label class="filter-label">Tên dự án:</label>
                                <input type="text" class="filter-input" name="projectName" value="${param.projectName}">
                            </div>
                            <div class="filter-col">
                                <label class="filter-label">Ngày bắt đầu:</label>
                                <input type="date" class="filter-input" name="startDate" id="startDateInput" value="${param.startDate}" max="">
                            </div>
                            <div class="filter-col">
                                <label class="filter-label">Ngày kết thúc:</label>
                                <input type="date" class="filter-input" name="endDate" id="endDateInput" value="${param.endDate}" max="">
                            </div>
                            <div class="filter-col filter-col-actions">
                                <button type="submit" class="btn-primary" id="btnFilter"><i class="fas fa-filter"></i> Lọc</button>
                            </div>
                        </div>

                    </form>
                    <c:if test="${not empty error}">
                        <div class="alert alert-danger" style="margin-bottom: 18px; margin-left: 0; max-width: 600px;">
                            ${error}
                        </div>
                    </c:if>
                    <div class="table-responsive">
                        <c:if test="${empty requests}">
                            <p class="no-results">Không tìm thấy yêu cầu nào phù hợp.</p>
                        </c:if>
                        <c:if test="${not empty requests}">
                            <table class="request-table">
                                <thead>
                                    <tr>
                                        <th class="${param.sortBy eq 'id' ? 'sorted' : ''}">
                                            <a href="RequestListServlet?sortBy=id&order=${param.sortBy eq 'id' && param.order eq 'asc' ? 'desc' : 'asc'}">ID
                                                <c:if test="${param.sortBy eq 'id'}">
                                                    <i class="fa fa-caret-${param.order eq 'asc' ? 'up' : 'down'}"></i>
                                                </c:if>
                                            </a>
                                        </th>
                                        <th class="${param.sortBy eq 'name' ? 'sorted' : ''}">
                                            <a href="RequestListServlet?sortBy=name&order=${param.sortBy eq 'name' && param.order eq 'asc' ? 'desc' : 'asc'}">Tên dự án
                                                <c:if test="${param.sortBy eq 'name'}">
                                                    <i class="fa fa-caret-${param.order eq 'asc' ? 'up' : 'down'}"></i>
                                                </c:if>
                                            </a>
                                        </th>
                                        <th class="${param.sortBy eq 'date' ? 'sorted' : ''}">
                                            <a href="RequestListServlet?sortBy=date&order=${param.sortBy eq 'date' && param.order eq 'asc' ? 'desc' : 'asc'}">Ngày tạo
                                                <c:if test="${param.sortBy eq 'date'}">
                                                    <i class="fa fa-caret-${param.order eq 'asc' ? 'up' : 'down'}"></i>
                                                </c:if>
                                            </a>
                                        </th>
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
                                            <td>${r.recipientName}</td>
                                            <td>
                                                <fmt:formatDate value="${r.createdAt}" pattern="yyyy-MM-dd"/>
                                            </td>
                                            <td>
                                                <span class="status-badge" data-status="${r.requestStatus}">
                                                    <c:choose>
                                                        <c:when test="${r.requestStatus eq 'Chờ duyệt'}">Chờ duyệt</c:when>
                                                        <c:when test="${r.requestStatus eq 'Đã duyệt'}">Đã duyệt</c:when>
                                                        <c:when test="${r.requestStatus eq 'Từ chối'}">Từ chối</c:when>
                                                        <c:otherwise>${r.requestStatus}</c:otherwise>
                                                    </c:choose>
                                                </span>
                                            </td>
                                            <td>${r.reason}</td>
                                            <td>${r.directorNote}</td>
                                            <td>
                                                <div class="action-buttons">
                                                    <a href="viewRequestDetail?requestId=${r.requestId}" class="btn-info btn-sm">Chi tiết</a>
                                                    <c:choose>
                                                        <c:when test="${sessionScope.roleId == 4 && r.requestStatus == 'Từ chối'}">
                                                            <a href="editRequest?requestId=${r.requestId}" class="btn-info btn-sm">Chỉnh sửa</a>
                                                        </c:when>
                                                        <c:otherwise>
                                                            <a class="btn-info btn-sm btn-disabled" style="pointer-events: none; opacity: 0.5;"></i> Chỉnh sửa</a>
                                                        </c:otherwise>
                                                    </c:choose>
                                                    <c:if test="${r.requestStatus == 'Chờ duyệt' && sessionScope.user.role == 'Giám đốc'}">
                                                        <a href="ApproveRequestServlet?id=${r.requestId}" class="btn-success btn-sm">Duyệt</a>
                                                        <a href="RejectRequestServlet?id=${r.requestId}" class="btn-danger btn-sm">Từ chối</a>
                                                    </c:if>
                                                    <a href="requestHistory?requestId=${r.requestId}" class="btn-info btn-sm">Lịch sử</a>
                                                </div>
                                            </td>
                                        </tr>
                                    </c:forEach>
                                </tbody>
                            </table>
                        </c:if>
                    </div>

                    <div class="pagination-container">
                        <c:if test="${totalPages > 1}">
                            <c:url var="pageUrl" value="requestHistory">
                                <c:param name="status" value="${param.status}" />
                                <c:param name="projectName" value="${param.projectName}" />
                                <c:param name="startDate" value="${param.startDate}" />
                                <c:param name="endDate" value="${param.endDate}" />
                                <c:if test="${not empty param.requestId}">
                                    <c:param name="requestId" value="${param.requestId}" />
                                </c:if>
                            </c:url>
                            <ul class="pagination-list">
                                <li class="pagination-item ${currentPage == 1 ? 'disabled' : ''}">
                                    <a class="pagination-link" href="${currentPage > 1 ? pageUrl.concat('&page=').concat(currentPage - 1) : '#'}" aria-label="Previous">&laquo;</a>
                                </li>
                                <c:choose>
                                    <c:when test="${currentPage <= 2}">
                                        <c:set var="endPage" value="3" />
                                        <c:if test="${totalPages < 3}">
                                            <c:set var="endPage" value="${totalPages}" />
                                        </c:if>
                                        <c:forEach var="i" begin="1" end="${endPage}">
                                            <li class="pagination-item ${currentPage == i ? 'active' : ''}">
                                                <a class="pagination-link" href="${pageUrl.concat('&page=').concat(i)}">${i}</a>
                                            </li>
                                        </c:forEach>
                                        <c:if test="${totalPages > 4}">
                                            <li class="pagination-item disabled"><span class="pagination-link">...</span></li>
                                            </c:if>
                                            <c:if test="${totalPages > 3}">
                                            <li class="pagination-item ${currentPage == totalPages ? 'active' : ''}">
                                                <a class="pagination-link" href="${pageUrl.concat('&page=').concat(totalPages)}">${totalPages}</a>
                                            </li>
                                        </c:if>
                                    </c:when>
                                    <c:when test="${currentPage >= totalPages - 2}">
                                        <li class="pagination-item">
                                            <a class="pagination-link" href="${pageUrl.concat('&page=1')}">1</a>
                                        </li>
                                        <c:if test="${totalPages > 4}">
                                            <li class="pagination-item disabled"><span class="pagination-link">...</span></li>
                                            </c:if>
                                            <c:set var="beginPage" value="${totalPages - 2}" />
                                            <c:if test="${beginPage < 2}">
                                                <c:set var="beginPage" value="2" />
                                            </c:if>
                                            <c:forEach var="i" begin="${beginPage}" end="${totalPages}">
                                            <li class="pagination-item ${currentPage == i ? 'active' : ''}">
                                                <a class="pagination-link" href="${pageUrl.concat('&page=').concat(i)}">${i}</a>
                                            </li>
                                        </c:forEach>
                                    </c:when>
                                    <c:otherwise>
                                        <li class="pagination-item">
                                            <a class="pagination-link" href="${pageUrl.concat('&page=1')}">1</a>
                                        </li>
                                        <li class="pagination-item disabled"><span class="pagination-link">...</span></li>
                                        <li class="pagination-item ${currentPage == currentPage ? 'active' : ''}">
                                            <a class="pagination-link" href="${pageUrl.concat('&page=').concat(currentPage)}">${currentPage}</a>
                                        </li>
                                        <c:if test="${currentPage + 1 < totalPages}">
                                            <li class="pagination-item">
                                                <a class="pagination-link" href="${pageUrl.concat('&page=').concat(currentPage + 1)}">${currentPage + 1}</a>
                                            </li>
                                            <li class="pagination-item disabled"><span class="pagination-link">...</span></li>
                                            </c:if>
                                        <li class="pagination-item ${currentPage == totalPages ? 'active' : ''}">
                                            <a class="pagination-link" href="${pageUrl.concat('&page=').concat(totalPages)}">${totalPages}</a>
                                        </li>
                                    </c:otherwise>
                                </c:choose>
                                <li class="pagination-item ${currentPage == totalPages ? 'disabled' : ''}">
                                    <a class="pagination-link" href="${currentPage < totalPages ? pageUrl.concat('&page=').concat(currentPage + 1) : '#'}" aria-label="Next">&raquo;</a>
                                </li>
                            </ul>
                        </c:if>
                    </div>
                    <script src="js/requestForm.js"></script>
                    <script src="js/requestList.js"></script>
                </div>
            </div>
        </div>
    </body>
</html>