package com.example.Team8.utils.http;

import com.example.Team8.utils.JSON;
import com.example.Team8.utils.callbacks.HTTPCallback;

public class HTTP_JSON {

    /**
     * OkHttp Request: uses Background threads
     * @param url: String type
     * @param callback: returns JSON type response
     */
    public static void fetch(String url, HTTPCallback<JSON> callback){
        new JSON_REQUEST(url).Run(callback);
    }
}
