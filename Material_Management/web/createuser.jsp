<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ page import="java.io.File" %>

<!DOCTYPE html>
<html lang="vi">
    <head>
        <meta charset="UTF-8" />
        <title>Add New User</title>
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/createuser.css" />
        <link rel="stylesheet" href="css/footer.css">
        <link rel="stylesheet" href="css/dashboard.css">
        <link rel="stylesheet" href="css/sidebar.css">
        <!-- Font Awesome -->
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/css/all.min.css">
    </head>
    <body>
        <div class="layout">
            <!-- Include Sidebar -->
            <jsp:include page="side.jsp" />
            <div class="main-content">
                <div class="form-wrapper">
                    <div class="page-header">
                        <h2 class="form-title">Add New User</h2>

                        <c:if test="${not empty error}">
                            <div class="alert error">${error}</div>
                        </c:if>



                        <form action="${pageContext.request.contextPath}/CreateUserServlet" method="post" enctype="multipart/form-data">

                            <div class="row file-upload">
                                <label for="imageFile" class="file-upload-label">Avatar:</label>
                                <input type="file" id="imageFile" name="imageFile" class="file-upload-input" accept="image/*" />
                            </div>

                            <div class="row">
                                <div class="column">
                                    <label for="fullName">Full Name:</label>
                                    <input type="text" id="fullName" name="fullName" required 
                                           value="${fn:escapeXml(fullName)}" oninput="generateEmail()" />
                                </div>
                                <div class="column">
                                    <label for="username">User Name:</label>
                                    <input type="text" id="username" name="username" required 
                                           value="${fn:escapeXml(username)}" />
                                </div>
                            </div>

                            <div class="row">
                                <div class="column">
                                    <label for="password">Password:</label>
                                    <input type="password" id="password" name="password" required />
                                </div>
                                <div class="column">
                                    <label for="gender">Gender</label>
                                    <select id="gender" name="gender">
                                        <option value="">Choose gender</option>
                                        <option value="Men" <c:if test="${gender == 'Men'}">selected</c:if>>Nam</option>
                                        <option value="Women" <c:if test="${gender == 'Women'}">selected</c:if>>Nữ</option>
                                        <option value="Other" <c:if test="${gender == 'Other'}">selected</c:if>>Khác</option>
                                        </select>
                                    </div>
                                </div>

                                <div class="row">
                                    <div class="column">
                                        <label for="dayofbirth">Day Of Birth</label>
                                        <input type="date" id="dayofbirth" name="dayofbirth" value="${dayofbirth}" />
                                </div>
                                <div class="column">
                                    <label for="email">Email</label>
                                    <input type="text" id="email" name="email" readonly value="${fn:escapeXml(email)}" />
                                </div>
                            </div>

                            <div class="row">
                                <div class="column">
                                    <label for="phone">Phone Number:</label>
                                    <input type="tel" id="phone" name="phone" value="${fn:escapeXml(userPhone)}" />
                                </div>
                                <div class="column">
                                    <label for="roleId">Role</label>
                                    <select id="roleId" name="roleId">
                                        <option value="">Choose role</option>
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
                                    <label class="status-label">Status</label>
                                    <div class="status-group">
                                        <label>
                                            <input type="radio" name="status" value="active" 
                                                   <c:if test="${status == 'active' || status == null}">checked</c:if> /> Active
                                            </label>
                                            <label>
                                                <input type="radio" name="status" value="inactive" 
                                                <c:if test="${status == 'inactive'}">checked</c:if> /> Inactive
                                            </label>
                                        </div>
                                    </div>
                                    <div class="column">
                                        <label for="priority">Priority:</label>
                                        <input type="number" id="priority" name="priority" min="0" value="${priority}" />
                                </div>
                            </div>

                            <div class="row full-width">
                                <label for="description">Description:</label>
                                <a href="../src/java/dao/UserDAO.java"></a>
                                <textarea id="description" name="description">${fn:escapeXml(description)}</textarea>
                            </div>

                            <div class="buttons">
                                <a href="${pageContext.request.contextPath}/UserDetailServlet" class="btn back-btn">Back</a>
                                <button type="submit" class="btn save-btn">Add User</button>
                            </div>


                        </form>
                    </div>
                </div>
            </div>
        </div>
    </body>
</html>
