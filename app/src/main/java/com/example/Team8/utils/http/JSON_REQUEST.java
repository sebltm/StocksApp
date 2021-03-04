package com.example.Team8.utils.http;

import com.example.Team8.utils.JSON;
import com.example.Team8.utils.callbacks.HTTPCallback;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

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
                onResponse.response(null);
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String body_str = null;
                boolean error = false;
                JSON j = null;

                try{
                    if(response.isSuccessful()){
                        ResponseBody body = response.body();
                        body_str = body.string();
                        body.close();
                    }
                }catch (Exception e){
                }

                if(body_str != null){
                    try{
                        if(response == null){
                            error = true;
                        }else{
                            j = new JSON(body_str);
                        }
                    }catch (Exception e){
                        error = true;
                        e.printStackTrace();
                    }
                }else{
                    error = true;
                }
                onResponse.response(error? null : j);
            }
        };
    }
}
