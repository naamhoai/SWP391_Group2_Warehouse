/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.sql.Timestamp; 


/**
 *
 * @author kien3
 */
public class UnitChangeHistory {
    private int historyId;
    private int unitId;
    private String unitName;
    private String actionType;
    private String oldValue;
    private String newValue;
    private String changedBy;
    private String role;
    private String note;
    private Timestamp changedAt;

    public UnitChangeHistory() {
    }

    public UnitChangeHistory(int historyId, int unitId, String unitName, String actionType, String oldValue, String newValue, String changedBy, String role, String note, Timestamp changedAt) {
        this.historyId = historyId;
        this.unitId = unitId;
        this.unitName = unitName;
        this.actionType = actionType;
        this.oldValue = oldValue;
        this.newValue = newValue;
        this.changedBy = changedBy;
        this.role = role;
        this.note = note;
        this.changedAt = changedAt;
    }

    public int getHistoryId() {
        return historyId;
    }

    public void setHistoryId(int historyId) {
        this.historyId = historyId;
    }

    public int getUnitId() {
        return unitId;
    }

    public void setUnitId(int unitId) {
        this.unitId = unitId;
    }

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }

    public String getActionType() {
        return actionType;
    }

    public void setActionType(String actionType) {
        this.actionType = actionType;
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

    public String getChangedBy() {
        return changedBy;
    }

    public void setChangedBy(String changedBy) {
        this.changedBy = changedBy;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Timestamp getChangedAt() {
        return changedAt;
    }

    public void setChangedAt(Timestamp changedAt) {
        this.changedAt = changedAt;
    }

    @Override
    public String toString() {
        return "UnitChangeHistory{" + "historyId=" + historyId + ", unitId=" + unitId + ", unitName=" + unitName + ", actionType=" + actionType + ", oldValue=" + oldValue + ", newValue=" + newValue + ", changedBy=" + changedBy + ", role=" + role + ", note=" + note + ", changedAt=" + changedAt + '}';
    }
    
    
}
