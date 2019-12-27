package com.trosales.hireusapp.classes.beans;

public class Regions {
    private String regionId,regionName,regionCode;

    public Regions(String regionId, String regionName, String regionCode) {
        this.regionId = regionId;
        this.regionName = regionName;
        this.regionCode = regionCode;
    }

    public String getRegionId() {
        return regionId;
    }

    public String getRegionName() {
        return regionName;
    }

    public String getRegionCode() {
        return regionCode;
    }

    @Override
    public String toString() {
        return regionName;
    }
}
