package com.example.admin.projectcapstonemobile.model;

public class Notification {
    private Integer id;
    private String title;
    private String detail;
    private boolean hidden;
    private boolean read;
    private String url;
    private String createdTime;
    private User user;
    private User creator;

    public Notification() {
    }

    public Notification(Integer id, String title, String detail, boolean hidden, boolean read, String url, String createdTime, User user, User creator) {
        this.id = id;
        this.title = title;
        this.detail = detail;
        this.hidden = hidden;
        this.read = read;
        this.url = url;
        this.createdTime = createdTime;
        this.user = user;
        this.creator = creator;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public boolean isHidden() {
        return hidden;
    }

    public void setHidden(boolean hidden) {
        this.hidden = hidden;
    }

    public boolean isRead() {
        return read;
    }

    public void setRead(boolean read) {
        this.read = read;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(String createdTime) {
        this.createdTime = createdTime;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public User getCreator() {
        return creator;
    }

    public void setCreator(User creator) {
        this.creator = creator;
    }
}
