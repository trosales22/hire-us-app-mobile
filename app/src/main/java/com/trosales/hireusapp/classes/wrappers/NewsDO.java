package com.trosales.hireusapp.classes.wrappers;

import org.jetbrains.annotations.NotNull;

public class NewsDO {
    private String newsID, newsCaption, newsDetails, newsLink, newsDisplayPhoto, newsCreator, newsCreatedDate, newsActiveFlag;

    public NewsDO(String newsID, String newsCaption, String newsDetails, String newsLink, String newsDisplayPhoto, String newsCreator, String newsCreatedDate, String newsActiveFlag) {
        this.newsID = newsID;
        this.newsCaption = newsCaption;
        this.newsDetails = newsDetails;
        this.newsLink = newsLink;
        this.newsDisplayPhoto = newsDisplayPhoto;
        this.newsCreator = newsCreator;
        this.newsCreatedDate = newsCreatedDate;
        this.newsActiveFlag = newsActiveFlag;
    }


    public String getNewsID() {
        return newsID;
    }

    public String getNewsCaption() {
        return newsCaption;
    }

    public String getNewsDetails() {
        return newsDetails;
    }

    public String getNewsLink() {
        return newsLink;
    }

    public String getNewsDisplayPhoto() {
        return newsDisplayPhoto;
    }

    public String getNewsCreator() {
        return newsCreator;
    }

    public String getNewsCreatedDate() {
        return newsCreatedDate;
    }

    public String getNewsActiveFlag() {
        return newsActiveFlag;
    }

    @NotNull
    @Override
    public String toString() {
        return "NewsDO{" +
                "newsID='" + newsID + '\'' +
                ", newsCaption='" + newsCaption + '\'' +
                ", newsDetails='" + newsDetails + '\'' +
                ", newsLink='" + newsLink + '\'' +
                ", newsDisplayPhoto='" + newsDisplayPhoto + '\'' +
                ", newsCreator='" + newsCreator + '\'' +
                ", newsCreatedDate='" + newsCreatedDate + '\'' +
                ", newsActiveFlag='" + newsActiveFlag + '\'' +
                '}';
    }
}
