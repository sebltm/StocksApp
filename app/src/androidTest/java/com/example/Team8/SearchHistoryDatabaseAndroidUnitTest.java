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

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Random;


/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class SearchHistoryDatabaseAndroidUnitTest {
    @Test
    public void nukeTable() {
        Context context = InstrumentationRegistry.getInstrumentation().getTargetContext();
        SearchHistoryDatabase database = SearchHistoryDatabase.getInstance(context);

        database.getSearchHistoryDao().deleteAll();
        assert (database.getSearchHistoryDao().getNumItems() == 0);
    }

    @Test
    public void insertOneItem() {
        Context context = InstrumentationRegistry.getInstrumentation().getTargetContext();
        SearchHistoryDatabase database = SearchHistoryDatabase.getInstance(context);
        List<AnalysisType> analysisTypeList = new ArrayList<>();

        Stock stock = createUniqueStock();

        SearchHistoryItem item = new SearchHistoryItem(stock, new Date(System.currentTimeMillis()), new Date(System.currentTimeMillis()), analysisTypeList, 15);

        database.getSearchHistoryDao().deleteAll();
        database.getSearchHistoryDao().insert(item);

        assert (database.getSearchHistoryDao().getNumItems() == 1);
    }

    @Test
    public void fetchOneItem() {
        Context context = InstrumentationRegistry.getInstrumentation().getTargetContext();
        SearchHistoryDatabase database = SearchHistoryDatabase.getInstance(context);
        List<AnalysisType> analysisTypeList = new ArrayList<>();

        Stock stock = createUniqueStock();

        SearchHistoryItem item = new SearchHistoryItem(stock, new Date(System.currentTimeMillis()), new Date(System.currentTimeMillis()), analysisTypeList, 15);

        database.getSearchHistoryDao().deleteAll();
        database.getSearchHistoryDao().insert(item);

        SearchHistoryItem fetched = database.getSearchHistoryDao().getItem(item.getStock(), item.getFrom(), item.getTo(), item.getAnalysisDays());

        assert (fetched.getStock().getDescription().equals(item.getStock().getDescription()));
        assert (item.getFrom().getTime() == fetched.getFrom().getTime());
        assert (item.getTo().getTime() == fetched.getTo().getTime());
    }

    @Test
    public void insert5Items() {
        Context context = InstrumentationRegistry.getInstrumentation().getTargetContext();
        SearchHistoryDatabase database = SearchHistoryDatabase.getInstance(context);
        List<AnalysisType> analysisTypeList = new ArrayList<>();

        SearchHistoryItem[] searchHistoryItems = new SearchHistoryItem[5];
        for (int i = 0; i < 5; i++) {
            Stock stock = createUniqueStock();
            searchHistoryItems[i] = new SearchHistoryItem(stock, new Date(System.currentTimeMillis()), new Date(System.currentTimeMillis()), analysisTypeList, 15);
        }

        database.getSearchHistoryDao().deleteAll();
        database.getSearchHistoryDao().insertAll(searchHistoryItems);

        assert (database.getSearchHistoryDao().getNumItems() == 5);

        database.getSearchHistoryDao().deleteAll();
    }

    @Test
    public void insertSameItem() {
        Context context = InstrumentationRegistry.getInstrumentation().getTargetContext();
        SearchHistoryDatabase database = SearchHistoryDatabase.getInstance(context);
        List<AnalysisType> analysisTypeList = new ArrayList<>();

        Stock stock = createUniqueStock();

        SearchHistoryItem item = new SearchHistoryItem(stock, new Date(System.currentTimeMillis()), new Date(System.currentTimeMillis()), analysisTypeList, 15);

        database.getSearchHistoryDao().deleteAll();
        database.getSearchHistoryDao().insert(item);
        database.getSearchHistoryDao().insert(item);

        assert (database.getSearchHistoryDao().getNumItems() == 1);

        database.getSearchHistoryDao().deleteAll();
    }

    @Test
    public void itemExists() {
        Context context = InstrumentationRegistry.getInstrumentation().getTargetContext();
        SearchHistoryDatabase database = SearchHistoryDatabase.getInstance(context);
        List<AnalysisType> analysisTypeList = new ArrayList<>();

        Stock stock = createUniqueStock();

        SearchHistoryItem item = new SearchHistoryItem(stock, new Date(System.currentTimeMillis()), new Date(System.currentTimeMillis()), analysisTypeList, 15);
        database.getSearchHistoryDao().insert(item);
        boolean exists = database.getSearchHistoryDao().exists(item.getStock(), item.getFrom(), item.getTo(), item.getAnalysisDays());

        assert (exists);

        database.getSearchHistoryDao().deleteAll();
    }

    @Test
    public void load5Items() {
        Context context = InstrumentationRegistry.getInstrumentation().getTargetContext();
        SearchHistoryDatabase database = SearchHistoryDatabase.getInstance(context);
        List<AnalysisType> analysisTypeList = new ArrayList<>();

        SearchHistoryItem[] searchHistoryItems = new SearchHistoryItem[20];
        for (int i = 0; i < 20; i++) {
            Stock stock = createUniqueStock();
            searchHistoryItems[i] = new SearchHistoryItem(stock, new Date(System.currentTimeMillis()), new Date(System.currentTimeMillis()), analysisTypeList, 15);
        }

        database.getSearchHistoryDao().deleteAll();
        database.getSearchHistoryDao().insertAll(searchHistoryItems);

        List<SearchHistoryItem> fetchedItems = database.getSearchHistoryDao().loadN(5);
        assert (fetchedItems.size() == 5);
    }

    public Stock createUniqueStock() {
        byte[] array = new byte[16];
        new Random().nextBytes(array);
        String randomDescription = new String(array, Charset.defaultCharset());

        HashMap<String, String> data = new HashMap<String, String>() {{
            put("currency", "USD");
            put("description", randomDescription);
            put("displaySymbol", "APL");
            put("figi", "idk");
            put("mic", "idk");
            put("symbol", "APL");
            put("type", "Common stock");
        }};

        return new Stock(data);
    }
}