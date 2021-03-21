package com.example.Team8.database;

import androidx.room.TypeConverter;

import com.example.Team8.utils.AnalysisType;
import com.example.Team8.utils.PricePoint;
import com.example.Team8.utils.Stock;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.Date;
import java.util.List;

public class StockHistoryConverter {

    @TypeConverter
    public String fromAnalysisTypeList(List<AnalysisType> analysisTypeList) {
        if (analysisTypeList == null) return null;
        return new Gson().toJson(analysisTypeList, new Token<List<AnalysisType>>().get());
    }

    @TypeConverter
    public List<AnalysisType> toAnalysisTypeList(String analysisTypeString) {
        if (analysisTypeString == null) return null;
        return new Gson().fromJson(analysisTypeString, new Token<List<AnalysisType>>().get());
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
        if (stock == null) return null;
        return new Gson().toJson(stock, new Token<Stock>().get());
    }

    @TypeConverter
    public Stock toStock(String stockString) {
        if (stockString == null) return null;
        return new Gson().fromJson(stockString, new Token<Stock>().get());
    }

    @TypeConverter
    public String fromPricePoint(PricePoint pricePoint) {
        if (pricePoint == null) return null;
        return new Gson().toJson(pricePoint, new Token<PricePoint>().get());
    }

    @TypeConverter
    public PricePoint toPricePoint(String pricePointString) {
        if (pricePointString == null) return null;
        return new Gson().fromJson(pricePointString, new Token<PricePoint>().get());
    }
}

class Token<T> {
    public Type get() {
        return new TypeToken<T>() {
        }.getType();
    }
}
