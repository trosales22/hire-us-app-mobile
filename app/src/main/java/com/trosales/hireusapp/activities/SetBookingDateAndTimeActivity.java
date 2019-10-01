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
import android.widget.Toast;

import com.androidbuts.multispinnerfilter.KeyPairBoolData;
import com.androidbuts.multispinnerfilter.MultiSpinnerSearch;
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
    @BindView(R.id.btnChoosePreferredDate) MultiSpinnerSearch btnChoosePreferredDate;
    @BindView(R.id.scheduleListView) ListView scheduleListView;
    @BindView(R.id.lblSelectedSchedule) TextView lblSelectedSchedule;
    @BindView(R.id.btnProceedToCheckout) AppCompatButton btnProceedToCheckout;

    private ArrayList<String> availableScheduleItems = new ArrayList<>();
    private SparseBooleanArray sparseBooleanArray = new SparseBooleanArray();

    @RequiresApi(api = Build.VERSION_CODES.P)
    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_booking_date_and_time);
        ButterKnife.bind(this);

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        List<String> datesList = getDatesUpToSpecificMonths(3);
        final List<KeyPairBoolData> keyPairBoolDataList = new ArrayList<>();

        for (int i = 0; i < datesList.size(); i++) {
            KeyPairBoolData h = new KeyPairBoolData();
            h.setId(i + 1);
            h.setName(datesList.get(i));
            h.setSelected(false);
            keyPairBoolDataList.add(h);
        }

        btnChoosePreferredDate.setItems(keyPairBoolDataList, -1, items -> {
            for (int i = 0; i < items.size(); i++) {
                if (items.get(i).isSelected()) {
                    Toast.makeText(SetBookingDateAndTimeActivity.this, i + " : " + items.get(i).getName() + " : " + items.get(i).isSelected(), Toast.LENGTH_LONG).show();
                }
            }
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
