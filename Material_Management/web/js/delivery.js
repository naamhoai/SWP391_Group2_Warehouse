function showAddEditForm(id, exportId, receiverName, deliveryAddress, status, deliveryDate, description) {
    const formTitle = document.getElementById('formTitle');
    const formAction = document.getElementById('formAction');
    const deliveryForm = document.getElementById('deliveryForm');
    const deliveryId = document.getElementById('deliveryId');
    const exportIdSelect = document.getElementById('exportId');
    const receiverNameInput = document.getElementById('receiverName');
    const deliveryAddressInput = document.getElementById('deliveryAddress');
    const deliveryDateInput = document.getElementById('deliveryDate');
    const statusSelect = document.getElementById('status');
    const descriptionInput = document.getElementById('description');

    formTitle.textContent = id ? 'Sửa giao hàng' : 'Thêm giao hàng';
    formAction.value = id ? 'edit' : 'add';
    deliveryForm.style.display = 'block';
    deliveryId.value = id || '';
    exportIdSelect.value = exportId || '';
    receiverNameInput.value = receiverName || '';
    deliveryAddressInput.value = deliveryAddress || '';
    deliveryDateInput.value = deliveryDate || '';
    statusSelect.value = status || 'Pending';
    descriptionInput.value = description || '';
}

function hideForm() {
    document.getElementById('deliveryForm').style.display = 'none';
}

function deleteDelivery(id) {
    if (confirm('Bạn có chắc muốn xóa phiếu giao hàng này?')) {
        window.location.href = 'delivery?action=delete&id=' + id;
    }
}

function filterByStatus() {
    const status = document.querySelector('select[name="statusFilter"]').value;
    window.location.href = 'delivery?status=' + encodeURIComponent(status);
}

// Load filtered data if status parameter exists
window.onload = function() {
    const urlParams = new URLSearchParams(window.location.search);
    const status = urlParams.get('status');
    if (status) {
        document.querySelector('select[name="statusFilter"]').value = decodeURIComponent(status);
    }
};