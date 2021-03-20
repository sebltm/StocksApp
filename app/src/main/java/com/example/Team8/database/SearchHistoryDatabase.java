package com.example.Team8.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.Team8.utils.SearchHistoryItem;

@Database(entities = {SearchHistoryItem.class}, version = 1)
public abstract class SearchHistoryDatabase extends RoomDatabase {

    private static final String DB_NAME = "searchHistory.db";
    public static volatile SearchHistoryDatabase instance;

    protected SearchHistoryDatabase() {
    }

    public static synchronized SearchHistoryDatabase getInstance(Context context) {
        if (instance == null) {
            instance = create(context);
        }
        return instance;
    }

    private static SearchHistoryDatabase create(final Context context) {
        return Room.databaseBuilder(context, SearchHistoryDatabase.class, DB_NAME).build();
    }

    public abstract SearchHistoryDao getSearchHistoryDao();

}
