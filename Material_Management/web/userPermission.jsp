<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <c:if test="${not empty error}">
            <meta name="error-message" content="${error}">
        </c:if>
        <c:if test="${not empty success}">
            <meta name="success-message" content="${success}">
        </c:if>
        <title>Role Permission Management</title>
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/css/all.min.css">
        <link rel="stylesheet" href="https://fonts.googleapis.com/css2?family=Poppins:wght@400;500;600;700&display=swap">
        <link rel="stylesheet" href="css/sidebar.css">
        <link rel="stylesheet" href="css/userPermission.css">
        <link rel="stylesheet" href="css/footer.css">
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/toastify-js/1.11.2/toastify.min.css">
        <script src="https://cdnjs.cloudflare.com/ajax/libs/toastify-js/1.11.2/toastify.min.js"></script>
        <script src="js/userPermission.js" defer></script>
    </head>
    <body>
        
        <jsp:include page="sidebar.jsp"/>
                     
        <div class="main-content">
            <div class="permission-container">
                <div class="header-section">
                    <h2 class="page-title"><i class="fas fa-user-shield"></i> Phân quyền vai trò</h2>

                    <div class="theme-toggle">
                        <a href="permissionLogs.jsp" class="history-btn" aria-label="View history">
                            <i class="fas fa-history"></i> Xem lịch sử
                        </a>
                        <button id="themeToggle" aria-label="Toggle theme">
                            <i class="fas fa-moon"></i> Dark Mode
                        </button>
                    </div>
                </div>

                <!-- Role Permission Form -->
                <c:if test="${not empty role}">
                    <div class="user-info-card">
                        <h3><i class="fas fa-shield-alt"></i> Vai trò: ${role.rolename}</h3>
                        <c:choose>
                            <c:when test="${role.roleid == 2}">
                                <p class="role-description"><i class="fas fa-info-circle"></i> Director Role: Có thể xem tất cả dữ liệu và quản lý các hoạt động cấp cao</p>
                            </c:when>
                            <c:when test="${role.roleid == 3}">
                                <p class="role-description"><i class="fas fa-info-circle"></i> Warehouse Staff Role: Có thể quản lý kho và xử lý giao hàng</p>
                            </c:when>
                            <c:when test="${role.roleid == 4}">
                                <p class="role-description"><i class="fas fa-info-circle"></i> Employee Role: Quyền truy cập cơ bản để xem và quản lý dữ liệu của chính họ</p>
                            </c:when>
                        </c:choose>
                    </div>

                    <form id="permissionForm" action="savePermissions" method="POST" class="permission-form">
                        <input type="hidden" name="roleId" value="${role.roleid}">
                        <div class="permission-grid">
                            <c:forEach items="${['category','inventory','order','delivery','user']}" var="module">
                                <div class="permission-card">
                                    <h4>
                                        <i class="fas ${module == 'category' ? 'fa-tags' : module == 'inventory' ? 'fa-warehouse' : module == 'order' ? 'fa-shopping-cart' : module == 'delivery' ? 'fa-truck' : 'fa-users'}"></i>
                                        Quản lý ${module == 'category' ? 'Danh mục' : module == 'inventory' ? 'Kho' : module == 'order' ? 'Đơn hàng' : module == 'delivery' ? 'Giao hàng' : 'Người dùng'}
                                    </h4>
                                    <div class="permission-options">
                                        <c:forEach items="${['view','add','edit','delete']}" var="action">
                                            <c:set var="permissionKey" value="${module}_${action}" />
                                            <label class="permission-option">
                                                <input type="checkbox" name="${permissionKey}" value="true"
                                                       ${rolePermissions[permissionKey] ? 'checked' : ''}>
                                                <span>
                                                    <i class="fas ${action == 'view' ? 'fa-eye' : action == 'add' ? 'fa-plus' : action == 'edit' ? 'fa-edit' : 'fa-trash'}"></i>
                                                    ${action == 'view' ? 'Xem' : action == 'add' ? 'Thêm' : action == 'edit' ? 'Sửa' : 'Xóa'}
                                                </span>
                                            </label>
                                        </c:forEach>
                                    </div>
                                </div>
                            </c:forEach>
                        </div>
                        <div class="form-actions">
                            <button type="submit" class="save-btn" id="saveBtn">
                                <i class="fas fa-save"></i> Lưu phân quyền
                            </button>
                            <a href="permissionList" class="back-btn" aria-label="Back to permission list">
                                <i class="fas fa-arrow-left"></i> Quay lại
                            </a>
                        </div>
                    </form>
                </c:if>
            </div>

            <div class="loading" id="loadingOverlay" style="display: none;">
                <div class="loading-spinner">
                    <i class="fas fa-spinner fa-spin"></i> Đang xử lý...
                </div>
            </div>

            <jsp:include page="footer.jsp" />
            
        </div>
    </body>
</html>
