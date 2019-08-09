package com.trosales.hireusapp.classes.wrappers;

import org.jetbrains.annotations.NotNull;

public class TalentsDO {
    private String talent_id,fullname,height,talentFee,talentFeeType,location,gender,talentDisplayPhoto,categoryNames;

    private int talentAge;

    public TalentsDO(String talent_id, String fullname, String height, String talentFee, String talentFeeType, String location, Integer talentAge, String gender, String talentDisplayPhoto, String categoryNames) {
        this.talent_id = talent_id;
        this.fullname = fullname;
        this.height = height;
        this.talentFee = talentFee;
        this.talentFeeType = talentFeeType;
        this.location = location;
        this.talentAge = talentAge;
        this.gender = gender;
        this.talentDisplayPhoto = talentDisplayPhoto;
        this.categoryNames = categoryNames;
    }

    public String getTalent_id() {
        return talent_id;
    }

    public String getFullname() {
        return fullname;
    }

    public String getHeight() {
        return height;
    }

    public String getTalentFee() {
        return talentFee;
    }

    public String getTalentFeeType() {
        return talentFeeType;
    }

    public String getLocation() {
        return location;
    }

    public int getTalentAge() {
        return talentAge;
    }

    public String getGender() {
        return gender;
    }

    public String getTalentDisplayPhoto() {
        return talentDisplayPhoto;
    }

    public String getCategoryNames() {
        return categoryNames;
    }

    @NotNull
    @Override
    public String toString() {
        return "TalentsDO{" +
                "talent_id='" + talent_id + '\'' +
                ", fullname='" + fullname + '\'' +
                ", height='" + height + '\'' +
                ", talentFee='" + talentFee + '\'' +
                ", talentFeeType='" + talentFeeType + '\'' +
                ", location='" + location + '\'' +
                ", gender='" + gender + '\'' +
                ", talentDisplayPhoto='" + talentDisplayPhoto + '\'' +
                ", categoryNames='" + categoryNames + '\'' +
                '}';
    }
}
