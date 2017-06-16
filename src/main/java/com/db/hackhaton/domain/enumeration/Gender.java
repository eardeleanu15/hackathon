package com.db.hackhaton.domain.enumeration;

public enum Gender {
    MALE("Male"),FEMALE("Female"), UNKNOWN("Unknown");

    private String displayedGender;

    Gender(String displayedGender) {
        this.displayedGender = displayedGender;
    }

    public String getDisplayedGender() {
        return displayedGender;
    }

    public void setDisplayedGender(String displayedGender) {
        this.displayedGender = displayedGender;
    }
}
