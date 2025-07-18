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
    private String deliveryType;

    public String getDeliveryType() {
        return deliveryType;
    }

    public Delivery(int id, int exportId, int userId, String recipientName, String deliveryAddress, String status, Timestamp deliveryDate, String description, String materialName, String contactPerson, String contactPhone, String deliveryType) {
        this.id = id;
        this.exportId = exportId;
        this.userId = userId;
        this.recipientName = recipientName;
        this.deliveryAddress = deliveryAddress;
        this.status = status;
        this.deliveryDate = deliveryDate;
        this.description = description;
        this.materialName = materialName;
        this.contactPerson = contactPerson;
        this.contactPhone = contactPhone;
        this.deliveryType = deliveryType;
    }

    public Delivery() {
    }

    public void setDeliveryType(String deliveryType) {
        this.deliveryType = deliveryType;
    }

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

    public String getContactPerson() { 
        return contactPerson;
    }
public void setContactPerson(String contactPerson) { 
        this.contactPerson = contactPerson;
    }

    public String getContactPhone() { 
        return contactPhone;
    }

    public void setContactPhone(String contactPhone) { 
        this.contactPhone = contactPhone;
    }
}