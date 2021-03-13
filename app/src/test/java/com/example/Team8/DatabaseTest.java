package com.example.Team8;

import android.content.Context;

import androidx.test.core.app.ApplicationProvider;

import com.example.Team8.database.SearchHistoryDatabase;
import com.example.Team8.utils.AnalysisType;
import com.example.Team8.utils.SearchHistoryItem;
import com.example.Team8.utils.Stock;

import org.junit.Test;
import org.mockito.Mock;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class DatabaseTest {
    @Mock
    Stock stock;

    Context context = ApplicationProvider.getApplicationContext();

    SearchHistoryDatabase database = SearchHistoryDatabase.getInstance(context);
    List<AnalysisType> analysisTypeList = new ArrayList<>();

    @Test
    public void test_insert_one_item() {
        SearchHistoryItem item = new SearchHistoryItem(stock, new Date(System.currentTimeMillis()), new Date(System.currentTimeMillis()), analysisTypeList, 15);
        database.getSearchHistoryDao().insert(item);

        assert (database.getSearchHistoryDao().getNumItems() == 1);
    }
}