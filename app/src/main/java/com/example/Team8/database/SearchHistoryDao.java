package com.example.Team8.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.TypeConverters;

import com.example.Team8.utils.SearchHistoryItem;
import com.example.Team8.utils.Stock;

import java.util.Date;
import java.util.List;

@Dao
public interface SearchHistoryDao {

    @Query("SELECT * from SearchHistory WHERE stock = (:stock) AND  date_from = (:dateFrom) AND date_to = (:dateTo) AND analysis_days >= (:analysisDays) LIMIT 1")
    @TypeConverters(StockHistoryConverter.class)
    SearchHistoryItem getItem(Stock stock, Date dateFrom, Date dateTo, int analysisDays);

    @Query("SELECT * from SearchHistory WHERE date_executed < (:dateFrom)")
    List<SearchHistoryItem> loadAllByDateExecuted(Long dateFrom);

    @Query("SELECT * from SearchHistory ORDER BY date_executed DESC LIMIT (:n)")
    List<SearchHistoryItem> loadN(Integer n);

    @Query("SELECT EXISTS (SELECT * FROM SearchHistory WHERE stock = (:stock) AND  date_from = (:dateFrom) AND date_to = (:dateTo) AND analysis_days >= (:analysisDays))")
    @TypeConverters(StockHistoryConverter.class)
    boolean exists(Stock stock, Date dateFrom, Date dateTo, int analysisDays);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(SearchHistoryItem searchHistoryItem);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(SearchHistoryItem... searchHistoryItems);

    @Delete
    void delete(SearchHistoryItem searchHistoryItem);

    @Query("SELECT count(*) FROM SearchHistory")
    int getNumItems();

    @Query("DELETE FROM SearchHistory")
    void deleteAll();
}
