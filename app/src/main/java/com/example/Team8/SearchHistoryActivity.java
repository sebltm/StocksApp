package com.example.Team8;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toolbar;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.Team8.adapters.SearchHistoryAdapter;
import com.example.Team8.database.SearchHistoryDatabase;
import com.example.Team8.utils.SearchHistoryItem;

import java.util.ArrayList;
import java.util.List;

public class SearchHistoryActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_history);

        Toolbar toolbar = findViewById(R.id.search_history_toolbar);
        setActionBar(toolbar);

        SharedPreferences preferences = this.getPreferences(Context.MODE_PRIVATE);
        int numItems = preferences.getInt("num_items", 25);

        SearchHistoryDatabase database = SearchHistoryDatabase.getInstance(this);
        RecyclerView searchHistoryList = findViewById(R.id.search_history_list);

        List<SearchHistoryItem> historyItems = new ArrayList<>();
        SearchHistoryAdapter adapter = new SearchHistoryAdapter(historyItems, this);
        searchHistoryList.setAdapter(adapter);
        searchHistoryList.setLayoutManager(new LinearLayoutManager(this));

        new Thread(() -> {
            historyItems.addAll(database.getSearchHistoryDao().loadN(numItems));
            SearchHistoryActivity.this.runOnUiThread(adapter::notifyDataSetChanged);
        }).start();

        TextView textView = findViewById(R.id.search_history_subtitle);
        textView.setText(getString(R.string.search_history_num_items, numItems));

        Spinner spinner = findViewById(R.id.dropdown_database_elements);
        ArrayAdapter<CharSequence> dropdownDatabaseAdapater = ArrayAdapter.createFromResource(this, R.array.database_elements, android.R.layout.simple_spinner_item);
        dropdownDatabaseAdapater.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(dropdownDatabaseAdapater);

        Button clearHistory = findViewById(R.id.search_history_clear);
        clearHistory.setOnClickListener(v -> new Thread(() -> {
            SearchHistoryDatabase db = SearchHistoryDatabase.getInstance(this);
            db.getSearchHistoryDao().deleteAll();
            adapter.refreshInternalList(numItems);
            runOnUiThread(adapter::notifyDataSetChanged);
        }).start());
    }
}