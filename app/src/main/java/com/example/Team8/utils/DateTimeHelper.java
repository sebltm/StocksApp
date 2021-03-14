package com.example.Team8.utils;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class DateTimeHelper {

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

    public static Long dateDiff(Date date_1, Date date_2){
        if(date_1.compareTo(date_2) == 0) return (long) 0;
        Date d1 = date_1.compareTo(date_2) < 0? date_1 : date_2;
        Date d2 = date_1.compareTo(date_2) > 0? date_1 : date_2;
        long diffInMillies = Math.abs(d1.getTime() - d2.getTime());
        return TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);
    }
}
