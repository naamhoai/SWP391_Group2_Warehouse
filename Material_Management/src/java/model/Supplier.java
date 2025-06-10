package model;

import java.sql.Timestamp;

public class Supplier {
    private int supplierId;
    private String supplierName;
    private String contactPerson;
    private String supplierPhone;
    private String address;
    private String status;
    private Timestamp createdAt;
    private Timestamp updatedAt;

    // Constructor mặc định
    public Supplier() {
    }

    // Constructor đầy đủ tham số
    public Supplier(int supplierId, String supplierName, String contactPerson, String supplierPhone, 
            String address, String status, Timestamp createdAt, Timestamp updatedAt) {
        this.supplierId = supplierId;
        this.supplierName = supplierName;
        this.contactPerson = contactPerson;
        this.supplierPhone = supplierPhone;
        this.address = address;
        this.status = status;
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