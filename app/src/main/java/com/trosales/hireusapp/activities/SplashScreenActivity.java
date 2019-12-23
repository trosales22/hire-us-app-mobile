package com.trosales.hireusapp.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.trosales.hireusapp.BuildConfig;
import com.trosales.hireusapp.R;
import com.trosales.hireusapp.classes.commons.AppSecurity;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SplashScreenActivity extends AppCompatActivity {

    @BindView(R.id.lblAppVersion) TextView lblAppVersion;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        ButterKnife.bind(this);

        AppSecurity.disableScreenshotRecording(this);

        lblAppVersion.setText("HIRE US PH v" + BuildConfig.VERSION_NAME);

        Handler handler = new Handler();
        handler.postDelayed(() -> {
            Intent intent=new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent);
            finish();
        },3000);
    }
}
