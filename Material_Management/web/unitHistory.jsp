<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html lang="vi">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Lịch sử thay đổi đơn vị tính</title>
        <link rel="stylesheet" href="./css/unitHistory.css">
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">
    </head>
    <body>
        <div class="container">
            <!-- Header -->
            <header class="header">
                <div class="header-content">
                    <h1 class="title">
                        <i class="fas fa-history"></i>
                        Lịch sử thay đổi đơn vị tính
                    </h1>
                    <p class="subtitle">Theo dõi lịch sử thay đổi đơn vị tính trong hệ thống quản lý kho vật tư</p>
                </div>
            </header>

            <!-- Bộ lọc -->
            <form action="UnitChangeHistoryServlet" method="post">
                <div class="filter-section">
                    <div class="search-container">
                        <div class="search-box">
                            <i class="fas fa-search search-icon"></i>
                            <input type="text"  id="searchInput" placeholder="Tìm kiếm theo tên đơn vị, người thực hiện..." class="search-input"
                                    maxlength="10"
                                   title="nhập không quá 10 kí tự"
                                   name ="search"
                                   >
                        </div>
                    </div>

                    <div class="filter-controls">
                        <div class="filter-group">
                            <label for="operationFilter">Loại thao tác:</label>
                            <select id="operationFilter" name="operationFilter" class="filter-select">
                                <option value="">Tất cả thao tác</option>
                                <option value="Thêm mới" ${param.operationFilter == 'Thêm mới' ? 'selected' : ''}>Thêm mới</option>
                                <option value="Cập nhật tỉ lệ." ${param.operationFilter == 'Cập nhật tỉ lệ.' ? 'selected' : ''}>Cập nhật tỉ lệ.</option>
                                <option value="Đổi trạng thái" ${param.operationFilter == 'Đổi trạng thái' ? 'selected' : ''}>Đổi trạng thái</option>

                            </select>
                        </div>

                        <div class="filter-group">
                            <label for="userFilter">Vai trò:</label>
                            <select id="userFilter" name="userFilter" class="filter-select">
                                <option value="">Tất cả vai trò</option>
                                <option value="Admin" ${param.userFilter == 'Admin' ? 'selected' : ''}>Quản trị viên</option>
                                <option value="Quản lý kho" ${param.userFilter == 'Quản lý kho' ? 'selected' : ''}>Quản lý kho</option>

                            </select>
                        </div>

                        <div class="filter-group">
                            <label for="dateFilter">Từ ngày:</label>
                            <input type="date" name="date" id="dateFilter" value="${param.dateFilter}" class="filter-input">
                        </div>
                        </form>
                        <button class="btn-filter" onclick="apDungBoLoc()">
                            <i class="fas fa-filter"></i>
                            Lọc
                        </button>

                        <button class="btn-reset" onclick="lamMoiBoLoc()">
                            <i class="fas fa-undo"></i>
                            Làm mới
                        </button>
                    </div>
                </div>


                <div class="table-section">
                    <div class="table-header">
                        <h3>Danh sách lịch sử thay đổi</h3>
                    </div>

                    <div class="table-container">
                        <table class="data-table" id="historyTable">
                            <thead>
                                <tr>
                                    <th>STT</th>
                                    <th>Tên đơn vị</th>
                                    <th>Loại thao tác</th>
                                    <th>Giá trị cũ</th>
                                    <th>Giá trị mới</th>
                                    <th>Người thực hiện</th>
                                    <th>Vai trò</th>
                                    <th>Ghi chú</th>
                                    <th>Thời gian</th>
                                 
                                </tr>
                            </thead>
                            <tbody id="historyTableBody">
                                <c:forEach var="item" items="${historyList}" varStatus="status">
                                    <tr>
                                        <td>${status.index + 1}</td>
                                        <td>${item.unitName}</td>
                                        <td>${item.actionType}</td>
                                        <td><c:out value="${item.oldValue}" /></td>
                                        <td><c:out value="${item.newValue}" /></td>
                                        <td>${item.changedBy}</td>
                                        <td>${item.role}</td>
                                        <td>${item.note}</td>
                                        <td>${item.changedAt}</td>
                                      
                                    </tr>
                                </c:forEach>
                            </tbody>
                        </table>
                    </div>

                    <!-- Phân trang -->
                    <div class="pagination">
                        <c:set var="currentPage" value="${currentPage}" />
                        <c:set var="totalPages" value="${pages}" />

                        <c:if test="${currentPage > 1}">
                            <a class="btn-page" href="UnitChangeHistoryServlet?page=${currentPage - 1}&search=${param.search}&actionType=${param.actionType}&role=${param.role}&date=${param.date}">
                                <i class="fas fa-chevron-left"></i> Trước
                            </a>
                        </c:if>

                        <c:forEach var="i" begin="1" end="${totalPages}">
                            <a href="UnitChangeHistoryServlet?page=${i}&search=${param.search}&actionType=${param.actionType}&role=${param.role}&date=${param.date}"
                               class="btn-page ${i == currentPage ? 'active' : ''}">
                                ${i}
                            </a>
                        </c:forEach>

                        <c:if test="${currentPage < totalPages}">
                            <a class="btn-page" href="UnitChangeHistoryServlet?page=${currentPage + 1}&search=${param.search}&actionType=${param.actionType}&role=${param.role}&date=${param.date}">
                                Sau <i class="fas fa-chevron-right"></i>
                            </a>
                        </c:if>
                    </div>

                </div>
                <div style="display: flex; justify-content: flex-start; margin-top: 24px;">
                    <a href="unitConversionSeverlet" class="btn-reset"><i class="fas fa-arrow-left"></i> Trở lại</a>
                </div>
        </div>

        <!-- Modal chi tiết -->
        <!--    <div id="detailModal" class="modal">
                <div class="modal-content">
                    <div class="modal-header">
                        <h3>Chi tiết thay đổi đơn vị tính</h3>
                        <button class="btn-close" onclick="dongModal()">
                            <i class="fas fa-times"></i>
                        </button>
                    </div>
                    <div class="modal-body" id="modalContent">
                         Nội dung chi tiết sẽ được JavaScript tạo 
                    </div>
                    <div class="modal-footer">
                        <button class="btn-secondary" onclick="dongModal()">Đóng</button>
                    </div>
                </div>
            </div>-->

        <script>
            // Các hàm JavaScript cơ bản
            function apDungBoLoc() {
                // Xử lý lọc dữ liệu
                console.log('Áp dụng bộ lọc');
            }

            function lamMoiBoLoc() {
             
                document.getElementById('searchInput').value = '';
                document.getElementById('operationFilter').value = '';
                document.getElementById('userFilter').value = '';
                document.getElementById('dateFilter').value = '';
                 document.getElementById('filterForm').submit();
            }

            function chuyenTrang(huong) {
                // Xử lý chuyển trang
                console.log('Chuyển trang:', huong);
            }

            function xemChiTiet(id) {
                // Hiển thị modal chi tiết
                document.getElementById('detailModal').style.display = 'block';
            }

            function dongModal() {
                // Đóng modal
                document.getElementById('detailModal').style.display = 'none';
            }

            // Đóng modal khi click bên ngoài
            window.onclick = function (event) {
                const modal = document.getElementById('detailModal');
                if (event.target === modal) {
                    dongModal();
                }
            }
        </script>
    </body>
</html> 