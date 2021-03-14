package com.example.Team8.utils;

import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.Date;
import java.util.List;

public class StockHistoryConverter {

    @TypeConverter
    public String fromAnalysisTypeList(List<AnalysisType> analysisTypeList) {
        if (analysisTypeList == null) {
            return null;
        }

        Gson gson = new Gson();
        Type type = new TypeToken<List<AnalysisType>>() {
        }.getType();
        return gson.toJson(analysisTypeList, type);
    }

    @TypeConverter
    public List<AnalysisType> toAnalysisTypeList(String analysisTypeString) {
        if (analysisTypeString == null) {
            return null;
        }

        Gson gson = new Gson();
        Type type = new TypeToken<List<AnalysisType>>() {
        }.getType();
        return gson.fromJson(analysisTypeString, type);
    }

    @TypeConverter
    public Long fromDate(Date date) {
        return date == null ? null : date.getTime();
    }

    @TypeConverter
    public Date toDate(Long dateLong) {
        return dateLong == null ? null : new Date(dateLong);
    }

    @TypeConverter
    public String fromStock(Stock stock) {
        if (stock == null) {
            return null;
        }

        Gson gson = new Gson();
        Type type = new TypeToken<Stock>() {
        }.getType();
        return gson.toJson(stock, type);
    }

    @TypeConverter
    public Stock toStock(String stockString) {
        if (stockString == null) {
            return null;
        }

        Gson gson = new Gson();
        Type type = new TypeToken<Stock>() {
        }.getType();
        return gson.fromJson(stockString, type);
    }

    @TypeConverter
    public String fromPricePoint(PricePoint pricePoint) {
        if (pricePoint == null) {
            return null;
        }

        Gson gson = new Gson();
        Type type = new TypeToken<PricePoint>() {
        }.getType();
        return gson.toJson(pricePoint, type);
    }

    @TypeConverter
    public PricePoint toPricePoint(String pricePointString) {
        if (pricePointString == null) {
            return null;
        }

        Gson gson = new Gson();
        Type type = new TypeToken<PricePoint>() {
        }.getType();
        return gson.fromJson(pricePointString, type);
    }
}
