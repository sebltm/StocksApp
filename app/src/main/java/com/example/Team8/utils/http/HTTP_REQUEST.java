package com.example.Team8.utils.http;

import com.example.Team8.utils.callbacks.HTTPCallback;

import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

public abstract class HTTP_REQUEST<T extends HTTPCallback<A>, A> {

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

    protected void setResponse(T onResponse, A value) {
        try {
            if (onResponse != null) {
                onResponse.response(value);
            }
        } catch (Exception e) {
            onResponse.response(null);
        }
    }

    protected String getBodyStr(Response response) {
        String body_str = null;
        try {
            if (response.isSuccessful()) {
                ResponseBody body = response.body();

                if (body == null) return null;

                body_str = body.string();
                body.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return body_str;
    }

    protected abstract Callback setCallback(T onResponse);

    public void run(T onResponse) {
        try {
            client.newCall(request).enqueue(setCallback(onResponse));
        } catch (Exception e) {
            e.printStackTrace();
            setResponse(onResponse, null);
        }
    }
}
