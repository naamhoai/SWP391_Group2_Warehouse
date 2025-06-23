<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.sql.*" %>
<%@ page import="java.util.*" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Danh sách vật tư theo nhà cung cấp</title>
    <link rel="stylesheet" href="css/materialSupplierList.css">
</head>
<body>
    <div class="container">
        <!-- Back button -->
        <div style="position:fixed; bottom:32px; left:32px; z-index:1000;">
            <a href="http://localhost:8080/Material_Management/suppliers" class="btn-cancel" style="background:#4a90e2; color:#fff; font-weight:bold; padding:10px 28px; border-radius:4px; text-decoration:none; font-size:16px;">&larr; Quay lại danh sách nhà cung cấp</a>
        </div>
        
        <h1>Danh sách vật tư theo nhà cung cấp</h1>
        
        <%
            // Xử lý các tham số đầu vào
            request.setCharacterEncoding("UTF-8");
            
            // Tạo mảng số lượng items per page
            List<Integer> itemsPerPageOptions = Arrays.asList(5, 10, 20, 50);
            pageContext.setAttribute("itemsPerPageOptions", itemsPerPageOptions);
            
            // Lấy các tham số tìm kiếm và lọc
            String supplierId = request.getParameter("supplierId");
            String searchQuery = request.getParameter("search");
            String statusFilter = request.getParameter("status");
            String sortField = request.getParameter("sort");
            String sortDir = request.getParameter("dir");
            
            // Giá trị mặc định
            if (sortField == null) sortField = "material_name";
            if (sortDir == null) sortDir = "asc";
            if (statusFilter == null) statusFilter = "All";
            
            // Xử lý phân trang
            int currentPage = 1;
            int itemsPerPage = 10;
            
            try {
                String pageStr = request.getParameter("page");
                if (pageStr != null) {
                    currentPage = Math.max(1, Integer.parseInt(pageStr));
                }
                
                String itemsPerPageStr = request.getParameter("itemsPerPage");
                if (itemsPerPageStr != null) {
                    itemsPerPage = Integer.parseInt(itemsPerPageStr);
                }
            } catch (NumberFormatException e) {
                currentPage = 1;
                itemsPerPage = 10;
            }
            
            String error = null;
            List<Map<String, String>> materialList = new ArrayList<>();
            int totalItems = 0;
            int totalPages = 0;
            
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
                Connection conn = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/material_system_2", 
                    "root", "123456"
                );
                
                // Xây dựng WHERE clause cho đếm
                StringBuilder countWhere = new StringBuilder();
                List<Object> countParams = new ArrayList<>();
                
                if (supplierId != null && !supplierId.trim().isEmpty()) {
                    countWhere.append(" AND m.supplier_id = ?");
                    countParams.add(supplierId);
                }
                
                if (searchQuery != null && !searchQuery.trim().isEmpty()) {
                    countWhere.append(" AND m.name LIKE ?");
                    countParams.add("%" + searchQuery.trim() + "%");
                }
                
                if (statusFilter != null && !statusFilter.equals("All")) {
                    if (statusFilter.equals("Có sẵn")) {
                        countWhere.append(" AND m.material_condition = 'Mới'");
                    } else if (statusFilter.equals("Cần mua")) {
                        countWhere.append(" AND m.material_condition != 'Mới'");
                    }
                }
                
                // Đếm tổng số vật tư
                String countSql = "SELECT COUNT(*) as total FROM materials m " +
                                 "INNER JOIN supplier s ON m.supplier_id = s.supplier_id " +
                                 "WHERE 1=1" + countWhere.toString();
                
                PreparedStatement countPs = conn.prepareStatement(countSql);
                for (int i = 0; i < countParams.size(); i++) {
                    countPs.setObject(i + 1, countParams.get(i));
                }
                ResultSet countRs = countPs.executeQuery();
                if (countRs.next()) {
                    totalItems = countRs.getInt("total");
                }
                countRs.close();
                countPs.close();
                
                // Tính tổng số trang
                totalPages = (int) Math.ceil((double) totalItems / itemsPerPage);
                if (totalPages == 0) totalPages = 1;
                
                // Đảm bảo trang hiện tại hợp lệ
                if (currentPage < 1) currentPage = 1;
                if (currentPage > totalPages) currentPage = totalPages;
                
                // Xây dựng WHERE clause cho query chính
                StringBuilder whereClause = new StringBuilder();
                List<Object> queryParams = new ArrayList<>();
                
                if (supplierId != null && !supplierId.trim().isEmpty()) {
                    whereClause.append(" AND m.supplier_id = ?");
                    queryParams.add(supplierId);
                }
                
                if (searchQuery != null && !searchQuery.trim().isEmpty()) {
                    whereClause.append(" AND m.name LIKE ?");
                    queryParams.add("%" + searchQuery.trim() + "%");
                }
                
                if (statusFilter != null && !statusFilter.equals("All")) {
                    if (statusFilter.equals("Có sẵn")) {
                        whereClause.append(" AND m.material_condition = 'Mới'");
                    } else if (statusFilter.equals("Cần mua")) {
                        whereClause.append(" AND m.material_condition != 'Mới'");
                    }
                }
                
                // Xây dựng ORDER BY clause
                String orderByClause = "ORDER BY ";
                switch (sortField) {
                    case "material_name":
                        orderByClause += "m.name " + sortDir;
                        break;
                    case "supplier_name":
                        orderByClause += "s.supplier_name " + sortDir + ", m.name ASC";
                        break;
                    case "status":
                        orderByClause += "m.material_condition " + sortDir + ", m.name ASC";
                        break;
                    default:
                        orderByClause += "m.name ASC";
                }
                
                // Lấy dữ liệu vật tư với filter và phân trang
                String sql = "SELECT m.name as material_name, s.supplier_name, " +
                             "CASE WHEN m.material_condition = 'Mới' THEN 'Có sẵn' ELSE 'Cần mua' END as status " +
                             "FROM materials m " +
                             "INNER JOIN supplier s ON m.supplier_id = s.supplier_id " +
                             "WHERE 1=1" + whereClause.toString() + " " +
                             orderByClause + " " +
                             "LIMIT ? OFFSET ?";
                
                PreparedStatement ps = conn.prepareStatement(sql);
                int paramIndex = 1;
                
                // Thêm các tham số filter
                for (Object param : queryParams) {
                    ps.setObject(paramIndex++, param);
                }
                
                // Thêm tham số phân trang
                ps.setInt(paramIndex++, itemsPerPage);
                ps.setInt(paramIndex, (currentPage - 1) * itemsPerPage);
                
                ResultSet rs = ps.executeQuery();
                
                while (rs.next()) {
                    Map<String, String> material = new HashMap<>();
                    material.put("material_name", rs.getString("material_name"));
                    material.put("supplier_name", rs.getString("supplier_name"));
                    material.put("status", rs.getString("status"));
                    materialList.add(material);
                }
                rs.close();
                ps.close();
                
                conn.close();
                
            } catch (Exception e) {
                error = "Lỗi: " + e.getMessage();
                e.printStackTrace();
            }
            
            // Tính toán phân trang
            int startPage = Math.max(1, currentPage - 2);
            int endPage = Math.min(totalPages, currentPage + 2);
            
            // Set attributes for JSTL
            pageContext.setAttribute("materialList", materialList);
            pageContext.setAttribute("currentPage", currentPage);
            pageContext.setAttribute("totalPages", totalPages);
            pageContext.setAttribute("itemsPerPage", itemsPerPage);
            pageContext.setAttribute("supplierId", supplierId);
            pageContext.setAttribute("searchQuery", searchQuery);
            pageContext.setAttribute("statusFilter", statusFilter);
            pageContext.setAttribute("sortField", sortField);
            pageContext.setAttribute("sortDir", sortDir);
            pageContext.setAttribute("startPage", startPage);
            pageContext.setAttribute("endPage", endPage);
            pageContext.setAttribute("error", error);
        %>
        
        <!-- Filter Form -->
        <form method="get" action="materialSupplierList.jsp" class="filter-form">
            <div class="filter-group">
                <label for="supplierId">Nhà cung cấp:</label>
                <select name="supplierId" id="supplierId" onchange="this.form.submit()">
                    <option value="">Tất cả nhà cung cấp</option>
                    <%
                        try {
                            Class.forName("com.mysql.cj.jdbc.Driver");
                            Connection conn = DriverManager.getConnection(
                                "jdbc:mysql://localhost:3306/material_system_2", 
                                "root", "123456"
                            );
                            
                            String supplierSql = "SELECT supplier_id, supplier_name FROM supplier ORDER BY supplier_name";
                            PreparedStatement supplierPs = conn.prepareStatement(supplierSql);
                            ResultSet supplierRs = supplierPs.executeQuery();
                            
                            while (supplierRs.next()) {
                                String supId = supplierRs.getString("supplier_id");
                                String supplierName = supplierRs.getString("supplier_name");
                                String selected = supId.equals(supplierId) ? "selected" : "";
                                out.println("<option value='" + supId + "' " + selected + ">" + supplierName + "</option>");
                            }
                            supplierRs.close();
                            supplierPs.close();
                            conn.close();
                        } catch (Exception e) {
                            out.println("<div class='error'>Lỗi khi tải danh sách nhà cung cấp: " + e.getMessage() + "</div>");
                        }
                    %>
                </select>
            </div>
            
            <div class="filter-group">
                <label for="status">Trạng thái:</label>
                <select name="status" id="status" onchange="this.form.submit()">
                    <option value="All" <c:if test="${statusFilter == 'All'}">selected</c:if>>Tất cả</option>
                    <option value="Có sẵn" <c:if test="${statusFilter == 'Có sẵn'}">selected</c:if>>Có sẵn</option>
                    <option value="Cần mua" <c:if test="${statusFilter == 'Cần mua'}">selected</c:if>>Cần mua</option>
                </select>
            </div>
            
            <div class="filter-group">
                <label for="itemsPerPage">Hiển thị:</label>
                <select name="itemsPerPage" id="itemsPerPage" onchange="this.form.submit()">
                    <c:forEach items="${itemsPerPageOptions}" var="size">
                        <option value="${size}" <c:if test="${itemsPerPage == size}">selected</c:if>>${size} items</option>
                    </c:forEach>
                </select>
            </div>
            
            <div class="filter-group">
                <label for="search">Tìm kiếm:</label>
                <div class="search-group">
                    <input type="text" 
                           name="search" 
                           id="search"
                           placeholder="Tìm theo tên vật tư..." 
                           value="${searchQuery}"/>
                    <button type="submit" class="search-btn">Tìm</button>
                </div>
            </div>
            
            <!-- Hidden fields to maintain state -->
            <input type="hidden" name="supplierId" value="${supplierId}">
            <input type="hidden" name="status" value="${statusFilter}">
            <input type="hidden" name="itemsPerPage" value="${itemsPerPage}">
            <input type="hidden" name="sort" value="${sortField}">
            <input type="hidden" name="dir" value="${sortDir}">
        </form>
        
        <c:if test="${not empty error}">
            <div class="error">
                ${error}
            </div>
        </c:if>
        
        <c:choose>
            <c:when test="${empty materialList}">
                <div class="info">
                    Không có dữ liệu vật tư nào được tìm thấy.
                </div>
            </c:when>
            <c:otherwise>
                <table>
                    <thead>
                        <tr>
                            <th class="sortable">
                                <form method="get" action="materialSupplierList.jsp" class="sort-form">
                                    <input type="hidden" name="supplierId" value="${supplierId}">
                                    <input type="hidden" name="status" value="${statusFilter}">
                                    <input type="hidden" name="itemsPerPage" value="${itemsPerPage}">
                                    <input type="hidden" name="search" value="${searchQuery}">
                                    <input type="hidden" name="sort" value="material_name">
                                    <input type="hidden" name="dir" value="${sortField == 'material_name' && sortDir == 'asc' ? 'desc' : 'asc'}">
                                    <button type="submit" class="sort-button ${sortField == 'material_name' ? sortDir : ''}">Tên vật tư</button>
                                </form>
                            </th>
                            <th class="sortable">
                                <form method="get" action="materialSupplierList.jsp" class="sort-form">
                                    <input type="hidden" name="supplierId" value="${supplierId}">
                                    <input type="hidden" name="status" value="${statusFilter}">
                                    <input type="hidden" name="itemsPerPage" value="${itemsPerPage}">
                                    <input type="hidden" name="search" value="${searchQuery}">
                                    <input type="hidden" name="sort" value="supplier_name">
                                    <input type="hidden" name="dir" value="${sortField == 'supplier_name' && sortDir == 'asc' ? 'desc' : 'asc'}">
                                    <button type="submit" class="sort-button ${sortField == 'supplier_name' ? sortDir : ''}">Nhà cung cấp</button>
                                </form>
                            </th>
                            <th class="sortable">
                                <form method="get" action="materialSupplierList.jsp" class="sort-form">
                                    <input type="hidden" name="supplierId" value="${supplierId}">
                                    <input type="hidden" name="status" value="${statusFilter}">
                                    <input type="hidden" name="itemsPerPage" value="${itemsPerPage}">
                                    <input type="hidden" name="search" value="${searchQuery}">
                                    <input type="hidden" name="sort" value="status">
                                    <input type="hidden" name="dir" value="${sortField == 'status' && sortDir == 'asc' ? 'desc' : 'asc'}">
                                    <button type="submit" class="sort-button ${sortField == 'status' ? sortDir : ''}">Trạng thái</button>
                                </form>
                            </th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach items="${materialList}" var="material">
                            <tr>
                                <td>${material.material_name}</td>
                                <td>${material.supplier_name}</td>
                                <td class="${material.status == 'Có sẵn' ? 'status-active' : 'status-inactive'}">
                                    ${material.status}
                                </td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
                
                <!-- Phân trang -->
                <c:if test="${totalPages > 0}">
                    <div class="pagination">
                        <form method="get" action="materialSupplierList.jsp" class="pagination-form">
                            <input type="hidden" name="supplierId" value="${supplierId}">
                            <input type="hidden" name="status" value="${statusFilter}">
                            <input type="hidden" name="itemsPerPage" value="${itemsPerPage}">
                            <input type="hidden" name="search" value="${searchQuery}">
                            <input type="hidden" name="sort" value="${sortField}">
                            <input type="hidden" name="dir" value="${sortDir}">

                            <button type="submit" name="page" value="${currentPage - 1}"
                                    class="page-button" ${currentPage <= 1 ? 'disabled' : ''}>
                                Previous
                            </button>
                            
                            <c:if test="${startPage > 1}">
                                <button type="submit" name="page" value="1" class="page-button">1</button>
                                <c:if test="${startPage > 2}">
                                    <span class="page-ellipsis">...</span>
                                </c:if>
                            </c:if>
                            
                            <c:forEach begin="${startPage}" end="${endPage}" var="i">
                                <button type="submit" name="page" value="${i}"
                                        class="page-button ${i == currentPage ? 'active' : ''}">
                                    ${i}
                                </button>
                            </c:forEach>
                            
                            <c:if test="${endPage < totalPages}">
                                <c:if test="${endPage < totalPages - 1}">
                                    <span class="page-ellipsis">...</span>
                                </c:if>
                                <button type="submit" name="page" value="${totalPages}" class="page-button">
                                    ${totalPages}
                                </button>
                            </c:if>
                            
                            <button type="submit" name="page" value="${currentPage + 1}"
                                    class="page-button" ${currentPage >= totalPages ? 'disabled' : ''}>
                                Next
                            </button>
                        </form>
                    </div>
                    
                    <div class="pagination-info">
                        Page ${currentPage} of ${totalPages}
                    </div>
                </c:if>
            </c:otherwise>
        </c:choose>
    </div>
</body>
</html> 