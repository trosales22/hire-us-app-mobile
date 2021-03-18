package com.agentcoder.hire_us_ph.classes.beans;

import org.jetbrains.annotations.NotNull;

public class TalentCategory {
    private String categoryId,categoryName;

    public TalentCategory(String categoryId, String categoryName) {
        this.categoryId = categoryId;
        this.categoryName = categoryName;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    @NotNull
    @Override
    public String toString() {
        return categoryName;
    }
}
