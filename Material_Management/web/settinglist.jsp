<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Setting List</title>
        <link rel="stylesheet" href="css/sidebar.css">
        <link rel="stylesheet" href="./css/settingList.css">
         <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/css/all.min.css">


    </head>
    <body>

        <jsp:include page="sidebar.jsp" />

        <div id="main-content">
            <div class="page-header">
                <h2>User Management</h2>
                <c:if test="${not empty messUpdate}">
                    <div class="success-message">${messUpdate}</div>
                </c:if>
            </div>
            <div>
                <button><a href="${pageContext.request.contextPath}/CreateUserServlet">Add User</a></button>
            </div>

            <form action="settinglist" method="get">
                <div class="filter-section">
                    <select name="type">
                        <option value="all">All Types</option>
                        <c:forEach var="r" items="${requestScope.listrole}">
                            <option value="${r.rolename}">${r.rolename}</option>
                        </c:forEach>
                    </select>
                    <select name="status">
                        <option value="All">All Statuses</option>
                        <option value="active">Active</option>
                        <option value="inactive">Inactive</option>
                    </select>
                    <input type="text" placeholder="Enter number to search" name="priority">
                    <input type="text" placeholder="Enter keyword(s) to search" name="searchname">


                    <button type="submit" value="filter" name="save">

                        <i class="fas fa-search"></i> Search
                    </button>
                    <select name="sortBy">
                        <option value="">Sort By</option>
                        <option value="idasc">ID (Ascending)</option>
                        <option value="iddesc">ID (Descending)</option>
                        <option value="nameasc">Name (A-Z)</option>
                        <option value="namedesc">Name (Z-A)</option>
                        <option value="priorityasc">Priority (Low-High)</option>
                        <option value="prioritydesc">Priority (High-Low)</option>
                    </select>

                    <button type="submit" value="sort" name="save">
                        Sort
                    </button>
                </div>
            </form>

            <div class="table-container">
                <table>
                    <thead>
                        <tr>
                            <th> </th>
                            <th>Id</th>
                            <th>Name</th>
                            <th>Type</th>
                            <th>description</th>
                            <th>Priority</th>
                            <th>Status</th>
                            <th>Action</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="u" items="${requestScope.list}">
                            <tr>
                                <td>

                                </td>
                                <td>${u.user_id}</td>
                                <td>${u.fullname}</td>
                                <td>${u.role.rolename}</td>
                                <td>${u.description}</td>
                                <td>${u.priority}</td>
                                <td>${u.status}</td>
                                <td class="action">
                                    <a href="detailuser?roleid=${u.role.roleid}&userid=${u.user_id}">
                                        <i class="fas fa-edit"></i> Edit
                                    </a>
                                    <a href="settinglist?sta=${u.user_id}&action=inactive" class="inactive">
                                        <i class="fas fa-ban"></i> Inactive
                                    </a>
                                    <a href="settinglist?sta=${u.user_id}&action=active" class="active">
                                        <i class="fas fa-check"></i> Active
                                    </a>
                                </td>
                            </tr>
                        </c:forEach>
                        <c:if test="${not empty requestScope.mess}">
                            <tr>
                                <td colspan="8" class="message">${requestScope.mess}</td>
                            </tr>
                        </c:if>
                    </tbody>
                </table>
            </div>

            <div class="pagination">
                <c:forEach begin="1" end="${pages}" var="p">
                    <form method="get" action="settinglist" style="display:inline;">
                        <input type="hidden" name="page" value="${p}" />
                        <input type="hidden" name="status" value="${param.status}" />
                        <input type="hidden" name="type" value="${param.type}" />
                        <input type="hidden" name="priority" value="${param.priority}" />
                        <input type="hidden" name="searchname" value="${param.searchname}" />
                        <input type="hidden" name="sortBy" value="${param.sortBy}" />
                        <input type="hidden" name="save" value="${param.save}" />

                        <button type="submit">${p}</button>
                    </form>
                </c:forEach>
            </div>


    </body> 
</html>
