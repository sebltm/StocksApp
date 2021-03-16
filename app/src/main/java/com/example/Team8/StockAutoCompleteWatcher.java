package com.example.Team8;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

import androidx.core.content.ContextCompat;

import com.example.Team8.adapters.StockAdapter;
import com.example.Team8.utils.API;
import com.example.Team8.utils.Stock;

import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class StockAutoCompleteWatcher implements TextWatcher {

    final Executor mainExecutor;
    private final HashMap<String, List<Stock>> pastRequests = new HashMap<>();
    private final StockAdapter stockAdapter;
    private final EventHandler eventHandler;
    Future<?> scheduleFuture;
    Runnable runnable;

    public StockAutoCompleteWatcher(StockAdapter stockAdapter, SearchActivity activity) {
        this.stockAdapter = stockAdapter;
        this.eventHandler = activity;
        mainExecutor = ContextCompat.getMainExecutor(activity);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (scheduleFuture != null) {
            scheduleFuture.cancel(true);
        }
    }

    @Override
    public void afterTextChanged(Editable s) {

        runnable = () -> {
            eventHandler.handleLoadingSymbols(View.VISIBLE);
            String symbolReq = s.toString().toLowerCase();

            if (!pastRequests.containsKey(symbolReq)) {

                eventHandler.handleLoadingSymbols(View.VISIBLE);
                API.getInstance().search(symbolReq, stocks -> {

                    if (stocks != null) {
                        pastRequests.put(symbolReq, stocks);
                        stockAdapter.addAll(stocks);
                    }

                    stockAdapter.notifyDataSetChanged();
                    stockAdapter.getFilter().filter(symbolReq);

                    eventHandler.handleLoadingSymbols(View.INVISIBLE);
                });
            } else {
                eventHandler.handleLoadingSymbols(View.INVISIBLE);
            }
        };

        //Only search after 500ms of no activity
        scheduleFuture = new ScheduledThreadPoolExecutor(1).schedule(runnable, 500, TimeUnit.MILLISECONDS);
    }

    public interface EventHandler {
        void handleLoadingSymbols(int visibility);
    }
}
