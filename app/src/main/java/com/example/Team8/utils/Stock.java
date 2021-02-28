package com.example.Team8.utils;

import java.util.ArrayList;
import java.util.HashMap;

public class Stock {

    public static ArrayList<Stock> stock_symbols = new ArrayList<>();
    private final String currency;
    private final String description;
    private final String displaySymbol;
    private final String figi;
    private final String mic;
    private final String symbol;
    private final String type;
    private final HashMap _data;

    public Stock(HashMap data) {
        _data = data;
        currency = (String) _data.get("currency");
        description = (String) _data.get("description");
        displaySymbol = (String) _data.get("displaySymbol");
        figi = (String) _data.get("figi");
        mic = (String) _data.get("mic");
        symbol = (String) _data.get("symbol");
        type = (String) _data.get("type");
        stock_symbols.add(this);
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
}
