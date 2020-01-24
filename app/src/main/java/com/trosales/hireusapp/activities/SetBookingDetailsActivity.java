package com.trosales.hireusapp.activities;

import android.annotation.SuppressLint;
import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;

import android.util.Log;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.bumptech.glide.Glide;
import com.trosales.hireusapp.R;
import com.trosales.hireusapp.classes.commons.AppSecurity;
import com.trosales.hireusapp.classes.constants.EndPoints;
import com.trosales.hireusapp.classes.constants.Messages;
import com.trosales.hireusapp.classes.constants.Tags;
import com.trosales.hireusapp.fragments.SetWorkingDatesBottomSheetFragment;

import org.apache.commons.lang3.RandomStringUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.pedant.SweetAlert.SweetAlertDialog;
import customfonts.MyTextView;

public class SetBookingDetailsActivity extends AppCompatActivity {
    @BindView(R.id.imgTalentDisplayPhoto)
    ImageView imgTalentDisplayPhoto;
    @BindView(R.id.lblTalentFullname)
    MyTextView lblTalentFullName;
    @BindView(R.id.lblTalentCategories)
    MyTextView lblTalentCategories;
    public static MyTextView lblSelectedDate;
    public static String selectedDates;
    @BindView(R.id.btnSetWorkingDates)
    Button btnSetWorkingDates;
    @BindView(R.id.txtBookingWorkingHours)
    EditText txtBookingWorkingHours;
    @BindView(R.id.txtBookingEventTitle)
    EditText txtBookingEventTitle;
    @BindView(R.id.txtBookingVenueOrLocation)
    EditText txtBookingVenueOrLocation;
    @BindView(R.id.txtBookingTalentFee)
    EditText txtBookingTalentFee;
    @BindView(R.id.txtBookingOtherDetails)
    EditText txtBookingOtherDetails;
    @BindView(R.id.btnSendOffer)
    Button btnSendOffer;

    private String selectedClientId, selectedTalentId;

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

        lblSelectedDate = findViewById(R.id.lblSelectedDate);

        lblTalentFullName.setText(bookingBundleArgs.getString("talent_full_name"));
        lblTalentCategories.setText(bookingBundleArgs.getString("talent_category"));

        selectedClientId = bookingBundleArgs.getString("client_id");
        selectedTalentId = bookingBundleArgs.getString("talent_id");

        btnSetWorkingDates.setOnClickListener(view -> {
            SetWorkingDatesBottomSheetFragment setWorkingDatesBottomSheetFragment = new SetWorkingDatesBottomSheetFragment();
            setWorkingDatesBottomSheetFragment.setCancelable(false);
            setWorkingDatesBottomSheetFragment.show(((SetBookingDetailsActivity) view.getContext()).getSupportFragmentManager(), "SetWorkingDatesBottomSheetFragment");
        });

        btnSendOffer.setOnClickListener(v -> {

            if(txtBookingWorkingHours.getText().toString().trim().isEmpty() ||
                    txtBookingEventTitle.getText().toString().trim().isEmpty() ||
                    txtBookingVenueOrLocation.getText().toString().trim().isEmpty() ||
                    txtBookingTalentFee.getText().toString().trim().isEmpty()){

                new SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE)
                        .setTitleText(Messages.WARNING_MSG)
                        .setContentText("Please fill out all required fields.")
                        .setConfirmClickListener(sweetAlertDialog -> {
                            if (txtBookingWorkingHours.getText().toString().trim().isEmpty()) {
                                txtBookingWorkingHours.setError("Please enter booking working hours!");
                            }

                            if (txtBookingEventTitle.getText().toString().trim().isEmpty()) {
                                txtBookingEventTitle.setError("Please enter booking event title!");
                            }

                            if (txtBookingVenueOrLocation.getText().toString().trim().isEmpty()) {
                                txtBookingVenueOrLocation.setError("Please enter booking venue/location!");
                            }

                            if (txtBookingTalentFee.getText().toString().trim().isEmpty()) {
                                txtBookingTalentFee.setError("Please enter booking fee!");
                            }

                            sweetAlertDialog.dismissWithAnimation();
                        })
                        .show();
            }else{
                new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("Are you sure?")
                        .setContentText("Please check inputted details before proceeding.")
                        .setConfirmText("Yes")
                        .setConfirmClickListener(sDialog -> {
                            sDialog.dismissWithAnimation();
                            addToBookingList(getBookingParams());
                        })
                        .setCancelText("No")
                        .setCancelClickListener(SweetAlertDialog::dismissWithAnimation)
                        .show();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private HashMap<String, String> getBookingParams() {
        HashMap<String, String> params = new HashMap<>();
        params.put("talent_id", selectedTalentId);
        params.put("client_id", selectedClientId);
        params.put("booking_event_title", txtBookingEventTitle.getText().toString().trim());
        params.put("booking_talent_fee", txtBookingTalentFee.getText().toString().trim());
        params.put("booking_venue_location", txtBookingVenueOrLocation.getText().toString().trim());
        params.put("booking_date", selectedDates.trim());
        params.put("booking_time", txtBookingWorkingHours.getText().toString().trim());
        params.put("booking_other_details", txtBookingOtherDetails.getText().toString().trim());

        return params;
    }

    private void addToBookingList(HashMap<String, String> params) {
        SweetAlertDialog pDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#48b1bf"));
        pDialog.setTitleText(Messages.PLEASE_WAIT_WHILE_ADDING_TO_BOOKING_LIST_MSG);
        pDialog.setCancelable(false);
        pDialog.show();

        AndroidNetworking.post(EndPoints.ADD_TO_BOOKING_LIST_URL)
                .addBodyParameter("booking_generated_id", RandomStringUtils.randomAlphanumeric(8).toUpperCase())
                .addBodyParameter("talent_id", params.get("talent_id"))
                .addBodyParameter("client_id", params.get("client_id"))
                .addBodyParameter("booking_event_title", params.get("booking_event_title"))
                .addBodyParameter("booking_talent_fee", params.get("booking_talent_fee"))
                .addBodyParameter("booking_venue_location", params.get("booking_venue_location"))
                .addBodyParameter("booking_date", params.get("booking_date"))
                .addBodyParameter("booking_time", params.get("booking_time"))
                .addBodyParameter("booking_other_details", params.get("booking_other_details"))
                .setTag(Tags.SET_BOOKING_DATE_AND_TIME_ACTIVITY)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        pDialog.dismiss();
                        getClientBookingResponse(response);
                    }

                    @SuppressLint("LongLogTag")
                    @Override
                    public void onError(ANError anError) {
                        pDialog.dismiss();
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

                        Log.e(Tags.SET_BOOKING_DATE_AND_TIME_ACTIVITY, errorResponse);
                    }
                });
    }

    private void getClientBookingResponse(JSONObject response) {
        try {
            String status = response.getString("status");
            String msg = response.has("msg") ? response.getString("msg") : "";

            if (status.equals("OK")) {
                new SweetAlertDialog(this, SweetAlertDialog.SUCCESS_TYPE)
                        .setTitleText("Congratulations! Your offer was successfully send to the chosen talent/s.")
                        .setContentText(msg)
                        .setConfirmClickListener(sweetAlertDialog -> {
                            finish();
                            startActivity(new Intent(this, BookingListActivity.class));
                        })
                        .show();
            } else {
                new SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE)
                        .setTitleText("Oops..")
                        .setContentText(msg)
                        .show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
