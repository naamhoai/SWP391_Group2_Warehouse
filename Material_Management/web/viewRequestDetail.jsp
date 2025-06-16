<%-- 
    Document   : viewRequestDetail
    Created on : Jun 12, 2025, 5:01:36 PM
    Author     : kimoa
--%>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Chi tiết yêu cầu vật tư</title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    </head>
    <body>
        <div class="container mt-4">
            <h2>Chi tiết yêu cầu vật tư</h2>
            <div class="mb-3">
                <label><b>Mã yêu cầu:</b></label> ${request.requestId}<br>
                <label><b>Loại yêu cầu:</b></label> ${request.requestType}<br>
                <label><b>Người yêu cầu (ID):</b></label> ${request.userId}<br>
                <label><b>Lý do:</b></label> ${request.reason}<br>
                <label><b>Trạng thái:</b></label> ${request.requestStatus}<br>
                <label><b>Thời gian tạo:</b></label> ${request.createdAt}<br>
                <c:if test="${request.requestStatus eq 'Approved' or request.requestStatus eq 'Rejected'}">
                    <c:if test="${not empty request.directorNote}">
                        <label><b>Ghi chú của giám đốc:</b></label> ${request.directorNote}<br>
                    </c:if>
                </c:if>
            </div>
            <h4>Danh sách vật tư</h4>
            <table class="table table-bordered">
                <thead>
                    <tr>
                        <th>Danh mục</th>
                        <th>Loại vật tư</th>
                        <th>Tên vật tư</th>
                        <th>Số lượng</th>
                        <th>Đơn vị</th>
                        <th>Mô tả</th>
                        <th>Tình trạng</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="d" items="${details}">
                        <tr>
                            <td>${d.parentCategoryName}</td>
                            <td>${d.categoryName}</td>
                            <td>${d.materialName}</td>
                            <td>${d.quantity}</td>
                            <td>${d.unitName}</td>
                            <td>${d.description}</td>
                            <td>${d.materialCondition}</td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>

            <!-- Phần xử lý cho giám đốc -->
            <c:if test="${request.requestStatus eq 'Pending'}">
                <div class="mb-2 mt-2">
                    <label for="directorNote"><b>Lý do (bắt buộc):</b></label>
                    <textarea id="directorNote" class="form-control" required rows="2" placeholder="Nhập lý do phê duyệt hoặc từ chối..."></textarea>
                </div>
                <form id="approveForm" action="approveRequest" method="post" style="display:inline;">
                    <input type="hidden" name="requestId" value="${request.requestId}">
                    <input type="hidden" name="directorNote" id="approveNote">
                    <button type="submit" class="btn btn-success">Phê duyệt</button>
                </form>
                <form id="rejectForm" action="rejectRequest" method="post" style="display:inline;">
                    <input type="hidden" name="requestId" value="${request.requestId}">
                    <input type="hidden" name="directorNote" id="rejectNote">
                    <button type="submit" class="btn btn-danger">Từ chối</button>
                </form>
            </c:if>

            <!-- Phần xử lý cho nhân viên kho -->
            <c:if test="${request.requestStatus eq 'Approved'}">
                <div class="mt-3">
                    <a href="createPurchaseOrder?requestId=${request.requestId}" class="btn btn-primary">
                        <i class="fas fa-plus"></i> Tạo đơn nhập hàng
                    </a>
                </div>
            </c:if>

            <!-- Nút quay lại -->
            <div class="mt-3">
                <c:choose>
                    <c:when test="${sessionScope.userRole eq 'Director'}">
                        <a href="director" class="btn btn-secondary">Quay lại</a>
                    </c:when>
                    <c:when test="${sessionScope.userRole eq 'Warehouse Staff'}">
                        <a href="warehouseEmployeeDashboard" class="btn btn-secondary">Quay lại</a>
                    </c:when>
                    <c:otherwise>
                        <a href="javascript:history.back()" class="btn btn-secondary">Quay lại</a>
                    </c:otherwise>
                </c:choose>
            </div>

            <script>
                // Khi bấm nút nào thì copy lý do vào form đó rồi submit
                document.getElementById('approveForm')?.addEventListener('submit', function (e) {
                    var note = document.getElementById('directorNote').value.trim();
                    if (!note) {
                        alert('Vui lòng nhập lý do!');
                        e.preventDefault();
                        return false;
                    }
                    document.getElementById('approveNote').value = note;
                    return true;
                });

                document.getElementById('rejectForm')?.addEventListener('submit', function (e) {
                    var note = document.getElementById('directorNote').value.trim();
                    if (!note) {
                        alert('Vui lòng nhập lý do!');
                        e.preventDefault();
                        return false;
                    }
                    document.getElementById('rejectNote').value = note;
                    return true;
                });
            </script>
        </div>
    </body>
</html>