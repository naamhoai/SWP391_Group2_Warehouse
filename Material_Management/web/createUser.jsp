<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<!DOCTYPE html>
<html lang="vi">
    <head>
        <meta charset="UTF-8" />
        <title>Tạo người dùng mới</title>
        <link rel="stylesheet" href="css/createUser.css" />
        <link rel="stylesheet" href="css/footer.css">
        <link rel="stylesheet" href="css/sidebar.css">
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/css/all.min.css">
    </head>
    <body>
        <div class="layout">
            <%@include file="sidebar.jsp" %>
            <div class="form-wrapper">
                <h2 class="form-title">Tạo người dùng mới</h2>
                <c:if test="${not empty error}">
                    <div class="alert error">${error}</div>
                </c:if>
                <form action="${pageContext.request.contextPath}/CreateUserServlet" method="post" enctype="multipart/form-data" id="createUserForm">
                    <div class="avatar-container">
                        <img id="avatarPreview" src="#" alt="Avatar" style="display: none;"/>
                        <label for="imageFile" class="custom-file-label"><i class="fas fa-upload"></i> Chọn ảnh đại diện</label>
                        <input type="file" id="imageFile" name="imageFile" accept="image/*" />
                        <div id="imageError" class="error-message"></div>
                    </div>
                    <div class="row">
                        <div class="column">
                            <label for="fullName">Họ và tên: <span class="required">*</span></label>
                            <input type="text" id="fullName" name="fullName" required value="${fn:escapeXml(fullName)}" oninput="generateEmail()" />
                            <div class="error-message" id="fullNameError"></div>
                        </div>
                        <div class="column">
                            <label for="password">Mật khẩu: <span class="required">*</span></label>
                            <div class="password-container">
                                <input type="password" id="password" name="password" required value="${fn:escapeXml(password)}" />
                                <span class="toggle-password" onclick="togglePassword()">
                                    <i id="eyeIcon" class="fas fa-eye"></i>
                                </span>
                            </div>
                            <div class="password-requirements">
                                <small>Mật khẩu phải có ít nhất 8 ký tự, bao gồm chữ hoa, chữ thường, số và ký tự đặc biệt</small>
                            </div>
                            <div class="error-message" id="passwordError"></div>
                        </div>
                    </div>
                    <div class="row">
                        <div class="column">
                            <label for="gender">Giới tính: <span class="required">*</span></label>
                            <select id="gender" name="gender">
                                <option value="">Chọn giới tính</option>
                                <option value="Nam" <c:if test="${gender == 'Nam'}">selected</c:if>>Nam</option>
                                <option value="Nữ" <c:if test="${gender == 'Nữ'}">selected</c:if>>Nữ</option>
                                <option value="Khác" <c:if test="${gender == 'Khác'}">selected</c:if>>Khác</option>
                                </select>
                                <div class="error-message" id="genderError"></div>
                            </div>
                            <div class="column">
                                <label for="phone">Số điện thoại:</label>
                                <input type="tel" id="phone" name="phone" value="${fn:escapeXml(phone)}" placeholder="VD: 0123456789" 
                                   pattern="[0-9]+" 
                                   oninput="this.value = this.value.replace(/[^0-9]/g, '')" 
                                   maxlength="11" />
                            <div class="error-message" id="phoneError"></div>
                        </div>
                    </div>
                    <div class="row">

                        <div class="column">
                            <label for="email">Email: <span class="required">*</span></label>
                            <input type="email" id="email" name="email" value="${fn:escapeXml(email)}" readonly />
                            <div class="email-info">
                                <small>Email sẽ được tạo tự động từ họ và tên</small>
                            </div>
                            <div class="error-message" id="emailError"></div>
                        </div>
                        <div class="column">
                            <label class="status-label">Trạng thái:</label>
                            <div class="status-group">
                                <label><input type="radio" name="status" value="Hoạt động" checked /> Hoạt động</label>
                                <label><input type="radio" name="status" value="Không hoạt động" /> Không hoạt động</label>
                            </div>
                        </div>
                    </div>
                    <div class="row">

                        <div class="column">
                            <label for="roleId">Vai trò: <span class="required">*</span></label>
                            <select id="roleId" name="roleId">
                                <option value="">Chọn vai trò</option>
                                <c:forEach var="role" items="${roleList}">
                                    <option value="${role.roleid}" <c:if test="${role.roleid == roleId}">selected</c:if>>${role.rolename}</option>
                                </c:forEach>
                            </select>
                            <div class="error-message" id="roleIdError"></div>
                        </div>
                    </div>
                    <div class="buttons">
                        <a href="${pageContext.request.contextPath}/settinglist" class="btn back-btn">Quay lại</a>
                        <button type="submit" class="btn save-btn">Add User</button>
                    </div>
                </form>
            </div>
        </div>
        <script src="js/sidebar.js"></script>
        <script src="${pageContext.request.contextPath}/js/createUser.js"></script>
    </body>
</html>