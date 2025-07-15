package model;

import java.sql.Timestamp;

public class Request {

    private int requestId;
    private int userId;
    private String reason;
    private String recipientName;
    private String deliveryAddress;
    private String contactPerson;
    private String contactPhone;
    private String requestStatus; 
    private Timestamp createdAt;
    private String directorNote;
    private String creatorName;
    private String creatorRole;

    public Request() {
    }

    public Request(int requestId, int userId, String reason, String recipientName, 
                   String deliveryAddress, String contactPerson, String contactPhone, String requestStatus, 
                   Timestamp createdAt, String directorNote, String creatorName, String creatorRole) {
        this.requestId = requestId;
        this.userId = userId;
        this.reason = reason;
        this.recipientName = recipientName;
        this.deliveryAddress = deliveryAddress;
        this.contactPerson = contactPerson;
        this.contactPhone = contactPhone;
        this.requestStatus = requestStatus;
        this.createdAt = createdAt;
        this.directorNote = directorNote;
        this.creatorName = creatorName;
        this.creatorRole = creatorRole;
    }

    public Request(int requestId, int userId, String reason, String recipientName, 
                   String deliveryAddress, String contactPerson, String contactPhone, String requestStatus, 
                   Timestamp createdAt, String directorNote) {
        this.requestId = requestId;
        this.userId = userId;
        this.reason = reason;
        this.recipientName = recipientName;
        this.deliveryAddress = deliveryAddress;
        this.contactPerson = contactPerson;
        this.contactPhone = contactPhone;
        this.requestStatus = requestStatus;
        this.createdAt = createdAt;
        this.directorNote = directorNote;
    }

    public int getRequestId() {
        return requestId;
    }

    public void setRequestId(int requestId) {
        this.requestId = requestId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
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

    public String getRequestStatus() {
        return requestStatus;
    }

    public void setRequestStatus(String requestStatus) {
        this.requestStatus = requestStatus;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public String getDirectorNote() {
        return directorNote;
    }

    public void setDirectorNote(String directorNote) {
        this.directorNote = directorNote;
    }

    public String getCreatorName() {
        return creatorName;
    }

    public void setCreatorName(String creatorName) {
        this.creatorName = creatorName;
    }

    public String getCreatorRole() {
        return creatorRole;
    }

    public void setCreatorRole(String creatorRole) {
        this.creatorRole = creatorRole;
    }

}
