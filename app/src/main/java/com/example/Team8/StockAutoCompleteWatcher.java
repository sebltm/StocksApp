package com.example.Team8;

import android.text.Editable;
import android.text.TextWatcher;

import com.example.Team8.adapters.StockAdapter;
import com.example.Team8.utils.API;
import com.example.Team8.utils.Stock;

import java.util.HashMap;
import java.util.List;

public class StockAutoCompleteWatcher implements TextWatcher {

    public static StockAutoCompleteWatcher autoCompleteWatcher = null;
    private final HashMap<String, List<Stock>> pastRequests = new HashMap<>();
    StockAdapter stockAdapter;

    private StockAutoCompleteWatcher() {
    }

    public static StockAutoCompleteWatcher getInstance(StockAdapter stockAdapter) {
        if (autoCompleteWatcher == null) {
            autoCompleteWatcher = new StockAutoCompleteWatcher();
        }

        autoCompleteWatcher.stockAdapter = stockAdapter;
        return autoCompleteWatcher;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {

        String symbolReq = s.toString().toLowerCase();

        if (!pastRequests.containsKey(symbolReq)) {
            API.getInstance().search(symbolReq, stocks -> {
                pastRequests.put(symbolReq, stocks);
                stockAdapter.addAll(stocks);
                stockAdapter.notifyDataSetChanged();
            });
        } else {
            stockAdapter.clear();
            stockAdapter.addAll(pastRequests.get(symbolReq));
            stockAdapter.notifyDataSetChanged();
        }
    }
}
