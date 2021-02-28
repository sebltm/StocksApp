package com.example.Team8;

import android.widget.ArrayAdapter;
import android.widget.Filter;

import com.example.Team8.utils.Stock;

import java.util.ArrayList;
import java.util.List;

public class StockFilter extends Filter {

    List<Stock> stocks;
    List<Stock> stocksDefault;
    ArrayAdapter<?> parent;

    public StockFilter(List<Stock> stocks, ArrayAdapter<?> parent) {
        this.stocks = stocks;
        this.stocksDefault = new ArrayList<>(stocks);
        this.parent = parent;
    }

    @Override
    public CharSequence convertResultToString(Object resultValue) {
        return ((Stock) resultValue).getDisplaySymbol();
    }

    @Override
    protected FilterResults performFiltering(CharSequence constraint) {
        FilterResults filterResults = new FilterResults();
        List<Stock> stockSuggestions = new ArrayList<>();

        if (constraint != null) {
            for (Stock stock : stocksDefault) {
                if (stock.getDisplaySymbol().toLowerCase()
                        .startsWith(constraint.toString().toLowerCase())
                        || stock.getDescription().toLowerCase()
                        .startsWith(constraint.toString().toLowerCase())) {
                    stockSuggestions.add(stock);
                }
            }

            filterResults.values = stockSuggestions;
            filterResults.count = stockSuggestions.size();
        }

        return filterResults;
    }

    @Override
    protected void publishResults(CharSequence constraint, FilterResults results) {
        stocks.clear();
        if (results != null && results.count > 0) {
            for (Object object : (List<?>) results.values) {
                if (object instanceof Stock) {
                    stocks.add((Stock) object);
                }
            }
            parent.notifyDataSetChanged();
        } else if (constraint == null) {
            stocks.addAll(stocksDefault);
            parent.notifyDataSetInvalidated();
        }
    }
}


