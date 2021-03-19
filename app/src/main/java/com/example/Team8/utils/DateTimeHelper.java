package com.example.Team8.utils;

import android.annotation.SuppressLint;
import android.os.Build;

import androidx.annotation.RequiresApi;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class DateTimeHelper {

    private static final List<Integer> NON_BUSINESS_DAYS = Arrays.asList(
            Calendar.SATURDAY,
            Calendar.SUNDAY
    );

    public static String toEpoch(Date d) {
        return String.valueOf(d.getTime() / 1000L);
    }

    public static Date toDateTime(String epoch) {
        return new Date(Long.parseLong(epoch) * 1000L);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static Date toDate(LocalDate local_date) {
        return Date.from(local_date.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
    }

    public static Long dateDiff(Date date_1, Date date_2) {
        if (date_1.compareTo(date_2) == 0) return (long) 0;
        Date d1 = date_1.compareTo(date_2) < 0 ? date_1 : date_2;
        Date d2 = date_1.compareTo(date_2) > 0 ? date_1 : date_2;
        long diffInMillies = Math.abs(d1.getTime() - d2.getTime());
        return TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);
    }

    public static Calendar addBusinessDays(Calendar cal, int businessDays) {
        Calendar calCopy = (Calendar) cal.clone();

        for (int i = 0; i < Math.abs(businessDays); ) {
            calCopy.add(Calendar.DAY_OF_MONTH, businessDays > 0 ? 1 : -1);

            if (!NON_BUSINESS_DAYS.contains(calCopy.get(Calendar.DAY_OF_WEEK))) {
                i++;
            }
        }

        return calCopy;
    }

    public static String getDateStringOnly(Date date){
        @SuppressLint("SimpleDateFormat") SimpleDateFormat formatter = new SimpleDateFormat("dd MMMM yyyy");
        return formatter.format(date);
    };
}
