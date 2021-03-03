package com.example.Team8.utils;

import java.util.List;

public interface StockDataCallback {
    public void response(List<PricePoint> price_points, Stock stock);
}