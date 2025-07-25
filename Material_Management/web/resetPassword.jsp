<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Đặt lại mật khẩu</title>
    <link rel="stylesheet" href="./css/resetpassword.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/css/all.min.css">
</head>
<body>
    <div class="container">
        <h2>Đặt lại mật khẩu</h2>
        <form action="resetPassword" method="POST">
            <div class="form-group">
                <label for="fullname">Họ và tên</label>
                <input type="text" name="fullname" id="fullname" value="${fullname != null ? fullname : ''}" required placeholder="Nhập họ và tên" readonly>
            </div>
            <div class="form-group">
                <label for="email">Địa chỉ Email</label>
                <input type="email" name="email" id="email" value="${email}" required placeholder="Nhập email của bạn" readonly>
            </div>
            <div class="form-group">
                <label for="password">Mật khẩu mới</label>
                <div class="password-wrapper">
                    <input type="password" name="password" id="password" required placeholder="Nhập mật khẩu mới">
                    <span class="toggle-password" onclick="togglePassword('password', this)"><i class="fas fa-eye"></i></span>
                </div>
            </div>
            <div class="form-group">
                <label for="confirm_password">Xác nhận mật khẩu</label>
                <div class="password-wrapper">
                    <input type="password" name="confirm_password" id="confirm_password" required placeholder="Nhập lại mật khẩu mới">
                    <span class="toggle-password" onclick="togglePassword('confirm_password', this)"><i class="fas fa-eye"></i></span>
                </div>
            </div>
            <button type="submit" class="btn-submit">Đặt lại mật khẩu</button>
            <div class="back-link-container">
                <a href="adminDashboard.jsp" class="back-link">← Về trang chủ</a>
            </div>
            <p class="text-danger">${mess}</p>
        </form>
    </div>
</body>
<script>
function togglePassword(fieldId, el) {
    var input = document.getElementById(fieldId);
    var icon = el.querySelector('i');
    if (input.type === 'password') {
        input.type = 'text';
        icon.classList.remove('fa-eye');
        icon.classList.add('fa-eye-slash');
    } else {
        input.type = 'password';
        icon.classList.remove('fa-eye-slash');
        icon.classList.add('fa-eye');
    }
}
</script>
</html>
