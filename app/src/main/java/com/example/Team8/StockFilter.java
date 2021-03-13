package com.example.Team8;

import android.widget.Filter;

import com.example.Team8.adapters.StockAdapter;
import com.example.Team8.utils.Stock;

import java.util.ArrayList;
import java.util.List;

public class StockFilter extends Filter {

    StockAdapter parent;

    public StockFilter(StockAdapter parent) {
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
            List<Stock> originalStock = parent.getOriginalStock();
            for (Stock stock : originalStock) {
                if (stock.getDisplaySymbol().toLowerCase()
                        .contains(constraint.toString().toLowerCase())
                        || stock.getDescription().toLowerCase()
                        .contains(constraint.toString().toLowerCase())
                        || stock.getSymbol().toLowerCase()
                        .contains(constraint.toString().toLowerCase())) {
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
        if (results != null && results.count > 0) {
            parent.setFilteredStock((List<Stock>) results.values);
            parent.notifyDataSetChanged();
        } else if (constraint == null) {
            parent.notifyDataSetInvalidated();
        }
    }
}


