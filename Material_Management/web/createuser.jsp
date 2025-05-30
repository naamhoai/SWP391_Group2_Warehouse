<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ page import="java.io.File" %>

<!DOCTYPE html>
<html lang="vi">
    <head>
        <meta charset="UTF-8" />
        <title>Tạo người dùng mới</title>
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/createuser.css" />
    </head>
    <body>
        <div class="form-container">
            <h2 class="form-title">Tạo người dùng mới</h2>

            <c:if test="${not empty error}">
                <div class="alert error">${error}</div>
            </c:if>
            
            

            <form action="${pageContext.request.contextPath}/CreateUserServlet" method="post" enctype="multipart/form-data">

                <div class="row file-upload">
                    <label for="imageFile" class="file-upload-label">Ảnh đại diện (upload mới):</label>
                    <input type="file" id="imageFile" name="imageFile" class="file-upload-input" accept="image/*" />
                </div>

                <div class="row">
                    <div class="column">
                        <label for="fullName">Họ và tên</label>
                        <input type="text" id="fullName" name="fullName" required 
                               value="${fn:escapeXml(fullName)}" oninput="generateEmail()" />
                    </div>
                    <div class="column">
                        <label for="username">Tên đăng nhập</label>
                        <input type="text" id="username" name="username" required 
                               value="${fn:escapeXml(username)}" />
                    </div>
                </div>

                <div class="row">
                    <div class="column">
                        <label for="password">Mật khẩu</label>
                        <input type="password" id="password" name="password" required />
                    </div>
                    <div class="column">
                        <label for="gender">Giới tính</label>
                        <select id="gender" name="gender">
                            <option value="">Chọn giới tính</option>
                            <option value="Men" <c:if test="${gender == 'Men'}">selected</c:if>>Nam</option>
                            <option value="Women" <c:if test="${gender == 'Women'}">selected</c:if>>Nữ</option>
                            <option value="Other" <c:if test="${gender == 'Other'}">selected</c:if>>Khác</option>
                            </select>
                        </div>
                    </div>

                    <div class="row">
                        <div class="column">
                            <label for="dayofbirth">Ngày sinh</label>
                            <input type="date" id="dayofbirth" name="dayofbirth" value="${dayofbirth}" />
                    </div>
                    <div class="column">
                        <label for="email">Email (tự tạo từ họ tên)</label>
                        <input type="text" id="email" name="email" readonly value="${fn:escapeXml(email)}" />
                    </div>
                </div>

                <div class="row">
                    <div class="column">
                        <label for="phone">Số điện thoại</label>
                        <input type="tel" id="phone" name="phone" value="${fn:escapeXml(userPhone)}" />
                    </div>
                    <div class="column">
                        <label for="roleId">Vai trò</label>
                        <select id="roleId" name="roleId">
                            <option value="">Chọn vai trò</option>
                            <c:forEach var="role" items="${roleList}">
                                <option value="${role.roleid}" <c:if test="${role.roleid == selectedRoleId}">selected</c:if>>
                                    ${role.rolename}
                                </option>
                            </c:forEach>
                        </select>
                    </div>
                </div>

                <div class="row">
                    <div class="column">
                        <label class="status-label">Trạng thái</label>
                        <div class="status-group">
                            <label>
                                <input type="radio" name="status" value="active" 
                                       <c:if test="${status == 'active' || status == null}">checked</c:if> /> Hoạt động
                                </label>
                                <label>
                                    <input type="radio" name="status" value="inactive" 
                                    <c:if test="${status == 'inactive'}">checked</c:if> /> Không hoạt động
                                </label>
                            </div>
                        </div>
                        <div class="column">
                            <label for="priority">Mức ưu tiên</label>
                            <input type="number" id="priority" name="priority" min="0" value="${priority}" />
                    </div>
                </div>

                <div class="row full-width">
                    <label for="description">Mô tả</label>
                    <a href="../src/java/dao/UserDAO.java"></a>
                    <textarea id="description" name="description">${fn:escapeXml(description)}</textarea>
                </div>

                <div class="buttons">
                    <button type="submit" class="save-btn">Tạo người dùng</button>
                </div>

            </form>
        </div>
    </body>
</html>
