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

    private final int resource;
    private final List<Stock> stocks;

    public StockAdapter(@NonNull Context context, int resource, @NonNull List<Stock> stocks) {
        super(context, resource, stocks);
        this.stocks = stocks;
        this.resource = resource;
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
            viewHolder.stockSymbol.setText(getItem(position).getDisplaySymbol());
            viewHolder.stockDesc.setText(getItem(position).getDescription());
        }

        return convertView;
    }

    @Override
    public void add(@Nullable Stock object) {
        stocks.add(object);
    }

    @Override
    public int getCount() {
        return stocks.size();
    }

    @Override
    public void addAll(Stock... items) {
        stocks.addAll(new ArrayList<>(Arrays.asList(items)));
    }

    @Nullable
    @Override
    public Stock getItem(int position) {
        return stocks.get(position);
    }

    @NonNull
    @Override
    public Filter getFilter() {
        return new StockFilter(this);
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