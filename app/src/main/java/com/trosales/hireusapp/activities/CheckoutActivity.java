package com.trosales.hireusapp.activities;

import android.annotation.SuppressLint;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.text.Html;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.trosales.hireusapp.R;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CheckoutActivity extends AppCompatActivity {
    @BindView(R.id.imgTalentDisplayPhoto) ImageView imgTalentDisplayPhoto;
    @BindView(R.id.lblTalentFullname) TextView lblTalentFullname;
    @BindView(R.id.lblTalentRatePerHour) TextView lblTalentRatePerHour;
    @BindView(R.id.lblTalentCategories) TextView lblTalentCategories;
    @BindView(R.id.lblBookingPreferredDate)TextView lblBookingPreferredDate;
    @BindView(R.id.lblBookingPreferredTime) TextView lblBookingPreferredTime;
    @BindView(R.id.lblBookingOtherDetails) TextView lblBookingOtherDetails;
    @BindView(R.id.btnCancelBooking) AppCompatButton btnCancelBooking;

    private String selectedDate, selectedTime, selectedTalentId;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);
        ButterKnife.bind(this);

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        Bundle bundle = getIntent().getExtras();
        selectedDate = Objects.requireNonNull(bundle).getString("temp_booking_date");
        selectedTime = Objects.requireNonNull(bundle).getString("temp_booking_time");
        selectedTalentId = Objects.requireNonNull(bundle).getString("temp_talent_id");

        Picasso
                .with(getApplicationContext())
                .load(bundle.getString("talent_profile_pic"))
                .placeholder(R.drawable.no_profile_pic)
                .into(imgTalentDisplayPhoto);

        lblTalentFullname.setText(bundle.getString("talent_fullname"));
        lblTalentRatePerHour.setText(Html.fromHtml("&#8369;" + bundle.getString("talent_rate_per_hour") + " per hour"));
        lblTalentCategories.setText(bundle.getString("talent_category"));
        lblBookingPreferredDate.setText(selectedDate);
        lblBookingPreferredTime.setText(selectedTime);

        StringBuilder sbBookingOtherDetails = new StringBuilder();
        sbBookingOtherDetails
                .append("Total hours: ")
                .append(bundle.getString("total_hours"))
                .append("\nTotal amount: ")
                .append(Html.fromHtml("&#8369;"))
                .append(bundle.getString("total_amount"));

        lblBookingOtherDetails.setText( sbBookingOtherDetails.toString());

        btnCancelBooking.setOnClickListener(v -> {
            //insert logic here
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
}
