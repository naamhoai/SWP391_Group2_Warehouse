/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.sql.Timestamp;
import java.util.List;

/**
 *
 * @author kien3
 */
public class ImportHistory {
    private int id;
    private String roles;
    private String reason;
    private String deliveredBy;
    private String receivedBy;
    private String deliveryPhone;
    private String projectName;
    private String materialName;
    private int quantity;
    private String unit;
    private String status;
    private Timestamp createdAt;

    public ImportHistory() {
    }

    public ImportHistory(int id, String roles, String reason, String deliveredBy, String receivedBy, String deliveryPhone, String projectName, String materialName, int quantity, String unit, String status, Timestamp createdAt) {
        this.id = id;
        this.roles = roles;
        this.reason = reason;
        this.deliveredBy = deliveredBy;
        this.receivedBy = receivedBy;
        this.deliveryPhone = deliveryPhone;
        this.projectName = projectName;
        this.materialName = materialName;
        this.quantity = quantity;
        this.unit = unit;
        this.status = status;
        this.createdAt = createdAt;
    }

    // Constructor không có id và createdAt (dùng khi tạo mới)
    public ImportHistory(String roles, String reason, String deliveredBy, String receivedBy, String deliveryPhone, String projectName, String materialName, int quantity, String unit, String status) {
        this.roles = roles;
        this.reason = reason;
        this.deliveredBy = deliveredBy;
        this.receivedBy = receivedBy;
        this.deliveryPhone = deliveryPhone;
        this.projectName = projectName;
        this.materialName = materialName;
        this.quantity = quantity;
        this.unit = unit;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getRoles() {
        return roles;
    }

    public void setRoles(String roles) {
        this.roles = roles;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getDeliveredBy() {
        return deliveredBy;
    }

    public void setDeliveredBy(String deliveredBy) {
        this.deliveredBy = deliveredBy;
    }

    public String getReceivedBy() {
        return receivedBy;
    }

    public void setReceivedBy(String receivedBy) {
        this.receivedBy = receivedBy;
    }

    public String getDeliveryPhone() {
        return deliveryPhone;
    }

    public void setDeliveryPhone(String deliveryPhone) {
        this.deliveryPhone = deliveryPhone;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getMaterialName() {
        return materialName;
    }

    public void setMaterialName(String materialName) {
        this.materialName = materialName;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
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

    @Override
    public String toString() {
        return "ImportHistory{" + "id=" + id + ", roles=" + roles + ", reason=" + reason + ", deliveredBy=" + deliveredBy + ", receivedBy=" + receivedBy + ", deliveryPhone=" + deliveryPhone + ", projectName=" + projectName + ", materialName=" + materialName + ", quantity=" + quantity + ", unit=" + unit + ", status=" + status + ", createdAt=" + createdAt + '}';
    }
    
    
}
