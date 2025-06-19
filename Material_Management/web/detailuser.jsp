<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="UTF-8">
        <title>Cài đặt người dùng</title>

        <link rel="stylesheet" href="css/detailuser.css">

    </head>
    <body>
        <div class="container">
            <h2>Setting Details</h2>
            <h3 style="color: red">${messkk}</h3>

            <form action="detailuser" method="post">

                <div class="row">
                    <div>
                        <label>Tên</label>
                        <input type="text" name="name" value="${user.fullname}"required>

                    </div>
                    <div>
                        <label>Kiểu vai trò</label>
                        <select name="role">
                            <c:forEach var="i" items="${requestScope.lits}">
                                <option value="${i.roleid}">${i.rolename}</option>
                            </c:forEach>
                        </select>
                    </div>
                </div>
                <div>
                    <label>gmail</label>
                    <input type="text" name="email" value="${user.email}"required>
                </div>
                <div>
                    <label>Mật khẩu </label>
                    <input type="password" name="pass" value="${user.password}"readonly>
                </div>

                <div class="row">
                    <div>
                        <label>Ưu tiên</label>
                        <input type="text" name="priority" value="${user.priority}"required>
                    </div>
                    <div>
                        <label>Trạng thái</label>
                        <div class="radio-group">
                            <label><input type="radio" name="status" value="active"> Active</label>
                            <label><input type="radio" name="status" value="inactive"> Inactive</label>
                        </div>
                    </div>
                </div>

                <div class="row">
                    <div style="width: 100%;">
                        <label>Mô tả</label>
                        <textarea rows="3" name="description">${user.description}</textarea>
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
