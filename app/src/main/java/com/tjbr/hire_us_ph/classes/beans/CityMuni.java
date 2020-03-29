package com.tjbr.hire_us_ph.classes.beans;

import org.jetbrains.annotations.NotNull;

public class CityMuni {
    private String cityMuniId,cityMuniDescription,cityMuniCode;

    public CityMuni(String cityMuniId, String cityMuniDescription, String cityMuniCode) {
        this.cityMuniId = cityMuniId;
        this.cityMuniDescription = cityMuniDescription;
        this.cityMuniCode = cityMuniCode;
    }

    public String getCityMuniId() {
        return cityMuniId;
    }

    public String getCityMuniDescription() {
        return cityMuniDescription;
    }

    public String getCityMuniCode() {
        return cityMuniCode;
    }

    @NotNull
    @Override
    public String toString() {
        return cityMuniDescription;
    }
}
