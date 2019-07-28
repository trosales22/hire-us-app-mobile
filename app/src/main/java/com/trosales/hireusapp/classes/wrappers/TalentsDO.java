package com.trosales.hireusapp.classes.wrappers;

public class TalentsDO {
    private String talent_id,fullname,height,talentFee,talentFeeType,location,email,
            contactNumber,gender,talentDisplayPhoto;

    public TalentsDO(String talent_id, String fullname, String height, String talentFee, String talentFeeType, String location, String email, String contactNumber, String gender, String talentDisplayPhoto) {
        this.talent_id = talent_id;
        this.fullname = fullname;
        this.height = height;
        this.talentFee = talentFee;
        this.talentFeeType = talentFeeType;
        this.location = location;
        this.email = email;
        this.contactNumber = contactNumber;
        this.gender = gender;
        this.talentDisplayPhoto = talentDisplayPhoto;
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

    public String getEmail() {
        return email;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public String getGender() {
        return gender;
    }

    public String getTalentDisplayPhoto() {
        return talentDisplayPhoto;
    }

    @Override
    public String toString() {
        return "TalentsDO{" +
                "talent_id='" + talent_id + '\'' +
                ", fullname='" + fullname + '\'' +
                ", height='" + height + '\'' +
                ", talentFee='" + talentFee + '\'' +
                ", talentFeeType='" + talentFeeType + '\'' +
                ", location='" + location + '\'' +
                ", email='" + email + '\'' +
                ", contactNumber='" + contactNumber + '\'' +
                ", gender='" + gender + '\'' +
                ", talentDisplayPhoto='" + talentDisplayPhoto + '\'' +
                '}';
    }
}
