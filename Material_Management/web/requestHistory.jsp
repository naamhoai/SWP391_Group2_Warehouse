<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html>
    <head>
        <title>Lịch sử yêu cầu xuất vật tư</title>
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
        <style>
            .request-table th.action-col,
            .request-table td.action-col {
                width: 60px !important;
                min-width: 40px !important;
                max-width: 105px !important;
                text-align: center;
                white-space: nowrap;
            }
            .request-table th.note-col,
            .request-table td.note-col {
                width: 180px !important;
                min-width: 120px !important;
                max-width: 280px !important;
                text-align: center;
                white-space: normal;
            }
        </style>
    </head>
    <body>
        <div class="main-layout">
            <div class="sidebar">
                <%@include file="sidebar.jsp" %>
            </div>
            <div class="main-content">
                <div class="request-list-container">

                    <h2 class="request-list-title">Lịch sử yêu cầu xuất vật tư</h2>
                    <!-- Bộ lọc -->
                    <form method="get" action="requestHistory" id="filterForm" accept-charset="UTF-8">
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

                        <c:if test="${not empty param.requestId}">
                            <input type="hidden" name="requestId" value="${param.requestId}">
                        </c:if>
                    </form>
                    <div class="table-responsive">
                        <table class="request-table">
                            <thead>
                                <tr>
                                    <th class="${param.sortBy eq 'id' ? 'sorted' : ''}">
                                        <a href="requestHistory?sortBy=id&order=${param.sortBy eq 'id' && param.order eq 'asc' ? 'desc' : 'asc'}<c:if test="${not empty param.requestId}">&requestId=${param.requestId}</c:if>">ID
                                            <c:if test="${param.sortBy eq 'id'}">
                                                <i class="fa fa-caret-${param.order eq 'asc' ? 'up' : 'down'}"></i>
                                            </c:if>
                                        </a>
                                    </th>
                                    <th class="${param.sortBy eq 'name' ? 'sorted' : ''}">
                                        <a href="requestHistory?sortBy=name&order=${param.sortBy eq 'name' && param.order eq 'asc' ? 'desc' : 'asc'}<c:if test="${not empty param.requestId}">&requestId=${param.requestId}</c:if>">Tên dự án
                                            <c:if test="${param.sortBy eq 'name'}">
                                                <i class="fa fa-caret-${param.order eq 'asc' ? 'up' : 'down'}"></i>
                                            </c:if>
                                        </a>
                                    </th>
                                    <th class="${param.sortBy eq 'date' ? 'sorted' : ''}">
                                        <a href="requestHistory?sortBy=date&order=${param.sortBy eq 'date' && param.order eq 'asc' ? 'desc' : 'asc'}<c:if test="${not empty param.requestId}">&requestId=${param.requestId}</c:if>">Thời gian
                                            <c:if test="${param.sortBy eq 'date'}">
                                                <i class="fa fa-caret-${param.order eq 'asc' ? 'up' : 'down'}"></i>
                                            </c:if>
                                        </a>
                                    </th>
                                    <th>Trạng thái</th>
                                    <th>Người thay đổi</th>
                                    <th>Lý do</th>
                                    <th class="note-col">Ghi chú</th>
                                    <th class="action-col">Hành động</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:if test="${noRecords || empty historyList}">
                                    <tr>
                                        <td colspan="10" style="text-align:center;color:#888;font-style:italic;">Không có bản ghi nào.</td>
                                    </tr>
                                </c:if>
                                <c:set var="lastReason" value="" scope="page"/>
                                <c:set var="lastRequestId" value="-1" scope="page"/>
                                <c:forEach var="h" items="${historyList}">
                                    <tr>
                                        <td>${h.requestId}</td>
                                        <td>${h.recipientName}</td>


                                        <td><fmt:formatDate value="${h.changeTime}" pattern="yyyy-MM-dd"/></td>
                                        <td>
                                            <span class="status-badge" data-status="${h.newStatus}">
                                                <c:choose>
                                                    <c:when test="${h.newStatus eq 'Chờ duyệt'}">Chờ duyệt</c:when>
                                                    <c:when test="${h.newStatus eq 'Đã duyệt'}">Đã duyệt</c:when>
                                                    <c:when test="${h.newStatus eq 'Từ chối'}">Từ chối</c:when>
                                                    <c:otherwise>${h.newStatus}</c:otherwise>
                                                </c:choose>
                                            </span>
                                        </td>
                                        <td>
                                            <c:choose>
                                                <c:when test="${not empty userNameMap[h.changedBy]}">
                                                    ${userNameMap[h.changedBy]}
                                                    <c:if test="${not empty userRoleMap[h.changedBy]}">
                                                        <br><small style="color: #666; font-size: 11px;">(${userRoleMap[h.changedBy]})</small>
                                                    </c:if>
                                                </c:when>
                                                <c:otherwise>
                                                    <span style="color: #999; font-style: italic;">Không xác định</span>
                                                </c:otherwise>
                                            </c:choose>
                                        </td>
                                        <td>
                                            <c:if test="${h.requestId != lastRequestId}">
                                                <c:set var="lastReason" value=""/>
                                                <c:set var="lastRequestId" value="${h.requestId}"/>
                                            </c:if>
                                            <c:choose>
                                                <c:when test="${not empty h.changeReason}">
                                                    ${h.changeReason}
                                                    <c:set var="lastReason" value="${h.changeReason}"/>
                                                </c:when>
                                                <c:otherwise>
                                                    ${lastReason}
                                                </c:otherwise>
                                            </c:choose>
                                        </td>
                                        <td class="note-col">
                                            <c:choose>
                                                <c:when test="${not empty h.directorNote}">
                                                    ${h.directorNote}
                                                </c:when>
                                                <c:otherwise>

                                                </c:otherwise>
                                            </c:choose>
                                        </td>
                                        <td class="action-col">
                                            <a href="requestHistoryDetail?historyId=${h.historyId}" class="btn-info btn-sm">Chi tiết</a>
                                        </td>
                                    </tr>
                                </c:forEach>
                            </tbody>
                        </table>
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
                </div>
            </div>
        </div>
        <script src="js/requestForm.js"></script>
        <script src="js/requestList.js"></script>
        <script src="js/sidebar.js"></script>
    </body>
</html>