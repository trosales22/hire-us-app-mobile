package com.trosales.hireusapp.classes.wrappers;

import com.trosales.hireusapp.classes.beans.Location;

import org.jetbrains.annotations.NotNull;

public class TalentsDO {
    private String talent_id, fullname, height, gender, talentDisplayPhoto, categoryNames;
    private int talentAge;
    private Location location;

    public TalentsDO(String talent_id, String fullname, String height, String gender, String talentDisplayPhoto, String categoryNames, int talentAge, Location location) {
        this.talent_id = talent_id;
        this.fullname = fullname;
        this.height = height;
        this.gender = gender;
        this.talentDisplayPhoto = talentDisplayPhoto;
        this.categoryNames = categoryNames;
        this.talentAge = talentAge;
        this.location = location;
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

    public String getGender() {
        return gender;
    }

    public String getTalentDisplayPhoto() {
        return talentDisplayPhoto;
    }

    public String getCategoryNames() {
        return categoryNames;
    }

    public int getTalentAge() {
        return talentAge;
    }

    public Location getLocation() {
        return location;
    }

    @NotNull
    @Override
    public String toString() {
        return "TalentsDO{" +
                "talent_id='" + talent_id + '\'' +
                ", fullname='" + fullname + '\'' +
                ", height='" + height + '\'' +
                ", gender='" + gender + '\'' +
                ", talentDisplayPhoto='" + talentDisplayPhoto + '\'' +
                ", categoryNames='" + categoryNames + '\'' +
                ", talentAge=" + talentAge +
                ", location=" + location +
                '}';
    }
}
