<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="vi">
    <head>
        <meta charset="UTF-8" />
        <title>Cập nhật thông tin cá nhân</title>
        <style>
            body {
                font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
                background-color: #f5f7fa;
                margin: 0;
                min-height: 100vh;
                display: flex;
                justify-content: center;
                align-items: center;
                padding: 20px;
            }

            .container {
                background-color: #fff;
                padding: 30px 40px;
                border-radius: 12px;
                box-shadow: 0 8px 24px rgba(0,0,0,0.1);
                max-width: 600px;
                width: 100%;
                box-sizing: border-box;
            }

            h2 {
                margin-bottom: 24px;
                font-weight: 700;
                color: #333;
                text-align: center;
            }

            form {
                display: flex;
                flex-direction: column;
                gap: 16px;
            }

            label {
                font-weight: 600;
                color: #555;
                margin-bottom: 6px;
            }

            input[type="text"],
            input[type="email"],
            input[type="password"],
            input[type="tel"] {
                padding: 10px 14px;
                font-size: 1rem;
                border: 1.5px solid #ccc;
                border-radius: 8px;
                transition: border-color 0.3s ease, box-shadow 0.3s ease;
                width: 100%;
                box-sizing: border-box;
            }

            input[type="text"]:focus,
            input[type="email"]:focus,
            input[type="password"]:focus,
            input[type="tel"]:focus {
                border-color: #4a90e2;
                box-shadow: 0 0 6px rgba(74,144,226,0.5);
                outline: none;
            }

            input[readonly] {
                background-color: #f0f0f0;
                color: #777;
                cursor: not-allowed;
            }

            button[type="submit"] {
                background-color: #4a90e2;
                color: white;
                font-weight: 700;
                padding: 12px 0;
                border: none;
                border-radius: 8px;
                font-size: 1.1rem;
                cursor: pointer;
                transition: background-color 0.3s ease;
                margin-top: 12px;
            }

            button[type="submit"]:hover {
                background-color: #357abd;
            }

            .alert {
                padding: 12px 16px;
                margin-bottom: 20px;
                border-radius: 8px;
                font-weight: 600;
                font-size: 0.95rem;
            }

            .alert.error {
                background-color: #fdecea;
                color: #d93025;
                border: 1px solid #d93025;
            }

            .alert.success {
                background-color: #e6f4ea;
                color: #188038;
                border: 1px solid #188038;
            }
        </style>
    </head>
    <body>
        <div class="container">
            <h2>Cập nhật thông tin cá nhân</h2>

            <c:if test="${not empty error}">
                <div class="alert error">${error}</div>
            </c:if>
            <c:if test="${not empty success}">
                <div class="alert success">${success}</div>
            </c:if>

            <form action="${pageContext.request.contextPath}/updateUserProfileServlet" method="post" enctype="multipart/form-data">
                <label for="imageFile">Ảnh đại diện (upload mới):</label>
                <input type="file" id="imageFile" name="imageFile" accept="image/*" />

                <br/>
                <!-- (Tùy chọn) Chọn ảnh có sẵn -->
                <label for="existingImage">Hoặc chọn ảnh đã upload:</label>
                <select name="existingImage" id="existingImage">
                    <option value="">--Chọn ảnh có sẵn--</option>
                    <%-- Đọc thư mục image và liệt kê các file ảnh --%>
                    <%
                      jakarta.servlet.ServletContext context = application;
                      String imageDirPath = context.getRealPath("image");
                      java.io.File imageDir = new java.io.File(imageDirPath);
                      if(imageDir.exists() && imageDir.isDirectory()) {
                          String[] images = imageDir.list();
                          if(images != null) {
                              for(String img : images) {
                                  if(img.toLowerCase().endsWith(".png") || img.toLowerCase().endsWith(".jpg") ||
                                     img.toLowerCase().endsWith(".jpeg") || img.toLowerCase().endsWith(".gif")) {
                    %>
                    <option value="image/<%=img%>"><%=img%></option>
                    <%
                                  }
                              }
                          }
                      }
                    %>
                </select>

                <input type="hidden" name="user_id" value="${user.user_id}" />

                <label for="username">Tên đăng nhập:</label>
                <input type="text" id="username" name="username" value="${user.username}" required />

                <label for="fullname">Họ và tên:</label>
                <input type="text" id="fullname" name="fullname" value="${user.fullname}" required />

                <label for="email">Email:</label>
                <input type="email" id="email" name="email" value="${user.email}" required />

                <label for="password">Mật khẩu:</label>
                <input type="password" id="password" name="password" value="${user.password}" required />

                <label for="phone">Số điện thoại:</label>
                <input type="tel" id="phone" name="phone" value="${user.phone}" />

                <label for="role_id">Vai trò:</label>
                <input type="text" id="role_id" name="role_id" value="${user.role_id}" readonly />

                <label for="status">Trạng thái:</label>
                <input type="text" id="status" name="status" value="${user.status}" readonly />

                <label for="priority">Mức ưu tiên:</label>
                <input type="text" id="priority" name="priority" value="${user.priority}" readonly />

                <button type="submit">Cập nhật</button>
            </form>
        </div>
    </body>
</html>
