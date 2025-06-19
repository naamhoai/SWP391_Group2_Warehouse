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
    </head>
    <body>
        <div class="request-list-container">
            <h2>Danh sách yêu cầu</h2>

            <!-- Bộ lọc -->
            <form method="get" action="RequestListServlet" id="filterForm" accept-charset="UTF-8">
                <div class="filter-row">
                    <div class="filter-col">
                        <label class="filter-label">Tìm kiếm:</label>
                        <input type="text" class="filter-input" name="search" value="${param.search}" 
                               placeholder="Tìm theo loại, lý do hoặc người tạo">
                    </div>
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
                        <label class="filter-label">Ngày tạo:</label>
                        <input type="date" class="filter-input" name="date" id="dateInput" value="${param.date}">
                    </div>
                </div>
                <div class="filter-row filter-actions">
                    <div class="filter-col filter-col-actions">
                        <button type="submit" class="btn-primary">
                            <i class="fas fa-search"></i> Tìm kiếm
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
                                    <a href="viewRequestDetail?requestId=${r.requestId}" class="btn-info btn-sm">Chi tiết</a>
                                    <c:if test="${r.requestStatus == 'Pending' && sessionScope.user.role == 'Director'}">
                                        <a href="ApproveRequestServlet?id=${r.requestId}" class="btn-success btn-sm">Duyệt</a>
                                        <a href="RejectRequestServlet?id=${r.requestId}" class="btn-danger btn-sm">Từ chối</a>
                                    </c:if>
                                </td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
            </div>

            <!-- Phân trang -->
            <div class="pagination-container">
                <ul class="pagination-list">
                    <li class="pagination-item ${currentPage == 1 ? 'disabled' : ''}">
                        <a class="pagination-link" href="#" onclick="changePage(${currentPage - 1})" aria-label="Previous">
                            <span aria-hidden="true">&laquo;</span>
                        </a>
                    </li>
                    <c:forEach begin="1" end="${totalPages}" var="i">
                        <li class="pagination-item ${currentPage == i ? 'active' : ''}">
                            <a class="pagination-link" href="#" onclick="changePage(${i})">${i}</a>
                        </li>
                    </c:forEach>
                    <li class="pagination-item ${currentPage == totalPages ? 'disabled' : ''}">
                        <a class="pagination-link" href="#" onclick="changePage(${currentPage + 1})" aria-label="Next">
                            <span aria-hidden="true">&raquo;</span>
                        </a>
                    </li>
                </ul>
            </div>

            <script>
                function resetForm() {
                    // Xóa giá trị của các trường input
                    document.querySelector('input[name="search"]').value = '';
                    document.getElementById('statusSelect').value = '';
                    document.getElementById('requestTypeSelect').value = '';
                    document.getElementById('dateInput').value = '';
                    document.getElementById('pageSizeSelect').value = '10';
                    document.getElementById('sortInput').value = 'asc';
                    document.getElementById('pageInput').value = '1';
                    
                    // Gửi form để load lại trang với các giá trị mặc định
                    document.getElementById('filterForm').submit();
                }

                function changePage(page) {
                    document.getElementById('pageInput').value = page;
                    document.getElementById('filterForm').submit();
                }
                
                function toggleSort() {
                    var sortInput = document.getElementById('sortInput');
                    var newSort = sortInput.value === 'desc' ? 'asc' : 'desc';
                    sortInput.value = newSort;
                    
                    // Cập nhật hiển thị hướng sắp xếp
                    document.getElementById('sortDirection').textContent = 
                        newSort === 'desc' ? '(Z-A ↓)' : '(A-Z ↑)';
                    
                    document.getElementById('filterForm').submit();
                }

                // Lưu giá trị đã chọn vào localStorage khi submit form
                document.getElementById('filterForm').addEventListener('submit', function() {
                    localStorage.setItem('lastSearch', document.querySelector('input[name="search"]').value);
                    localStorage.setItem('lastStatus', document.getElementById('statusSelect').value);
                    localStorage.setItem('lastRequestType', document.getElementById('requestTypeSelect').value);
                    localStorage.setItem('lastDate', document.getElementById('dateInput').value);
                    localStorage.setItem('lastPageSize', document.getElementById('pageSizeSelect').value);
                    localStorage.setItem('lastSort', document.getElementById('sortInput').value);
                });

                // Khôi phục giá trị đã chọn khi load trang
                window.onload = function() {
                    // Chỉ khôi phục nếu không có tham số trên URL
                    if (!window.location.search) {
                        var lastSearch = localStorage.getItem('lastSearch');
                        var lastStatus = localStorage.getItem('lastStatus');
                        var lastRequestType = localStorage.getItem('lastRequestType');
                        var lastDate = localStorage.getItem('lastDate');
                        var lastPageSize = localStorage.getItem('lastPageSize');
                        var lastSort = localStorage.getItem('lastSort');

                        if (lastSearch) document.querySelector('input[name="search"]').value = lastSearch;
                        if (lastStatus) document.getElementById('statusSelect').value = lastStatus;
                        if (lastRequestType) document.getElementById('requestTypeSelect').value = lastRequestType;
                        if (lastDate) document.getElementById('dateInput').value = lastDate;
                        if (lastPageSize) document.getElementById('pageSizeSelect').value = lastPageSize;
                        if (lastSort) document.getElementById('sortInput').value = lastSort;
                    }
                }
            </script>
        </div>
    </body>
</html>