package com.example.Team8.utils.http;

import com.example.Team8.utils.callbacks.InternetCallback;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.Callable;

public class InternetAccessor implements Callable<String> {
    private final String url;
    private InternetCallback delegate = null;
    private boolean CallbackUsed = false;

    public InternetAccessor(String url) {
        this.url = url;
    }

    public void setDelegate(InternetCallback c) {
        delegate = c;
    }

    private String fetchUrl(String url) {
        String urlContent;
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
                myStrBuff.append(urlContent).append('\n');
            }

        } catch (Exception e) {
            if (!CallbackUsed) {
                CallbackUsed = true;
                delegate.internetAccessCompleted(null);
            }
            return null;
        }

        return myStrBuff.toString();
    }

    @Override
    public String call() {
        String myData;
        try {
            myData = fetchUrl(this.url);
        } catch (Exception _e) {
            if (!CallbackUsed) {
                CallbackUsed = true;
                delegate.internetAccessCompleted(null);
            }
            return null;
        }

        if (!CallbackUsed) {
            CallbackUsed = true;
            delegate.internetAccessCompleted(myData);
        }

        return myData;
    }
}


