package com.example.admin.projectcapstonemobile.model;

import java.io.Serializable;
import java.util.Date;

public class TaskIssue implements Serializable {
    private Integer id;
    private Task task;
    private String summary;
    private String detail;
    private Boolean completed;
    private Date createdDate;
    private Boolean available;

    public TaskIssue(Integer id, Task task, String summary, String detail, Boolean completed, Date createdDate, Boolean available) {
        this.id = id;
        this.task = task;
        this.summary = summary;
        this.detail = detail;
        this.completed = completed;
        this.createdDate = createdDate;
        this.available = available;
    }

    public TaskIssue() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Task getTask() {
        return task;
    }

    public void setTask(Task task) {
        this.task = task;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public Boolean getCompleted() {
        return completed;
    }

    public void setCompleted(Boolean completed) {
        this.completed = completed;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Boolean getAvailable() {
        return available;
    }

    public void setAvailable(Boolean available) {
        this.available = available;
    }
}
