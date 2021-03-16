package com.example.Team8;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

import androidx.core.content.ContextCompat;

import com.example.Team8.adapters.StockAdapter;
import com.example.Team8.utils.API;
import com.example.Team8.utils.Stock;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class StockAutoCompleteWatcher implements TextWatcher {

    private final HashMap<String, List<Stock>> pastRequests = new HashMap<>();
    private static Date lastRequest;
    final Executor mainExecutor;
    private final StockAdapter stockAdapter;
    private final EventHandler eventHandler;
    private final List<Boolean> loading;
    Future<?> scheduleFuture;
    Runnable runnable;

    public StockAutoCompleteWatcher(StockAdapter stockAdapter, SearchActivity activity) {
        this.stockAdapter = stockAdapter;
        this.eventHandler = activity;
        loading = new ArrayList<>();
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
            String symbolReq = s.toString().toLowerCase();

            if (!pastRequests.containsKey(symbolReq)) {

                System.err.println(String.format("FETCHING FOR %s", symbolReq));

                int currentSize = loading.size();
                loading.add(true);
                eventHandler.handleLoadingSymbols(View.VISIBLE);
                API.getInstance().search(symbolReq, stocks -> {

                    if (stocks != null) {
                        pastRequests.put(symbolReq, stocks);
                        stockAdapter.addAll(stocks);
                    }

                    System.err.println(String.format("FILTERING FOR %s", symbolReq));
                    stockAdapter.notifyDataSetChanged();
                    stockAdapter.getFilter().filter(symbolReq);

                    loading.set(currentSize, false);
                    int visible = View.INVISIBLE;
                    for (boolean setVisible : loading) {
                        if (setVisible) {
                            visible = View.VISIBLE;
                            break;
                        }
                    }
                    eventHandler.handleLoadingSymbols(visible);
                });
            } else {
                int visible = View.INVISIBLE;
                for (boolean setInvisible : loading) {
                    if (!setInvisible) {
                        visible = View.VISIBLE;
                        break;
                    }
                }
                eventHandler.handleLoadingSymbols(visible);
            }
        };

        //Only search after 500ms of no activity
        scheduleFuture = new ScheduledThreadPoolExecutor(1).schedule(runnable, 500, TimeUnit.MILLISECONDS);
    }

    public interface EventHandler {
        void handleLoadingSymbols(int visibility);
    }
}
