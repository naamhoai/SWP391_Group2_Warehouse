<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Reset Password</title>
    <link rel="stylesheet" href="./css/resetpassword.css">
</head>
<body>
    <div class="container">
        <h2>Reset Password</h2>
        <form action="resetPassword" method="POST">
            <div class="form-group">
                <label for="email">Email Address</label>
                <input type="email" name="email" id="email" value="${email}" required placeholder="Enter your email">
            </div>
            <div class="form-group">
                <label for="password">New Password</label>
                <input type="password" name="password" id="password" required placeholder="Enter new password">
            </div>
            <div class="form-group">
                <label for="confirm_password">Confirm Password</label>
                <input type="password" name="confirm_password" id="confirm_password" required placeholder="Confirm new password">
            </div>
            <button type="submit" class="btn-submit">Reset Password</button>
            <div class="back-link-container">
                <a href="home.jsp" class="back-link">← Back to Home</a>
            </div>
            <p class="text-danger">${mess}</p>
        </form>
    </div>
</body>
</html>
