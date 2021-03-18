package com.agentcoder.hire_us_ph.classes.beans;

import org.jetbrains.annotations.NotNull;

public class Provinces {
    private String provinceId,provinceDesc,provinceCode;

    public Provinces(String provinceId, String provinceDesc, String provinceCode) {
        this.provinceId = provinceId;
        this.provinceDesc = provinceDesc;
        this.provinceCode = provinceCode;
    }

    public String getProvinceId() {
        return provinceId;
    }

    public String getProvinceDesc() {
        return provinceDesc;
    }

    public String getProvinceCode() {
        return provinceCode;
    }

    @NotNull
    @Override
    public String toString() {
        return provinceDesc;
    }
}
