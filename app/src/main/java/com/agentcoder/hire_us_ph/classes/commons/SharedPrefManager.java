package com.agentcoder.hire_us_ph.classes.commons;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashMap;

public class SharedPrefManager {
    @SuppressLint("StaticFieldLeak")
    private static SharedPrefManager mInstance;
    @SuppressLint("StaticFieldLeak")
    private static Context mContext;

    private static final String SHARED_PREF_NAME = "mySharedPref";
    private static final String KEY_USER = "keyUser";
    private static final String KEY_USER_ID = "keyUserId";
    private static final String KEY_USER_ROLE = "keyUserRole";
    private static final String KEY_FILTER_PROVINCE = "keyFilterProvince";
    private static final String KEY_FILTER_CITY_MUNI = "keyFilterCityMuni";
    private static final String KEY_FILTER_AGE_FROM = "keyFilterAgeFrom";
    private static final String KEY_FILTER_AGE_TO = "keyFilterAgeTo";
    private static final String KEY_FILTER_HEIGHT_FROM = "keyFilterHeightFrom";
    private static final String KEY_FILTER_HEIGHT_TO = "keyFilterHeightTo";
    private static final String KEY_FILTER_RATE_PER_HOUR_FROM = "keyFilterRatePerHourFrom";
    private static final String KEY_FILTER_RATE_PER_HOUR_TO = "keyFilterRatePerHourTo";
    private static final String KEY_FILTER_GENDER = "keyFilterGender";

    private SharedPrefManager(Context context) {
        mContext = context;
    }

    public static synchronized SharedPrefManager getInstance(Context context){
        if(mInstance == null){
            mInstance = new SharedPrefManager(context);
        }

        return mInstance;
    }

    public void loginUser(String emailOrUsername){
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString(KEY_USER, emailOrUsername);
        editor.apply();
    }

    public boolean isLoggedIn(){
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_USER, null) != null;
    }

    public void logoutUser(){
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
    }

    public void saveUserIdSession(String userId){
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString(KEY_USER_ID, userId);
        editor.apply();
    }

    public void saveUserRole(String userRole){
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString(KEY_USER_ROLE, userRole);
        editor.apply();
    }

    public void setFilteringOption(HashMap<String, String> filteringOption){
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_FILTER_PROVINCE, filteringOption.get("province_code"));
        editor.putString(KEY_FILTER_CITY_MUNI, filteringOption.get("city_muni_code"));
        editor.putString(KEY_FILTER_AGE_FROM, filteringOption.get("age_from"));
        editor.putString(KEY_FILTER_AGE_TO, filteringOption.get("age_to"));
        editor.putString(KEY_FILTER_HEIGHT_FROM, filteringOption.get("height_from"));
        editor.putString(KEY_FILTER_HEIGHT_TO, filteringOption.get("height_to"));
        editor.putString(KEY_FILTER_RATE_PER_HOUR_FROM, filteringOption.get("rate_per_hour_from"));
        editor.putString(KEY_FILTER_RATE_PER_HOUR_TO, filteringOption.get("rate_per_hour_to"));
        editor.putString(KEY_FILTER_GENDER, filteringOption.get("gender"));
        editor.apply();
    }

    public HashMap<String, String> getFilteringOption(){
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        HashMap<String, String> filteringOption = new HashMap<>();

        filteringOption.put("province_code", sharedPreferences.getString(KEY_FILTER_PROVINCE, null));
        filteringOption.put("city_muni_code", sharedPreferences.getString(KEY_FILTER_CITY_MUNI, null));
        filteringOption.put("age_from", sharedPreferences.getString(KEY_FILTER_AGE_FROM, null));
        filteringOption.put("age_to", sharedPreferences.getString(KEY_FILTER_AGE_TO, null));
        filteringOption.put("height_from", sharedPreferences.getString(KEY_FILTER_HEIGHT_FROM, null));
        filteringOption.put("height_to", sharedPreferences.getString(KEY_FILTER_HEIGHT_TO, null));
        filteringOption.put("rate_per_hour_from", sharedPreferences.getString(KEY_FILTER_RATE_PER_HOUR_FROM, null));
        filteringOption.put("rate_per_hour_to", sharedPreferences.getString(KEY_FILTER_RATE_PER_HOUR_TO, null));
        filteringOption.put("gender", sharedPreferences.getString(KEY_FILTER_GENDER, null));

        return filteringOption;
    }

    public String getEmailOrUsername(){
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_USER,null);
    }

    public String getUserId(){
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_USER_ID,null);
    }

    public String getUserRole(){
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_USER_ROLE,null);
    }
}
