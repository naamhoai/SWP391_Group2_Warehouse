<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ page import="java.io.File" %>

<!DOCTYPE html>
<html lang="vi">
    <head>
        <meta charset="UTF-8" />
        <title>Tạo người dùng mới</title>
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/createUser.css" />
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/css/all.min.css">
        
    </head>
    <body>
        <div class="layout">
            <!-- Include Sidebar -->
            <div class="main-content">
                <div class="form-wrapper">
                    <div class="page-header">
                        <h2 class="form-title">Tạo người dùng mới</h2>

                        <c:if test="${not empty error}">
                            <div class="alert error">${error}</div>
                        </c:if>

                        <form action="${pageContext.request.contextPath}/CreateUserServlet" method="post" enctype="multipart/form-data" id="createUserForm">

                            <div class="row file-upload">
                                <label for="imageFile" class="file-upload-label">Avatar:</label>
                                <input type="file" id="imageFile" name="imageFile" class="file-upload-input" accept="image/*" />
                                <div class="error-message" id="imageError"></div>
                            </div>

                            <div class="row">
                                <div class="column">
                                    <label for="fullName">Họ và tên: <span class="required">*</span></label>
                                    <input type="text" id="fullName" name="fullName" required 
                                           value="${fn:escapeXml(fullName)}" oninput="generateEmail()" />
                                    <div class="error-message" id="fullNameError"></div>
                                </div>
                                
                            </div>

                            <div class="row">
                                <div class="column">
                                    <label for="password">Mật khẩu: <span class="required">*</span></label>
                                    <div style="position: relative;">
                                        <input type="password" id="password" name="password" required value="${fn:escapeXml(password)}" />
                                        <span class="toggle-password" onclick="togglePassword()" style="position:absolute;top:50%;right:10px;transform:translateY(-50%);cursor:pointer;">
                                            <i id="eyeIcon" class="fas fa-eye"></i>
                                        </span>
                                    </div>
                                    <div class="error-message" id="passwordError"></div>
                                    <div class="password-requirements">
                                        <small>Mật khẩu phải có ít nhất 8 ký tự, bao gồm chữ hoa, chữ thường, số và ký tự đặc biệt</small>
                                    </div>
                                </div>
                                <div class="column">
                                    <label for="gender">Giới tính: <span class="required">*</span></label>
                                    <select id="gender" name="gender">
                                        <option value="">Chọn giới tính</option>
                                        <option value="Men" <c:if test="${gender == 'Men'}">selected</c:if>>Nam</option>
                                        <option value="Women" <c:if test="${gender == 'Women'}">selected</c:if>>Nữ</option>
                                        <option value="Other" <c:if test="${gender == 'Other'}">selected</c:if>>Khác</option>
                                        </select>
                                    <div class="error-message" id="genderError"></div>
                                    </div>
                                </div>

                                <div class="row">
                                    <div class="column">
                                        <label for="dayofbirth">Ngày sinh: <span class="required">*</span></label>
                                        <input type="date" id="dayofbirth" name="dayofbirth" value="${dayofbirth}" />
                                        <div class="error-message" id="dayofbirthError"></div>
                                </div>
                                <div class="column">
                                    <label for="email">Email: <span class="required">*</span></label>
                                    <input type="email" id="email" name="email" value="${fn:escapeXml(email)}" readonly />
                                    <div class="error-message" id="emailError"></div>
                                    <div class="email-info">
                                        <small>Email sẽ được tạo tự động từ họ và tên</small>
                                    </div>
                                </div>
                            </div>

                            <div class="row">
                                <div class="column">
                                    <label for="phone">Số điện thoại:</label>
                                    <input type="tel" id="phone" name="phone" value="${fn:escapeXml(phone)}" placeholder="VD: 0123456789" />
                                    <div class="error-message" id="phoneError"></div>
                                </div>
                                <div class="column">
                                    <label for="roleId">Vai trò: <span class="required">*</span></label>
                                    <select id="roleId" name="roleId">
                                        <option value="">Chọn vai trò</option>
                                        <c:forEach var="role" items="${roleList}">
                                            <option value="${role.roleid}" <c:if test="${role.roleid == roleId}">selected</c:if>>
                                                ${role.rolename}
                                            </option>
                                        </c:forEach>
                                    </select>
                                    <div class="error-message" id="roleIdError"></div>
                                </div>
                            </div>

                            <div class="row">
                                <div class="column">
                                    <label class="status-label">Trạng thái:</label>
                                    <div class="status-group">
                                        <label>
                                            <input type="radio" name="status" value="active" 
                                                   <c:if test="${status == 'active' || status == null}">checked</c:if> /> Active
                                            </label>
                                            
                                        </div>
                                    </div>
                                    <div class="column">
                                        <label for="priority">Mức ưu tiên:</label>
                                        <input type="number" id="priority" name="priority" min="2" max="4" value="${priority}" />
                                        <div class="error-message" id="priorityError"></div>
                                    </div>
                                </div>
                            </div>

                            <div class="row full-width">
                                <label for="description">Mô tả:</label>
                                <textarea id="description" name="description" maxlength="500">${fn:escapeXml(description)}</textarea>
                                <div class="error-message" id="descriptionError"></div>
                                <div class="char-count">
                                    <small><span id="charCount">0</span>/500 ký tự</small>
                                </div>
                            </div>

                            <div class="buttons">
                                <a href="${pageContext.request.contextPath}/settinglist" class="btn back-btn">Quay lại</a>
                                <button type="submit" class="btn save-btn">Add User</button>
                            </div>

                        </form>
                    </div>
                </div>
            </div>
        </div>

        <script>
            // Function to remove accents from Vietnamese text
            function removeAccent(str) {
                if (str == null) return null;
                return str
                    .normalize('NFD')
                    .replace(/[\u0300-\u036f]/g, '')
                    .replace(/đ/g, 'd').replace(/Đ/g, 'D');
            }

            // Function to generate email from full name
            function generateEmail() {
                const fullName = document.getElementById('fullName').value.trim();
                if (fullName.length >= 2) {
                    // Chuẩn hóa tên, loại bỏ khoảng trắng thừa
                    const normalizedName = fullName.replace(/\s+/g, ' ');
                    const nameParts = normalizedName.split(' ');
                    
                    if (nameParts.length >= 2) {
                        // Lấy họ (phần cuối cùng)
                        const lastName = nameParts[nameParts.length - 1];
                        const lastNameNoAccent = removeAccent(lastName);
                        // Họ: viết hoa chữ cái đầu, còn lại viết thường
                        const formattedLastName = lastNameNoAccent.substring(0, 1).toUpperCase() + 
                                                 lastNameNoAccent.substring(1).toLowerCase();
                        
                        // Tạo chữ cái đầu của tên (các phần trước họ)
                        let initials = '';
                        for (let i = 0; i < nameParts.length - 1; i++) {
                            const partNoAccent = removeAccent(nameParts[i]);
                            if (partNoAccent && partNoAccent.length > 0) {
                                initials += partNoAccent.substring(0, 1).toUpperCase();
                            }
                        }
                        
                        // Tạo số ngẫu nhiên 6 chữ số
                        const randomNum = Math.floor(100000 + Math.random() * 900000);
                        
                        // Tạo email
                        const email = formattedLastName + initials + randomNum + '@gmail.com';
                        document.getElementById('email').value = email;
                    }
                }
            }

            // Initialize character count and generate email if needed
            document.addEventListener('DOMContentLoaded', function() {
                const description = document.getElementById('description');
                const charCount = document.getElementById('charCount');
                
                // Update character count on input
                description.addEventListener('input', function() {
                    charCount.textContent = this.value.length;
                });
                
                // Initialize character count
                charCount.textContent = description.value.length;
                
                // Generate email if fullName is already filled (from previous submission)
                const fullName = document.getElementById('fullName').value.trim();
                if (fullName.length >= 2) {
                    generateEmail();
                }
                
                // Set priority based on selected role when page loads (for error cases)
                const roleSelect = document.getElementById('roleId');
                const priorityInput = document.getElementById('priority');
                if (roleSelect && priorityInput && roleSelect.value) {
                    const selectedRole = roleSelect.value;
                    if (rolePriorityMap[selectedRole]) {
                        priorityInput.value = rolePriorityMap[selectedRole];
                    }
                }
            });


            document.addEventListener('DOMContentLoaded', function() {
                const roleSelect = document.getElementById('roleId');
                const priorityInput = document.getElementById('priority');
                if (roleSelect && priorityInput) {
                    roleSelect.addEventListener('change', function() {
                        const selectedRole = roleSelect.value;
                        if (rolePriorityMap[selectedRole]) {
                            priorityInput.value = rolePriorityMap[selectedRole];
                        } else {
                            priorityInput.value = '';
                        }
                    });
                }
            });

            function togglePassword() {
                var pwd = document.getElementById('password');
                var icon = document.getElementById('eyeIcon');
                if (pwd.type === 'password') {
                    pwd.type = 'text';
                    icon.classList.remove('fa-eye');
                    icon.classList.add('fa-eye-slash');
                } else {
                    pwd.type = 'password';
                    icon.classList.remove('fa-eye-slash');
                    icon.classList.add('fa-eye');
                }
            }
        </script>
    </body>
</html>
