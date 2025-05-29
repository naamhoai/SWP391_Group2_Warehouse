<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ page import="java.io.File" %>

<!DOCTYPE html>
<html lang="vi">
    <head>
        <meta charset="UTF-8" />
        <title>Tạo người dùng mới</title>
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/createUser.css" />


    </head>
    <body>
        <div class="form-container">
            <h2 class="form-title">Tạo người dùng mới</h2>

            <c:if test="${not empty error}">
                <div class="alert error">${error}</div>
            </c:if>
            <c:if test="${not empty success}">
                <div class="alert success">${success}</div>
            </c:if>
            <c:if test="${not empty roleIdError}">
                <div class="alert error">${roleIdError}</div>
            </c:if>
            <c:if test="${not empty emailError}">
                <div class="alert error">${emailError}</div>
            </c:if>
            <c:if test="${not empty roleError}">
                <div class="alert error">${roleError}</div>
            </c:if>

            <form action="${pageContext.request.contextPath}/createUserServlet" method="post" enctype="multipart/form-data">

                <label class="file-upload-label" for="imageFile">Ảnh đại diện (upload mới):</label>
                <input class="file-upload-input" type="file" id="imageFile" name="imageFile" accept="image/*" />

                <label for="fullName">Họ và tên</label>
                <input type="text" id="fullName" name="fullName" required
                       value="${fn:escapeXml(fullName)}" oninput="generateEmail()" />

                <label for="email">Email (tự tạo từ họ tên)</label>
                <input type="text" id="email" name="email" 
                       value="${fn:escapeXml(email)}" readonly />

                <label for="password">Mật khẩu</label>
                <input type="password" id="password" name="password" required />

                <label for="phone">Số điện thoại</label>
                <input type="tel" id="phone" name="phone"
                       value="${fn:escapeXml(userPhone)}" />

                <label for="roleId">Vai trò</label>
                <select id="roleId" name="roleId">
                    <option value="">Chọn vai trò</option>
                    <c:forEach var="role" items="${roleList}">
                        <option value="${role.roleid}"
                                <c:if test="${role.roleid == selectedRoleId}">selected</c:if> >
                            ${role.rolename}
                        </option>
                    </c:forEach>
                </select>

                <label class="status-label">Trạng thái</label>
                <div class="status-group">
                    <label><input type="radio" name="status" value="active"
                                  <c:if test="${status == 'active' || status == null}">checked</c:if> /> Hoạt động</label>
                        <label><input type="radio" name="status" value="inactive"
                            <c:if test="${status == 'inactive'}">checked</c:if> /> Không hoạt động</label>
                    </div>

                    <label for="priority">Mức ưu tiên</label>
                <c:choose>
                    <c:when test="${not empty priority}">
                        <input type="number" id="priority" name="priority" min="0" value="${priority}" />
                    </c:when>
                    <c:otherwise>
                        <input type="number" id="priority" name="priority" min="0" value="0" />
                    </c:otherwise>
                </c:choose>

                <label for="description">Mô tả</label>
                <textarea id="description" name="description"></textarea>

                <button type="submit">Tạo người dùng</button>
            </form>

        </div>



    </body>
</html>
