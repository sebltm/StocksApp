package com.example.Team8.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.Team8.R;
import com.example.Team8.StockFilter;
import com.example.Team8.utils.Stock;

import java.util.List;

public class StockAdapter extends BaseAdapter implements Filterable {

    protected Filter filter;
    private final int resource;
    private final List<Stock> originalStock;
    private final LayoutInflater mInflater;
    private List<Stock> filteredStock;

    public StockAdapter(@NonNull Context context, int resource, @NonNull List<Stock> stocks) {
        this.originalStock = stocks;
        this.filteredStock = stocks;
        this.resource = resource;
        mInflater = LayoutInflater.from(context);
        this.filter = new StockFilter(this);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        ViewHolderItem viewHolder;

        if (convertView == null) {
            convertView = mInflater.inflate(this.resource, parent, false);

            viewHolder = new ViewHolderItem();
            viewHolder.stockSymbol = convertView.findViewById(R.id.stockSymbol);
            viewHolder.stockDesc = convertView.findViewById(R.id.stockDescription);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolderItem) convertView.getTag();
        }

        Stock stock = filteredStock.get(position);

        if (stock != null) {
            viewHolder.stock = stock;
            viewHolder.stockSymbol.setText(stock.getDisplaySymbol());
            viewHolder.stockDesc.setText(stock.getDescription());
        }
        return convertView;
    }

    @Override
    public int getCount() {
        return filteredStock.size();
    }

    @Nullable
    @Override
    public Stock getItem(int position) {
        return filteredStock.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void addAll(List<Stock> stocks) {
        originalStock.addAll(stocks);
    }

    public List<Stock> getOriginalStock() {
        return originalStock;
    }

    public void setFilteredStock(List<Stock> filteredStock) {
        this.filteredStock = filteredStock;
    }

    @NonNull
    @Override
    public Filter getFilter() {
        return filter;
    }

    public static class ViewHolderItem {
        public Stock stock;
        TextView stockSymbol;
        TextView stockDesc;
    }
}