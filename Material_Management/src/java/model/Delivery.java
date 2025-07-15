package model;

import java.sql.Timestamp;

public class Delivery {

    private int id;
    private int exportId; 
    private int userId; 
    private String recipientName;
    private String deliveryAddress; 
    private String status; 
    private Timestamp deliveryDate; 
    private String description; 
    private String materialName; 
    private String contactPerson; 
    private String contactPhone;  

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getExportId() {
        return exportId;
    }

    public void setExportId(int exportId) {
        this.exportId = exportId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getRecipientName() {
        return recipientName;
    }

    public void setRecipientName(String recipientName) {
        this.recipientName = recipientName;
    }

    public String getDeliveryAddress() {
        return deliveryAddress;
    }

    public void setDeliveryAddress(String deliveryAddress) {
        this.deliveryAddress = deliveryAddress;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Timestamp getDeliveryDate() {
        return deliveryDate;
    }

    public void setDeliveryDate(Timestamp deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getMaterialName() {
        return materialName;
    }

    public void setMaterialName(String materialName) {
        this.materialName = materialName;
    }

    public String getContactPerson() { // Thêm getter
        return contactPerson;
    }

    public void setContactPerson(String contactPerson) { // Thêm setter
        this.contactPerson = contactPerson;
    }

    public String getContactPhone() { // Thêm getter
        return contactPhone;
    }

    public void setContactPhone(String contactPhone) { // Thêm setter
        this.contactPhone = contactPhone;
    }
}
