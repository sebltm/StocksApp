package com.example.Team8.utils.http;

import android.os.AsyncTask;

import com.example.Team8.utils.callbacks.InternetCallback;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class InternetAccessor extends AsyncTask<String, Void, String> {
    private InternetCallback delegate = null;
    private static InternetAccessor instance = null;
    private boolean CallbackUsed = false;

    public void setDelegate(InternetCallback c) {
        delegate = c;
    }

    public static InternetAccessor getInstance() {
        if (instance == null) {
            instance = new InternetAccessor();
        }
        return instance;
    }

    @Override
    protected void onPreExecute() {
    }

    @Override
    protected String doInBackground(String... params) {
        String url = params[0];
        String myData = "";
        try {
            myData = fetchUrl(url);
        } catch (Exception _e) {
            if(!CallbackUsed){
                CallbackUsed = true;
                delegate.internetAccessCompleted(null);
            }
            return null;
        }
        return myData;
    }

    private String fetchUrl(String url) {
        String urlContent = "";
        StringBuilder myStrBuff = new StringBuilder();

        try {
            URL myUrl = new URL(url);
            HttpURLConnection myConn = (HttpURLConnection) myUrl.openConnection();
            myConn.setRequestProperty("User-Agent", "");
            myConn.setRequestMethod("GET");
            myConn.setDoInput(true);
            myConn.connect();

            InputStream myInStrm = myConn.getInputStream();
            BufferedReader myBuffRdr = new BufferedReader(new InputStreamReader(myInStrm));

            while ((urlContent = myBuffRdr.readLine()) != null) {
                myStrBuff.append(urlContent + '\n');
            }

        } catch (Exception e) {
            if(!CallbackUsed) {
                CallbackUsed = true;
                delegate.internetAccessCompleted(null);
            }
            return null;
        }

        return myStrBuff.toString();
    }

    @Override
    protected void onPostExecute(String result) {
        if(!CallbackUsed) {
            CallbackUsed = true;
            delegate.internetAccessCompleted(result);
        }
    }

    @Override
    protected void onProgressUpdate(Void... values) {
    }
}

