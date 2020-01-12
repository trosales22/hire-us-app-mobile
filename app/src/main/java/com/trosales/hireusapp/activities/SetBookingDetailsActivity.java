package com.trosales.hireusapp.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.AppCompatButton;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.trosales.hireusapp.R;
import com.trosales.hireusapp.classes.commons.AppSecurity;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.pedant.SweetAlert.SweetAlertDialog;
import customfonts.MyTextView;

public class SetBookingDetailsActivity extends AppCompatActivity{
    @BindView(R.id.imgTalentDisplayPhoto) ImageView imgTalentDisplayPhoto;
    @BindView(R.id.lblTalentFullname) MyTextView lblTalentFullname;
    @BindView(R.id.lblTalentCategories) MyTextView lblTalentCategories;
    @BindView(R.id.lblSelectedSchedule) MyTextView lblSelectedSchedule;
    @BindView(R.id.txtBookingEventTitle) EditText txtBookingEventTitle;
    @BindView(R.id.txtBookingVenueOrLocation) EditText txtBookingVenueOrLocation;
    @BindView(R.id.txtBookingTalentFee) EditText txtBookingTalentFee;
    @BindView(R.id.btnProceed) AppCompatButton btnProceed;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_booking_details);
        ButterKnife.bind(this);

        AppSecurity.disableScreenshotRecording(this);

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        Bundle bookingBundleArgs = getIntent().getExtras();

        Glide
                .with(getApplicationContext())
                .load(bookingBundleArgs.getString("talent_profile_pic"))
                .placeholder(R.drawable.no_profile_pic)
                .into(imgTalentDisplayPhoto);

        lblTalentFullname.setText(bookingBundleArgs.getString("talent_fullname"));
        lblTalentCategories.setText(bookingBundleArgs.getString("talent_category"));

        StringBuilder sbBookingDetails = new StringBuilder();
        sbBookingDetails
                .append("Selected Date: \n")
                .append(bookingBundleArgs.getString("selected_date")).append("\n\n")
                .append("Selected Time: \n")
                .append(bookingBundleArgs.getString("selected_time")).append("\n");

        lblSelectedSchedule.setText(sbBookingDetails.toString());

        btnProceed.setOnClickListener(v -> {

            if(txtBookingEventTitle.getText().toString().trim().isEmpty()){
                showErrorMessage("Please enter booking event title!");
            } else if(txtBookingVenueOrLocation.getText().toString().trim().isEmpty()){
                showErrorMessage("Please enter booking venue/location!");
            } else if(txtBookingTalentFee.getText().toString().trim().isEmpty()){
                showErrorMessage("Please enter booking fee!");
            }else{
//                Intent intent = new Intent(this, CheckoutActivity.class);
//                bundle.putString("temp_booking_date", bundle.getString("selected_date"));
//                bundle.putString("temp_booking_time", bundle.getString("selected_time"));
//                bundle.putString("temp_talent_id", SharedPrefManager.getInstance(getApplicationContext()).getTalentId());
//                bundle.putString("talent_fullname", bundle.getString("talent_fullname"));
//                bundle.putString("talent_profile_pic", bundle.getString("talent_profile_pic"));
//                bundle.putString("talent_category", bundle.getString("talent_category"));
//                bundle.putString("booking_event_title", txtBookingEventTitle.getText().toString().trim());
//                bundle.putString("booking_preferred_venue", txtBookingVenueOrLocation.getText().toString().trim());
//                bundle.putString("booking_talent_fee", txtBookingTalentFee.getText().toString().trim());
//                intent.putExtras(bundle);
//                startActivity(intent);

                new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("Are you sure?")
                        .setContentText("Please check inputted details before proceeding.")
                        .setConfirmText("Yes")
                        .setConfirmClickListener(sDialog -> {
                            new SweetAlertDialog(this, SweetAlertDialog.SUCCESS_TYPE)
                                    .setTitleText("Congratulations!")
                                    .setContentText("Booking successful! Please check your email for more info. Thank you.")
                                    .setConfirmClickListener(sweetAlertDialog -> startActivity(new Intent(this, MainActivity.class)))
                                    .show();
                        })
                        .setCancelText("No")
                        .setCancelClickListener(SweetAlertDialog::dismissWithAnimation)
                        .show();
            }
        });
    }

    private void showErrorMessage(String msg){
        new SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE)
                .setTitleText("Oops...")
                .setContentText(msg)
                .show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
