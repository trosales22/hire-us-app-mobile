package com.trosales.hireusapp.classes.commons;

import android.annotation.SuppressLint;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

public class DateTimeHelper {
    public static String convert24HrsTimeTo12hrs(String time){
        String format12hours = null;

        try {
            @SuppressLint("SimpleDateFormat") SimpleDateFormat _24HourSDF = new SimpleDateFormat("HH:mm");
            @SuppressLint("SimpleDateFormat") SimpleDateFormat _12HourSDF = new SimpleDateFormat("hh:mm a");
            Date date24hour = _24HourSDF.parse(time);

            format12hours = _12HourSDF.format(date24hour);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return format12hours;
    }

    public static String getTotalHours(String date1, String date2, String time1, String time2){
        DecimalFormat crunchifyFormatter = null;
        int diffhours = 0;

        try {
            String format = "MM/dd/yyyy hh:mm a";

            @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat(format);

            Date dateObj1 = sdf.parse(date1 + " " + time1);
            Date dateObj2 = sdf.parse(date2 + " " + time2);
            System.out.println(dateObj1);
            System.out.println(dateObj2 + "\n");

            crunchifyFormatter = new DecimalFormat("###,###");

            // getTime() returns the number of milliseconds since January 1, 1970, 00:00:00 GMT represented by this Date object
            long diff = dateObj2.getTime() - dateObj1.getTime();

            diffhours = (int) (diff / (60 * 60 * 1000));
            System.out.println("difference between hours: " + crunchifyFormatter.format(diffhours));
        }catch (ParseException e) {
            e.printStackTrace();
        }

        return Objects.requireNonNull(crunchifyFormatter).format(diffhours);
    }
}
