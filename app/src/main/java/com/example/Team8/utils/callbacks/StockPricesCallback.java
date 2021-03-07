package com.example.Team8.utils.callbacks;

import com.example.Team8.utils.DataPoint;
import com.example.Team8.utils.PricePoint;
import com.example.Team8.utils.Stock;

import java.util.List;

public interface StockPricesCallback {
    void response(List<DataPoint> stockPrices, Stock stock);
}