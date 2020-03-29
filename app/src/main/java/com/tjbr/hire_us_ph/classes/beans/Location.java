package com.tjbr.hire_us_ph.classes.beans;

import org.jetbrains.annotations.NotNull;

public class Location {
    private String region, province, cityMuni, barangay, bldgVillage, zipCode;

    public Location(String region, String province, String cityMuni, String barangay, String bldgVillage, String zipCode) {
        this.region = region;
        this.province = province;
        this.cityMuni = cityMuni;
        this.barangay = barangay;
        this.bldgVillage = bldgVillage;
        this.zipCode = zipCode;
    }

    public String getRegion() {
        return region;
    }

    public String getProvince() {
        return province;
    }

    public String getCityMuni() {
        return cityMuni;
    }

    public String getBarangay() {
        return barangay;
    }

    public String getBldgVillage() {
        return bldgVillage;
    }

    public String getZipCode() {
        return zipCode;
    }

    @NotNull
    @Override
    public String toString() {
        return "Location{" +
                "region='" + region + '\'' +
                ", province='" + province + '\'' +
                ", cityMuni='" + cityMuni + '\'' +
                ", barangay='" + barangay + '\'' +
                ", bldgVillage='" + bldgVillage + '\'' +
                ", zipCode='" + zipCode + '\'' +
                '}';
    }
}
