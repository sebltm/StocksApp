package com.example.Team8;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.example.Team8.adapters.StockAdapter;
import com.example.Team8.database.SearchHistoryDao;
import com.example.Team8.database.SearchHistoryDatabase;
import com.example.Team8.ui.main.DatePickerFragment;
import com.example.Team8.utils.AnalysisType;
import com.example.Team8.utils.Resolution;
import com.example.Team8.utils.SearchHistoryItem;
import com.example.Team8.utils.Stock;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends Activity implements StockAutoCompleteWatcher.EventHandler {
    private static final ArrayList<Stock> STOCKS = new ArrayList<>();
    private static Stock selectedStock;
    private final Context context = this;
    private ProgressBar spinner;
    private DatePickerFragment fromDate;
    private DatePickerFragment toDate;
    private AutoCompleteTextView stockAutocomplete;
    private CheckBox smaCheckbox;
    private CheckBox emaCheckbox;
    private CheckBox macdCheckbox;
    private CheckBox macdavgCheckbox;
    private TextView analysisDaysView;
    private SearchHistoryDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        selectedStock = null;

        Toolbar toolbar = findViewById(R.id.toolbar);
        setActionBar(toolbar);

        spinner = findViewById(R.id.progressBar);
        spinner.setVisibility(View.GONE);

        // Create date picker selection interface
        EditText editTextFromDate = findViewById(R.id.mDatePickerFrom);
        EditText editTextToDate = findViewById(R.id.mDatePickerTo);

        fromDate = new DatePickerFragment(editTextFromDate, this);
        toDate = new DatePickerFragment(editTextToDate, this);

        // Create stock selection interface
        StockAdapter stockAdapter = new StockAdapter(this, R.layout.stock_dropdown_item, STOCKS);
        stockAutocomplete = findViewById(R.id.stockDropdown);
        stockAutocomplete.setAdapter(stockAdapter);

        stockAutocomplete.setOnItemClickListener((parent, view, position, id) -> {
            InputMethodManager in = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            in.hideSoftInputFromWindow(view.getApplicationWindowToken(), 0);
            StockAdapter.ViewHolderItem stockItem = (StockAdapter.ViewHolderItem) view.getTag();
            selectedStock = stockItem.stock;
        });

        StockAutoCompleteWatcher autoCompleteWatcher = new StockAutoCompleteWatcher(stockAdapter, this);
        stockAutocomplete.addTextChangedListener(autoCompleteWatcher);

        // Create analysis selection interface
        smaCheckbox = findViewById(R.id.SMAcheckbox);
        emaCheckbox = findViewById(R.id.EMAcheckbox);
        macdCheckbox = findViewById(R.id.MACDcheckbox);
        macdavgCheckbox = findViewById(R.id.MACDAVGcheckbox);

        analysisDaysView = findViewById(R.id.analysisDays);

        Button resetBttn = findViewById(R.id.resetBttn);
        resetBttn.setOnClickListener(this::resetView);

        Button searchHistory = findViewById(R.id.search_history);
        searchHistory.setOnClickListener(v -> {
            Intent intent = new Intent(SearchActivity.this, SearchHistoryActivity.class);
            context.startActivity(intent);
        });

        database = SearchHistoryDatabase.getInstance(this);

        // Create Search button
        Button searchBttn = findViewById(R.id.searchBttn);
        searchBttn.setOnClickListener(this::executeSearch);

        //Reset all to null
        resetView(null);
    }

    @Override
    public void handleLoadingSymbols(int visibility) {
        runOnUiThread(() -> spinner.setVisibility(visibility));
    }

    @Override
    public void handleNetworkError() {
        runOnUiThread(() -> Toast.makeText(SearchActivity.this, "Network error, please try again.", Toast.LENGTH_LONG).show());
    }

    private void resetView(View v) {
        fromDate.clear();
        toDate.clear();

        stockAutocomplete.getText().clear();
        spinner.setVisibility(View.INVISIBLE);

        smaCheckbox.setChecked(false);
        emaCheckbox.setChecked(false);
        macdCheckbox.setChecked(false);
        macdavgCheckbox.setChecked(false);

        analysisDaysView.setText("");

        View current = SearchActivity.this.getCurrentFocus();
        if (current != null) current.clearFocus();
    }

    private void executeSearch(View v) {
        spinner.setVisibility(View.VISIBLE);
        String stockSymbol = stockAutocomplete.getText().toString();

        if (analysisDaysView.getText().toString().isEmpty()) {
            Toast.makeText(SearchActivity.this, "Please fill in the number of days for SMA or EMA field", Toast.LENGTH_SHORT).show();
            return;
        }

        int analysisDays = Integer.parseInt(analysisDaysView.getText().toString());

        if (stockSymbol.isEmpty()) {
            Toast.makeText(SearchActivity.this, "Please select the correct stock symbol to run the search", Toast.LENGTH_SHORT).show();
            return;
        }

        if (smaCheckbox.isChecked() || emaCheckbox.isChecked()) {
            if (analysisDays <= 0) {
                Toast.makeText(SearchActivity.this, "Please enter the correct number of days for SMA or EMA analysis", Toast.LENGTH_SHORT).show();
                return;
            }
        }

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

        if (fromDate.getCal().compareTo(toDate.getCal()) <= 0 && selectedStock != null && !analysisTypes.isEmpty()) {
            new Thread(() -> {
                SearchHistoryDao dao = database.getSearchHistoryDao();
                if (dao.exists(selectedStock, fromDate.getCal().getTime(), toDate.getCal().getTime())) {
                    SearchHistoryItem searchHistoryItem = dao.getItem(selectedStock, fromDate.getCal().getTime(), toDate.getCal().getTime());
                    searchHistoryItem.setAnalysisDays(analysisDays);

                    GraphActivity.searchItem = searchHistoryItem;
                    Intent intent = new Intent(SearchActivity.this, GraphActivity.class);
                    context.startActivity(intent);
                } else {
                    selectedStock.fetchData(
                            Resolution.types.get("D"),
                            fromDate.getCal(), toDate.getCal(), analysisDays,
                            (price_points, stock) -> {

                                if (price_points == null || price_points.getClose().size() == 0) {
                                    spinner.setVisibility(View.INVISIBLE);
                                    handleNetworkError();
                                    return;
                                }

                                SearchHistoryItem searchHistoryItem = new SearchHistoryItem(selectedStock, fromDate.getCal(), toDate.getCal(), analysisTypes, analysisDays);
                                // Insert search history object into the database
                                dao.insert(searchHistoryItem);

                                SearchActivity.this.runOnUiThread(() -> {
                                    spinner.setVisibility(View.INVISIBLE);

                                    GraphActivity.searchItem = searchHistoryItem;
                                    Intent intent = new Intent(SearchActivity.this, GraphActivity.class);
                                    context.startActivity(intent);
                                });
                            });
                }
            }).start();
        } else if (selectedStock != null) {
            spinner.setVisibility(View.INVISIBLE);
            Toast.makeText(SearchActivity.this, "\"From\" date must be smaller or equal \"to\" date", Toast.LENGTH_LONG).show();
            fromDate.setDayEqual(toDate);
        } else {
            spinner.setVisibility(View.INVISIBLE);
            Toast.makeText(SearchActivity.this, "Please select a stock smybol from the autcomplete list", Toast.LENGTH_LONG).show();
            stockAutocomplete.showDropDown();
        }
    }
}