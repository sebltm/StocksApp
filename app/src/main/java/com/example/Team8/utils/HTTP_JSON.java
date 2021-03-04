package com.example.Team8.utils;

import com.example.Team8.utils.callbacks.JSONCallback;

public class HTTP_JSON {

    public static void fetch(String url, JSONCallback json_callback){
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
}
