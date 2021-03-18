package com.agentcoder.hire_us_ph.classes.commons;

import com.agentcoder.hire_us_ph.classes.beans.CategoryDetector;
import com.agentcoder.hire_us_ph.classes.constants.CategoriesConstants;

import java.util.ArrayList;

public class CategorySetter {
    public static String getTalentCategory(CategoryDetector categoryDetector){
        ArrayList<String> talentCategoriesArrayList = new ArrayList<>();
        StringBuilder sbTalentCategories = new StringBuilder();

        if(categoryDetector.isCelebrity()){
            talentCategoriesArrayList.add(CategoriesConstants.CELEBRITY);
        }

        if(categoryDetector.isComedian()){
            talentCategoriesArrayList.add(CategoriesConstants.COMEDIANS);
        }

        if(categoryDetector.isDancer()){
            talentCategoriesArrayList.add(CategoriesConstants.DANCER);
        }

        if(categoryDetector.isDiskJockey()){
            talentCategoriesArrayList.add(CategoriesConstants.DISK_JOCKEY);
        }

        if(categoryDetector.isEmceeOrHost()){
            talentCategoriesArrayList.add(CategoriesConstants.EMCEE_HOST);
        }

        if(categoryDetector.isModelOrBrandAmbassador()){
            talentCategoriesArrayList.add(CategoriesConstants.MODELS_BA);
        }

        if(categoryDetector.isMovieOrTvTalent()){
            talentCategoriesArrayList.add(CategoriesConstants.MOVIE_TV_TALENTS);
        }

        if(categoryDetector.isSingerOrBand()){
            talentCategoriesArrayList.add(CategoriesConstants.SINGER_BAND);
        }

        for(String category : talentCategoriesArrayList){
            sbTalentCategories.append(category).append(",");
        }

        //convert StringBuffer to String
        String strList = sbTalentCategories.toString();

        //remove last comma from String if you want
        if( strList.length() > 0 )
            strList = strList.substring(0, strList.length() - 1);

        return strList;
    }
}
