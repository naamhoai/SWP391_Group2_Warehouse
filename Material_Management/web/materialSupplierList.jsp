<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="model.MaterialSupplier" %>
<html>
<head>
    <title>Danh sách vật tư theo nhà cung cấp</title>
    <link rel="stylesheet" href="css/materialSupplierList.css">
</head>
<body>
<div class="container">
    <h1>Danh sách vật tư theo nhà cung cấp</h1>
    <form class="filter-form" method="get" action="material-suppliers">
        <div class="filter-group">
            <label>Nhà cung cấp:</label>
            <select name="supplier_id">
                <option value="">-- Chọn nhà cung cấp --</option>
                <% String currentSupplier = request.getAttribute("supplier_id") != null ? (String)request.getAttribute("supplier_id") : ""; %>
                <option value="1" <%= "1".equals(currentSupplier) ? "selected" : "" %>>Công ty TNHH Vật Liệu Xây Dựng Hòa Bình</option>
                <option value="2" <%= "2".equals(currentSupplier) ? "selected" : "" %>>Công ty CP VLXD Sài Gòn Xanh</option>
                <option value="3" <%= "3".equals(currentSupplier) ? "selected" : "" %>>Công ty TNHH Vật Tư Xây Dựng An Phát</option>
                <option value="4" <%= "4".equals(currentSupplier) ? "selected" : "" %>>Công ty TNHH Thép Việt Nhật</option>
                <option value="5" <%= "5".equals(currentSupplier) ? "selected" : "" %>>Công ty VLXD Minh Phát</option>
            </select>
        </div>
        <div class="filter-group">
            <label>Trạng thái:</label>
            <select name="status">
                <option value="">Tất cả</option>
                <option value="available">Có sẵn</option>
            </select>
        </div>
        <div class="filter-group">
            <label>Hiển thị:</label>
            <select name="pageSize">
                <option value="5" <%= "5".equals(request.getAttribute("pageSize")+"") ? "selected" : "" %>>5 items</option>
                <option value="10" <%= request.getAttribute("pageSize") == null || "10".equals(request.getAttribute("pageSize")+"") ? "selected" : "" %>>10 items</option>
                <option value="20" <%= "20".equals(request.getAttribute("pageSize")+"") ? "selected" : "" %>>20 items</option>
                <option value="50" <%= "50".equals(request.getAttribute("pageSize")+"") ? "selected" : "" %>>50 items</option>
            </select>
        </div>
        <div class="search-group">
            <input type="text" name="keyword" placeholder="Tìm theo tên vật tư..." value="<%= request.getAttribute("keyword") != null ? (String)request.getAttribute("keyword") : "" %>" />
            <button class="search-btn" type="submit">Tìm</button>
        </div>
    </form>
    <table>
        <thead>
            <tr>
                <th>Tên vật tư</th>
                <th>Nhà cung cấp</th>
                <th>Trạng thái</th>
            </tr>
        </thead>
        <tbody>
            <%
                List<MaterialSupplier> list = (List<MaterialSupplier>) request.getAttribute("materialSupplierList");
                int currentPage = request.getAttribute("currentPage") != null ? (Integer)request.getAttribute("currentPage") : 1;
                int pageSize = request.getAttribute("pageSize") != null ? (Integer)request.getAttribute("pageSize") : 10;
                int total = request.getAttribute("total") != null ? (Integer)request.getAttribute("total") : 0;
                int totalPages = request.getAttribute("totalPages") != null ? (Integer)request.getAttribute("totalPages") : 1;
                String keyword = request.getAttribute("keyword") != null ? (String)request.getAttribute("keyword") : "";
                String supplierId = request.getAttribute("supplier_id") != null ? (String)request.getAttribute("supplier_id") : "";
                if (list != null && !list.isEmpty()) {
                    for (MaterialSupplier ms : list) {
            %>
            <tr>
                <td><%= ms.getMaterialName() %></td>
                <td><%= ms.getSupplierName() %></td>
                <td class="status-active">Có sẵn</td>
            </tr>
            <%
                    }
                } else {
            %>
            <tr><td colspan="3" class="no-data">Không có dữ liệu</td></tr>
            <%
                }
            %>
        </tbody>
    </table>
    <div class="pagination">
        <form class="pagination-form" method="get" action="material-suppliers">
            <input type="hidden" name="supplier_id" value="<%= supplierId %>" />
            <input type="hidden" name="keyword" value="<%= keyword %>" />
            <input type="hidden" name="pageSize" value="<%= pageSize %>" />
            <button class="page-button" type="submit" name="page" value="<%= currentPage-1 %>" <%= currentPage <= 1 ? "disabled" : "" %>>Previous</button>
            <% for (int i = 1; i <= totalPages; i++) { %>
                <button class="page-button <%= i == currentPage ? "active" : "" %>" type="submit" name="page" value="<%= i %>"><%= i %></button>
            <% } %>
            <button class="page-button" type="submit" name="page" value="<%= currentPage+1 %>" <%= currentPage >= totalPages ? "disabled" : "" %>>Next</button>
        </form>
        <div class="pagination-info">Tổng số vật tư: <%= total %></div>
    </div>
    <a href="suppliers" class="back-btn">Quay lại danh sách nhà cung cấp</a>
</div>
</body>
</html>