<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Lịch sử xuất kho</title>
        <link rel="stylesheet" href="css/exportForm.css">
        <link rel="stylesheet" href="css/sidebar.css">
        <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/css/all.min.css" rel="stylesheet">
    </head>
    <body>
        <div class="main-layout">
            <jsp:include page="sidebar.jsp" />
            <div class="main-content">

                <div class="request-form-container">
                    <h2>Lịch sử xuất kho</h2>
                    <div style="display: flex; justify-content: flex-end; margin: 20px 40px 0 0;">
                        <a href="exportFormPending" class="export-list-btn">Danh sách phiếu xuất kho</a>
                    </div>
                    <c:if test="${param.success eq 'true'}">
                        <div class="alert success">
                            <i class="fas fa-check-circle"></i> Xuất kho thành công!
                        </div>
                    </c:if>
                    <form id="filterForm" method="get" action="exportFormHistory" accept-charset="UTF-8">
                        <div class="filter-row">
                            <div class="filter-col">
                                <label class="filter-label">Tên dự án:</label>
                                <input type="text" class="filter-input" name="projectName" placeholder="Tìm theo tên dự án..." value="${param.projectName}">
                            </div>
                            <div class="filter-col">
                                <label class="filter-label">Ngày bắt đầu:</label>
                                <input type="date" class="filter-input" id="startDateInput" name="startDate" value="${param.startDate}">
                            </div>
                            <div class="filter-col">
                                <label class="filter-label">Ngày kết thúc:</label>
                                <input type="date" class="filter-input" id="endDateInput" name="endDate" value="${param.endDate}">
                            </div>
                            <div class="filter-col filter-col-actions">
                                <button type="submit" class="btn-primary" id="btnFilter"><i class="fas fa-filter"></i> Lọc</button>
                            </div>
                        </div>
                    </form>

                    <table class="request-table">
                        <thead>
                            <tr>
                                <th class="id-col">
                                    <a href="?sortField=export_id&sortDir=${param.sortField == 'export_id' && param.sortDir == 'asc' ? 'desc' : 'asc'}">
                                        Mã phiếu xuất kho
                                        <c:if test="${param.sortField == 'export_id'}">
                                            <c:choose>
                                                <c:when test="${param.sortDir == 'asc'}">&#9650;</c:when>
                                                <c:otherwise>&#9660;</c:otherwise>
                                            </c:choose>
                                        </c:if>
                                    </a></th>
                                <th class="creator-col">Người tạo</th>
                                <th class="time-col">
                                    <a href="?sortField=export_date&sortDir=${param.sortField == 'export_date' && param.sortDir == 'asc' ? 'desc' : 'asc'}">
                                        Ngày tạo
                                        <c:if test="${param.sortField == 'export_date'}">
                                            <c:choose>
                                                <c:when test="${param.sortDir == 'asc'}">&#9650;</c:when>
                                                <c:otherwise>&#9660;</c:otherwise>
                                            </c:choose>
                                        </c:if>
                                    </a></th>
                                <th class="project-col"><a href="?sortField=recipient_name&sortDir=${param.sortField == 'recipient_name' && param.sortDir == 'asc' ? 'desc' : 'asc'}">
                                        Tên dự án
                                        <c:if test="${param.sortField == 'recipient_name'}">
                                            <c:choose>
                                                <c:when test="${param.sortDir == 'asc'}">&#9650;</c:when>
                                                <c:otherwise>&#9660;</c:otherwise>
                                            </c:choose>
                                        </c:if>
                                    </a></th>
                                <th class="nowrap-col">Trạng thái</th>
                                <th class="note-col">Ghi chú</th>
                                <th class="id-col">Chi tiết</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="f" items="${exportForms}">
                                <c:if test="${f.exportId != 0}">
                                    <tr>
                                        <td class="id-col">${f.exportId}</td>
                                        <td class="creator-col">${f.userName}
                                            <br>
                                            <span class="user-role">(${f.userRole})</span>
                                        </td>
                                        <td class="time-col">
                                            <fmt:formatDate value="${f.exportDate}" pattern="yyyy-MM-dd HH:mm"/>
                                        </td>
                                        <td class="project-col">${f.recipientName}</td>
                                        <td class="nowrap-col">${f.status}</td>
                                        <td class="note-col">${f.description}</td>

                                        <td class="id-col">
                                            <a href="exportFormDetail?exportId=${f.exportId}" class="export-list-btn">Xem</a>

                                        </td>
                                    </tr>
                                </c:if>
                            </c:forEach>
                        </tbody>
                    </table>
                    <div class="pagination-container">
                        <c:if test="${totalPages > 0}">
                            <c:url var="pageUrl" value="exportFormHistory">
                                <c:param name="status" value="${param.status}" />
                                <c:param name="startDate" value="${param.startDate}" />
                                <c:param name="endDate" value="${param.endDate}" />
                                <c:param name="sort" value="${param.sort}" />
                            </c:url>

                            <ul class="pagination-list">
                                <li class="pagination-item ${currentPage == 1 ? 'disabled' : ''}">
                                    <a class="pagination-link" href="${currentPage > 1 ? pageUrl.concat('&page=').concat(currentPage - 1) : '#'}" aria-label="Previous">&laquo;</a>
                                </li>

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

                                <li class="pagination-item ${currentPage == totalPages ? 'disabled' : ''}">
                                    <a class="pagination-link" href="${currentPage < totalPages ? pageUrl.concat('&page=').concat(currentPage + 1) : '#'}" aria-label="Next">&raquo;</a>
                                </li>
                            </ul>
                        </c:if>
                    </div>
                </div>
                <script src="js/requestList.js"></script>
            </div>
        </div>
    </body>
</html> 