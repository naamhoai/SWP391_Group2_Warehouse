<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Setting List</title>
      
        <link rel="stylesheet" href="./Css/settinglist.css">
      
        
    </head>
    <body>

        

        <div id="main-content">
            <div class="page-header">
                <h2>Setting List</h2>
                <c:if test="${not empty messUpdate}">
                    <div class="success-message">${messUpdate}</div>
                </c:if>
            </div>

            <form action="settinglist" method="post">
                <div class="filter-section">
                    <select name="type">
                        <option value="all">All Types</option>
                        <c:forEach var="r" items="${requestScope.listrole}">
                            <option value="${r.rolename}">${r.rolename}</option>
                        </c:forEach>
                    </select>
                    <select name="status">
                        <option value="all">All Statuses</option>
                        <option value="active">Active</option>
                        <option value="inactive">Deactive</option>
                    </select>
                    <input type="text" placeholder="Enter number to search" name="priority">
                    <input type="text" placeholder="Enter keyword(s) to search" name="searchname">

                    </select>

                    <button type="submit" value="filter"name="save">
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
                    <button type="submit" value="sort"name="save">
                        Sort
                    </button>
                </div>
            </form>

            <div class="top-right">
                <a href="#"><i class="fas fa-plus"></i> New User</a>
            </div>

            <div class="table-container">
                <table>
                    <thead>
                        <tr>

                            <th><input type="checkbox"/> </th>

                            <th>Id</th>
                            <th>Name</th>
                            <th>Type</th>
                            <th>Value</th>
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
                                <td>${u.username}</td>
                                <td></td>
                                <td>${u.priority}</td>
                                <td>${u.status}</td>
                                <td class="action">
                                    <a href="detailuser?roleid=${u.role_id}&userid=${u.user_id}">
                                        <i class="fas fa-edit"></i> Edit
                                    </a>
                                    <a href="Status?sta=${u.user_id}&action=inactive" class="inactive">
                                        <i class="fas fa-ban"></i> Inactive
                                    </a>
                                    <a href="Status?sta=${u.user_id}&action=active" class="active">
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
                <button><i class="fas fa-chevron-left"></i></button>
                <button class="active">1</button>
                <button>2</button>
                <button>3</button>
                <button>...</button>
                <button>10</button>
                <button><i class="fas fa-chevron-right"></i></button>
            </div>

           
    </body>
</html>
