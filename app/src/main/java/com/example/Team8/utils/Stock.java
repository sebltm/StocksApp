package com.example.Team8.utils;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class Stock {

    public static ArrayList<Stock> stocks = new ArrayList<Stock>();
    private String currency;
    private String description;
    private String displaySymbol;
    private String figi;
    private String mic;
    private String symbol;
    private String type;
    private List<PricePoint> priceHistory = new ArrayList<>();


    public Stock(HashMap data) {
        currency = (String) data.get("currency");
        description = (String) data.get("description");
        displaySymbol = (String) data.get("displaySymbol");
        figi = (String) data.get("figi");
        mic = (String) data.get("mic");
        symbol = (String) data.get("symbol");
        type = (String) data.get("type");
        stocks.add(this);
    }

    public String getCurrency() {
        return currency;
    }

    public String getDescription() {
        return description;
    }

    public String getDisplaySymbol() {
        return displaySymbol;
    }

    public String getFigi() {
        return figi;
    }

    public String getMic() {
        return mic;
    }

    public String getSymbol() {
        return symbol;
    }

    public String getType() {
        return type;
    }

    @Override
    public String toString() {
        return "Stock{" +
                "currency='" + currency + '\'' +
                ", description='" + description + '\'' +
                ", displaySymbol='" + displaySymbol + '\'' +
                ", figi='" + figi + '\'' +
                ", mic='" + mic + '\'' +
                ", symbol='" + symbol + '\'' +
                ", type='" + type + '\'' +
                '}';
    }

    public List<AnalysisPoint> calculateSMA(int nDays) {
        return new ArrayList<>();
    }

    public List<AnalysisPoint> calculateEMA(int nDays) {
        return new ArrayList<>();
    }

    public List<AnalysisPoint> calculateMACD() {
        return new ArrayList<>();
    }

    public List<AnalysisPoint> calculateMACDAVG() {
        return new ArrayList<>();
    }

    private void setResponseCallback(StockDataCallback callback, List<PricePoint> value) {
        if (callback != null) {
            callback.response(value);
        }
    }

    public void fetchData(String resolution, Date from, Date to, StockDataCallback callback) {
        String getStockCandlesURL;
        try {
            getStockCandlesURL = API.getStockCandles(symbol, Resolution.types.get(resolution), from, to);
        } catch (Exception e) {
            setResponseCallback(callback, null);
            throw new NoSuchFieldError();
        }

//        System.out.println(String.format("FETCH DATA FOR %2$s: %1$s", getStockCandlesURL, symbol));

        HTTP_JSON.fetch(getStockCandlesURL,
                response -> {
                    if (response == null) {
                        setResponseCallback(callback, null);
                        return;
                    }
                    JSON j = (JSON) response;
                    if (response.getType().equals("object")) {
                        HashMap data = j.getDataObj();
                        boolean status = API.isValidStatus((String) data.get("s"));
                        if (!status) {
//                            System.out.println("NO DATA FOUND");
                            setResponseCallback(callback, null);
                            return;
                        }
                        PricePoint pp = new PricePoint(data);
                        priceHistory.add(pp);
                        setResponseCallback(callback, priceHistory);
                    } else {
                        setResponseCallback(callback, null);
                    }
                });
    }
}
