package com.example.admin.projectcapstonemobile.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

public class Task implements Serializable {
    private Integer id;
    private String title;
    private String summary;
    private String description;
    private Date createdTime;
    private Date startTime;
    private Date endTime;
    private Integer priority;
    private String status;
    private Date lastModifiedTime;
    private Date completeTime;
    private Project project;
    private Task parentTask;
    private User creator;
    private User executor;
    private List<User> relatives;
    private boolean available;
    private List<TaskIssue> issues;
    private List<Document> documents;

    public Task(Integer id, String title, String summary, String description, Date createdTime,
                Date startTime, Date endTime, Integer priority, String status, Date lastModifiedTime,
                Date completeTime, Project project, Task parentTask, User creator, User executor,
                List<User> relatives, boolean available, List<TaskIssue> issues, List<Document> documents) {
        this.id = id;
        this.title = title;
        this.summary = summary;
        this.description = description;
        this.createdTime = createdTime;
        this.startTime = startTime;
        this.endTime = endTime;
        this.priority = priority;
        this.status = status;
        this.lastModifiedTime = lastModifiedTime;
        this.completeTime = completeTime;
        this.project = project;
        this.parentTask = parentTask;
        this.creator = creator;
        this.executor = executor;
        this.relatives = relatives;
        this.available = available;
        this.issues = issues;
        this.documents = documents;
    }

    public Task(Integer id, String title, String summary, String description, Date createdTime,
                Date startTime, Date endTime, Integer priority, String status, Task parentTask,
                User creator, User executor, List<Document> documents) {
        this.id = id;
        this.title = title;
        this.summary = summary;
        this.description = description;
        this.createdTime = createdTime;
        this.startTime = startTime;
        this.endTime = endTime;
        this.priority = priority;
        this.status = status;
        this.parentTask = parentTask;
        this.creator = creator;
        this.executor = executor;
        this.documents = documents;
    }

    public Task(Integer id, Integer priority, Date endTime, Date startTime, String status,
                String title, String summary, Date createdTime, User creator, User executor) {
        this.id = id;
        this.title = title;
        this.summary = summary;
        this.createdTime = createdTime;
        this.startTime = startTime;
        this.endTime = endTime;
        this.priority = priority;
        this.status = status;
        this.creator = creator;
        this.executor = executor;
    }

    public Task() {
    }

    public Task(Integer id) {
        this.id = id;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Task getParentTask() {
        return parentTask;
    }

    public void setParentTask(Task parentTask) {
        this.parentTask = parentTask;
    }

    public User getCreator() {
        return creator;
    }

    public void setCreator(User creator) {
        this.creator = creator;
    }

    public User getExecutor() {
        return executor;
    }

    public void setExecutor(User executor) {
        this.executor = executor;
    }

    public List<Document> getDocuments() {
        return documents;
    }

    public void setDocuments(List<Document> documents) {
        this.documents = documents;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public Date getLastModifiedTime() {
        return lastModifiedTime;
    }

    public void setLastModifiedTime(Date lastModifiedTime) {
        this.lastModifiedTime = lastModifiedTime;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public List<User> getRelatives() {
        return relatives;
    }

    public void setRelatives(List<User> relatives) {
        this.relatives = relatives;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public List<TaskIssue> getIssues() {
        return issues;
    }

    public void setIssues(List<TaskIssue> issues) {
        this.issues = issues;
    }
}
