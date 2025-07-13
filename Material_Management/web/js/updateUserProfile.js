document.addEventListener("DOMContentLoaded", function () {
    const form = document.querySelector("form");
    
    // Thêm event listeners cho các trường cần validate
    document.getElementById("fullname").addEventListener("input", validateFullName);
    document.getElementById("phone").addEventListener("input", validatePhone);
    document.getElementById("dayofbirth").addEventListener("change", validateDayOfBirth);
    document.getElementById("gender").addEventListener("change", validateGender);
    
    // Thêm event listener cho input file để xem trước ảnh
    document.getElementById("imageFile").addEventListener("change", previewAvatar);

    form.addEventListener("submit", function (event) {
        let isValid = true;
        
        // Xóa tất cả thông báo lỗi cũ
        document.querySelectorAll(".error-message").forEach(el => el.textContent = "");
        
        // Validate các trường
        if (!validateFullName())
            isValid = false;
        if (!validatePhone())
            isValid = false;
        if (!validateGender())
            isValid = false;
        if (!validateDayOfBirth())
            isValid = false;
        
        // Validation cho file ảnh
        const imageFile = document.getElementById("imageFile").files[0];
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
});

function showError(fieldId, message) {
    const field = document.getElementById(fieldId);
    let errorDiv = field.parentNode.querySelector('.error-message');
    if (!errorDiv) {
        errorDiv = document.createElement('div');
        errorDiv.className = 'error-message';
        field.parentNode.appendChild(errorDiv);
    }
    errorDiv.textContent = message;
    errorDiv.style.color = 'red';
    errorDiv.style.fontSize = '12px';
    errorDiv.style.marginTop = '5px';
}

function hideError(fieldId) {
    const field = document.getElementById(fieldId);
    let errorDiv = field.parentNode.querySelector('.error-message');
    if (errorDiv) {
        errorDiv.textContent = '';
    }
}

function validateFullName() {
    const fullName = document.getElementById("fullname").value.trim();
    const original = document.getElementById("originalFullname").value.trim();
    if (fullName === original) {
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
    const phone = document.getElementById("phone").value.trim();
    if (phone && !/^\d{10,11}$/.test(phone)) {
        showError("phone", "Số điện thoại phải có 10-11 chữ số.");
        return false;
    }
    hideError("phone");
    return true;
}

function validateDayOfBirth() {
    const dayofbirth = document.getElementById("dayofbirth").value;
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
    const gender = document.getElementById("gender").value;
    if (!gender) {
        showError("gender", "Giới tính là bắt buộc.");
        return false;
    }
    hideError("gender");
    return true;
}

function previewAvatar(event) {
    const [file] = event.target.files;
    
    if (file) {
        // Kiểm tra loại file
        if (!file.type.startsWith('image/')) {
            alert("Vui lòng chọn file ảnh hợp lệ.");
            return;
        }
        
        // Kiểm tra kích thước file (giới hạn 5MB)
        if (file.size > 5 * 1024 * 1024) {
            alert("Kích thước file không được vượt quá 5MB.");
            return;
        }
        
        const preview = document.getElementById('avatarPreview');
        preview.src = URL.createObjectURL(file);
    }
} 