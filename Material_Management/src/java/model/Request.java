package model;

import java.util.Date;

public class Request {
    private int id;
    private String type;
    private String content;
    private String requester;
    private String department;
    private Date requestTime;
    private String status;
    private String comment;
    private int approverId;

    public Request() {
    }

    public Request(int id, String type, String content, String requester, String department, 
                  Date requestTime, String status, String comment, int approverId) {
        this.id = id;
        this.type = type;
        this.content = content;
        this.requester = requester;
        this.department = department;
        this.requestTime = requestTime;
        this.status = status;
        this.comment = comment;
        this.approverId = approverId;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getRequester() {
        return requester;
    }

    public void setRequester(String requester) {
        this.requester = requester;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public Date getRequestTime() {
        return requestTime;
    }

    public void setRequestTime(Date requestTime) {
        this.requestTime = requestTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public int getApproverId() {
        return approverId;
    }

    public void setApproverId(int approverId) {
        this.approverId = approverId;
    }
} 