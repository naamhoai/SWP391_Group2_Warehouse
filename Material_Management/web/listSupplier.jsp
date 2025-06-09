<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Supplier List</title>
        <link rel="stylesheet" href="css/sidebar.css">
        <link rel="stylesheet" href="css/supplier.css">
        <link rel="stylesheet" href="css/footer.css">
    </head>
    <body>
        <jsp:include page="sidebar.jsp"/>
        <div class="main-content">
            <div class="page-content">
                <div class="content-header">
                    <h1>Supplier List</h1>
                    <a href="supplier?action=add" class="btn-new">+ Add New</a>
                </div>

                <c:if test="${not empty sessionScope.error}">
                    <div class="message error">
                        ${sessionScope.error}
                    </div>
                    <c:remove var="error" scope="session"/>
                </c:if>

                <c:if test="${not empty sessionScope.message}">
                    <div class="message success">
                        ${sessionScope.message}
                    </div>
                    <c:remove var="message" scope="session"/>
                </c:if>

                <form action="supplier" method="get" class="filter-form">
                    <select name="status" class="filter-select">
                        <option value="">All Status</option>
                        <option value="active" ${param.status == 'active' ? 'selected' : ''}>Active</option>
                        <option value="inactive" ${param.status == 'inactive' ? 'selected' : ''}>Inactive</option>
                    </select>

                    <input type="text" name="keyword" placeholder="Search by name"
                           value="${param.keyword != null ? param.keyword : ''}" class="filter-input">

                    <button type="submit" name="sortBy" value="name" class="sort-btn ${param.sortBy == 'name' ? 'active' : ''}">
                        Sort by Name
                    </button>

                    <button type="submit" name="sortBy" value="id" class="sort-btn ${param.sortBy == 'id' ? 'active' : ''}">
                        Sort by ID
                    </button>

                    <button type="submit" class="search-btn">Search</button>
                </form>

                <div class="table-container">
                    <table class="supplier-table">
                        <thead>
                            <tr>
                                <th>ID</th>
                                <th>Supplier Name</th>
                                <th>Contact Person</th>
                                <th>Phone Number</th>
                                <th>Address</th>
                                <th>Status</th>
                                <th>Created Date</th>
                                <th>Actions</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:choose>
                                <c:when test="${not empty listSupplier}">
                                    <c:forEach items="${listSupplier}" var="s">
                                        <tr>
                                            <td>${s.supplierId}</td>
                                            <td>${s.supplierName}</td>
                                            <td>${s.contactPerson}</td>
                                            <td>${s.supplierPhone}</td>
                                            <td>${s.address}</td>
                                            <td>
                                                <span class="status-badge ${s.status == 'active' ? 'status-active' : 'status-inactive'}">
                                                    ${s.status == 'active' ? 'Active' : 'Inactive'}
                                                </span>
                                            </td>
                                            <td><fmt:formatDate value="${s.createdAt}" pattern="dd/MM/yyyy HH:mm"/></td>
                                            <td class="action-buttons">
                                                <a href="supplier?action=edit&id=${s.supplierId}" class="btn-edit">Edit</a>
                                                <a href="#" onclick="confirmDelete(${s.supplierId})" class="btn-delete">Delete</a>
                                            </td>
                                        </tr>
                                    </c:forEach>
                                </c:when>
                                <c:otherwise>
                                    <tr>
                                        <td colspan="8" class="no-data">No suppliers found.</td>
                                    </tr>
                                </c:otherwise>
                            </c:choose>
                        </tbody>
                    </table>
                </div>
            </div>
            <jsp:include page="footer.jsp"/>
        </div>

        <script>
            function confirmDelete(id) {
                if (confirm('Are you sure you want to delete this supplier?')) {
                    window.location.href = 'supplier?action=delete&id=' + id;
                }
            }
        </script>
    </body>
</html> 