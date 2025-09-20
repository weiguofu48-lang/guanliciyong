package com.enterprise.entity;

public enum EmployeeStatus {
    ON_PROBATION("试用期"),
    REGULAR("正式"),
    ON_LEAVE("休假中"),
    RESIGNED("已离职");

    private final String displayName;

    EmployeeStatus(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
