package model;

import java.sql.Timestamp;
import java.util.List;

public class PurchaseOrder {
    private int purchaseOrderId;
    private int supplierId;
    private int userId;
    private Timestamp orderDate;
    private double totalAmount;
    private String status;
    private String note;
    private List<PurchaseOrderDetail> details;
    private String creatorName;
    private String supplierName;
    private String creatorRoleName;
    private String approvalStatus;
    private String contactPerson;
    private String supplierPhone;
    private String rejectionReason;

    public PurchaseOrder() {
    }

    public int getPurchaseOrderId() {
        return purchaseOrderId;
    }

    public void setPurchaseOrderId(int purchaseOrderId) {
        this.purchaseOrderId = purchaseOrderId;
    }

    public int getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(int supplierId) {
        this.supplierId = supplierId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public Timestamp getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(Timestamp orderDate) {
        this.orderDate = orderDate;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public List<PurchaseOrderDetail> getDetails() {
        return details;
    }

    public void setDetails(List<PurchaseOrderDetail> details) {
        this.details = details;
    }

    public String getCreatorName() {
        return creatorName;
    }
    public void setCreatorName(String creatorName) {
        this.creatorName = creatorName;
    }
    public String getSupplierName() { return supplierName; }
    public void setSupplierName(String supplierName) { this.supplierName = supplierName; }
    public String getCreatorRoleName() { return creatorRoleName; }
    public void setCreatorRoleName(String creatorRoleName) { this.creatorRoleName = creatorRoleName; }

    public String getApprovalStatus() {
        return approvalStatus;
    }
    public void setApprovalStatus(String approvalStatus) {
        this.approvalStatus = approvalStatus;
    }

    public String getContactPerson() {
        return contactPerson;
    }
    public void setContactPerson(String contactPerson) {
        this.contactPerson = contactPerson;
    }
    public String getSupplierPhone() {
        return supplierPhone;
    }
    public void setSupplierPhone(String supplierPhone) {
        this.supplierPhone = supplierPhone;
    }

    public String getRejectionReason() {
        return rejectionReason;
    }
    public void setRejectionReason(String rejectionReason) {
        this.rejectionReason = rejectionReason;
    }

    @Override
    public String toString() {
        return "PurchaseOrder{" + "purchaseOrderId=" + purchaseOrderId + ", supplierId=" + supplierId + 
               ", userId=" + userId + ", orderDate=" + orderDate + ", totalAmount=" + totalAmount + ", status=" + status + 
               ", note=" + note + ", details=" + details + '}';
    }
}
