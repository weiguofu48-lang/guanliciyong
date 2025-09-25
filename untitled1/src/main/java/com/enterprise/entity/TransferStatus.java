package com.enterprise.entity;

public enum TransferStatus {
    PENDING("待发送"),
    SENT("已发送"),
    RECEIVED("已接收"),
    EXPIRED("已过期"),
    CANCELLED("已取消");

    private final String description;

    TransferStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}

