package com.example.admin.projectcapstonemobile.model;

import java.io.Serializable;
import java.util.Date;

public class Comment implements Serializable {
    private Integer id;
    private String content;
    private Date createdDate;
    private Date lastModifiedDate;
    private Integer status;
    private User user;
    private Task task;
    private Integer parentCommentId;

    public Comment(Integer id, String content, Date createdDate, Date lastModifiedDate, Integer status, User user, Task task, Integer parentCommentId) {
        this.id = id;
        this.content = content;
        this.createdDate = createdDate;
        this.lastModifiedDate = lastModifiedDate;
        this.status = status;
        this.user = user;
        this.task = task;
        this.parentCommentId = parentCommentId;
    }

    public Comment(String content, Task task, Integer status) {
        this.content = content;
        this.status = status;
        this.task = task;
    }

    public Comment(Integer id) {
        this.id = id;
    }

    public Comment(String content, Task task, Integer status, Date createdDate) {
        this.content = content;
        this.createdDate = createdDate;
        this.status = status;
        this.task = task;
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

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Date getLastModifiedDate() {
        return lastModifiedDate;
    }

    public void setLastModifiedDate(Date lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Task getTask() {
        return task;
    }

    public void setTask(Task task) {
        this.task = task;
    }

    public Integer getParentCommentId() {
        return parentCommentId;
    }

    public void setParentCommentId(Integer parentCommentId) {
        this.parentCommentId = parentCommentId;
    }
}
