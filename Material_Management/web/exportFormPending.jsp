<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Danh sách phiếu xuất kho</title>
        <link rel="stylesheet" href="css/exportForm.css">
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/css/all.min.css">
        <link rel="stylesheet" href="css/sidebar.css">
    </head>
    <body>
        <div class="main-layout">
            <div class="sidebar">
                <jsp:include page="sidebar.jsp" />
            </div>
            <div class="main-content" style="display: block;">
                <!-- PHẦN 1: HEADER -->
                <div class="dashboard-header">
                    <div class="header-left">
                        <h2>Danh sách phiếu xuất kho</h2>
                    </div>
                    <div class="header-actions">
                        <a href="exportFormHistory" class="btn-add"><i class="fas fa-history"></i> Lịch sử xuất kho</a>
                    </div>
                </div>
                <!-- PHẦN 2: FILTER -->

                <form id="filterForm" method="get" action="exportFormPending" accept-charset="UTF-8">
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


                <table class="supplier-table">
                    <thead>
                        <tr>
                            <th class="id-col">
                                <a href="?projectName=${param.projectName}&startDate=${param.startDate}&endDate=${param.endDate}&sortField=request_id&sortDir=${param.sortField == 'request_id' && param.sortDir == 'asc' ? 'desc' : 'asc'}">
                                    Mã yêu cầu
                                    <c:if test="${param.sortField == 'request_id'}">
                                        <c:choose>
                                            <c:when test="${param.sortDir == 'asc'}">&#9650;</c:when>
                                            <c:otherwise>&#9660;</c:otherwise>
                                        </c:choose>
                                    </c:if>
                                </a>
                            </th>
                            <th class="project-col">
                                <a href="?projectName=${param.projectName}&startDate=${param.startDate}&endDate=${param.endDate}&sortField=recipient_name&sortDir=${param.sortField == 'recipient_name' && param.sortDir == 'asc' ? 'desc' : 'asc'}">
                                    Tên dự án
                                    <c:if test="${param.sortField == 'recipient_name'}">
                                        <c:choose>
                                            <c:when test="${param.sortDir == 'asc'}">&#9650;</c:when>
                                            <c:otherwise>&#9660;</c:otherwise>
                                        </c:choose>
                                    </c:if>
                                </a>
                            </th>
                            <th class="creator-col">Người tạo</th>
                            <th class="time-col">
                                <a href="?projectName=${param.projectName}&startDate=${param.startDate}&endDate=${param.endDate}&sortField=created_at&sortDir=${param.sortField == 'created_at' && param.sortDir == 'asc' ? 'desc' : 'asc'}">
                                    Ngày tạo
                                    <c:if test="${param.sortField == 'created_at'}">
                                        <c:choose>
                                            <c:when test="${param.sortDir == 'asc'}">&#9650;</c:when>
                                            <c:otherwise>&#9660;</c:otherwise>
                                        </c:choose>
                                    </c:if>
                                </a>
                            </th>
                            <th class="note-col">Lý do</th>
                            <th class="nowrap-col">Địa chỉ giao</th>
                            <th class="project-col">Chi tiết</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="r" items="${approvedRequests}">
                            <tr>
                                <td class="id-col">${r.requestId}</td>
                                <td class="project-col">${r.recipientName}</td>
                                <td class="creator-col">${r.creatorName}</td>
                                <td class="time-col">
                                    <fmt:formatDate value="${r.createdAt}" pattern="yyyy-MM-dd HH:mm"/>
                                </td>
                                <td class="note-col">${r.reason}</td>
                                <td class="nowrap-col">${r.deliveryAddress}</td>
                                <td class="project-col">
                                    <a href="createExportForm?requestId=${r.requestId}" class="btn-view">Xuất kho</a>
                                </td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>

                <!-- PHÂN TRANG RA NGOÀI, CĂN GIỮA -->
                <div class="pagination" style="justify-content:center; display:flex;">
                    <c:if test="${totalPages > 0}">
                        <c:url var="pageUrl" value="exportFormPending">
                            <c:param name="projectName" value="${param.projectName}" />
                            <c:param name="startDate" value="${param.startDate}" />
                            <c:param name="endDate" value="${param.endDate}" />
                            <c:param name="sortBy" value="${param.sortBy}" />
                            <c:param name="sortDir" value="${param.sortDir}" />
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
                            <li class="pagination-item ${currentPage == totalPages ? 'disabled' : ''}">
                                <a class="pagination-link" href="${currentPage < totalPages ? pageUrl.concat('&page=').concat(currentPage + 1) : '#'}" aria-label="Next">&raquo;</a>
                            </li>
                        </ul>
                    </c:if>
                </div>
                <script src="js/requestList.js"></script>
            </div>
        </div>
    </body>
</html> 