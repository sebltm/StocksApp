package com.example.Team8.utils.callbacks;

import com.example.Team8.utils.PricePoint;
import com.example.Team8.utils.Stock;

import java.util.List;

public interface StockDataCallback {
    void response(List<PricePoint> price_points, Stock stock);
}