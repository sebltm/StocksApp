package com.example.Team8.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Date;

public class API {
    public static final String domain = "https://finnhub.io";
    private static String endpoint = "/api/v1";
    private static String API_KEY = "c0kg3ln48v6und6ris7g";

    public static String join(String path){
        path = path.startsWith("/")? path : String.format("/%1$s", path);
        path = path.endsWith("/")? path.substring(0, path.length()-1) : path;
        return String.format("%1$s%2$s%3$s&token=%4$s", domain, endpoint, path, API_KEY);
    }

    public static String getStockSymbols(String exchange){
        return join(String.format("/stock/symbol?exchange=%1$s", exchange));
    }

    public static String getStockSymbols(){
        return getStockSymbols("US");
    }

    public static String getStockCandles(String symbol, String resolution, Date from, Date to){
        return join(String.format("/stock/candle?symbol=%1$s&resolution=%2$s&from=%3$s&to=%4$s", symbol, resolution, DateTimeHelper.toEpoch(from), DateTimeHelper.toEpoch(to)));
    }

    public static String getSearchSymbolURL(String q) throws UnsupportedEncodingException {
        return join(String.format("/search?q=%1$s", encodeValue(q)));
    }

    public static boolean isValidStatus(String s){
        ArrayList<String> invalid_status = new ArrayList<>();
        invalid_status.add("no_data");
        return !invalid_status.contains(s);
    }

    private static String encodeValue(String value) throws UnsupportedEncodingException {
        return URLEncoder.encode(value, StandardCharsets.UTF_8.toString());
    }

}
