<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page import="dao.MaterialDAO" %>
<%@ page import="model.Material" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Arrays" %>
<%@ page import="java.text.NumberFormat" %>
<%@ page import="java.util.Locale" %>
<%@ page import="java.io.File" %>

<%
    // 1. Xử lý các tham số đầu vào
    request.setCharacterEncoding("UTF-8");
    
    // Lấy đường dẫn thực của thư mục image
    String imagePath = application.getRealPath("/image");
    pageContext.setAttribute("imagePath", imagePath);
    
    // Tạo mảng số lượng items per page
    List<Integer> itemsPerPageOptions = Arrays.asList(5, 10, 20, 50);
    pageContext.setAttribute("itemsPerPageOptions", itemsPerPageOptions);
    
    // Lấy các tham số tìm kiếm và lọc
    String searchQuery = request.getParameter("search");
    String categoryFilter = request.getParameter("category");
    String statusFilter = request.getParameter("status");
    
    // Lấy tham số sắp xếp
    String sortField = request.getParameter("sort");
    String sortDir = request.getParameter("dir");
    
    if (sortField == null) sortField = "material_id"; // Mặc định sắp xếp theo ID
    if (sortDir == null) sortDir = "asc"; // Mặc định sắp xếp tăng dần
    
    // Giá trị mặc định cho bộ lọc
    categoryFilter = (categoryFilter == null) ? "All" : categoryFilter;
    statusFilter = (statusFilter == null) ? "All" : statusFilter;
    
    // 2. Xử lý phân trang
    int currentPage = 1;
    int itemsPerPage = 5;
    
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
        itemsPerPage = 5;
    }
    
    // 3. Lấy dữ liệu từ database
    MaterialDAO materialDAO = new MaterialDAO();
    int totalPages = materialDAO.getTotalPages(searchQuery, categoryFilter, statusFilter, itemsPerPage);
    if (currentPage > totalPages && totalPages > 0) {
        currentPage = totalPages;
    }
    
    List<Material> materials = materialDAO.getAllMaterials(searchQuery, categoryFilter, statusFilter, currentPage, itemsPerPage, sortField, sortDir);
    List<String> categories = materialDAO.getAllCategories();
    
    // Format tiền tệ
    NumberFormat numberFormat = NumberFormat.getNumberInstance(new Locale("en", "US"));
    
    // Tính toán phân trang
    int startPage = Math.max(1, currentPage - 2);
    int endPage = Math.min(totalPages, currentPage + 2);
    
    // Set attributes for JSTL
    pageContext.setAttribute("materials", materials);
    pageContext.setAttribute("categories", categories);
    pageContext.setAttribute("currentPage", currentPage);
    pageContext.setAttribute("totalPages", totalPages);
    pageContext.setAttribute("itemsPerPage", itemsPerPage);
    pageContext.setAttribute("searchQuery", searchQuery);
    pageContext.setAttribute("categoryFilter", categoryFilter);
    pageContext.setAttribute("statusFilter", statusFilter);
    pageContext.setAttribute("numberFormat", numberFormat);
    pageContext.setAttribute("startPage", startPage);
    pageContext.setAttribute("endPage", endPage);
    pageContext.setAttribute("sortField", sortField);
    pageContext.setAttribute("sortDir", sortDir);
%>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Product List</title>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/css/all.min.css">
    <link rel="stylesheet" href="./css/materiallist.css">
    <link rel="stylesheet" href="./css/sidebar.css">
</head>
<body>
    <!-- Include the sidebar -->
    <jsp:include page="side.jsp" />

    <div id="main-content">
        <div class="container">
            <div class="top-bar">
                <h1 class="page-title">Product List</h1>
                <a href="addProduct.jsp" class="add-button">+ Add Product</a>
            </div>

            <form method="get" action="materiallist.jsp">
                <div class="filters">
                    <div class="filter-item">
                        <label>Category</label>
                        <select name="category" onchange="this.form.submit()">
                            <option value="All" <c:if test="${categoryFilter == 'All'}">selected</c:if>>All</option>
                            <c:forEach items="${categories}" var="category">
                                <option value="${category}" <c:if test="${category == categoryFilter}">selected</c:if>>
                                    ${category}
                                </option>
                            </c:forEach>
                        </select>
                    </div>

                    <div class="filter-item">
                        <label>Status</label>
                        <select name="status" onchange="this.form.submit()">
                            <option value="All" <c:if test="${statusFilter == 'All'}">selected</c:if>>All</option>
                            <option value="In Stock" <c:if test="${statusFilter == 'In Stock'}">selected</c:if>>In Stock</option>
                            <option value="Out of Stock" <c:if test="${statusFilter == 'Out of Stock'}">selected</c:if>>Out of Stock</option>
                        </select>
                    </div>

                    <div class="filter-item">
                        <label>Display</label>
                        <select name="itemsPerPage" onchange="this.form.submit()">
                            <c:forEach items="${itemsPerPageOptions}" var="size">
                                <option value="${size}" <c:if test="${itemsPerPage == size}">selected</c:if>>${size} items</option>
                            </c:forEach>
                        </select>
                    </div>

                    <div class="filter-item">
                        <label>Search</label>
                        <div class="search-group">
                            <input type="text" 
                                   name="search" 
                                   placeholder="Search by product name" 
                                   value="${searchQuery}"/>
                            <button type="submit" class="search-btn">Search</button>
                        </div>
                    </div>
                </div>

                <!-- Hidden fields to maintain state -->
                <input type="hidden" name="category" value="${categoryFilter}">
                <input type="hidden" name="status" value="${statusFilter}">
                <input type="hidden" name="itemsPerPage" value="${itemsPerPage}">
                <c:if test="${not empty searchQuery}">
                    <input type="hidden" name="search" value="${searchQuery}">
                </c:if>
            </form>

            <table class="product-table">
                <thead>
                    <tr>
                        <th>Image</th>
                        <th class="sortable">
                            <form method="get" action="materiallist.jsp" class="sort-form">
                                <!-- Maintain current filters -->
                                <input type="hidden" name="category" value="${categoryFilter}">
                                <input type="hidden" name="status" value="${statusFilter}">
                                <input type="hidden" name="itemsPerPage" value="${itemsPerPage}">
                                <c:if test="${not empty searchQuery}">
                                    <input type="hidden" name="search" value="${searchQuery}">
                                </c:if>
                                
                                <input type="hidden" name="sort" value="material_id">
                                <input type="hidden" name="dir" value="${sortField == 'material_id' && sortDir == 'asc' ? 'desc' : 'asc'}">
                                <button type="submit" class="sort-button ${sortField == 'material_id' ? sortDir : ''}">ID</button>
                            </form>
                        </th>
                        <th>Product Name</th>
                        <th class="sortable">
                            <form method="get" action="materiallist.jsp" class="sort-form">
                                <!-- Maintain current filters -->
                                <input type="hidden" name="category" value="${categoryFilter}">
                                <input type="hidden" name="status" value="${statusFilter}">
                                <input type="hidden" name="itemsPerPage" value="${itemsPerPage}">
                                <c:if test="${not empty searchQuery}">
                                    <input type="hidden" name="search" value="${searchQuery}">
                                </c:if>
                                
                                <input type="hidden" name="sort" value="price">
                                <input type="hidden" name="dir" value="${sortField == 'price' && sortDir == 'asc' ? 'desc' : 'asc'}">
                                <button type="submit" class="sort-button ${sortField == 'price' ? sortDir : ''}">Price</button>
                            </form>
                        </th>
                        <th class="sortable">
                            <form method="get" action="materiallist.jsp" class="sort-form">
                                <!-- Maintain current filters -->
                                <input type="hidden" name="category" value="${categoryFilter}">
                                <input type="hidden" name="status" value="${statusFilter}">
                                <input type="hidden" name="itemsPerPage" value="${itemsPerPage}">
                                <c:if test="${not empty searchQuery}">
                                    <input type="hidden" name="search" value="${searchQuery}">
                                </c:if>
                                
                                <input type="hidden" name="sort" value="quantity">
                                <input type="hidden" name="dir" value="${sortField == 'quantity' && sortDir == 'asc' ? 'desc' : 'asc'}">
                                <button type="submit" class="sort-button ${sortField == 'quantity' ? sortDir : ''}">Quantity</button>
                            </form>
                        </th>
                        <th>Category</th>
                        <th>Status</th>
                        <th>Actions</th>
                    </tr>
                </thead>
                <tbody>
                    <c:choose>
                        <c:when test="${empty materials}">
                            <tr>
                                <td colspan="8" class="no-data">No products found</td>
                            </tr>
                        </c:when>
                        <c:otherwise>
                            <c:forEach items="${materials}" var="material">
                                <tr>
                                    <td>
                                        <c:if test="${not empty material.imageUrl}">
                                            <img src="./image/${material.imageUrl}" 
                                                 alt="${material.name}" 
                                                 class="product-image">
                                        </c:if>
                                    </td>
                                    <td>${material.materialId}</td>
                                    <td>${material.name}</td>
                                    <td>${numberFormat.format(material.price)}</td>
                                    <td>${material.quantity}</td>
                                    <td>${material.categoryName}</td>
                                    <td class="${material.quantity > 0 ? 'status-in-stock' : 'status-out-of-stock'}">
                                        ${material.quantity > 0 ? 'In Stock' : 'Out of Stock'}
                                    </td>
                                    <td class="action-buttons">
                                        <a href="editProduct.jsp?id=${material.materialId}" 
                                           class="table-button edit-button">Edit</a>
                                        <form method="post" action="deleteProduct" class="delete-form">
                                            <input type="hidden" name="id" value="${material.materialId}">
                                            <button type="submit" class="table-button delete-button" 
                                                    onclick="return confirm('Are you sure you want to delete this product?')">
                                                Delete
                                            </button>
                                        </form>
                                    </td>
                                </tr>
                            </c:forEach>
                        </c:otherwise>
                    </c:choose>
                </tbody>
            </table>

            <c:if test="${totalPages > 0}">
                <div class="pagination">
                    <form method="get" action="materiallist.jsp" class="pagination-form">
                        <input type="hidden" name="category" value="${categoryFilter}">
                        <input type="hidden" name="status" value="${statusFilter}">
                        <input type="hidden" name="itemsPerPage" value="${itemsPerPage}">
                        <input type="hidden" name="sort" value="${sortField}">
                        <input type="hidden" name="dir" value="${sortDir}">
                        <c:if test="${not empty searchQuery}">
                            <input type="hidden" name="search" value="${searchQuery}">
                        </c:if>

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
        </div>
    </div>
</body>
</html>
