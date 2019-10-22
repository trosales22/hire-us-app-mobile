package com.trosales.hireusapp.activities;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.AppCompatCheckBox;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.github.nikartm.support.StripedProcessButton;
import com.trosales.hireusapp.R;
import com.trosales.hireusapp.classes.commons.SharedPrefManager;
import com.trosales.hireusapp.classes.commons.Validation;
import com.trosales.hireusapp.classes.constants.EndPoints;
import com.trosales.hireusapp.classes.constants.Messages;
import com.trosales.hireusapp.classes.constants.Tags;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import spencerstudios.com.ezdialoglib.EZDialog;
import spencerstudios.com.ezdialoglib.Font;

public class LoginActivity extends AppCompatActivity {
    @BindView(R.id.frmLoginAccount_txtEmailOrUsername) EditText frmLoginAccount_txtEmailOrUsername;
    @BindView(R.id.frmLoginAccount_txtPassword)EditText frmLoginAccount_txtPassword;
    @BindView(R.id.chkShowPassword) AppCompatCheckBox chkShowPassword;
    @BindView(R.id.chkLoginAsTalentOrModel) AppCompatCheckBox chkLoginAsTalentOrModel;
    @BindView(R.id.btnLoginUser) StripedProcessButton btnLoginUser;
    @BindView(R.id.btnGoToForgotPasswordPage)TextView btnGoToForgotPasswordPage;
    @BindView(R.id.btnGoToSignUpPage) TextView btnGoToSignUpPage;

    private boolean loginAsTalent = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        AndroidNetworking.initialize(getApplicationContext());

        if(SharedPrefManager.getInstance(this).isLoggedIn()){
            finish();
            startActivity(new Intent(this, MainActivity.class));
        }

        chkShowPassword.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                chkShowPassword.setText(Messages.HIDE_PASSWORD_MSG);
                frmLoginAccount_txtPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            } else {
                chkShowPassword.setText(Messages.SHOW_PASSWORD_MSG);
                frmLoginAccount_txtPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
            }
        });

        chkLoginAsTalentOrModel.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                loginAsTalent = true;
            } else {
                loginAsTalent = false;
            }
        });

        btnLoginUser.setCornerRadius(10);
        btnLoginUser.setOnClickListener(v -> {
            final String emailOrUsername = frmLoginAccount_txtEmailOrUsername.getText().toString().trim();
            final String password = frmLoginAccount_txtPassword.getText().toString().trim();

            if (validateInputs(emailOrUsername, password)) {
                btnLoginUser.start();

                new android.os.Handler().postDelayed(
                        () -> {
                            loginUser(emailOrUsername, password);
                            btnLoginUser.stop();
                        }, 500
                );
            }
        });
    }

    public void onLoginFailed(String message) {
        new EZDialog.Builder(this)
                .setTitle(Messages.WARNING_MSG)
                .setMessage(message)
                .setPositiveBtnText(Messages.OKAY_MSG)
                .setHeaderColor(R.color.white)
                .setButtonTextColor(R.color.colorPrimary)
                .setTitleTextColor(R.color.white)
                .setMessageTextColor(R.color.black)
                .setFont(Font.COMFORTAA)
                .setCancelableOnTouchOutside(false)
                .OnPositiveClicked(() -> {
                    //todo
                })
                .build();
    }

    public boolean validateInputs(String emailOrUsername, String password) {
        boolean valid = true;

        if(!Validation.checkEmailOrUsername(emailOrUsername)){
            frmLoginAccount_txtEmailOrUsername.setError(Messages.VALID_EMAIL_OR_USERNAME);
            valid = false;
        }else{
            frmLoginAccount_txtEmailOrUsername.setError(null);
        }

        if(!Validation.checkPasswordIfEmpty(password)){
            frmLoginAccount_txtPassword.setError(Messages.VALID_PASSWORD);
            valid = false;
        }else{
            frmLoginAccount_txtPassword.setError(null);
        }

        return valid;
    }

    public void loginUser(final String emailOrUsername, final String password) {
        String finalLoginUrl;

        if(loginAsTalent){
            finalLoginUrl = EndPoints.LOGIN_TALENT_URL;
        }else{
            finalLoginUrl = EndPoints.LOGIN_USER_URL;
        }

        AndroidNetworking.post(finalLoginUrl)
                .addBodyParameter("username_or_email", emailOrUsername)
                .addBodyParameter("password", password)
                .setTag(Tags.LOGIN_ACTIVITY)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        getResponse(response, emailOrUsername);
                    }

                    @Override
                    public void onError(ANError anError) {
                        String errorResponse = "\n\nCode: " +
                                anError.getErrorCode() +
                                "\nDetail: " +
                                anError.getErrorDetail() +
                                "\nBody: " +
                                anError.getErrorBody() +
                                "\nResponse: " +
                                anError.getResponse() +
                                "\nMessage: " +
                                anError.getMessage();

                        Log.e(Tags.LOGIN_ACTIVITY, errorResponse);
                    }
                });
    }

    private void getResponse(JSONObject response, String emailOrUsername){
        try {
            String status = response.getString("status");
            String msg = response.has("msg") ? response.getString("msg") : "";

            if(status.equals("OK")){
                SharedPrefManager.getInstance(getApplicationContext()).loginUser(emailOrUsername);

                if(loginAsTalent){
                    SharedPrefManager.getInstance(getApplicationContext()).saveUserRole("TALENT_MODEL");
                }else{
                    SharedPrefManager.getInstance(getApplicationContext()).saveUserRole("CLIENT");
                }

                finish();
                startActivity(new Intent(this, MainActivity.class));
            }else{
                onLoginFailed(msg);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
