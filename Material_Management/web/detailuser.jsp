<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="UTF-8">
        <title>Cấu hình người dùng</title>
        <link rel="stylesheet" href="css/detailuser.css">

    </head>
    <body>
        <div class="container">
            <h2>Cấu hình người dùng</h2>
            <h3 style="color: red">${messkk}</h3>
            <form action="detailuser" method="post">
                <div class="row">
                    <div>
                        <label>Tên</label>
                        <input type="text" name="name" value="${user.fullname}" readonly maxlength="20">
                    </div>
                    <div>
                        <label>Kiểu vai trò</label>
                        <select name="role" <c:if test="${user.role.roleid == 2}">disabled</c:if>>
                            <c:forEach var="i" items="${requestScope.lits}">
                                <c:choose>
                                    <c:when test="${i.roleid == 2}">
                                        <c:if test="${user.role.roleid == 2}">
                                            <option value="2" selected>Giám đốc</option>
                                        </c:if>
                                    </c:when>
                                    <c:when test="${i.roleid != 1 && i.roleid != 2}">
                                        <option value="${i.roleid}" <c:if test="${i.roleid == user.role.roleid}">selected</c:if>>
                                            ${i.rolename}
                                        </option>
                                    </c:when>
                                </c:choose>
                            </c:forEach>
                        </select>
                        <c:if test="${user.role.roleid == 2}">
                            <input type="hidden" name="role" value="2"/>
                        </c:if>
                    </div>
                </div>
                <div class="row radio-row">
                    <label>Trạng thái</label>
                    <div class="radio-group">
                        <label>
                            <input type="radio" name="status" value="Hoạt động"
                                   <c:if test="${user.status == 'Hoạt động'}">checked</c:if> > Hoạt động
                        </label>
                        <label>
                            <input type="radio" name="status" value="Không hoạt động"
                                   <c:if test="${user.status == 'Không hoạt động'}">checked</c:if> > Không hoạt động
                        </label>
                    </div>
                </div>
                <input type="hidden" name="userid" value="${user.user_id}">
                <input type="hidden" name="roleid" value="${user.role.roleid}">

                <div class="hopbut">
                    <a href="settinglist">Trở lại </a>
                    <button type="submit">Lưu</button>
                </div>
            </form>
        </div>


    </body>
</html>
