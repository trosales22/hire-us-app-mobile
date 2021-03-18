package com.agentcoder.hire_us_ph.classes.beans;

import org.jetbrains.annotations.NotNull;

public class CategoryDetector {
    private boolean isCelebrity,isComedian,isDancer,isDiskJockey,isEmceeOrHost,
        isModelOrBrandAmbassador,isMovieOrTvTalent,isSingerOrBand;

    public CategoryDetector(boolean isCelebrity, boolean isComedian, boolean isDancer, boolean isDiskJockey, boolean isEmceeOrHost, boolean isModelOrBrandAmbassador, boolean isMovieOrTvTalent, boolean isSingerOrBand) {
        this.isCelebrity = isCelebrity;
        this.isComedian = isComedian;
        this.isDancer = isDancer;
        this.isDiskJockey = isDiskJockey;
        this.isEmceeOrHost = isEmceeOrHost;
        this.isModelOrBrandAmbassador = isModelOrBrandAmbassador;
        this.isMovieOrTvTalent = isMovieOrTvTalent;
        this.isSingerOrBand = isSingerOrBand;
    }

    public boolean isCelebrity() {
        return isCelebrity;
    }

    public boolean isComedian() {
        return isComedian;
    }

    public boolean isDancer() {
        return isDancer;
    }

    public boolean isDiskJockey() {
        return isDiskJockey;
    }

    public boolean isEmceeOrHost() {
        return isEmceeOrHost;
    }

    public boolean isModelOrBrandAmbassador() {
        return isModelOrBrandAmbassador;
    }

    public boolean isMovieOrTvTalent() {
        return isMovieOrTvTalent;
    }

    public boolean isSingerOrBand() {
        return isSingerOrBand;
    }

    @NotNull
    @Override
    public String toString() {
        return "CategoryDetector{" +
                "isCelebrity=" + isCelebrity +
                ", isComedian=" + isComedian +
                ", isDancer=" + isDancer +
                ", isDiskJockey=" + isDiskJockey +
                ", isEmceeOrHost=" + isEmceeOrHost +
                ", isModelOrBrandAmbassador=" + isModelOrBrandAmbassador +
                ", isMovieOrTvTalent=" + isMovieOrTvTalent +
                ", isSingerOrBand=" + isSingerOrBand +
                '}';
    }
}
