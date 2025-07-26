<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>Lịch sử sửa đổi vật tư</title>
    <link rel="stylesheet" href="css/materialDetailHistory.css">
    <link rel="stylesheet" href="css/sidebar.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/css/all.min.css">
</head>
<body>
    <jsp:include page="sidebar.jsp" />
    <div class="main-content">
        <div class="header-section">
            <h1 class="page-title">
                <c:choose>
                    <c:when test="${showAll}">Lịch sử sửa đổi tất cả vật tư</c:when>
                    <c:otherwise>Lịch sử sửa đổi vật tư #${materialId}</c:otherwise>
                </c:choose>
            </h1>
            <div class="header-buttons">
                <a href="MaterialListServlet" class="btn btn-secondary"><i class="fas fa-arrow-left"></i> Quay lại danh sách</a>
            </div>
        </div>
        <div class="content-card">
            <form method="get" action="materialDetailHistory" id="filterForm" class="filter-form" style="margin-bottom: 24px;">
                <div class="filter-row" style="flex-wrap: nowrap; overflow-x: auto;">
                    <div class="filter-group">
                        <i class="fas fa-user-shield"></i>
                        <select name="roleName">
                            <option value="">Vai trò</option>
                            <option value="Admin" <c:if test="${param.roleName == 'Admin'}">selected</c:if>>Admin</option>
                            <option value="Giám đốc" <c:if test="${param.roleName == 'Giám đốc'}">selected</c:if>>Giám đốc</option>
                            <option value="Nhân viên kho" <c:if test="${param.roleName == 'Nhân viên kho'}">selected</c:if>>Nhân viên kho</option>
                            <option value="Nhân viên công ty" <c:if test="${param.roleName == 'Nhân viên công ty'}">selected</c:if>>Nhân viên công ty</option>
                        </select>
                    </div>
                    <div class="filter-group">
                        <i class="fas fa-calendar-alt"></i>
                        <label style="margin-right: 4px; white-space: nowrap;">Từ ngày</label>
                        <input type="date" name="fromDate" placeholder="Từ ngày" value="${param.fromDate}"/>
                    </div>
                    <div class="filter-group">
                        <i class="fas fa-calendar-alt"></i>
                        <label style="margin-right: 4px; white-space: nowrap;">Đến ngày</label>
                        <input type="date" name="toDate" placeholder="Đến ngày" value="${param.toDate}"/>
                    </div>
                </div>
                <div class="filter-row" style="flex-wrap: nowrap; overflow-x: auto; margin-top: 8px;">
                    <div class="filter-group">
                        <i class="fas fa-search"></i>
                        <input type="text" name="keyword" placeholder="Tên vật tư hoặc người thực hiện..." value="${fn:escapeXml(param.keyword)}"/>
                    </div>
                    <button type="submit" class="btn btn-secondary">Tìm kiếm</button>
                    <button type="button" class="btn btn-secondary" onclick="resetFilters()">Đặt lại</button>
                </div>
            </form>
            <script>
                function resetFilters() {
                    var form = document.getElementById('filterForm');
                    var selects = form.querySelectorAll('select');
                    selects.forEach(function(select) {
                        select.selectedIndex = 0;
                    });
                    var inputs = form.querySelectorAll('input[type="text"], input[type="date"]');
                    inputs.forEach(function(input) {
                        input.value = '';
                    });
                    form.submit();
                }
            </script>
            <table class="data-table">
                <thead>
                    <tr>
                        <th>Thời gian sửa</th>
                        <c:if test="${showAll}">
                            <th>Mã vật tư</th>
                            <th>Tên vật tư</th>
                        </c:if>
                        <th>Người sửa</th>
                        <th style="width: 100px;">Vai trò</th>
                        <th style="width: 180px;">Trường bị sửa</th>
                        <th style="width: 200px;">Giá trị cũ</th>
                        <th style="width: 200px;">Giá trị mới</th>
                    </tr>
                </thead>
                <tbody>
                <c:choose>
                    <c:when test="${totalCount == 0}">
                        <tr><td colspan="8" class="no-data">Chưa có lịch sử sửa đổi nào.</td></tr>
                    </c:when>
                    <c:otherwise>
                        <c:forEach var="h" items="${historyList}">
                            <c:set var="fields" value="${fn:split(h.fieldName, ',')}" />
                            <c:set var="olds" value="${fn:split(h.oldValue, ',')}" />
                            <c:set var="news" value="${fn:split(h.newValue, ',')}" />
                            <c:set var="rowspan" value="${fn:length(fields)}" />
                            <c:forEach var="item" items="${fields}" varStatus="status">
                                <tr>
                                    <c:if test="${status.index == 0}">
                                        <td rowspan="${rowspan}"><fmt:formatDate value="${h.changedAt}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
                                        <c:if test="${showAll}">
                                            <td rowspan="${rowspan}">${h.materialId}</td>
                                            <td rowspan="${rowspan}">${h.materialName}</td>
                                        </c:if>
                                        <td rowspan="${rowspan}">${h.userName}</td>
                                        <td rowspan="${rowspan}">${h.roleName}</td>
                                    </c:if>
                                    <td>${fn:trim(item)}</td>
                                    <td>${fn:trim(olds[status.index])}</td>
                                    <td>${fn:trim(news[status.index])}</td>
                                </tr>
                            </c:forEach>
                        </c:forEach>
                    </c:otherwise>
                </c:choose>
                </tbody>
            </table>
        </div>
    </div>
</body>
</html> 