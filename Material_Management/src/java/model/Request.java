package model;

import java.sql.Timestamp;

public class Request {
    private int requestId;
    private String requestType; // 'Mua Vật Tư' hoặc 'Xuất Kho'
    private int userId;
    private String reason;
    private String requestStatus; // 'Pending', 'Approved', 'Rejected'
    private Timestamp createdAt;

    // Constructors
    public Request() {}

    public Request(int requestId, String requestType, int userId, String reason, String requestStatus, Timestamp createdAt) {
        this.requestId = requestId;
        this.requestType = requestType;
        this.userId = userId;
        this.reason = reason;
        this.requestStatus = requestStatus;
        this.createdAt = createdAt;
    }

    // Getters and Setters
    public int getRequestId() {
        return requestId;
    }
    public void setRequestId(int requestId) {
        this.requestId = requestId;
    }

    public String getRequestType() {
        return requestType;
    }
    public void setRequestType(String requestType) {
        this.requestType = requestType;
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
}
