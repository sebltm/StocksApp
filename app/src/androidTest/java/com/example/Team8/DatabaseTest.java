package com.example.Team8;

import android.content.Context;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import com.example.Team8.database.SearchHistoryDatabase;
import com.example.Team8.utils.AnalysisType;
import com.example.Team8.utils.SearchHistoryItem;
import com.example.Team8.utils.Stock;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;


/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class DatabaseTest {
    @Test
    public void nuke_table() {
        Context context = InstrumentationRegistry.getInstrumentation().getTargetContext();
        SearchHistoryDatabase database = SearchHistoryDatabase.getInstance(context);

        database.getSearchHistoryDao().deleteAll();
        assert (database.getSearchHistoryDao().getNumItems() == 0);
    }

    @Test
    public Stock test_insert_one_item() {
        Context context = InstrumentationRegistry.getInstrumentation().getTargetContext();
        SearchHistoryDatabase database = SearchHistoryDatabase.getInstance(context);
        List<AnalysisType> analysisTypeList = new ArrayList<>();

        HashMap<String, String> data = new HashMap<String, String>() {{
            put("currency", "USD");
            put("description", "fakecompany");
            put("displaySymbol", "APL");
            put("figi", "idk");
            put("mic", "idk");
            put("symbol", "APL");
            put("type", "Common stock");
        }};

        Stock stock = new Stock(data);

        SearchHistoryItem item = new SearchHistoryItem(stock, new Date(System.currentTimeMillis()), new Date(System.currentTimeMillis()), analysisTypeList, 15);

        database.getSearchHistoryDao().deleteAll();
        database.getSearchHistoryDao().insert(item);

        assert (database.getSearchHistoryDao().getNumItems() == 1);

        return stock;
    }

    public void fetch_one_item() {
        nuke_table();
        Stock stock = test_insert_one_item();

    }
}