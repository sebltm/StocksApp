package com.example.Team8.utils;

import com.example.Team8.utils.callbacks.StocksCallback;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class API {
    public static final String domain = "https://finnhub.io";
    private final String endpoint = "/api/v1";
    private final String API_KEY = "c0kg3ln48v6und6ris7g";
    private static API instance = null;

    private API() {
    }

    public static API getInstance() {
        if (instance == null) {
            instance = new API();
        }
        return instance;
    }

    public String join(String path){
        path = path.startsWith("/")? path : String.format("/%1$s", path);
        path = path.endsWith("/")? path.substring(0, path.length()-1) : path;
        return String.format("%1$s%2$s%3$s&token=%4$s", domain, endpoint, path, API_KEY);
    }

    public String getStockSymbolsURL(String exchange){
        return join(String.format("/stock/symbol?exchange=%1$s", exchange));
    }

    public String getStockSymbolsURL(){
        return getStockSymbolsURL("US");
    }

    public String getStockCandlesURL(String symbol, String resolution, Date from, Date to){
        return join(String.format("/stock/candle?symbol=%1$s&resolution=%2$s&from=%3$s&to=%4$s", symbol, resolution, DateTimeHelper.toEpoch(from), DateTimeHelper.toEpoch(to)));
    }

    public String getSearchSymbolURL(String q) throws UnsupportedEncodingException {
        return join(String.format("/search?q=%1$s", encodeValue(q)));
    }

    public boolean isValidStatus(String s){
        ArrayList<String> invalid_status = new ArrayList<String>(){{
            add("no_data");
        }};
        return !invalid_status.contains(s);
    }

    private String encodeValue(String value) throws UnsupportedEncodingException {
        return URLEncoder.encode(value, StandardCharsets.UTF_8.toString());
    }

    private void setSearchCallback(StocksCallback callback, List<Stock> value) {
        if (callback != null) {
            callback.response(value);
        }
    }

    public void search(String query, StocksCallback callback) {
        String getSearchURL;
        try {
            getSearchURL = getSearchSymbolURL(query);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            setSearchCallback(callback, null);
            return;
        }

        HTTP_JSON.fetch(getSearchURL,
                response -> {
                    if (response == null) {
                        setSearchCallback(callback, null);
                        return;
                    }
                    if (response.getType().equals("object")) {
                        HashMap data = response.getDataObj();
                        int count = 0;
                        try {
                            count = (int) data.get("count");
                        } catch (Exception ignored) {
                        }

                        if (count > 0) {
//                            System.out.println(String.format("SEARCH COUNT >> %s %s", count, results.length));
                            List<Stock> stocks_result = new ArrayList<Stock>() {{
                                Object[] results = (Object[]) data.get("result");
                                results = results != null ? results : new Object[0];
                                for (Object o : results) {
                                    add(new Stock((HashMap) o));
                                }
                            }};
                            setSearchCallback(callback, stocks_result.size() > 0 ? stocks_result : null);
                        } else {
                            setSearchCallback(callback, null);
//                            System.out.println("NO RESULTS");
                        }
                    }
                });
    }

}
