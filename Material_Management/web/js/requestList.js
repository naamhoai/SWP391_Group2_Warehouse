
document.addEventListener('DOMContentLoaded', function () {
    const today = new Date().toISOString().split('T')[0];
    const startDateInput = document.getElementById('startDateInput');
    const endDateInput = document.getElementById('endDateInput');
    if (startDateInput)
        startDateInput.setAttribute('max', today);
    if (endDateInput)
        endDateInput.setAttribute('max', today);

    const statusSelect = document.getElementById('statusSelect');
    if (statusSelect) {
        statusSelect.addEventListener('change', function () {
            const pageInput = document.getElementById('pageInput');
            if (pageInput)
                pageInput.value = '1';
            const filterForm = document.getElementById('filterForm');
            if (filterForm)
                filterForm.submit();
        });
    }

    const filterForm = document.getElementById('filterForm');
    if (filterForm) {
        filterForm.addEventListener('submit', function (e) {
            if (!validateDates()) {
                e.preventDefault();
                return false;
            }
        });
    }

});

function validateDateInput(input) {
    const selectedDate = new Date(input.value);
    const today = new Date();
    today.setHours(23, 59, 59, 999);

    if (selectedDate > today) {
        showError(`Ngày ${input.name === 'startDate' ? 'bắt đầu' : 'kết thúc'} không được trong tương lai`);
        input.focus();
        return false;
    }

    return true;
}

function validateDates() {
    const startDateInput = document.getElementById('startDateInput');
    const endDateInput = document.getElementById('endDateInput');

    clearErrors();

    if ((!startDateInput || !startDateInput.value) && (!endDateInput || !endDateInput.value)) {
        return true;
    }

    if (startDateInput && startDateInput.value && !validateDateInput(startDateInput)) {
        return false;
    }

    if (endDateInput && endDateInput.value && !validateDateInput(endDateInput)) {
        return false;
    }

    if (startDateInput && endDateInput && startDateInput.value && endDateInput.value) {
        const startDate = new Date(startDateInput.value);
        const endDate = new Date(endDateInput.value);

        if (startDate > endDate) {
            showError('Ngày bắt đầu không được sau ngày kết thúc');
            startDateInput.focus();
            return false;
        }
    }

    return true;
}

function showError(message) {
    clearErrors();

    const errorDiv = document.createElement('div');
    errorDiv.className = 'error-message';
    errorDiv.style.cssText = `
        color: #dc3545;
        background-color: #f8d7da;
        border: 1px solid #f5c6cb;
        border-radius: 4px;
        padding: 10px;
        margin: 10px 0;
        font-size: 14px;
        display: flex;
        align-items: center;
        gap: 8px;
    `;
    errorDiv.innerHTML = `
        <i class="fas fa-exclamation-triangle"></i>
        <span>${message}</span>
    `;

    const filterForm = document.getElementById('filterForm');
    if (filterForm) {
        filterForm.parentNode.insertBefore(errorDiv, filterForm.nextSibling);
    }

    setTimeout(() => {
        if (errorDiv.parentNode) {
            errorDiv.parentNode.removeChild(errorDiv);
        }
    }, 5000);
}

function clearErrors() {
    const existingErrors = document.querySelectorAll('.error-message');
    existingErrors.forEach(error => error.remove());
}
