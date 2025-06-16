<%-- requestList.jsp --%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Danh sách yêu cầu</title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    </head>
    <body>
        <div class="container mt-4">
            <h2>Danh sách yêu cầu</h2>

            <!-- Bộ lọc -->
            <form method="get" action="RequestListServlet">
                <div class="row mb-3">
                    <div class="col-md-3">
                        <select class="form-select" name="status">
                            <option value="" ${empty param.status || not empty param.search ? 'selected' : ''}>Tất cả trạng thái</option>
                            <option value="Pending" ${param.status == 'Pending' && empty param.search ? 'selected' : ''}>Chờ duyệt</option>
                            <option value="Approved" ${param.status == 'Approved' && empty param.search ? 'selected' : ''}>Đã duyệt</option>
                            <option value="Rejected" ${param.status == 'Rejected' && empty param.search ? 'selected' : ''}>Từ chối</option>
                        </select>
                    </div>
                    <div class="col-md-3">
                        <input type="date" class="form-control" name="date" value="${empty param.search ? param.date : ''}" placeholder="Lọc theo ngày">
                    </div>
                    <div class="col-md-2">
                        <button type="submit" name="search" value="1" class="btn btn-primary">Tìm kiếm</button>
                    </div>
                    <div class="col-md-2">
                        <button type="submit" name="sort" value="${param.sort == 'desc' ? 'asc' : 'desc'}" class="btn btn-secondary">
                            Sắp xếp theo ID ${param.sort == 'desc' ? '↑' : '↓'}
                        </button>
                    </div>
                </div>
            </form>

            <!-- Bảng danh sách -->
            <div class="table-responsive">
                <table class="table table-striped">
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
                                    <span class="badge ${r.requestStatus == 'Pending' ? 'bg-warning' : 
                                                         r.requestStatus == 'Approved' ? 'bg-success' : 'bg-danger'}">
                                              ${r.requestStatus}
                                          </span>
                                    </td>
                                    <td>${r.reason}</td>
                                    <td>${r.directorNote}</td>
                                    <td>
                                        <a href="RequestDetailServlet?id=${r.requestId}" class="btn btn-info btn-sm">Chi tiết</a>
                                        <c:if test="${r.requestStatus == 'Pending' && sessionScope.user.role == 'Director'}">
                                            <a href="ApproveRequestServlet?id=${r.requestId}" class="btn btn-success btn-sm">Duyệt</a>
                                            <a href="RejectRequestServlet?id=${r.requestId}" class="btn btn-danger btn-sm">Từ chối</a>
                                        </c:if>
                                    </td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </div>
            </div>

            <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
        </div>
    </body>
</html>