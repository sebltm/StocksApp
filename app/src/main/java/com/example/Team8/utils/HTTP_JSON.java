package com.example.Team8.utils;

import com.example.Team8.utils.callbacks.JSONCallback;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;

public class HTTP_JSON {

    public static void fetch(String url, JSONCallback json_callback) {
        InternetAccessor x = new InternetAccessor(url);

        x.setDelegate(response -> {
            boolean error = false;
            JSON j = null;
            try {
                if (response == null) {
                    error = true;
                } else {
                    j = new JSON(response);
                }
            } catch (Exception e) {
                error = true;
                e.printStackTrace();
            }
            json_callback.response(error ? null : j);
        });

        FutureTask<String> urlAccess = new FutureTask<>(x);
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(urlAccess);
        executorService.shutdown();
    }
}
