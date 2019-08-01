package com.trosales.hireusapp.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.github.nikartm.support.StripedProcessButton;
import com.trosales.hireusapp.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LoginActivity extends AppCompatActivity {
    @BindView(R.id.btnLoginUser) StripedProcessButton btnLoginUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        btnLoginUser.setOnClickListener(v -> {
            startActivity(new Intent(this, HomeActivity.class));
        });
    }
}
