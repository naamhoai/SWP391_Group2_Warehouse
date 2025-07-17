<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html lang="vi">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Lịch sử nhập kho</title>
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
                        Lịch sử Nhập Kho
                    </h1>
                    <p class="subtitle">Theo dõi lịch sử nhập lại số lượng vật tư trong kho</p>
                </div>
            </header>

            <!-- Bộ lọc -->
            <form action="importHistory" method="post">
                <div class="filter-section">
                    <div class="search-container">
                        <div class="search-box">
                            <i class="fas fa-search search-icon"></i>
                            <input type="text"  id="searchInput" placeholder="Tìm kiếm theo tên vật tư, người giao đến..." class="search-input"
                                   maxlength="10"
                                   title="nhập không quá 10 kí tự"
                                   name ="search"
                                   >
                        </div>
                    </div>

                    <div class="filter-controls">
                        <div class="filter-group">
                            <label for="operationFilter">Loại Đơn vị:</label>
                            <select id="operationFilter" name="unit" class="filter-select">
                                <option value="">Tất cả Đơn Vị</option>
                                <option value="Cái" ${param.unit == 'Cái' ? 'selected' : ''}>Cái</option>
                                <option value="Mét" ${param.unit == 'Mét' ? 'selected' : ''}>Mét</option>
                                <option value="Kg" ${param.unit == 'Kg' ? 'selected' : ''}>Kg</option>
                                <option value="Lít" ${param.unit == 'Lít' ? 'selected' : ''}>Lít</option>
                            </select>
                        </div>

                        <div class="filter-group">
                            <label for="userFilter">Tình trạng:</label>
                            <select id="userFilter" name="status" class="filter-select">
                                <option value="">Tất cả Tình trạng</option>
                                <option value="Cũ" ${param.status == 'Cũ' ? 'selected' : ''}>Cũ</option>
                                <option value="Mới" ${param.status == 'Mới' ? 'selected' : ''}>Mới</option>

                            </select>
                        </div>

                        <div class="filter-group">
                            <label for="dateFilter">Từ ngày:</label>
                            <input type="date" name="date" id="dateFilter" value="${param.date}" class="filter-input">
                        </div>
                        </form>
                        <button class="btn-filter">
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
                                    <th>Tên vật tư</th>
                                    <th>Số lượng</th>
                                    <th>Đơn vị</th>
                                    <th>Tình trạng</th>
                                    <th>Người nhập</th>
                                    <th>Vai trò</th>
                                    <th>Người giao</th>
                                    <th>SĐT người giao</th>
                                    <th>Tên dự án</th>
                                    <th>Lý do</th>
                                    <th>Thời gian nhập</th>
                                </tr>
                            </thead>
                            <tbody id="historyTableBody">
                                <c:forEach var="item" items="${historyList}" varStatus="status">
                                    <tr>
                                        <td>${status.index + 1}</td>
                                        <td>${item.materialName}</td>
                                        <td>${item.quantity}</td>
                                        <td>${item.unit}</td>
                                        <td>${item.status}</td>
                                        <td>${item.receivedBy}</td>
                                        <td>${item.roles}</td>
                                        <td>${item.deliveredBy}</td>
                                        <td>${item.deliveryPhone}</td>
                                        <td>${item.projectName}</td>
                                        <td>${item.reason}</td>
                                        <td>${item.createdAt}</td>
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
                            <a class="btn-page" href="importHistory?page=${currentPage - 1}&search=${search}&status=${status}&date=${date} &unit=${unit}">
                                <i class="fas fa-chevron-left"></i> Trước
                            </a>
                        </c:if>

                        <c:choose>
                            <c:when test="${totalPages <= 5}">
                                <c:forEach var="i" begin="1" end="${totalPages}">
                                    <a href="importHistory?page=${i}&search=${userFilter}&status=${status}&date=${date}&unit=${unit}"
                                       class="btn-page ${i == currentPage ? 'active' : ''}">
                                        ${i}
                                    </a>
                                </c:forEach>
                            </c:when>
                            <c:otherwise>
                                <a href="importHistory?page=1&search=${search}&status=${status}&date=${date}&unit=${unit}"
                                   class="btn-page ${currentPage == 1 ? 'active' : ''}">1</a>
                                <c:if test="${currentPage > 4}">
                                    <span class="dots">...</span>
                                </c:if>
                                <c:forEach var="i" begin="${currentPage - 2 > 2 ? currentPage - 2 : 2}" end="${currentPage + 2 < totalPages ? currentPage + 2 : totalPages - 1}">
                                    <a href="importHistory?page=${i}&search=${search}&status=${status}&date=${date}&unit=${unit}"
                                       class="btn-page ${i == currentPage ? 'active' : ''}">
                                        ${i}
                                    </a>
                                </c:forEach>
                                <c:if test="${currentPage < totalPages - 3}">
                                    <span class="dots">...</span>
                                </c:if>
                                <a href="importHistory?page=${totalPages}&search=${search}&status=${status}&date=${date}&unit=${unit}"
                                   class="btn-page ${currentPage == totalPages ? 'active' : ''}">${totalPages}</a>
                            </c:otherwise>
                        </c:choose>

                        <c:if test="${currentPage < totalPages}">
                            <a class="btn-page" href="importHistory?page=${currentPage + 1}&search=${search}&status=${status}&date=${date}&unit=${unit}">
                                Sau <i class="fas fa-chevron-right"></i>
                            </a>
                        </c:if>
                    </div>

                </div>
                <div style="display: flex; justify-content: flex-start; margin-top: 24px;">
                    <a href="ImportWarehouseServlet" class="btn-reset"><i class="fas fa-arrow-left"></i> Trở lại</a>
                </div>
        </div>
        <script>
         function lamMoiBoLoc() {
             
                document.getElementById('searchInput').value = '';
                document.getElementById('operationFilter').value = '';
                document.getElementById('userFilter').value = '';
                document.getElementById('dateFilter').value = '';
                 document.getElementById('filterForm').submit();
            }

            function dongModal() {
                // Đóng modal
                document.getElementById('detailModal').style.display = 'none';
            }


            window.onclick = function (event) {
                const modal = document.getElementById('detailModal');
                if (event.target === modal) {
                    dongModal();
                }
            }
        </script>
    </body>
</html> 