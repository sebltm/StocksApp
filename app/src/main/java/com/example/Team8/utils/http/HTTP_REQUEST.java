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

    private final OkHttpClient client = new OkHttpClient();
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

    @SuppressWarnings("unchecked")
    protected void setResponse(T onResponse, Object value){
        try{
            if(onResponse != null){
                onResponse.response(value);
            }
        }catch (Exception e){
            onResponse.response(null);
        }
    }

    protected String getBodyStr(Response response){
        String body_str = null;
        try {
            if (response.isSuccessful()) {
                ResponseBody body = response.body();

                if(body == null) return null;

                body_str = body.string();
                body.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return body_str;
    }

    protected Callback setCallback(T onResponse) {
        return new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                e.printStackTrace();
                setResponse(onResponse, null);
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) {
                setResponse(onResponse, getBodyStr(response));
            }
        };
    }

    public void Run(T onResponse) {
        try {
            client.newCall(request).enqueue(setCallback(onResponse));
        } catch (Exception e) {
            e.printStackTrace();
            setResponse(onResponse, null);
        }
    }
}
