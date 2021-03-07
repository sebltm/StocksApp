package com.example.Team8.utils;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.TypeConverters;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Entity(tableName = "SearchHistory", primaryKeys = {"stock", "date_from", "date_to", "analysis_type"})
public class SearchHistoryItem {

    @NonNull
    @TypeConverters(StockHistoryConverter.class)
    private Stock stock;

    @NonNull
    @ColumnInfo(name = "date_from")
    @TypeConverters(StockHistoryConverter.class)
    private Date from;

    @NonNull
    @ColumnInfo(name = "date_to")
    @TypeConverters(StockHistoryConverter.class)
    private Date to;

    @NonNull
    @ColumnInfo(name = "analysis_type")
    @TypeConverters(StockHistoryConverter.class)
    private List<AnalysisType> analysisTypes;

    public SearchHistoryItem(@NonNull Stock stock, Calendar from, Calendar to, @NonNull List<AnalysisType> analysisTypes) {
        this.stock = stock;
        this.from = from.getTime();
        this.to = to.getTime();
        this.analysisTypes = analysisTypes;
    }

    public SearchHistoryItem(@NonNull Stock stock, @NonNull Date from, @NonNull Date to, @NonNull List<AnalysisType> analysisTypes) {
        this.stock = stock;
        this.from = from;
        this.to = to;
        this.analysisTypes = analysisTypes;
    }

    @NonNull
    public Date getFrom() {
        return from;
    }

    public void setFrom(@NonNull Date from) {
        this.from = from;
    }

    @NonNull
    public Date getTo() {
        return to;
    }

    public void setTo(@NonNull Date to) {
        this.to = to;
    }

    @NonNull
    public List<AnalysisType> getAnalysisTypes() {
        return analysisTypes;
    }

    public void setAnalysisTypes(@NonNull List<AnalysisType> analysisTypes) {
        this.analysisTypes = analysisTypes;
    }

    @NonNull
    public Stock getStock() {
        return stock;
    }

    public void setStock(@NonNull Stock stock) {
        this.stock = stock;
    }
}
