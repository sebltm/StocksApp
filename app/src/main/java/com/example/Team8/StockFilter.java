package com.example.Team8;

import android.widget.ArrayAdapter;
import android.widget.Filter;

import com.example.Team8.utils.Stock;

import java.util.ArrayList;
import java.util.List;

public class StockFilter extends Filter {

    ArrayAdapter<Stock> parent;

    public StockFilter(ArrayAdapter<Stock> parent) {
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
            for (int i = 0; i < parent.getCount(); i++) {
                Stock stock = parent.getItem(i);
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
        if (results != null && results.count > 0) {
            parent.clear();
            parent.addAll((ArrayList<Stock>) results.values);
            parent.notifyDataSetChanged();
        } else if (constraint == null) {
            parent.notifyDataSetInvalidated();
        }
    }
}


