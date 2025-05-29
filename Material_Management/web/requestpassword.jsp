<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <title>Reset Password</title>
        <link rel="stylesheet" href="./css/requestpassword.css">
    </head>
    <body>
        <div class="container">
            <h2>Forgot Password</h2>
            <form action="requestPassword" method="POST">
                <div class="form-group">
                    <label for="email">Email Address</label>
                    <input type="email" name="email" id="email" required placeholder="Enter your email">
                </div>
                <button type="submit" class="btn-submit">Reset Password</button>
            </form>
            <a href="home.jsp" class="back-link">← Back to Login</a>
            <p class="text-danger">${mess}</p>
        </div>
    </body>
</html>
