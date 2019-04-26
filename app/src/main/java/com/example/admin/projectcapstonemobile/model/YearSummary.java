package com.example.admin.projectcapstonemobile.model;


import java.io.Serializable;
import java.util.List;

public class YearSummary implements Serializable {
    private Integer dayOffPerYear;
    private Integer dayOffApproved;
    private Integer dayOffRemain;
    private List<LeaveRequest> leaveRequestSummaries;

    public YearSummary() {
    }

    public YearSummary(Integer dayOffPerYear, Integer dayOffApproved, Integer dayOffRemain, List<LeaveRequest> leaveRequestSummaries) {
        this.dayOffPerYear = dayOffPerYear;
        this.dayOffApproved = dayOffApproved;
        this.dayOffRemain = dayOffRemain;
        this.leaveRequestSummaries = leaveRequestSummaries;
    }

    public Integer getDayOffPerYear() {
        return dayOffPerYear;
    }

    public void setDayOffPerYear(Integer dayOffPerYear) {
        this.dayOffPerYear = dayOffPerYear;
    }

    public Integer getDayOffApproved() {
        return dayOffApproved;
    }

    public void setDayOffApproved(Integer dayOffApproved) {
        this.dayOffApproved = dayOffApproved;
    }

    public Integer getDayOffRemain() {
        return dayOffRemain;
    }

    public void setDayOffRemain(Integer dayOffRemain) {
        this.dayOffRemain = dayOffRemain;
    }

    public List<LeaveRequest> getLeaveRequestSummaries() {
        return leaveRequestSummaries;
    }

    public void setLeaveRequestSummaries(List<LeaveRequest> leaveRequestSummaries) {
        this.leaveRequestSummaries = leaveRequestSummaries;
    }
}
