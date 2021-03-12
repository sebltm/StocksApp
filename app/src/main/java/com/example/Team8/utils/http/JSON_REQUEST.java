package com.example.Team8.utils.http;

import com.example.Team8.utils.JSON;
import com.example.Team8.utils.callbacks.HTTPCallback;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Request;
import okhttp3.Response;

public class JSON_REQUEST extends HTTP_REQUEST<HTTPCallback<JSON>> {
    public JSON_REQUEST(String url) {
        super(url);
    }

    public JSON_REQUEST(Request request) {
        super(request);
    }

    @Override
    protected Callback setCallback(HTTPCallback<JSON> onResponse) {
        return new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                e.printStackTrace();
                setResponse(onResponse, null);
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) {
                String body_str = getBodyStr(response);
                boolean error = false;
                JSON j = null;
                try {
                    if (body_str != null) {
                        j = new JSON(body_str);
                        if (j.getType() == null) {
                            error = true;
                        }
                    } else {
                        error = true;
                    }
                }catch (Exception e){
                    e.printStackTrace();
                    error = true;
                }
                setResponse(onResponse, error? null : j);
            }
        };
    }
}
