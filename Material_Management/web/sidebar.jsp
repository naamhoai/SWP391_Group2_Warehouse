<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page import="jakarta.servlet.http.HttpServletRequest" %>


<html lang="vi">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <<link rel="stylesheet" href="css/sidebar.css"/>
    </head>
    
    <body>
        <%
            String dashboardPath = "";
            Integer roleId = (Integer) session.getAttribute("role_id");
            if (roleId != null) {
                switch (roleId) {
                    case 1: dashboardPath = "/adminDashboard"; break;
                    case 2: dashboardPath = "/director"; break;
                    case 3: dashboardPath = "/warehouseStaffDashboard.jsp"; break;
                    case 4: dashboardPath = "/staffDashboard.jsp"; break;
                    default: dashboardPath = "/adminDashboard"; 
                }
            }
        %>

        <div class="sidebar">
            <ul class="sidebar-menu">
                <li class="menu-item">
                    <a href="${pageContext.request.contextPath}<%=dashboardPath%>" class="menu-link${pageContext.request.servletPath == dashboardPath ? ' active' : ''}">
                        <i class="fas fa-tachometer-alt menu-icon"></i>
                        <span class="menu-text">Bảng Điều Khiển</span>
                    </a>
                </li>
                
                <% if (roleId != null && roleId == 3) { %>
                <li class="menu-item">
                    <a href="${pageContext.request.contextPath}/CreateExportFormServlet" class="menu-link">
                        <i class="fas fa-plus-circle menu-icon"></i>
                        <span class="menu-text">Tạo đơn xuất kho</span>
                    </a>
                </li>
                <% } %>
                <c:if test="${not empty sidebarMenuList}">
                    <c:forEach var="menu" items="${sidebarMenuList}">
                        <c:if test="${menu.parentId == null && menu.menuName != 'Bảng Điều Khiển'}">
                            <li class="menu-item">
                                <a href="${pageContext.request.contextPath}${menu.url}"
                                   class="menu-link${pageContext.request.servletPath == menu.url ? ' active' : ''}">
                                    <i class="${menu.icon != null ? menu.icon : 'fas fa-circle'} menu-icon"></i>
                                    <span class="menu-text">${menu.menuName}</span>
                                </a>
                                <c:if test="${not empty sidebarMenuList}">
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
                                </c:if>
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
    </body>
</html> 
