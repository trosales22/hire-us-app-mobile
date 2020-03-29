package com.tjbr.hire_us_ph.classes.wrappers;

import org.jetbrains.annotations.NotNull;

public class UnavailableDatesDO {
    private String talentId,days,monthYear,createdDate;

    public UnavailableDatesDO(String talentId, String days, String monthYear, String createdDate) {
        this.talentId = talentId;
        this.days = days;
        this.monthYear = monthYear;
        this.createdDate = createdDate;
    }

    public String getTalentId() {
        return talentId;
    }

    public String getDays() {
        return days;
    }

    public String getMonthYear() {
        return monthYear;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    @NotNull
    @Override
    public String toString() {
        return "UnavailableDatesDO{" +
                "talentId='" + talentId + '\'' +
                ", days='" + days + '\'' +
                ", monthYear='" + monthYear + '\'' +
                ", createdDate='" + createdDate + '\'' +
                '}';
    }
}
