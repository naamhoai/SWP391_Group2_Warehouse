<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>


<div class="sidebar">

    <ul class="sidebar-menu">
        <c:if test="${not empty sidebarMenuList}">
            <c:forEach var="menu" items="${sidebarMenuList}">
                <c:if test="${menu.parentId == null}">
                    <li class="menu-item">
                        <a href="${pageContext.request.contextPath}${menu.url}"
                           class="menu-link${pageContext.request.servletPath == menu.url ? ' active' : ''}">
                            <i class="${menu.icon != null ? menu.icon : 'fas fa-circle'} menu-icon"></i>
                            <span class="menu-text">${menu.menuName}</span>
                        </a>
                        <ul class="submenu">
                            <c:forEach var="sub" items="${sidebarMenuList}">
                                <c:if test="${sub.parentId == menu.menuId}">
                                    <li>
                                        <a href="${pageContext.request.contextPath}${sub.url}"
                                           class="submenu-link${pageContext.request.servletPath == sub.url ? ' active' : ''}">
                                            <i class="${sub.icon != null ? sub.icon : 'fas fa-dot-circle'} menu-icon"></i>
                                            <span class="menu-text">${sub.menuName}</span>
                                        </a>
                                    </li>
                                </c:if>
                            </c:forEach>
                        </ul>
                    </li>
                </c:if>
            </c:forEach>
        </c:if>
    </ul>

    <div class="sidebar-logout">
        <a href="homepage.jsp" class="logout-link">
            <i class="fa fa-sign-out-alt menu-icon"></i>
            <span class="menu-text">Đăng xuất</span>
        </a>
    </div>
</div>
