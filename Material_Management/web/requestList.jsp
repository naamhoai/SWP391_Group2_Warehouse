<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Danh sách yêu cầu</title>
        <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/css/all.min.css" rel="stylesheet">
        <link rel="stylesheet" href="css/requestList.css">
        <link rel="stylesheet" href="css/sidebar.css">


    <body>
        <div class="main-layout">
            <div class="sidebar">
                <%@include file="sidebar.jsp" %>
            </div>
            <div class="main-content" style="display: block;">
                <!-- PHẦN 1: HEADER -->
                <div class="dashboard-header">
                    <div class="header-left">
                        <h2>Danh sách yêu cầu xuất vật tư</h2>
                    </div>
                    <div class="header-actions">
                        <a href="createRequest" class="btn-add"><i class="fas fa-plus"></i> Tạo yêu cầu xuất kho</a>
                        <a href="requestHistory" class="btn-add"><i class="fas fa-history"></i> Lịch sử</a>
                    </div>
                </div>
                <!-- PHẦN 2: FILTER -->
                <div class="filter-form">
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

                                <button type="submit" class="btn-primary btn-sm" id="btnFilter"> Tìm</button>
                                <button type="button" class="btn-secondary btn-sm" id="btnReset"><i class="fas fa-undo"></i> Đặt lại</button>
                            </div>
                        </div>
                    </form>
                </div>
                <c:if test="${not empty success}">
                    <div class="alert alert-success">
                        ${success}
                    </div>
                </c:if>
                <!-- PHẦN 3: TABLE -->
                <div class="table-card">
                    <table class="supplier-table">
                        <thead>
                            <tr>
                                <th>ID</th>
                                <th>Tên dự án</th>
                                <th>Ngày tạo</th>
                                <th>Trạng thái</th>
                                <th>Lý do</th>
                                <th>Ghi chú</th>
                                <th>Hành động</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:if test="${empty requests}">
                                <tr>
                                    <td colspan="7" style="text-align: center; padding: 20px; color: #666;">
                                        Không có yêu cầu nào phù hợp.
                                    </td>
                                </tr>
                            </c:if>
                            <c:if test="${not empty requests}">
                                <c:forEach items="${requests}" var="r">
                                    <tr>
                                        <td>${r.requestId}</td>
                                        <td>${r.recipientName}</td>
                                        <td><fmt:formatDate value="${r.createdAt}" pattern="yyyy-MM-dd"/></td>
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
                                                <a href="viewRequestDetail?requestId=${r.requestId}" class="btn-view">Chi tiết</a>
                                                <c:choose>
                                                    <c:when test="${sessionScope.roleId == 4 && r.requestStatus == 'Từ chối'}">
                                                        <a href="editRequest?requestId=${r.requestId}" class="btn-edit">Chỉnh sửa</a>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <a class="btn-edit btn-disabled" style="pointer-events: none; opacity: 0.5;"> Chỉnh sửa</a>
                                                    </c:otherwise>
                                                </c:choose>
                                                <c:if test="${r.requestStatus == 'Chờ duyệt' && sessionScope.user.role == 'Giám đốc'}">
                                                    <a href="ApproveRequestServlet?id=${r.requestId}" class="btn-success">Duyệt</a>
                                                    <a href="RejectRequestServlet?id=${r.requestId}" class="btn-danger">Từ chối</a>
                                                </c:if>
                                                <a href="requestHistory?requestId=${r.requestId}" class="btn-add">Lịch sử</a>
                                            </div>
                                        </td>
                                    </tr>
                                </c:forEach>
                            </c:if>
                        </tbody>
                    </table>
                </div>
                <!-- PHÂN TRANG RA NGOÀI, CĂN GIỮA -->
                <div class="pagination" style="justify-content:center; display:flex;">
                    <c:if test="${totalPages >= 1}">
                        <c:url var="pageUrl" value="RequestListServlet">
                            <c:param name="status" value="${param.status}" />
                            <c:param name="projectName" value="${param.projectName}" />
                            <c:param name="startDate" value="${param.startDate}" />
                            <c:param name="endDate" value="${param.endDate}" />
                            <c:param name="sortBy" value="${param.sortBy}" />
                            <c:param name="order" value="${param.order}" />
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
                <script>
                                    document.getElementById('btnReset').addEventListener('click', function () {
                                        const baseUrl = window.location.pathname;
                                        window.location.href = baseUrl;
                                    });
                </script>
            </div>
        </div>
    </div>
</body>
</html>