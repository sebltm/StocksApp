package com.example.Team8.utils.http;

import com.example.Team8.utils.JSON;
import com.example.Team8.utils.callbacks.HTTPCallback;
import com.example.Team8.utils.callbacks.JSONCallback;
import com.example.Team8.utils.http.InternetAccessor;

public class HTTP_JSON {

    /**
     * deprecated AsyncTask. Throws errors on multiple requests simultaneously
     * @param url: String type
     * @param json_callback: returns JSON type response
     */
    public static void fetchOLD(String url, JSONCallback json_callback){
        InternetAccessor x = InternetAccessor.getInstance();
        x.setDelegate(response -> {
            boolean error = false;
            JSON j = null;
            try{
                if(response == null){
                    error = true;
                }else{
                    j = new JSON(response);
                }
            }catch (Exception e){
                error = true;
                e.printStackTrace();
            }
            json_callback.response(error? null : j);
        });
        x.execute(url);
    }

    /**
     * OkHttp Request: uses Background threads
     * @param url: String type
     * @param callback: returns JSON type response
     */
    public static void fetch(String url, HTTPCallback<JSON> callback){
        new JSON_REQUEST(url).Run(callback);
    }
}
