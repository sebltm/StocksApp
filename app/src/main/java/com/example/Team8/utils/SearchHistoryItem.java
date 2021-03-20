package com.example.Team8.utils;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.TypeConverters;

import com.example.Team8.database.StockHistoryConverter;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Entity(tableName = "SearchHistory", primaryKeys = {"stock", "date_from", "date_to"})
public class SearchHistoryItem implements Serializable {

    @NonNull
    @ColumnInfo(name = "date_executed")
    @TypeConverters(StockHistoryConverter.class)
    private Date dateExecuted;

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

    @ColumnInfo(name = "analysis_days")
    private Integer analysisDays;

    public SearchHistoryItem(@NonNull Stock stock, Calendar from, Calendar to, @NonNull List<AnalysisType> analysisTypes, Integer analysisDays) {
        this.stock = stock;
        this.from = from.getTime();
        this.to = to.getTime();
        this.analysisTypes = analysisTypes;
        this.analysisDays = analysisDays;

        this.dateExecuted = new Date(System.currentTimeMillis());
    }

    public SearchHistoryItem(@NonNull Stock stock, @NonNull Date from, @NonNull Date to, @NonNull List<AnalysisType> analysisTypes, Integer analysisDays) {
        this.stock = stock;
        this.from = from;
        this.to = to;
        this.analysisTypes = analysisTypes;
        this.analysisDays = analysisDays;

        this.dateExecuted = new Date(System.currentTimeMillis());
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

    public Integer getAnalysisDays() {
        return analysisDays;
    }

    public void setAnalysisDays(Integer analysisDays) {
        this.analysisDays = analysisDays;
    }

    @NonNull
    public Date getDateExecuted() {
        return dateExecuted;
    }

    public void setDateExecuted(@NonNull Date dateExecuted) {
        this.dateExecuted = dateExecuted;
    }
}
