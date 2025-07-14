<!DOCTYPE html>
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
                <h2>Welcome Back</h2>
                <h3>Sign in to manage your materials</h3>
               <c:if test="${not empty requestScope.mess}">
                   <h4 style="color: red">${requestScope.mess}</h4>
                </c:if>
            
                <form action="login" method="post">
                    <div class="form-group">
                        <label for="gmail">Email</label>
                        <input type="email" id="gmail" name="gmail" required placeholder="Enter your email">
                    </div>

                    <div class="form-group">
                        <label for="pass">Password</label>
                        <input type="password" id="pass" name="pass" required placeholder="Enter your password">
                    </div>





                    <button type="submit" class="login-btn">Sign In</button>
                </form>

                <div class="back-to-home">
                    <a href="homepage.jsp"><i class="fas fa-arrow-left"></i> Back to Homepage</a>
                </div>
            </div>

            <div class="image-section">
                <div class="overlay-text">
                    <h3>Material Management System</h3>
                    <p>Efficiently manage your inventory and resources</p>
                </div>
            </div>
        </div>

        <a href="requestPassword.jsp" class="imgsp">
            <span class="hotline">Support</span>
            <img src="./image/customer-support-1714.png" alt="Customer Support"/>
        </a>

    </body>
</html>
