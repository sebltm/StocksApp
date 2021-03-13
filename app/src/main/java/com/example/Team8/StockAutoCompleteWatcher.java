package com.example.Team8;

import android.text.Editable;
import android.text.TextWatcher;

import com.example.Team8.adapters.StockAdapter;
import com.example.Team8.utils.API;
import com.example.Team8.utils.Stock;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class StockAutoCompleteWatcher implements TextWatcher {

    public static StockAutoCompleteWatcher autoCompleteWatcher = null;
    private final HashMap<String, List<Stock>> pastRequests = new HashMap<>();
    private static Date lastRequest;
    private Date currentRequest;
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

        currentRequest = new Date(System.currentTimeMillis());
        if (lastRequest == null || lastRequest.compareTo(currentRequest) < 0) {
            lastRequest = new Date(currentRequest.getTime());
        }

        if (!pastRequests.containsKey(symbolReq)) {
            API.getInstance().search(symbolReq, stocks -> {
                System.out.println(String.format("Started a new online search for %s at %s", symbolReq, currentRequest));
                System.out.flush();

                if (stocks != null) {
                    pastRequests.put(symbolReq, stocks);
                    stockAdapter.addAll(stocks);
                    stockAdapter.notifyDataSetChanged();
                    stockAdapter.getFilter().filter(s);

                    System.out.println(String.format("Finished online search for %s, added %d items", symbolReq, stocks.size()));
                } else {
                    System.out.println(String.format("Finished online search for %s, added 0 items", symbolReq));
                }
                System.out.flush();
            });
        }
    }
}
