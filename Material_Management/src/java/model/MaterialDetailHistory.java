package model;

import java.sql.Timestamp;

public class MaterialDetailHistory {
    private int historyId;
    private int materialId;
    private String fieldName;
    private String oldValue;
    private String newValue;
    private int changedBy;
    private String roleName;
    private Timestamp changedAt;
    private String materialName;
    private String userName;

    public MaterialDetailHistory() {}

    public MaterialDetailHistory(int historyId, int materialId, String fieldName, String oldValue, String newValue, int changedBy, String roleName, Timestamp changedAt) {
        this.historyId = historyId;
        this.materialId = materialId;
        this.fieldName = fieldName;
        this.oldValue = oldValue;
        this.newValue = newValue;
        this.changedBy = changedBy;
        this.roleName = roleName;
        this.changedAt = changedAt;
    }

    public int getHistoryId() {
        return historyId;
    }

    public void setHistoryId(int historyId) {
        this.historyId = historyId;
    }

    public int getMaterialId() {
        return materialId;
    }

    public void setMaterialId(int materialId) {
        this.materialId = materialId;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getOldValue() {
        return oldValue;
    }

    public void setOldValue(String oldValue) {
        this.oldValue = oldValue;
    }

    public String getNewValue() {
        return newValue;
    }

    public void setNewValue(String newValue) {
        this.newValue = newValue;
    }

    public int getChangedBy() {
        return changedBy;
    }

    public void setChangedBy(int changedBy) {
        this.changedBy = changedBy;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public Timestamp getChangedAt() {
        return changedAt;
    }

    public void setChangedAt(Timestamp changedAt) {
        this.changedAt = changedAt;
    }

    public String getMaterialName() {
        return materialName;
    }
    public void setMaterialName(String materialName) {
        this.materialName = materialName;
    }

    public String getUserName() {
        return userName;
    }
    public void setUserName(String userName) {
        this.userName = userName;
    }

    @Override
    public String toString() {
        return "MaterialDetailHistory{" +
                "historyId=" + historyId +
                ", materialId=" + materialId +
                ", fieldName='" + fieldName + '\'' +
                ", oldValue='" + oldValue + '\'' +
                ", newValue='" + newValue + '\'' +
                ", changedBy=" + changedBy +
                ", roleName='" + roleName + '\'' +
                ", changedAt=" + changedAt +
                '}';
    }
} 