package com.example.Team8.utils;

import androidx.annotation.NonNull;

import java.math.BigDecimal;
import java.util.Date;

public class DataPoint {
    BigDecimal value;
    Date dateTime;
    protected String pointType = "data_point";

    public DataPoint(BigDecimal value, Date dateTime) {
        this.value = value;
        this.dateTime = dateTime;
    }

    @NonNull
    @Override
    public String toString() {
        return String.format("[%3$s] DateTime: \"%2$s\" | Value: \"%1$s\"", value,  dateTime, pointType.toUpperCase());
    }
}
