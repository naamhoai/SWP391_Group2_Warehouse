package model;

import java.sql.Timestamp;
import java.util.List;
import java.util.ArrayList;

public class RequestHistory {

    private int historyId;
    private int requestId;
    private int changedBy;
    private Timestamp changeTime;
    private String oldStatus;
    private String newStatus;
    private String action;
    private String changeReason;
    private String directorNote;

    // Additional fields for display
    private String changedByUserName;
    private String changedByRoleName;

    // Chi tiết vật tư trong lịch sử thay đổi
    private List<RequestHistoryDetail> historyDetails;

    private String recipientName;

    private String deliveryAddress;
    private String contactPerson;
    private String contactPhone;
    private java.sql.Timestamp createdAt;
    private String creatorName;

    public RequestHistory() {
        this.historyDetails = new ArrayList<>();
    }

    public RequestHistory(int historyId, int requestId, int changedBy, Timestamp changeTime,
            String oldStatus, String newStatus, String action, String changeReason, String directorNote) {
        this.historyId = historyId;
        this.requestId = requestId;
        this.changedBy = changedBy;
        this.changeTime = changeTime;
        this.oldStatus = oldStatus;
        this.newStatus = newStatus;
        this.action = action;
        this.changeReason = changeReason;
        this.directorNote = directorNote;
        this.historyDetails = new ArrayList<>();
    }

    public int getHistoryId() {
        return historyId;
    }

    public void setHistoryId(int historyId) {
        this.historyId = historyId;
    }

    public int getRequestId() {
        return requestId;
    }

    public void setRequestId(int requestId) {
        this.requestId = requestId;
    }

    public int getChangedBy() {
        return changedBy;
    }

    public void setChangedBy(int changedBy) {
        this.changedBy = changedBy;
    }

    public Timestamp getChangeTime() {
        return changeTime;
    }

    public void setChangeTime(Timestamp changeTime) {
        this.changeTime = changeTime;
    }

    public String getOldStatus() {
        return oldStatus;
    }

    public void setOldStatus(String oldStatus) {
        this.oldStatus = oldStatus;
    }

    public String getNewStatus() {
        return newStatus;
    }

    public void setNewStatus(String newStatus) {
        this.newStatus = newStatus;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getChangeReason() {
        return changeReason;
    }

    public void setChangeReason(String changeReason) {
        this.changeReason = changeReason;
    }

    public String getDirectorNote() {
        return directorNote;
    }

    public void setDirectorNote(String directorNote) {
        this.directorNote = directorNote;
    }

    public String getChangedByUserName() {
        return changedByUserName;
    }

    public void setChangedByUserName(String changedByUserName) {
        this.changedByUserName = changedByUserName;
    }

    public String getChangedByRoleName() {
        return changedByRoleName;
    }

    public void setChangedByRoleName(String changedByRoleName) {
        this.changedByRoleName = changedByRoleName;
    }

    public List<RequestHistoryDetail> getHistoryDetails() {
        return historyDetails;
    }

    public void setHistoryDetails(List<RequestHistoryDetail> historyDetails) {
        this.historyDetails = historyDetails;
    }

    public void addHistoryDetail(RequestHistoryDetail detail) {
        if (this.historyDetails == null) {
            this.historyDetails = new ArrayList<>();
        }
        this.historyDetails.add(detail);
    }

    // Helper methods
    public boolean isApprovalAction() {
        return "Duyệt yêu cầu".equals(action);
    }

    public boolean isRejectionAction() {
        return "Từ chối yêu cầu".equals(action);
    }

    public boolean isEditAction() {
        return "Chỉnh sửa yêu cầu".equals(action) || "Gửi lại yêu cầu".equals(action);
    }

    public boolean isCreateAction() {
        return "Tạo yêu cầu".equals(action);
    }

    public String getStatusChangeDisplay() {
        if (oldStatus != null && newStatus != null) {
            return oldStatus + " → " + newStatus;
        }
        return newStatus;
    }

    public String getActionDisplay() {
        if (isApprovalAction()) {
            return "✅ " + action;
        } else if (isRejectionAction()) {
            return "❌ " + action;
        } else if (isEditAction()) {
            return "✏️ " + action;
        } else if (isCreateAction()) {
            return "📝 " + action;
        }
        return action;
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

    public java.sql.Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(java.sql.Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public String getCreatorName() {
        return creatorName;
    }

    public void setCreatorName(String creatorName) {
        this.creatorName = creatorName;
    }
}
