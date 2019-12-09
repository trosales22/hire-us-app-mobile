package com.trosales.hireusapp.activities;

import android.content.Intent;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import com.trosales.hireusapp.R;
import com.trosales.hireusapp.classes.commons.AppSecurity;

public class SplashScreenActivity extends AppCompatActivity {

    Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        AppSecurity.disableScreenshotRecording(this);

        handler=new Handler();
        handler.postDelayed(() -> {
            Intent intent=new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent);
            finish();
        },3000);
    }
}
