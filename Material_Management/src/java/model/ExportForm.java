package model;

import java.sql.Timestamp;
import java.math.BigDecimal;
import java.util.List;

public class ExportForm {

    private int exportId;
    private int requestId;
    private int userId;
    private Timestamp exportDate;
    private String reason;
    private String recipientName;
    private String deliveryAddress;
    private String contactPerson;
    private String contactPhone;
    private String status; // Đang xử lý, Hoàn thành, Đã hủy
    private String description;
    private String userRole;

    // Additional fields for display
    private String userName;
    private List<ExportMaterial> exportMaterials;

    public ExportForm() {
    }

    public ExportForm(int exportId, int requestId, int userId, Timestamp exportDate, String reason,
            String recipientName, String deliveryAddress, String contactPerson, String contactPhone,
            String status, String description) {
        this.exportId = exportId;
        this.requestId = requestId;
        this.userId = userId;
        this.exportDate = exportDate;
        this.reason = reason;
        this.recipientName = recipientName;
        this.deliveryAddress = deliveryAddress;
        this.contactPerson = contactPerson;
        this.contactPhone = contactPhone;
        this.status = status;
        this.description = description;
    }

    // Getters and Setters
    public int getExportId() {
        return exportId;
    }

    public void setExportId(int exportId) {
        this.exportId = exportId;
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

    public Timestamp getExportDate() {
        return exportDate;
    }

    public void setExportDate(Timestamp exportDate) {
        this.exportDate = exportDate;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public List<ExportMaterial> getExportMaterials() {
        return exportMaterials;
    }

    public void setExportMaterials(List<ExportMaterial> exportMaterials) {
        this.exportMaterials = exportMaterials;
    }

    public String getUserRole() {
        return userRole;
    }

    public void setUserRole(String userRole) {
        this.userRole = userRole;
    }

    // Helper methods
    public boolean isProcessing() {
        return "Đang xử lý".equals(status);
    }

    public boolean isCompleted() {
        return "Hoàn thành".equals(status);
    }

    public BigDecimal getTotalAmount() {
        if (exportMaterials == null || exportMaterials.isEmpty()) {
            return BigDecimal.ZERO;
        }
        return exportMaterials.stream()
                .map(ExportMaterial::getTotalPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
