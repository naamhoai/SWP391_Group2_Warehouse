<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Setting List</title>
        <link rel="stylesheet" href="css/sidebar.css">
        <link rel="stylesheet" href="css/settinglist.css">
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/css/all.min.css">


    </head>
    <body>

        <jsp:include page="sidebar.jsp" />

        <div id="main-content">
            <div class="page-header">
                <h2>Quản lý người dùng</h2>
                <c:if test="${not empty messUpdate}">
                    <div class="success-message">${messUpdate}</div>
                </c:if>
                <c:if test="${param.success == 'add'}">
                    <div class="success-message">Thêm người dùng mới thành công!</div>
                </c:if>
            </div>

            <form action="settinglist" method="get">
                <div class="filter-section">
                    <select name="type">
                        <option value="all">Vai trò</option>
                        <c:forEach var="r" items="${requestScope.listrole}">
                            <option value="${r.rolename}">${r.rolename}</option>
                        </c:forEach>
                    </select>
                    <select name="status">
                        <option value="All">Trạng thái </option>
                        <option value="Hoạt động">Hoạt động</option>
                        <option value="Không hoạt động">Không hoạt động</option>
                    </select>

                    <input type="text" placeholder="Nhập tên hoặc kí tự để tìm kiếm." name="searchname">


                    <button type="submit" value="filter" name="save">

                        <i class="fas fa-search"></i> Tìm Kiếm
                    </button>

                    <div>
                        <button><a href="${pageContext.request.contextPath}/settinglist">Làm mới </a></button>
                    </div>



                    </button>
                </div>
            </form>

            <div class="table-container">
                <table>
                    <thead>
                        <tr>
                            <th> </th>
                            <th>Id</th>
                            <th>Tên</th>
                            <th>Vai Trò</th>
                            <th>Trạng thái</th>
                            <th>Hành động</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="u" items="${requestScope.list}">
                            <tr>
                                <td></td>
                                <td>${u.user_id}</td>
                                <td>${u.fullname}</td>
                                <td>${u.role.rolename}</td>

                                <td>${u.status}</td>
                                <td class="action">
                                    <c:if test="${u.role.roleid != 1}">
                                        <a href="detailuser?roleid=${u.role.roleid}&userid=${u.user_id}">
                                            <i class="fas fa-edit"></i> chỉnh sửa
                                        </a>
                                    </c:if>
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
