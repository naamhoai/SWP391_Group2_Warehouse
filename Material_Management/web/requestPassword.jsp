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
            <h2>Reset Password</h2>
            <form action="requestPassword" method="POST">
                <div class="form-group">
                    <label for="fullname">Full Name</label>
                    <input type="text" name="fullname" id="fullname" class="form-control" required placeholder="Enter your full name">
                </div>
                <div class="form-group">
                    <label for="email">Email Address</label>
                    <input type="email" name="email" id="email" class="form-control" required placeholder="Enter your email">
                </div>
                <button type="submit" class="btn-submit">Reset Password</button>
            </form>
            <a href="login.jsp" class="back-link">← Back to Login</a>
            <p class="text-danger">${mess}</p>
        </div>
    </body>
</html>
