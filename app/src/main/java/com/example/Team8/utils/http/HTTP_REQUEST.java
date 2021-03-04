package com.example.Team8.utils.http;

import com.example.Team8.utils.callbacks.HTTPCallback;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class HTTP_REQUEST<T extends HTTPCallback> {

    private OkHttpClient client = new OkHttpClient();
    protected Request request = null;

    public HTTP_REQUEST(String url) {
        setRequest(url);
    }

    public HTTP_REQUEST(Request request) {
        this.request = request;
    }

    protected void setRequest(String url) {
        this.request = new Request.Builder()
                .url(url)
                .get()
                .build();
    }

    protected Callback setCallback(T onResponse) {
        return new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                e.printStackTrace();
                onResponse.response(null);
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String body_str = null;
                try{
                    if(response.isSuccessful()){
                        ResponseBody body = response.body();
                        body_str = body.string();
                        body.close();
                    }
                }catch (Exception e){
                }
                onResponse.response(body_str);
            }
        };
    }

    public void Run(T onResponse) {
        try {
            client.newCall(request).enqueue(setCallback(onResponse));
        } catch (Exception e) {
            e.printStackTrace();
            onResponse.response(null);
        }
    }
}
