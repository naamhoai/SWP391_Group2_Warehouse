// Hiển thị form thêm/sửa giao hàng
function showAddEditForm(id, purchaseOrderId, recipientName, deliveryAddress, status, deliveryDate, description, userId) {
    const formTitle = document.getElementById('formTitle');
    const formAction = document.getElementById('formAction');
    const deliveryForm = document.getElementById('deliveryForm');
    const deliveryId = document.getElementById('deliveryId');
    const purchaseOrderIdSelect = document.getElementById('purchaseOrderId');
    const recipientNameInput = document.getElementById('recipientName');
    const deliveryAddressInput = document.getElementById('deliveryAddress');
    const deliveryDateInput = document.getElementById('deliveryDate');
    const statusSelect = document.getElementById('status');
    const descriptionInput = document.getElementById('description');
    const userIdInput = document.getElementById('userId'); // Nếu có trường này trên form

    formTitle.textContent = id ? 'Sửa giao hàng' : 'Thêm giao hàng';
    formAction.value = id ? 'edit' : 'add';
    deliveryForm.style.display = 'block';

    deliveryId.value = id || '';
    purchaseOrderIdSelect.value = purchaseOrderId || '';
    recipientNameInput.value = recipientName || '';
    deliveryAddressInput.value = deliveryAddress || '';
    deliveryDateInput.value = deliveryDate || '';
    statusSelect.value = status || 'Chờ giao'; // Nếu ENUM đã là tiếng Việt
    descriptionInput.value = description || '';
    if (userIdInput) userIdInput.value = userId || '';
}

// Lọc theo trạng thái
function filterByStatus() {
    const status = document.querySelector('select[name="statusFilter"]').value;
    window.location.href = 'delivery?status=' + encodeURIComponent(status);
}

//// Khi load trang, giữ trạng thái bộ lọc nếu có
//window.onload = function() {
//    const urlParams = new URLSearchParams(window.location.search);
//    const status = urlParams.get('status');
//    if (status) {
//        const statusFilter = document.querySelector('select[name="statusFilter"]');
//        if (statusFilter) {
//            statusFilter.value = decodeURIComponent(status);
//        }
//    }
//};
