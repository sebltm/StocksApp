package com.example.Team8.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.Team8.R;
import com.example.Team8.StockFilter;
import com.example.Team8.utils.Stock;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class StockAdapter extends ArrayAdapter<Stock> {

    protected Filter filter;
    private final int resource;
    private final List<Stock> stocks;

    public StockAdapter(@NonNull Context context, int resource, @NonNull List<Stock> stocks) {
        super(context, resource, stocks);
        this.stocks = stocks;
        this.resource = resource;
        this.filter = new StockFilter(this);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        ViewHolderItem viewHolder;

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(this.resource, parent, false);

            viewHolder = new ViewHolderItem();
            viewHolder.stockSymbol = (TextView) convertView.findViewById(R.id.stockSymbol);
            viewHolder.stockDesc = (TextView) convertView.findViewById(R.id.stockDescription);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolderItem) convertView.getTag();
        }

        Stock stock = getItem(position);

        if (stock != null) {
            viewHolder.stock = stock;
            viewHolder.stockSymbol.setText(stock.getDisplaySymbol());
            viewHolder.stockDesc.setText(stock.getDescription());
        }
        return convertView;
    }


    @Override
    public void add(@Nullable Stock object) {
        stocks.add(object);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return stocks.size();
    }

    @Override
    public void addAll(Stock... items) {
        stocks.addAll(new ArrayList<>(Arrays.asList(items)));
        notifyDataSetChanged();
    }

    @Nullable
    @Override
    public Stock getItem(int position) {
        return stocks.get(position);
    }

    @NonNull
    @Override
    public Filter getFilter() {
        return filter;
    }

    @Override
    public void clear() {
        stocks.clear();
    }

    public static class ViewHolderItem {
        public Stock stock;
        TextView stockSymbol;
        TextView stockDesc;
    }
}