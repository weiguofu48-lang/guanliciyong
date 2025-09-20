package com.enterprise.entity;

public enum Gender {
    MALE("男"),
    FEMALE("女");

    private final String displayName;

    Gender(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
