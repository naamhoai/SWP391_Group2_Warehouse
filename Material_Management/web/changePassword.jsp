<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Đổi mật khẩu</title>
    <link rel="stylesheet" href="./css/resetpassword.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
</head>
<body>
    <div class="container">
        <h2>Đổi mật khẩu</h2>
        <form action="changePassword" method="POST" onsubmit="return validateForm();">
            <div class="form-group">
                <label for="old_password">Mật khẩu cũ</label>
                <div class="input-eye-wrapper">
                    <input type="password" name="old_password" id="old_password" required placeholder="Nhập mật khẩu cũ">
                    <span class="toggle-password" toggle="#old_password"><i class="fa-regular fa-eye"></i></span>
                </div>
            </div>
            <div class="form-group">
                <label for="new_password">Mật khẩu mới</label>
                <div class="input-eye-wrapper">
                    <input type="password" name="new_password" id="new_password" required placeholder="Nhập mật khẩu mới">
                    <span class="toggle-password" toggle="#new_password"><i class="fa-regular fa-eye"></i></span>
                </div>
            </div>
            <div class="form-group">
                <label for="confirm_password">Xác nhận mật khẩu mới</label>
                <div class="input-eye-wrapper">
                    <input type="password" name="confirm_password" id="confirm_password" required placeholder="Nhập lại mật khẩu mới">
                    <span class="toggle-password" toggle="#confirm_password"><i class="fa-regular fa-eye"></i></span>
                </div>
            </div>
            <button type="submit" class="btn-submit">Đổi mật khẩu</button>
            <div class="back-link-container">
                <a href="UserDetailServlet?userId=${sessionScope.userId}" class="back-link">← Về trang thông tin</a>
            </div>
            <p class="text-danger" id="error-message">${mess}</p>
        </form>
    </div>
</body>
<script>
function validatePassword(pw) {
    return /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[@#$%^&+=!]).{8,50}$/.test(pw);
}
function validateForm() {
    const oldPass = old_password.value.trim();
    const newPass = new_password.value.trim();
    const confirmPass = confirm_password.value.trim();
    const error = error-message;
    error.textContent = '';
    if (![oldPass, newPass, confirmPass].every(Boolean)) {
        error.textContent = 'Vui lòng nhập đầy đủ các trường.';
        return false;
    }
    if (!validatePassword(newPass)) {
        error.textContent = 'Mật khẩu mới phải 8-50 ký tự, có chữ hoa, chữ thường, số, ký tự đặc biệt.';
        return false;
    }
    if (newPass !== confirmPass) {
        error.textContent = 'Mật khẩu xác nhận không khớp.';
        return false;
    }
    if (oldPass === newPass) {
        error.textContent = 'Mật khẩu mới không được trùng mật khẩu cũ.';
        return false;
    }
    return true;
}
// Hiện/ẩn mật khẩu
const togglePasswordIcons = document.querySelectorAll('.toggle-password');
togglePasswordIcons.forEach(icon => {
    icon.addEventListener('click', function() {
        const input = document.querySelector(this.getAttribute('toggle'));
        if (input.type === 'password') {
            input.type = 'text';
            this.innerHTML = '<i class="fa-regular fa-eye-slash"></i>';
        } else {
            input.type = 'password';
            this.innerHTML = '<i class="fa-regular fa-eye"></i>';
        }
    });
});
</script>
</html> 