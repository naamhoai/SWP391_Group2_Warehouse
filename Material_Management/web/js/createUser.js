document.addEventListener("DOMContentLoaded", function () {
    console.log("[LOG] DOMContentLoaded - JS loaded");
    const form = document.getElementById("createUserForm");
    var fullNameInput = document.getElementById("fullName");
    if (fullNameInput) fullNameInput.addEventListener("input", validateFullName);

    var passwordInput = document.getElementById("password");
    if (passwordInput) passwordInput.addEventListener("input", validatePassword);

    var phoneInput = document.getElementById("phone");
    if (phoneInput) {
        phoneInput.addEventListener("input", validatePhone);
        phoneInput.addEventListener("input", function() {
            this.value = this.value.replace(/[^0-9]/g, '');
        });
    }

    var genderInput = document.getElementById("gender");
    if (genderInput) genderInput.addEventListener("change", validateGender);

    var imageFileInput = document.getElementById("imageFile");
    if (imageFileInput) imageFileInput.addEventListener("change", previewAvatar);

    updateCharCount();

    if (form) {
        form.addEventListener("submit", function (event) {
            let isValid = true;
            document.querySelectorAll(".error-message").forEach(el => el.textContent = "");
            if (!validateFullName())
                isValid = false;
            if (!validatePassword())
                isValid = false;
            if (!validatePhone())
                isValid = false;
            if (!validateGender())
                isValid = false;
            const roleId = document.getElementById("roleId").value;
            const roleIdError = document.getElementById("roleIdError");
            roleIdError.textContent = "";
            if (!roleId) {
                roleIdError.textContent = "Vai trò là bắt buộc.";
                isValid = false;
            }
            
            const imageFile = document.getElementById("imageFile").files[0];
            const imageError = document.getElementById("imageError");
            imageError.textContent = "";
            if (imageFile) {
                if (!imageFile.type.startsWith('image/')) {
                    imageError.textContent = "Vui lòng chọn file ảnh hợp lệ.";
                    isValid = false;
                } else if (imageFile.size > 5 * 1024 * 1024) {
                    imageError.textContent = "Kích thước file không được vượt quá 5MB.";
                    isValid = false;
                }
            }

            if (!isValid) {
                event.preventDefault();
            }
        });
    }
});

function validateFullName() {
    const fullName = document.getElementById("fullName").value.trim();
    const errorEl = document.getElementById("fullNameError");
    errorEl.textContent = "";
    if (!fullName || fullName.length > 100 || !/^[a-zA-ZÀ-ỹ\s]+$/.test(fullName)) {
        errorEl.textContent = "Họ và tên không hợp lệ.";
        return false;
    }
    const words = fullName.split(/\s+/);
    if (words.length < 2) {
        errorEl.textContent = "Họ và tên phải có ít nhất 2 từ.";
        return false;
    }
    for (let word of words) {
        if (word && word[0] !== word[0].toUpperCase()) {
            errorEl.textContent = "Mỗi từ trong họ tên phải viết hoa chữ cái đầu.";
            return false;
        }
    }
    return true;
}

function validatePassword() {
    const password = document.getElementById("password").value.trim();
    const errorEl = document.getElementById("passwordError");
    errorEl.textContent = "";
    if (!password || password.length < 8 || password.length > 50 || !/^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[@#$%^&+=!]).{8,50}$/.test(password)) {
        errorEl.textContent = "Mật khẩu không hợp lệ.";
        return false;
    }
    return true;
}

function validatePhone() {
    const phone = document.getElementById("phone").value.trim();
    const errorEl = document.getElementById("phoneError");
    errorEl.textContent = "";
    
    // Nếu không nhập số điện thoại thì không bắt buộc
    if (!phone) {
        return true;
    }
    
    // Kiểm tra chỉ chứa số
    if (!/^[0-9]+$/.test(phone)) {
        errorEl.textContent = "Số điện thoại chỉ được chứa số.";
        return false;
    }
    
    // Kiểm tra độ dài
    if (phone.length < 10) {
        errorEl.textContent = "Số điện thoại phải có ít nhất 10 chữ số.";
        return false;
    }
    
    if (phone.length > 11) {
        errorEl.textContent = "Số điện thoại không được quá 11 chữ số.";
        return false;
    }
    
    // Kiểm tra bắt đầu bằng số 0
    if (!phone.startsWith('0')) {
        errorEl.textContent = "Số điện thoại phải bắt đầu bằng số 0.";
        return false;
    }
    
    return true;
}

function validateGender() {
    const gender = document.getElementById("gender").value;
    const errorEl = document.getElementById("genderError");
    errorEl.textContent = "";
    if (!gender) {
        errorEl.textContent = "Giới tính là bắt buộc.";
        return false;
    }
    return true;
}

function previewAvatar(event) {
    console.log("[LOG] previewAvatar triggered", event);
    const [file] = event.target.files;
    const errorEl = document.getElementById("imageError");
    errorEl.textContent = "";

    if (file) {
        console.log("[LOG] File selected:", file);
        if (!file.type.startsWith('image/')) {
            errorEl.textContent = "Vui lòng chọn file ảnh hợp lệ.";
            return;
        }

        if (file.size > 5 * 1024 * 1024) {
            errorEl.textContent = "Kích thước file không được vượt quá 5MB.";
            return;
        }

        const preview = document.getElementById('avatarPreview');
        preview.src = URL.createObjectURL(file);
        preview.style.display = 'block';
        console.log("[LOG] Preview src set:", preview.src);
    } else {
        const preview = document.getElementById('avatarPreview');
        preview.style.display = 'none';
        preview.src = '#';
        console.log("[LOG] No file selected, hide preview");
    }
}

function removeAccent(str) {
    if (str == null)
        return null;
    return str
            .normalize('NFD')
            .replace(/[\u0300-\u036f]/g, '')
            .replace(/đ/g, 'd').replace(/Đ/g, 'D');
}

function generateEmail() {
    const fullName = document.getElementById('fullName').value.trim();
    if (fullName.length >= 2) {
        const normalizedName = fullName.replace(/\s+/g, ' ');
        const nameParts = normalizedName.split(' ');

        if (nameParts.length >= 2) {
            const lastName = nameParts[nameParts.length - 1];
            const lastNameNoAccent = removeAccent(lastName);
            const formattedLastName = lastNameNoAccent.substring(0, 1).toUpperCase() +
                    lastNameNoAccent.substring(1).toLowerCase();

            let initials = '';
            for (let i = 0; i < nameParts.length - 1; i++) {
                const partNoAccent = removeAccent(nameParts[i]);
                if (partNoAccent && partNoAccent.length > 0) {
                    initials += partNoAccent.substring(0, 1).toUpperCase();
                }
            }

            const randomNum = Math.floor(100000 + Math.random() * 900000);

            const email = formattedLastName + initials + randomNum + '@gmail.com';
            document.getElementById('email').value = email;
        }
    }
}

document.addEventListener('DOMContentLoaded', function () {
    const fullName = document.getElementById('fullName').value.trim();
    if (fullName.length >= 2) {
        generateEmail();
    }
});

function togglePassword() {
    const passwordInput = document.getElementById("password");
    const eyeIcon = document.getElementById("eyeIcon");

    if (passwordInput.type === "password") {
        passwordInput.type = "text";
        eyeIcon.className = "fas fa-eye-slash";
    } else {
        passwordInput.type = "password";
        eyeIcon.className = "fas fa-eye";
    }
}

function updateCharCount() {
    const description = document.getElementById("description").value;
    const charCount = document.getElementById("charCount");
    charCount.textContent = description.length;
}
