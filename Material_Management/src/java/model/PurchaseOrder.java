package model;

import java.sql.Timestamp;

public class PurchaseOrder {

    private int purchaseOrderId;
    private int supplierId;
    private String supplierName;
    private int userId; // Người tạo đơn mua
    private String username;
    private Timestamp orderDate;
    private double totalAmount;
    private String status;
    private int approvedBy; // Giám đốc duyệt đơn
    private String approvalStatus;

    // Constructor
    public PurchaseOrder(int supplierId, int userId, double totalAmount) {
        this.supplierId = supplierId;
        this.userId = userId;
        this.totalAmount = totalAmount;
        this.status = "Pending";
        this.approvalStatus = "Not Approved";
    }

    // Getters and Setters
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

    public String getSupplierName() {
        return supplierName;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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

    public int getApprovedBy() {
        return approvedBy;
    }

    public void setApprovedBy(int approvedBy) {
        this.approvedBy = approvedBy;
    }

    public String getApprovalStatus() {
        return approvalStatus;
    }

    public void setApprovalStatus(String approvalStatus) {
        this.approvalStatus = approvalStatus;
    }
}
