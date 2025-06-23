<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<div class="header">
    <div class="header-left">
        <span class="logo">Xin chào nhân viên kho</span>
    </div>
    <div class="header-right">
        <span class="notification">
            <i class="fas fa-bell"></i>
        </span>
        <span class="avatar-circle">
            <c:choose>
                <c:when test="${not empty sessionScope.user.fullname}">
                    ${fn:substring(sessionScope.user.fullname, 0, 1)}
                </c:when>
                <c:otherwise>N</c:otherwise>
            </c:choose>
        </span>
    </div>
</div> 