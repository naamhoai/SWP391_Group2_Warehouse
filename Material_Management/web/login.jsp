<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
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
                <p>Sign in to manage your materials</p>

                <form action="login" method="post">
                    <div class="form-group">
                        <label for="gmail">Email</label>
                        <input type="email" id="gmail" name="gmail" required placeholder="Enter your email">
                    </div>

                    <div class="form-group">
                        <label for="pass">Password</label>
                        <input type="password" id="pass" name="pass" required placeholder="Enter your password">
                    </div>

                    <c:if test="${not empty requestScope.mess}">
                        <div class="error-message">
                            ${requestScope.mess}
                        </div>
                    </c:if>

                    <button type="submit" class="login-btn">Sign In</button>
                </form>
                <div class="imgsp">
                    <a href="#"><img src="./image/customer-support-1714.png" alt="Customer Support"/></a>
                    <span class="hotline">Hotline: 1800-123-456</span>
                </div>

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
        
    </body>
</html>
