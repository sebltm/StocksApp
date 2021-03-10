package com.example.Team8;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.Team8.adapters.StockAdapter;
import com.example.Team8.database.SearchHistoryDatabase;
import com.example.Team8.utils.AnalysisType;
import com.example.Team8.utils.SearchHistoryItem;
import com.example.Team8.utils.Stock;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity {

    private static final ArrayList<Stock> STOCKS = new ArrayList<>();

    Stock selectedStock;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        // Create date picker selection interface
        EditText editTextFromDate = (EditText) findViewById(R.id.mDatePickerFrom);
        EditText editTextToDate = (EditText) findViewById(R.id.mDatePickerTo);

        DatePickerFragment fromDate = new DatePickerFragment(editTextFromDate, this);
        DatePickerFragment toDate = new DatePickerFragment(editTextToDate, this);

        // Create stock selection interface
        StockAdapter stockAdapter = new StockAdapter(this, R.layout.stock_dropdown_item, STOCKS);
        AutoCompleteTextView stockAutocomplete = (AutoCompleteTextView) findViewById(R.id.stockDropdown);
        stockAutocomplete.setAdapter(stockAdapter);

        stockAutocomplete.setOnItemClickListener((parent, view, position, id) -> {
            InputMethodManager in = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            in.hideSoftInputFromWindow(view.getApplicationWindowToken(), 0);
            StockAdapter.ViewHolderItem stockItem = (StockAdapter.ViewHolderItem) view.getTag();
            selectedStock = stockItem.stock;
        });

        StockAutoCompleteWatcher autoCompleteWatcher = StockAutoCompleteWatcher.getInstance(stockAdapter);
        stockAutocomplete.addTextChangedListener(autoCompleteWatcher);

        // Create analysis selection interface
        CheckBox smaCheckbox = (CheckBox) findViewById(R.id.SMAcheckbox);
        CheckBox emaCheckbox = (CheckBox) findViewById(R.id.EMAcheckbox);
        CheckBox macdCheckbox = (CheckBox) findViewById(R.id.MACDcheckbox);
        CheckBox macdavgCheckbox = (CheckBox) findViewById(R.id.MACDAVGcheckbox);

        TextView analysisDays = (TextView) findViewById(R.id.analysisDays);

        Button resetBttn = (Button) findViewById(R.id.resetBttn);
        resetBttn.setOnClickListener(v -> {
            fromDate.clear();
            toDate.clear();

            stockAutocomplete.getText().clear();

            smaCheckbox.setChecked(false);
            emaCheckbox.setChecked(false);
            macdCheckbox.setChecked(false);
            macdavgCheckbox.setChecked(false);

            analysisDays.setText("");

            View current = getCurrentFocus();
            if (current != null) current.clearFocus();
        });

        SearchHistoryDatabase database = SearchHistoryDatabase.getInstance(this);

        // Create Search button
        Button searchBttn = (Button) findViewById(R.id.searchBttn);
        searchBttn.setOnClickListener(v -> {

            List<AnalysisType> analysisTypes = new ArrayList<>();

            if (smaCheckbox.isChecked()) {
                analysisTypes.add(AnalysisType.SMA);
            }

            if (emaCheckbox.isChecked()) {
                analysisTypes.add(AnalysisType.EMA);
            }

            if (macdCheckbox.isChecked()) {
                analysisTypes.add(AnalysisType.MACD);
            }

            if (macdavgCheckbox.isChecked()) {
                analysisTypes.add(AnalysisType.MACDAVG);
            }

            if (fromDate.getCal().compareTo(toDate.getCal()) <= 0) {
                SearchHistoryItem searchHistoryItem = new SearchHistoryItem(selectedStock, fromDate.getCal(), toDate.getCal(), analysisTypes);

                // Insert search history object into the database
                // TODO deal with duplicates (e.g same stock, date from and to and analysis types)
                new Thread(() -> database.getSearchHistoryDao().insert(searchHistoryItem)).start();
            } else {
                Toast.makeText(this, "\"From\" date must be smaller or equal \"to\" date", Toast.LENGTH_LONG).show();
                fromDate.setDayEqual(toDate);
            }
        });
    }
}