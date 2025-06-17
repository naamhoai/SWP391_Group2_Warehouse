<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Permission List</title>
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/css/all.min.css">
        <link rel="stylesheet" href="https://fonts.googleapis.com/css2?family=Poppins:wght@400;500;600;700&display=swap">
        <link rel="stylesheet" href="css/sidebar.css">
        <link rel="stylesheet" href="css/footer.css">
        <link rel="stylesheet" href="css/permissionList.css">
        <script src="js/permissionList.js" defer></script>
    </head>
    <body>
        <jsp:include page="sidebar.jsp" />
        <div class="main-content">
            <div class="permission-list-container">
                <div class="header-section">
                    <h2 class="page-title"><i class="fas fa-key"></i> Danh sách quyền hệ thống</h2>
                    <div class="theme-toggle">
                        <button id="themeToggle" aria-label="Toggle theme">
                            <i class="fas fa-moon"></i> <span>Dark</span>
                        </button>
                    </div>
                </div>

                <!-- Role List -->
                <div class="role-list-section">
                    <h3><i class="fas fa-users"></i> Danh sách vai trò</h3>
                    <div class="role-grid">
                        <c:forEach items="${roles}" var="role">
                            <c:if test="${role.roleid >= 2 && role.roleid <= 4}">
                                <div class="role-card" data-role-id="${role.roleid}">
                                    <h4>${role.rolename}</h4>
                                    <a href="userPermission?roleId=${role.roleid}" class="action-btn">
                                        <i class="fas fa-edit"></i> Chỉnh sửa quyền
                                    </a>
                                </div>
                            </c:if>
                        </c:forEach>
                    </div>
                </div>

                <div class="controls-section">
                    <div class="search-box">
                        <input type="text" id="searchInput" placeholder="Tìm kiếm quyền..." aria-label="Search permissions">
                    </div>
                    <div class="filter-box">
                        <select id="moduleFilter" aria-label="Filter by module">
                            <option value="">Tất cả module</option>
                            <option value="category">Quản lý danh mục</option>
                            <option value="inventory">Quản lý kho</option>
                            <option value="order">Quản lý đơn hàng</option>
                            <option value="delivery">Quản lý giao hàng</option>
                            <option value="user">Quản lý người dùng</option>
                        </select>
                    </div>
                    <div class="sort-box">
                        <select id="sortSelect" aria-label="Sort permissions">
                            <option value="name_asc">Tên (A-Z)</option>
                            <option value="name_desc">Tên (Z-A)</option>
                            <option value="module_asc">Module (A-Z)</option>
                            <option value="module_desc">Module (Z-A)</option>
                        </select>
                    </div>
                </div>

                <div class="permission-grid" id="permissionGrid">
                    <c:forEach items="${permissions}" var="module">
                        <div class="permission-card" data-module="${module.key}">
                            <h3>
                                <i class="fas ${module.key == 'category' ? 'fa-tags' : 
                                                module.key == 'inventory' ? 'fa-boxes' : 
                                                module.key == 'order' ? 'fa-shopping-cart' : 
                                                module.key == 'delivery' ? 'fa-truck' : 
                                                'fa-users'}"></i>
                                   ${module.key == 'category' ? 'Quản lý danh mục' : 
                                     module.key == 'inventory' ? 'Quản lý kho' : 
                                     module.key == 'order' ? 'Quản lý đơn hàng' : 
                                     module.key == 'delivery' ? 'Quản lý giao hàng' : 
                                     'Quản lý người dùng'}
                                </h3>
                                <ul class="permission-list">
                                    <c:forEach items="${module.value}" var="permission">
                                        <li class="permission-item" data-permission-name="${permission.permissionName}">
                                            <i class="fas fa-check-circle"></i>
                                            <div class="permission-name">
                                                ${permission.permissionName}
                                                <div class="permission-description">
                                                    ${permission.description}
                                                </div>
                                            </div>
                                        </li>
                                    </c:forEach>
                                </ul>
                            </div>
                        </c:forEach>
                    </div>
                </div>
                <jsp:include page="footer.jsp" />
            </div>
        </body>
    </html>