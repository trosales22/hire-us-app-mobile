package com.agentcoder.hire_us_ph.classes.wrappers;

import com.agentcoder.hire_us_ph.classes.beans.Location;

import org.jetbrains.annotations.NotNull;

public class TalentsDO {
    private String talent_id, fullname, height, gender, talentDisplayPhoto, categoryIds;
    private int talentAge;
    private Location location;

    public TalentsDO(String talent_id, String fullname, String height, String gender, String talentDisplayPhoto, String categoryIds, int talentAge, Location location) {
        this.talent_id = talent_id;
        this.fullname = fullname;
        this.height = height;
        this.gender = gender;
        this.talentDisplayPhoto = talentDisplayPhoto;
        this.categoryIds = categoryIds;
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

    public String getCategoryIds() {
        return categoryIds;
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
                ", categoryIds='" + categoryIds + '\'' +
                ", talentAge=" + talentAge +
                ", location=" + location +
                '}';
    }
}
