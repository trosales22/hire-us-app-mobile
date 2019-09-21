package com.trosales.hireusapp.classes.commons;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

public class SharedPrefManager {
    @SuppressLint("StaticFieldLeak")
    private static SharedPrefManager mInstance;
    @SuppressLint("StaticFieldLeak")
    private static Context mContext;

    private static final String SHARED_PREF_NAME = "mySharedPref";
    private static final String KEY_USER = "keyUser";
    private static final String KEY_USER_ID = "keyUserId";
    private static final String KEY_USER_ROLE = "keyUserRole";
    private static final String KEY_TALENT_ID = "keyTalentId";

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

    public void saveTalentId(String talentId){
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString(KEY_TALENT_ID, talentId);
        editor.apply();
    }

    public void saveUserRole(String userRole){
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString(KEY_USER_ROLE, userRole);
        editor.apply();
    }

    public String getEmailOrUsername(){
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_USER,null);
    }

    public String getUserId(){
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_USER_ID,null);
    }

    public String getTalentId(){
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_TALENT_ID,null);
    }

    public String getUserRole(){
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_USER_ROLE,null);
    }
}
