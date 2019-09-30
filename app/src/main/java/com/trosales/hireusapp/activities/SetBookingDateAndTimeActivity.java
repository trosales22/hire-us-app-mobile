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
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.borax12.materialdaterangepicker.date.DatePickerDialog;
import com.borax12.materialdaterangepicker.time.RadialPickerLayout;
import com.borax12.materialdaterangepicker.time.TimePickerDialog;
import com.trosales.hireusapp.R;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SetBookingDateAndTimeActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {
    @BindView(R.id.btnChoosePreferredDate) Button btnChoosePreferredDate;
    @BindView(R.id.scheduleListView) ListView scheduleListView;
    @BindView(R.id.lblSelectedSchedule) TextView lblSelectedSchedule;
    @BindView(R.id.btnProceedToCheckout) AppCompatButton btnProceedToCheckout;

    private boolean mAutoHighlight = true;
    private ArrayList<String> availableScheduleItems = new ArrayList<>();
    private SparseBooleanArray sparseBooleanArray = new SparseBooleanArray();
    private String selectedDate = "";

    @RequiresApi(api = Build.VERSION_CODES.P)
    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_booking_date_and_time);
        ButterKnife.bind(this);

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        btnChoosePreferredDate.setOnClickListener(v -> {
            Calendar now = Calendar.getInstance();
            DatePickerDialog dpd = DatePickerDialog.newInstance(
                    SetBookingDateAndTimeActivity.this,
                    now.get(Calendar.YEAR),
                    now.get(Calendar.MONTH),
                    now.get(Calendar.DAY_OF_MONTH)
            );

            dpd.setAutoHighlight(mAutoHighlight);
            dpd.show(getFragmentManager(), "Datepickerdialog");
        });

        setAfternoonSchedule();
        setMorningSchedule();

        ArrayAdapter<String> adapter = new ArrayAdapter<>
                (
                        this,
                        R.layout.custom_checkable_textview,
                        android.R.id.text1, availableScheduleItems
                );

        scheduleListView.setAdapter(adapter);

        scheduleListView.setOnItemClickListener((parent, view, position, id) -> {
            sparseBooleanArray = scheduleListView.getCheckedItemPositions();
            StringBuilder sbSelectedSchedule = new StringBuilder();

            int i = 0;
            int scheduleCount = 0;

            while (i < sparseBooleanArray.size()) {
                if (sparseBooleanArray.valueAt(i)) {
                    sbSelectedSchedule
                            .append(scheduleListView.getItemAtPosition(sparseBooleanArray.keyAt(i)))
                            .append(",");
                }else{
                    sparseBooleanArray.removeAt(i);
                }

                scheduleCount = sparseBooleanArray.size();

                i++;
            }

            DecimalFormat formatter = new DecimalFormat("#,###.00");
            double ratePerHour = Double.parseDouble("1000");
            double totalAmountDouble = Double.parseDouble(String.valueOf((1000 * (scheduleCount))));
            String selectedTime;
            String totalHours;
            String totalAmountString;

            if(scheduleCount < 1){
                totalHours = "N/A";
                selectedTime = "N/A";
                totalAmountString = "N/A";
            }else if(scheduleCount == 1){
                totalHours = scheduleCount + " hr";
                selectedTime = removeLastCharacter(sbSelectedSchedule.toString());
                totalAmountString = Html.fromHtml("&#8369;") + formatter.format(totalAmountDouble);
            }else{
                totalHours = scheduleCount + " hrs";
                selectedTime = removeLastCharacter(sbSelectedSchedule.toString());
                totalAmountString = Html.fromHtml("&#8369;") + formatter.format(totalAmountDouble);
            }

            StringBuilder sbOutput = new StringBuilder();
            sbOutput
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

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth, int yearEnd, int monthOfYearEnd, int dayOfMonthEnd) {
        String dateFrom = (++monthOfYear) + "/" + dayOfMonth + "/" + year;
        String dateTo = (++monthOfYearEnd) + "/" + dayOfMonthEnd + "/" + yearEnd;

        StringBuilder sbSelectedDates = new StringBuilder();
        sbSelectedDates.append(dateFrom).append(" - ").append(dateTo);

        selectedDate = sbSelectedDates.toString();

        btnChoosePreferredDate.setText(selectedDate);
        Toast.makeText(this, "Selected date: " + selectedDate + "!", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute, int hourOfDayEnd, int minuteEnd) {
        String hourString = hourOfDay < 10 ? "0"+hourOfDay : ""+hourOfDay;
        String minuteString = minute < 10 ? "0"+minute : ""+minute;
        String hourStringEnd = hourOfDayEnd < 10 ? "0"+hourOfDayEnd : ""+hourOfDayEnd;
        String minuteStringEnd = minuteEnd < 10 ? "0"+minuteEnd : ""+minuteEnd;
        String time = "You picked the following time: From - "+hourString+"h"+minuteString+" To - "+hourStringEnd+"h"+minuteStringEnd;

        Toast.makeText(this, time, Toast.LENGTH_LONG).show();
    }

    private void setMorningSchedule(){
        int initialValue = 1;
        String meridian = " AM";

        availableScheduleItems.add("12-1 AM");

        for(int i = 1; i <= 11; i++){
            initialValue++;

            if(initialValue >= 12){
                meridian = " PM";
            }

            availableScheduleItems.add(i + "-" + initialValue + meridian);
        }
    }

    private void setAfternoonSchedule(){
        int initialValue = 1;
        String meridian = " AM";

        availableScheduleItems.add("12-1 PM");

        for(int i = 1; i <= 11; i++){
            initialValue++;

            if(initialValue >= 12){
                meridian = " AM";
            }

            availableScheduleItems.add(i + "-" + initialValue + meridian);
        }
    }
}
