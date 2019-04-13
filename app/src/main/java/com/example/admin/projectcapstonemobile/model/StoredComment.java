package com.example.admin.projectcapstonemobile.model;

import java.io.Serializable;
import java.util.Date;

public class StoredComment implements Serializable {
    private String content;
    private Comment comment;
    private Date createdDate;

    public StoredComment(String content, Comment comment, Date createdDate) {
        this.content = content;
        this.comment = comment;
        this.createdDate = createdDate;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Comment getComment() {
        return comment;
    }

    public void setComment(Comment comment) {
        this.comment = comment;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }
}
