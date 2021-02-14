package com.example.Team8.utils;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

public class DateTimeHelper {

    public static String toEpoch(Date d){
        return String.valueOf(d.getTime() / 1000L);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static Date toDate(LocalDate local_date){
        return Date.from(local_date.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
    }

}
