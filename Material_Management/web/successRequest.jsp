<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <title>Gửi yêu cầu thành công</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body>
<div class="container mt-5">
    <div class="alert alert-success">
        <h4 class="alert-heading">Thành công!</h4>
        <p>Bạn đã gửi yêu cầu vật tư thành công. Yêu cầu của bạn sẽ được giám đốc xem xét phê duyệt.</p>
        <hr>
        <a href="${pageContext.request.contextPath}/createRequest" class="btn btn-primary">Tạo yêu cầu mới</a>
        <a href="${pageContext.request.contextPath}/staffDashboard" class="btn btn-secondary">Về trang chính</a>
    </div>
</div>
    <a href="directorDashboard.jsp"></a>
</body>
</html>