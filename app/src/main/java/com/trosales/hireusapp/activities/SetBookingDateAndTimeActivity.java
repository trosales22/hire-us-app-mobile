package com.trosales.hireusapp.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.text.Html;
import android.util.SparseBooleanArray;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import com.trosales.hireusapp.R;

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

public class SetBookingDateAndTimeActivity extends AppCompatActivity{
    @BindView(R.id.listDateSchedule) ListView listDateSchedule;
    @BindView(R.id.listTimeSchedule) ListView listTimeSchedule;
    @BindView(R.id.lblSelectedSchedule) TextView lblSelectedSchedule;
    @BindView(R.id.btnComputeTotal) AppCompatButton btnComputeTotal;
    @BindView(R.id.btnProceedToCheckout) AppCompatButton btnProceedToCheckout;

    private List<String> availableDateScheduleItems, availableTimeScheduleItems;
    private SparseBooleanArray availableDateSparseBooleanArray, availableTimeSparseBooleanArray;
    private StringBuilder sbSelectedDateSched,sbSelectedTimeSched;
    private int dateScheduleCount = 0, timeScheduleCount = 0;

    @RequiresApi(api = Build.VERSION_CODES.P)
    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_booking_date_and_time);
        ButterKnife.bind(this);

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        availableDateScheduleItems = new ArrayList<>();
        availableTimeScheduleItems = new ArrayList<>();

        availableDateSparseBooleanArray = new SparseBooleanArray();
        availableTimeSparseBooleanArray = new SparseBooleanArray();

        sbSelectedDateSched = new StringBuilder();
        sbSelectedTimeSched = new StringBuilder();

        setDateSchedule();
        setTimeSchedule();

        btnComputeTotal.setOnClickListener(v -> {
            DecimalFormat formatter = new DecimalFormat("#,###.00");
            double ratePerHour = Double.parseDouble("1000");
            double totalAmountDouble = Double.parseDouble(String.valueOf((1000 * (timeScheduleCount) * (dateScheduleCount) )));
            String selectedDate,selectedTime;
            String totalHours;
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
                totalHours = timeScheduleCount + " hr";
                selectedTime = removeLastCharacter(sbSelectedTimeSched.toString());
                totalAmountString = Html.fromHtml("&#8369;") + formatter.format(totalAmountDouble);
            }else{
                totalHours = timeScheduleCount + " hrs";
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

        btnProceedToCheckout.setOnClickListener(v -> {
            startActivity(new Intent(this, CheckoutActivity.class));
        });
    }

    private List<String> getDatesUpToSpecificMonths(int months){
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
            availableDatesList.add(sdf.format(c.getTime()));
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

    private void setMorningSchedule(){
        int initialValue = 1;
        String meridian = " AM";

        availableTimeScheduleItems.add("12-1 AM");

        for(int i = 1; i <= 11; i++){
            initialValue++;

            if(initialValue >= 12){
                meridian = " PM";
            }

            availableTimeScheduleItems.add(i + "-" + initialValue + meridian);
        }
    }

    private void setAfternoonSchedule(){
        int initialValue = 1;
        String meridian = " AM";

        availableTimeScheduleItems.add("12-1 PM");

        for(int i = 1; i <= 11; i++){
            initialValue++;

            if(initialValue >= 12){
                meridian = " AM";
            }

            availableTimeScheduleItems.add(i + "-" + initialValue + meridian);
        }
    }

    @RequiresApi(Build.VERSION_CODES.P)
    private void setTimeSchedule(){
        setMorningSchedule();
        setAfternoonSchedule();

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

    @RequiresApi(Build.VERSION_CODES.P)
    private void setDateSchedule(){
        availableDateScheduleItems = getDatesUpToSpecificMonths(3);

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
