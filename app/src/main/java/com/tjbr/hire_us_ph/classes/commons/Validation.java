package com.tjbr.hire_us_ph.classes.commons;

public class Validation {
    public static boolean checkEmail(String email){
        return !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    public static boolean checkEmailOrUsername(String emailOrUsername){
        return !emailOrUsername.isEmpty();
    }

    public static boolean checkPassword(String password){
        return !password.isEmpty() && password.length() >= 4 && password.length() <= 10;
    }

    public static boolean checkPasswordIfEmpty(String password){
        return !password.isEmpty();
    }
}
