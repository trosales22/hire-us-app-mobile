package com.agentcoder.hire_us_ph.fragments;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.squareup.timessquare.CalendarPickerView;
import com.agentcoder.hire_us_ph.R;

import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.agentcoder.hire_us_ph.activities.SetBookingDetailsActivity.lblSelectedDate;
import static com.agentcoder.hire_us_ph.activities.SetBookingDetailsActivity.selectedDates;

public class SetWorkingDatesBottomSheetFragment extends BottomSheetDialogFragment{
    @BindView(R.id.calendarPickerView) CalendarPickerView calendarPickerView;
    @BindView(R.id.btnBackToTalentDetails) Button btnBackToTalentDetails;
    @BindView(R.id.btnSetBookingDates) Button btnSetBookingDates;

    private SimpleDateFormat sdf;
    private StringBuilder sbSelectedDates;

    public SetWorkingDatesBottomSheetFragment() {
    }

    @NotNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        BottomSheetDialog bottomSheetDialog=(BottomSheetDialog)super.onCreateDialog(savedInstanceState);
        bottomSheetDialog.setOnShowListener(dialog -> {
            BottomSheetDialog bottomSheetDialog1 = (BottomSheetDialog) dialog;
            FrameLayout bottomSheet = bottomSheetDialog1.findViewById(R.id.design_bottom_sheet);

            BottomSheetBehavior bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);
            bottomSheetBehavior.setPeekHeight(Resources.getSystem().getDisplayMetrics().heightPixels);
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        });
        return bottomSheetDialog;
    }

    @SuppressLint({"SimpleDateFormat", "SetTextI18n"})
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View itemView = inflater.inflate(R.layout.set_working_dates_bottom_sheet, container, false);
        ButterKnife.bind(this, itemView);

        sdf = new SimpleDateFormat("MM/dd/yyyy");
        sbSelectedDates = new StringBuilder();

        Calendar maxDate = Calendar.getInstance();
        maxDate.add(Calendar.YEAR, 1);

        Date dateToday = new Date();

        calendarPickerView
                .init(dateToday, maxDate.getTime())
                .inMode(CalendarPickerView.SelectionMode.MULTIPLE);

        btnBackToTalentDetails.setOnClickListener(view -> this.dismiss());

        btnSetBookingDates.setOnClickListener(view -> {
            for(Date date : calendarPickerView.getSelectedDates()){
                sbSelectedDates.append(sdf.format(date)).append(", ");
            }

            if(sbSelectedDates.length() == 0){
                lblSelectedDate.setText("Working Dates: N/A");
            }else{
                sbSelectedDates.deleteCharAt(sbSelectedDates.lastIndexOf(","));
                selectedDates = sbSelectedDates.toString();

                lblSelectedDate.setText("Working Dates: " + selectedDates);
            }


            this.dismiss();
        });

        return itemView;
    }
}
