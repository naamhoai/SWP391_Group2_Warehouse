<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!-- Welcome Header -->
<div class="welcome-header">
    <div class="welcome-text">
        Xin chào, <strong>${sessionScope.user.fullName != null ? sessionScope.user.fullName : 'Admin'}</strong>!
    </div>
    <div class="user-info">
        <div class="user-avatar">
            ${sessionScope.user.fullName != null ? sessionScope.user.fullName.charAt(0) : 'A'}
        </div>
    </div>
</div> 