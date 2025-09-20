package com.enterprise.entity;

public enum MovementType {
    ONBOARDING("入职"),
    REGULARIZATION("转正"),
    PROMOTION("晋升"),
    TRANSFER("调动"),
    LEAVE("请假"),
    RESIGNATION("离职");

    private final String displayName;

    MovementType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
