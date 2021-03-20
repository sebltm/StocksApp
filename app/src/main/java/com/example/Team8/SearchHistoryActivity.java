package com.example.Team8;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
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
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.ArrayList;
import java.util.List;

public class SearchHistoryActivity extends Activity implements SearchHistoryAdapter.SearchHistoryHolder.SearchItemEvent {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_history);

        Toolbar toolbar = findViewById(R.id.search_history_toolbar);
        setActionBar(toolbar);

        SharedPreferences preferences = this.getSharedPreferences("search_activity", Context.MODE_PRIVATE);
        final int[] numItems = {preferences.getInt("num_items", 25)};

        SearchHistoryDatabase database = SearchHistoryDatabase.getInstance(this);
        RecyclerView searchHistoryList = findViewById(R.id.search_history_list);

        List<SearchHistoryItem> historyItems = new ArrayList<>();
        SearchHistoryAdapter adapter = new SearchHistoryAdapter(historyItems, this);
        searchHistoryList.setAdapter(adapter);
        searchHistoryList.setLayoutManager(new LinearLayoutManager(this));

        new Thread(() -> {
            List<SearchHistoryItem> items = database.getSearchHistoryDao().loadN(numItems[0]);
            runOnUiThread(() -> {
                adapter.clear();
                adapter.addAll(items);
            });
        });

        TextView textView = findViewById(R.id.search_history_subtitle);
        textView.setText(getString(R.string.search_history_num_items, numItems[0]));

        Spinner spinner = findViewById(R.id.dropdown_database_elements);
        ArrayAdapter<CharSequence> dropdownDatabaseAdapater = ArrayAdapter.createFromResource(this, R.array.database_elements, android.R.layout.simple_spinner_item);
        dropdownDatabaseAdapater.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(dropdownDatabaseAdapater);

        int position = dropdownDatabaseAdapater.getPosition(String.valueOf(numItems[0]));
        spinner.setSelection(position);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = spinner.getSelectedItem().toString();
                numItems[0] = Integer.parseInt(selectedItem);
                preferences.edit().putInt("num_items", numItems[0]).apply();

                textView.setText(getString(R.string.search_history_num_items, numItems[0]));

                new Thread(() -> {
                    historyItems.clear();
                    historyItems.addAll(database.getSearchHistoryDao().loadN(numItems[0]));
                    SearchHistoryActivity.this.runOnUiThread(adapter::notifyDataSetChanged);
                }).start();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        Button clearHistory = findViewById(R.id.search_history_clear);
        clearHistory.setOnClickListener(v -> new Thread(() -> {
            SearchHistoryDatabase db = SearchHistoryDatabase.getInstance(this);
            db.getSearchHistoryDao().deleteAll();
            adapter.refreshInternalList(numItems[0]);
            runOnUiThread(adapter::notifyDataSetChanged);
        }).start());
    }

    @Override
    public void showSummary(SearchHistoryItem searchHistoryItem) {
        searchHistoryItem.getStock().showSummary(
                this,
                searchHistoryItem.getFrom(),
                searchHistoryItem.getTo()
        );
    }

    @Override
    public void repeatSearch(SearchHistoryItem searchHistoryItem) {
        Intent intent = new Intent(this, GraphActivity.class);
        GraphActivity.searchItem = searchHistoryItem;
        startActivity(intent);
    }
}