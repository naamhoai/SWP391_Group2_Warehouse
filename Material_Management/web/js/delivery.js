function showAddEditForm(id, exportId, receiverName, deliveryAddress, status, deliveryDate, description) {
    document.getElementById('formTitle').textContent = id ? 'Edit Delivery' : 'Add Delivery';
    document.getElementById('formAction').value = id ? 'edit' : 'add';
    document.getElementById('deliveryForm').style.display = 'block';
    document.getElementById('deliveryId').value = id || '';
    document.getElementById('exportId').value = exportId || '';
    document.getElementById('receiverName').value = receiverName || '';
    document.getElementById('deliveryAddress').value = deliveryAddress || '';
    document.getElementById('deliveryDate').value = deliveryDate ? new Date(deliveryDate).toISOString().split('T')[0] : '';
    document.getElementById('status').value = status || 'Pending';
    document.getElementById('description').value = description || '';
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
    window.location.href = 'delivery?status=' + status;
}

// Load filtered data if status parameter exists
window.onload = function() {
    const urlParams = new URLSearchParams(window.location.search);
    const status = urlParams.get('status');
    if (status) {
        document.querySelector('select[name="statusFilter"]').value = status;
    }
};