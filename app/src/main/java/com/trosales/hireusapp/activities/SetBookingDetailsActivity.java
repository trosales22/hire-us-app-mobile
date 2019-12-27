package com.trosales.hireusapp.activities;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import androidx.annotation.RequiresApi;
import android.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.AppCompatButton;
import android.text.Html;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.trosales.hireusapp.R;
import com.trosales.hireusapp.classes.commons.AppSecurity;
import com.trosales.hireusapp.classes.commons.SharedPrefManager;
import com.trosales.hireusapp.classes.constants.EndPoints;
import com.trosales.hireusapp.classes.constants.Tags;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SetBookingDetailsActivity extends AppCompatActivity{
    @BindView(R.id.listDateSchedule) ListView listDateSchedule;
    @BindView(R.id.listTimeSchedule) ListView listTimeSchedule;
    @BindView(R.id.lblSelectedSchedule) TextView lblSelectedSchedule;
    @BindView(R.id.lblBookingVenueCaption) TextView lblBookingVenueCaption;
    @BindView(R.id.btnComputeTotal) AppCompatButton btnComputeTotal;
    @BindView(R.id.btnSetBookingVenue) AppCompatButton btnSetBookingVenue;
    @BindView(R.id.btnProceedToCheckout) AppCompatButton btnProceedToCheckout;

    private List<String> availableDateScheduleItems, availableTimeScheduleItems;
    private SparseBooleanArray availableDateSparseBooleanArray, availableTimeSparseBooleanArray;
    private StringBuilder sbSelectedDateSched, sbSelectedTimeSched, sbReservedDate, sbReservedTime;
    private int dateScheduleCount = 0, timeScheduleCount = 0;
    private double totalAmountDouble;
    private String selectedDate, selectedTime, totalHours, selectedVenue;
    private Bundle bundle;
    private DecimalFormat formatter = new DecimalFormat("#,###.00");

    @RequiresApi(api = Build.VERSION_CODES.P)
    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_booking_details);
        ButterKnife.bind(this);

        AppSecurity.disableScreenshotRecording(this);

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        bundle = getIntent().getExtras();

        availableDateScheduleItems = new ArrayList<>();
        availableTimeScheduleItems = new ArrayList<>();

        availableDateSparseBooleanArray = new SparseBooleanArray();
        availableTimeSparseBooleanArray = new SparseBooleanArray();

        sbSelectedDateSched = new StringBuilder();
        sbSelectedTimeSched = new StringBuilder();
        sbReservedDate = new StringBuilder();
        sbReservedTime = new StringBuilder();

        getAlreadyReservedScheduleOfTalent();

        btnComputeTotal.setOnClickListener(v -> {
            double ratePerHour = Double.parseDouble(Objects.requireNonNull(bundle.getString("talent_rate_per_hour")).replace(",", ""));
            totalAmountDouble = Double.parseDouble(String.valueOf((ratePerHour * (timeScheduleCount) * (dateScheduleCount) )));
            String totalAmountString;

            if(dateScheduleCount < 1){
                selectedDate = "N/A";
            }else{
                selectedDate = removeLastCharacter(sbSelectedDateSched.toString());
            }

            if(timeScheduleCount < 1){
                totalHours = "N/A";
                selectedTime = "N/A";
                totalAmountString = "N/A";
            }else if(timeScheduleCount == 1){
                totalHours = (timeScheduleCount * dateScheduleCount) + " hr";
                selectedTime = removeLastCharacter(sbSelectedTimeSched.toString());
                totalAmountString = Html.fromHtml("&#8369;") + formatter.format(totalAmountDouble);
            }else{
                totalHours = (timeScheduleCount * dateScheduleCount) + " hrs";
                selectedTime = removeLastCharacter(sbSelectedTimeSched.toString());
                totalAmountString = Html.fromHtml("&#8369;") + formatter.format(totalAmountDouble);
            }

            StringBuilder sbOutput = new StringBuilder();
            sbOutput
                    .append(Html.fromHtml("<b>Selected date: </b>"))
                    .append(selectedDate)
                    .append("\n")
                    .append(Html.fromHtml("<b>Selected time: </b>"))
                    .append(selectedTime)
                    .append("\n")
                    .append(Html.fromHtml("<b>Rate per hour: </b>"))
                    .append(Html.fromHtml("&#8369;"))
                    .append(formatter.format(ratePerHour))
                    .append("\n")
                    .append(Html.fromHtml("<b>Total hours: </b>"))
                    .append(totalHours)
                    .append("\n")
                    .append(Html.fromHtml("<b>Total amount: </b>"))
                    .append(totalAmountString);

            lblSelectedSchedule.setText(sbOutput.toString());
        });

        btnSetBookingVenue.setOnClickListener(v -> {
            LayoutInflater layoutInflaterAndroid = LayoutInflater.from(SetBookingDetailsActivity.this);
            View mView = layoutInflaterAndroid.inflate(R.layout.user_input_dialog_box, null);
            AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(SetBookingDetailsActivity.this, R.style.AlertDialogTheme);
            alertDialogBuilderUserInput.setView(mView);

            final EditText userInputDialogEditText = mView.findViewById(R.id.userInputDialog);


            alertDialogBuilderUserInput
                    .setCancelable(false)
                    .setPositiveButton("Set", (dialogBox, id) -> {
                        selectedVenue = userInputDialogEditText.getText().toString().trim();
                        lblBookingVenueCaption.setText("Booking Venue: " + selectedVenue);
                    })
                    .setNegativeButton("Cancel",
                            (dialogBox, id) -> dialogBox.cancel());

            AlertDialog alertDialogAndroid = alertDialogBuilderUserInput.create();
            alertDialogAndroid.show();
        });

        btnProceedToCheckout.setOnClickListener(v -> {
            Log.d("debug", "selectedDate: " + selectedDate);
            Log.d("debug", "selectedTime: " + selectedTime);
            Log.d("debug", "totalHours: " + totalHours);
            Log.d("debug", "totalAmountDouble: " + totalAmountDouble);
            Log.d("debug", "selectedVenue: " + selectedVenue);

            if(selectedDate == null && selectedTime == null){
                Toast.makeText(this, "Please choose your preferred date & time. Thank you.", Toast.LENGTH_SHORT).show();
                return;
            }

            if(selectedVenue == null){
                Toast.makeText(this, "Please choose your preferred venue. Thank you.", Toast.LENGTH_SHORT).show();
                return;
            }

            if(totalHours == null){
                Toast.makeText(this, "Please click 'Compute Total' to auto calculate your booked schedule. Thank you.", Toast.LENGTH_SHORT).show();
                return;
            }

            Intent intent = new Intent(this, CheckoutActivity.class);
            bundle.putString("temp_booking_date", selectedDate);
            bundle.putString("temp_booking_time", selectedTime);
            bundle.putString("temp_talent_id", SharedPrefManager.getInstance(getApplicationContext()).getTalentId());
            bundle.putString("talent_fullname", bundle.getString("talent_fullname"));
            bundle.putString("talent_profile_pic", bundle.getString("talent_profile_pic"));
            bundle.putString("talent_category", bundle.getString("talent_category"));
            bundle.putString("talent_rate_per_hour", bundle.getString("talent_rate_per_hour"));
            bundle.putString("total_hours", totalHours);
            bundle.putString("total_amount", formatter.format(totalAmountDouble));
            bundle.putString("selected_venue", selectedVenue);
            intent.putExtras(bundle);
            startActivity(intent);
        });
    }

    private List<String> getDatesUpToSpecificMonths(int months, String reservedDate){
        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
        Calendar c = Calendar.getInstance();
        List<String> availableDatesList = new ArrayList<>();

        try {
            c.setTime(sdf.parse(sdf.format(new Date())));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        int maxDay = c.getActualMaximum(Calendar.DAY_OF_MONTH) * months;
        for(int co=0; co<=maxDay; co++){
            c.add(Calendar.DATE, 1);

            if(reservedDate == null){
                availableDatesList.add(sdf.format(c.getTime()));
            }else if(!reservedDate.contains(sdf.format(c.getTime()))) {
                availableDatesList.add(sdf.format(c.getTime()));
            }
        }

        return availableDatesList;
    }

    private static String removeLastCharacter(String str) {
        String result = null;
        if ((str != null) && (str.length() > 0)) {
            result = str.substring(0, str.length() - 1);
        }
        return result;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void setMorningSchedule(String reservedTime){
        int initialValue = 1;
        String meridian;

        if(reservedTime == null){
            availableTimeScheduleItems.add("12-1 AM");
        }else if(!reservedTime.contains("12-1 AM")) {
            availableTimeScheduleItems.add("12-1 AM");
        }

        for(int i = 1; i <= 11; i++){
            initialValue++;

            if(initialValue >= 12){
                meridian = " PM";
            }else{
                meridian = " AM";
            }

            if(reservedTime == null){
                availableTimeScheduleItems.add(i + "-" + initialValue + meridian);
            }else if(!reservedTime.contains(i + "-" + initialValue + meridian)){
                availableTimeScheduleItems.add(i + "-" + initialValue + meridian);
            }
        }
    }

    private void setAfternoonSchedule(String reservedTime){
        int initialValue = 1;
        String meridian;

        if(reservedTime == null){
            availableTimeScheduleItems.add("12-1 PM");
        }else if(!reservedTime.contains("12-1 PM")) {
            availableTimeScheduleItems.add("12-1 PM");
        }

        for(int i = 1; i <= 11; i++){
            initialValue++;

            if(initialValue >= 12){
                meridian = " AM";
            }else{
                meridian = " PM";
            }

            if(reservedTime == null){
                availableTimeScheduleItems.add(i + "-" + initialValue + meridian);
            }else if(!reservedTime.contains(i + "-" + initialValue + meridian)) {
                availableTimeScheduleItems.add(i + "-" + initialValue + meridian);
            }
        }
    }

    private void getAlreadyReservedScheduleOfTalent(){
        AndroidNetworking
                .get(EndPoints.GET_ALREADY_RESERVED_SCHED_URL.concat("?talent_id={talent_id}"))
                .addPathParameter("talent_id", SharedPrefManager.getInstance(getApplicationContext()).getTalentId())
                .setTag(Tags.SET_BOOKING_DATE_AND_TIME_ACTIVITY)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        getAlreadyReservedScheduleOfTalentResponse(response);
                    }

                    @SuppressLint("LongLogTag")
                    @Override
                    public void onError(ANError anError) {
                        Log.e(Tags.SET_BOOKING_DATE_AND_TIME_ACTIVITY, anError.getErrorDetail());
                    }
                });
    }

    @TargetApi(Build.VERSION_CODES.P)
    private void getAlreadyReservedScheduleOfTalentResponse(JSONObject response){
        try {
            JSONArray array = response.getJSONArray("already_reserved_sched_list");

            if (response.has("flag") && response.has("msg")) {
                Log.d("debug", response.getString("msg"));
            } else {
                for (int i = 0; i < array.length(); i++) {
                    JSONObject object = array.getJSONObject(i);

                    sbReservedDate.append(object.getString("preferred_date"));
                    sbReservedDate.append(",");
                    sbReservedTime.append(object.getString("preferred_time"));
                    sbReservedTime.append(",");
                }

                Log.d("debug", "preferred_date: " + sbReservedDate.toString());
                Log.d("debug", "preferred_time: " + sbReservedTime.toString());
                setDateSchedule(removeLastCharacter(sbReservedDate.toString()));
                setTimeSchedule(removeLastCharacter(sbReservedTime.toString()));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @TargetApi(Build.VERSION_CODES.P)
    private void setTimeSchedule(String reservedTime){
        setMorningSchedule(reservedTime);
        setAfternoonSchedule(reservedTime);

        ArrayAdapter<String> timeScheduleAdapter = new ArrayAdapter<>
                (
                        this,
                        R.layout.custom_checkable_textview,
                        android.R.id.text1, availableTimeScheduleItems
                );

        listTimeSchedule.setAdapter(timeScheduleAdapter);

        listTimeSchedule.setOnItemClickListener((parent, view, position, id) -> {
            availableTimeSparseBooleanArray = listTimeSchedule.getCheckedItemPositions();
            sbSelectedTimeSched = new StringBuilder();

            int i = 0;

            while (i < availableTimeSparseBooleanArray.size()) {
                if (availableTimeSparseBooleanArray.valueAt(i)) {
                    sbSelectedTimeSched
                            .append(listTimeSchedule.getItemAtPosition(availableTimeSparseBooleanArray.keyAt(i)))
                            .append(",");
                }else{
                    availableTimeSparseBooleanArray.removeAt(i);
                }

                timeScheduleCount = availableTimeSparseBooleanArray.size();
                i++;
            }
        });
    }

    @TargetApi(Build.VERSION_CODES.P)
    private void setDateSchedule(String reservedDate){
        availableDateScheduleItems = getDatesUpToSpecificMonths(3, reservedDate);

        ArrayAdapter<String> dateScheduleAdapter = new ArrayAdapter<>
                (
                        this,
                        R.layout.custom_checkable_textview,
                        android.R.id.text1, availableDateScheduleItems
                );

        listDateSchedule.setAdapter(dateScheduleAdapter);

        listDateSchedule.setOnItemClickListener((parent, view, position, id) -> {
            availableDateSparseBooleanArray = listDateSchedule.getCheckedItemPositions();
            sbSelectedDateSched = new StringBuilder();

            int i = 0;

            while (i < availableDateSparseBooleanArray.size()) {
                if (availableDateSparseBooleanArray.valueAt(i)) {
                    sbSelectedDateSched
                            .append(listDateSchedule.getItemAtPosition(availableDateSparseBooleanArray.keyAt(i)))
                            .append(",");
                }else{
                    availableDateSparseBooleanArray.removeAt(i);
                }

                dateScheduleCount = availableDateSparseBooleanArray.size();
                i++;
            }
        });
    }
}
