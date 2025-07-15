<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
    <head>
        <title></title>
        <style>
            body {
                font-family: Arial, sans-serif;
                background: #f8d7da;
                color: #721c24;
                margin: 0;
                padding: 0;
            }
            .container {
                text-align: center;
                max-width: 1000px;
                margin: 100px auto;
                background: #fff;
                border-radius: 8px;
                box-shadow: 0 2px 8px rgba(0,0,0,0.1);
                padding: 32px;
                border: 1px solid #f5c6cb;
            }
            h1 {
                color: #721c24;
            }
            .message {
                margin-top: 16px;
                font-size: 18px;
            }
            a {
                color: #0056b3;
                text-decoration: none;
            }
            a:hover {
                text-decoration: underline;
            }
        </style>
    </head>
    <body>
        <div class="container">
            <h1>Bạn không có quyền xem trang này</h1>
            <div class="message">
                <%= request.getAttribute("error") != null ? request.getAttribute("error") : "Chỉ dành cho người có quyền mới được xem." %>
            </div>
            <p><a href="#" class="btn-back" onclick="window.history.back()">
                    Quay về trang chủ
                </a>
            </p>
        </div>
    </body>
</html> 