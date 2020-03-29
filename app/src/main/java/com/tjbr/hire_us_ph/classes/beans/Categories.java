package com.tjbr.hire_us_ph.classes.beans;

import com.yalantis.filter.model.FilterModel;

import org.jetbrains.annotations.NotNull;

public class Categories implements FilterModel {
    private String text;
    private int color;

    public Categories(String text, int color) {
        this.text = text;
        this.color = color;
    }

    @NotNull
    @Override
    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public int getColor() {
        return color;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Categories)) return false;

        Categories tag = (Categories) o;

        if (getColor() != tag.getColor()) return false;
        return getText().equals(tag.getText());

    }

    @Override
    public int hashCode() {
        int result = getText().hashCode();
        result = 31 * result + getColor();
        return result;
    }
}
