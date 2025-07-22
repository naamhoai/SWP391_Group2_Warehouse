document.addEventListener("DOMContentLoaded", function () {
    const form = document.querySelector("form");
    
    var fullnameInput = document.getElementById("fullname");
    if (fullnameInput) fullnameInput.addEventListener("input", validateFullName);

    var phoneInput = document.getElementById("phone");
    if (phoneInput) phoneInput.addEventListener("input", validatePhone);

    var dayofbirthInput = document.getElementById("dayofbirth");
    if (dayofbirthInput) dayofbirthInput.addEventListener("change", validateDayOfBirth);

    var genderInput = document.getElementById("gender");
    if (genderInput) genderInput.addEventListener("change", validateGender);

    var imageFileInput = document.getElementById("imageFile");
    if (imageFileInput) imageFileInput.addEventListener("change", previewAvatar);

    if (form) {
        form.addEventListener("submit", function (event) {
            let isValid = true;
            document.querySelectorAll(".error-message").forEach(el => el.textContent = "");
            if (fullnameInput && !validateFullName())
                isValid = false;
            if (phoneInput && !validatePhone())
                isValid = false;
            if (genderInput && !validateGender())
                isValid = false;
            if (dayofbirthInput && !validateDayOfBirth())
                isValid = false;
            const imageFile = imageFileInput ? imageFileInput.files[0] : null;
            if (imageFile) {
                if (!imageFile.type.startsWith('image/')) {
                    showError("imageFile", "Vui lòng chọn file ảnh hợp lệ.");
                    isValid = false;
                } else if (imageFile.size > 5 * 1024 * 1024) {
                    showError("imageFile", "Kích thước file không được vượt quá 5MB.");
                    isValid = false;
                }
            }
            if (!isValid) {
                event.preventDefault();
            }
        });
    }
});

function showError(fieldId, message) {
    const field = document.getElementById(fieldId);
    let errorDiv = field && field.parentNode ? field.parentNode.querySelector('.error-message') : null;
    if (!errorDiv && field && field.parentNode) {
        errorDiv = document.createElement('div');
        errorDiv.className = 'error-message';
        field.parentNode.appendChild(errorDiv);
    }
    if (errorDiv) {
        errorDiv.textContent = message;
        errorDiv.style.color = 'red';
        errorDiv.style.fontSize = '12px';
        errorDiv.style.marginTop = '5px';
    }
}

function hideError(fieldId) {
    const field = document.getElementById(fieldId);
    let errorDiv = field && field.parentNode ? field.parentNode.querySelector('.error-message') : null;
    if (errorDiv) {
        errorDiv.textContent = '';
    }
}

function validateFullName() {
    const fullNameInput = document.getElementById("fullname");
    const originalInput = document.getElementById("originalFullname");
    if (!fullNameInput) return true;
    const fullName = fullNameInput.value.trim();
    const original = originalInput ? originalInput.value.trim() : "";
    if (originalInput && fullName === original) {
        hideError("fullname");
        return true;
    }
    if (!fullName || fullName.length > 100 || !/^[a-zA-ZÀ-ỹ\s]+$/.test(fullName)) {
        showError("fullname", "Họ và tên không hợp lệ.");
        return false;
    }
    const words = fullName.split(/\s+/);
    if (words.length < 2) {
        showError("fullname", "Họ và tên phải có ít nhất 2 từ.");
        return false;
    }
    for (let word of words) {
        if (word && word[0] !== word[0].toUpperCase()) {
            showError("fullname", "Mỗi từ trong họ tên phải viết hoa chữ cái đầu.");
            return false;
        }
    }
    hideError("fullname");
    return true;
}

function validatePhone() {
    const phoneInput = document.getElementById("phone");
    if (!phoneInput) return true;
    const phone = phoneInput.value.trim();
    if (phone && (phone.length < 10 || phone.length > 11 || !/^0[0-9]{9,10}$/.test(phone))) {
        showError("phone", "Vui lòng nhập số điện thoại hợp lệ (bắt đầu bằng 0, 10-11 số).");
        return false;
    }
    hideError("phone");
    return true;
}

function validateDayOfBirth() {
    const dayofbirthInput = document.getElementById("dayofbirth");
    if (!dayofbirthInput) return true;
    const dayofbirth = dayofbirthInput.value;
    if (!dayofbirth) {
        showError("dayofbirth", "Ngày sinh không được để trống.");
        return false;
    }
    const dob = new Date(dayofbirth);
    const today = new Date();
    const minDate = new Date();
    minDate.setFullYear(today.getFullYear() - 120);
    if (dob > today) {
        showError("dayofbirth", "Ngày sinh không được lớn hơn ngày hiện tại.");
        return false;
    }
    if (dob < minDate) {
        showError("dayofbirth", "Tuổi không được vượt quá 120.");
        return false;
    }
    const age = today.getFullYear() - dob.getFullYear();
    const m = today.getMonth() - dob.getMonth();
    const d = today.getDate() - dob.getDate();
    const isUnder18 = (age < 18) || (age === 18 && (m < 0 || (m === 0 && d < 0)));
    if (isUnder18) {
        showError("dayofbirth", "Người dùng phải đủ 18 tuổi trở lên.");
        return false;
    }
    hideError("dayofbirth");
    return true;
}

function validateGender() {
    const genderInput = document.getElementById("gender");
    if (!genderInput) return true;
    const gender = genderInput.value;
    if (!gender) {
        showError("gender", "Giới tính là bắt buộc.");
        return false;
    }
    hideError("gender");
    return true;
}

function previewAvatar(event) {
    const [file] = event.target.files;
    const preview = document.getElementById('avatarPreview');
    if (file) {
        if (!file.type.startsWith('image/')) {
            alert("Vui lòng chọn file ảnh hợp lệ.");
            return;
        }
        if (file.size > 5 * 1024 * 1024) {
            alert("Kích thước file không được vượt quá 5MB.");
            return;
        }
        if (preview) {
            preview.src = URL.createObjectURL(file);
            preview.style.display = 'block';
        }
    } else if (preview) {
        preview.style.display = 'none';
        preview.src = '#';
    }
} 