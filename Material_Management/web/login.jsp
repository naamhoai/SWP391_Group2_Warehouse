<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html lang="en">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Login - Material Management</title>
        <link rel="stylesheet" href="./css/login.css">
    </head>
    <body>
        <div class="container">
            <div class="form-section">
                <h2>Hệ Thống Quản Lý Vật Tư</h2>
                <h4 style="color: #1a3b5d;">Vui lòng đăng nhập để truy cập dữ liệu của bạn</h4>
               <c:if test="${not empty requestScope.mess}">
                   <h4 style="color: red">${requestScope.mess}</h4>
                </c:if>
            
                <form action="login" method="post">
                    <div class="form-group">
                        <label for="gmail">Email:</label>
                        <input type="email" id="gmail" name="gmail" required placeholder="Nhập Email">
                    </div>

                    <div class="form-group">
                        <label for="pass">Mật Khẩu:</label>
                        <input type="password" id="pass" name="pass" required placeholder="Nhập mật khẩu">
                    </div>

                    <button type="submit" class="login-btn">Đăng nhập</button>
                </form>

                <div class="back-to-home">
                    <a href="homepage.jsp"><i class="fas fa-arrow-left"></i> TRỞ VỀ TRANG CHỦ</a>
                </div>
            </div>

            <div class="image-section">
                <div class="overlay-text">
                    <h3>Hệ Thống Quản Lý Vật Tư</h3>
                    <p>Giúp bạn quản lý kho và tài nguyên dễ dàng hơn</p>
                </div>
            </div>
        </div>

        <a href="requestPassword.jsp" class="imgsp">
            <span class="hotline">Hỗ trợ</span>
            <img src="./image/customer-support-1714.png" alt="Customer Support"/>
        </a>

    </body>
</html>
