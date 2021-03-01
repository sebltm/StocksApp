package com.example.Team8;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.Team8.utils.SearchHistoryDatabase;
import com.example.Team8.utils.SearchHistoryItem;

import java.util.ArrayList;
import java.util.List;

public class SearchHistoryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_history);

        SharedPreferences preferences = this.getPreferences(Context.MODE_PRIVATE);
        int numItems = preferences.getInt("num_items", 5);

        SearchHistoryDatabase database = SearchHistoryDatabase.getInstance(this);
        RecyclerView searchHistoryList = (RecyclerView) findViewById(R.id.search_history_list);

        List<SearchHistoryItem> historyItems = new ArrayList<>();
        SearchHistoryAdapter adapter = new SearchHistoryAdapter(historyItems);
        searchHistoryList.setAdapter(adapter);
        searchHistoryList.setLayoutManager(new LinearLayoutManager(this));

        new Thread(() -> {
            historyItems.addAll(database.getSearchHistoryDao().loadN(numItems));
            SearchHistoryActivity.this.runOnUiThread(adapter::notifyDataSetChanged);
        }).start();


        TextView textView = findViewById(R.id.search_history_subtitle);
        textView.setText(getString(R.string.search_history_num_items, numItems));
    }
}