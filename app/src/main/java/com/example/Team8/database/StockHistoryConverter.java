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
import java.util.function.Supplier;

public class StockHistoryConverter {

    private static final Supplier<Type> stock_type_token = () -> new TypeToken<Stock>(){}.getType();
    private static final Supplier<Type> pricePoint_type_token = () -> new TypeToken<PricePoint>(){}.getType();
    private static final Supplier<Type> list_a_points_type_token = () -> new TypeToken<List<AnalysisType>>(){}.getType();

    @TypeConverter
    public String fromAnalysisTypeList(List<AnalysisType> analysisTypeList) {
        if (analysisTypeList == null) return null;
        return new Gson().toJson(analysisTypeList, list_a_points_type_token.get());
    }

    @TypeConverter
    public List<AnalysisType> toAnalysisTypeList(String analysisTypeString) {
        if (analysisTypeString == null) return null;
        return new Gson().fromJson(analysisTypeString, list_a_points_type_token.get());
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
        return new Gson().toJson(stock, stock_type_token.get());
    }

    @TypeConverter
    public Stock toStock(String stockString) {
        if (stockString == null) return null;
        return new Gson().fromJson(stockString, stock_type_token.get());
    }

    @TypeConverter
    public String fromPricePoint(PricePoint pricePoint) {
        if (pricePoint == null) return null;
        return new Gson().toJson(pricePoint, pricePoint_type_token.get());
    }

    @TypeConverter
    public PricePoint toPricePoint(String pricePointString) {
        if (pricePointString == null) return null;
        return new Gson().fromJson(pricePointString, pricePoint_type_token.get());
    }
}
