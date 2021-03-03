package com.example.Team8.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.Team8.utils.SearchHistoryItem;

import java.util.List;

@Dao
public interface SearchHistoryDao {

    @Query("SELECT * from SearchHistory")
    List<SearchHistoryItem> getAll();

    @Query("SELECT * from SearchHistory WHERE date_from < (:dateFrom)")
    List<SearchHistoryItem> loadAllByDateFrom(Long dateFrom);

    @Query("SELECT * from SearchHistory LIMIT (:n)")
    List<SearchHistoryItem> loadN(Integer n);

    @Insert
    void insert(SearchHistoryItem searchHistoryItem);

    @Insert
    void insertAll(SearchHistoryItem... searchHistoryItems);

    @Delete
    void delete(SearchHistoryItem searchHistoryItem);
}
