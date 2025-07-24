package model;

import java.sql.Timestamp;

public class Supplier {
    private int supplierId;
    private String supplierName;
    private String contactPerson;
    private String supplierPhone;
    private String address;
    private String status;
    private String statusReason;
    private Timestamp createdAt;
    private Timestamp updatedAt;
    private java.sql.Date startDate;

    // Constructor mặc định
    public Supplier() {
    }

    // Constructor đầy đủ tham số (bổ sung startDate)
    public Supplier(int supplierId, String supplierName, String contactPerson, String supplierPhone, 
            String address, String status, java.sql.Date startDate, Timestamp createdAt, Timestamp updatedAt) {
        this.supplierId = supplierId;
        this.supplierName = supplierName;
        this.contactPerson = contactPerson;
        this.supplierPhone = supplierPhone;
        this.address = address;
        this.status = status;
        this.startDate = startDate;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    // Constructor không có timestamps
    public Supplier(int supplierId, String supplierName, String contactPerson, String supplierPhone, 
            String address, String status) {
        this.supplierId = supplierId;
        this.supplierName = supplierName;
        this.contactPerson = contactPerson;
        this.supplierPhone = supplierPhone;
        this.address = address;
        this.status = status;
    }

    // Getters và Setters
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatusReason() {
        return statusReason;
    }

    public void setStatusReason(String statusReason) {
        this.statusReason = statusReason;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public Timestamp getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Timestamp updatedAt) {
        this.updatedAt = updatedAt;
    }

    // Getter/setter cho startDate
    public java.sql.Date getStartDate() {
        return startDate;
    }
    public void setStartDate(java.sql.Date startDate) {
        this.startDate = startDate;
    }

    // Phương thức kiểm tra trạng thái
    public boolean isActive() {
        return "active".equalsIgnoreCase(this.status);
    }

    @Override
    public String toString() {
        return "Supplier{" + "supplierId=" + supplierId + ", supplierName=" + supplierName + 
                ", contactPerson=" + contactPerson + ", supplierPhone=" + supplierPhone + 
                ", address=" + address + ", status=" + status + '}';
    }
} 