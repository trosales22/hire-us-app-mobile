package com.trosales.hireusapp.classes.commons;

import android.app.Activity;
import android.view.WindowManager;

public class AppSecurity {
    public static void disableScreenshotRecording(Activity activity){
        activity.getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
    }
}
