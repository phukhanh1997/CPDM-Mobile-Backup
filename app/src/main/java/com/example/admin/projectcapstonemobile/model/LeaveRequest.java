package com.example.admin.projectcapstonemobile.model;

import java.io.Serializable;
import java.util.Date;

public class LeaveRequest implements Serializable {
    private Integer id;
    private String content;
    private String fromDate;
    private String toDate;
    private Integer status;
    private User user;
    private User approver;
    private String createdDate;

    public LeaveRequest(Integer id, String content, String fromDate, String toDate, Integer status, User user, User approver, String createdDate) {
        this.id = id;
        this.content = content;
        this.fromDate = fromDate;
        this.toDate = toDate;
        this.status = status;
        this.user = user;
        this.approver = approver;
        this.createdDate = createdDate;
    }

    public LeaveRequest(Integer id, String content, String fromDate, String toDate, Integer status, User approver, String createdDate) {
        this.id = id;
        this.content = content;
        this.fromDate = fromDate;
        this.toDate = toDate;
        this.status = status;
        this.approver = approver;
        this.createdDate = createdDate;
    }

    public LeaveRequest(String content, String fromDate, String toDate, Integer status, User approver) {
        this.content = content;
        this.fromDate = fromDate;
        this.toDate = toDate;
        this.status = status;
        this.approver = approver;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getFromDate() {
        return fromDate;
    }

    public void setFromDate(String fromDate) {
        this.fromDate = fromDate;
    }

    public String getToDate() {
        return toDate;
    }

    public void setToDate(String toDate) {
        this.toDate = toDate;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public User getApprover() {
        return approver;
    }

    public void setApprover(User approver) {
        this.approver = approver;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
