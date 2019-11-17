package com.trosales.hireusapp.classes.beans;

import org.jetbrains.annotations.NotNull;

public class Talents {
    private String talentId, talentName;

    public Talents(String talentId, String talentName) {
        this.talentId = talentId;
        this.talentName = talentName;
    }

    public String getTalentId() {
        return talentId;
    }

    public String getTalentName() {
        return talentName;
    }

    @NotNull
    @Override
    public String toString() {
        return talentName;
    }
}
