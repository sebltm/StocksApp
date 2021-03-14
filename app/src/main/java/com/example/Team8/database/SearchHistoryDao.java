package com.example.Team8.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.TypeConverters;

import com.example.Team8.utils.AnalysisType;
import com.example.Team8.utils.SearchHistoryItem;
import com.example.Team8.utils.Stock;
import com.example.Team8.utils.StockHistoryConverter;

import java.util.Date;
import java.util.List;

@Dao
public interface SearchHistoryDao {

    @Query("SELECT * from SearchHistory WHERE stock = (:stock) AND  date_from = (:dateFrom) AND date_to = (:dateTo) AND analysis_type = (:analysisTypes)")
    @TypeConverters(StockHistoryConverter.class)
    SearchHistoryItem getItem(Stock stock, Date dateFrom, Date dateTo, List<AnalysisType> analysisTypes);

    @Query("SELECT * from SearchHistory WHERE date_from < (:dateFrom)")
    List<SearchHistoryItem> loadAllByDateFrom(Long dateFrom);

    @Query("SELECT * from SearchHistory LIMIT (:n)")
    List<SearchHistoryItem> loadN(Integer n);

    @Query("SELECT EXISTS (SELECT 1 FROM SearchHistory WHERE stock = (:stock) AND  date_from = (:dateFrom) AND date_to = (:dateTo) AND analysis_type = (:analysisTypes))")
    @TypeConverters(StockHistoryConverter.class)
    boolean exists(Stock stock, Date dateFrom, Date dateTo, List<AnalysisType> analysisTypes);

    @Insert
    void insert(SearchHistoryItem searchHistoryItem);

    @Insert
    void insertAll(SearchHistoryItem... searchHistoryItems);

    @Delete
    void delete(SearchHistoryItem searchHistoryItem);

    @Query("SELECT count(*) FROM SearchHistory")
    int getNumItems();

    @Query("DELETE FROM SearchHistory")
    void deleteAll();
}
