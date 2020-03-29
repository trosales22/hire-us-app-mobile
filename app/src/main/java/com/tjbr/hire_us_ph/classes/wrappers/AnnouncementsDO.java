package com.tjbr.hire_us_ph.classes.wrappers;

import org.jetbrains.annotations.NotNull;

public class AnnouncementsDO {
    private String announcementID, announcementCaption,
            announcementDetails, announcementUserID,
            announcementCreator, announcementCreatedDate;

    public AnnouncementsDO(String announcementID, String announcementCaption, String announcementDetails, String announcementUserID, String announcementCreator, String announcementCreatedDate) {
        this.announcementID = announcementID;
        this.announcementCaption = announcementCaption;
        this.announcementDetails = announcementDetails;
        this.announcementUserID = announcementUserID;
        this.announcementCreator = announcementCreator;
        this.announcementCreatedDate = announcementCreatedDate;
    }

    public String getAnnouncementID() {
        return announcementID;
    }

    public String getAnnouncementCaption() {
        return announcementCaption;
    }

    public String getAnnouncementDetails() {
        return announcementDetails;
    }

    public String getAnnouncementUserID() {
        return announcementUserID;
    }

    public String getAnnouncementCreator() {
        return announcementCreator;
    }

    public String getAnnouncementCreatedDate() {
        return announcementCreatedDate;
    }

    @NotNull
    @Override
    public String toString() {
        return "AnnouncementsDO{" +
                "announcementID='" + announcementID + '\'' +
                ", announcementCaption='" + announcementCaption + '\'' +
                ", announcementDetails='" + announcementDetails + '\'' +
                ", announcementUserID='" + announcementUserID + '\'' +
                ", announcementCreator='" + announcementCreator + '\'' +
                ", announcementCreatedDate='" + announcementCreatedDate + '\'' +
                '}';
    }
}
