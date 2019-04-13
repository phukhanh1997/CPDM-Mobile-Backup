package com.example.admin.projectcapstonemobile.model;

import java.io.Serializable;

public class Document implements Serializable {
    private Integer id;
    private String title;
    private String summary;

    public Document(Integer id, String title, String summary) {
        this.id = id;
        this.title = title;
        this.summary = summary;
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

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }
}
