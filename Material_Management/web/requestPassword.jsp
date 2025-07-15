<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <title>Yêu cầu đặt lại mật khẩu</title>
        <link rel="stylesheet" href="./css/requestpassword.css">
    </head>
    <body>
        <div class="container">
            <h2>Yêu cầu đặt lại mật khẩu</h2>
            <form action="requestPassword" method="POST">
                <div class="form-group">
                    <label for="email">Địa chỉ Email</label>
                    <input type="email" name="email" id="email" class="form-control" required placeholder="Nhập email của bạn">
                </div>
                <button type="submit" class="btn-submit">Gửi yêu cầu</button>
            </form>
            <a href="login.jsp" class="back-link">← Quay lại đăng nhập</a>
            <p class="text-danger">${mess}</p>
        </div>
    </body>
</html>
