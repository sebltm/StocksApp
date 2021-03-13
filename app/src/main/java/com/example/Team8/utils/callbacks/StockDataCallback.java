package com.example.Team8.utils.callbacks;

import com.example.Team8.utils.PricePoint;
import com.example.Team8.utils.Stock;

public interface StockDataCallback {
    void response(PricePoint price_points, Stock stock);
}