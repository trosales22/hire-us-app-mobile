package com.trosales.hireusapp.classes.beans;

import org.jetbrains.annotations.NotNull;

public class Location {
    String province,cityMuni,barangay,bldgVillage,zipCode;

    public Location(String province, String cityMuni, String barangay, String bldgVillage, String zipCode) {
        this.province = province;
        this.cityMuni = cityMuni;
        this.barangay = barangay;
        this.bldgVillage = bldgVillage;
        this.zipCode = zipCode;
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
                "province='" + province + '\'' +
                ", cityMuni='" + cityMuni + '\'' +
                ", barangay='" + barangay + '\'' +
                ", bldgVillage='" + bldgVillage + '\'' +
                ", zipCode='" + zipCode + '\'' +
                '}';
    }
}