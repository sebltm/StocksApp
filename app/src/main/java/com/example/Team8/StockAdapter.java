package com.example.Team8;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.Team8.utils.Stock;

import java.util.List;

public class StockAdapter extends ArrayAdapter<Stock> {

    private final Context ctx;
    private final List<Stock> stocks;
    private final int resource;

    public StockAdapter(@NonNull Context context, int resource, @NonNull List<Stock> stocks) {
        super(context, resource, stocks);
        this.ctx = context;
        this.stocks = stocks;
        this.resource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        ViewHolderItem viewHolder;

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(this.resource, parent, false);

            viewHolder = new ViewHolderItem();
            viewHolder.stockSymbol = (TextView) convertView.findViewById(R.id.stockSymbol);
            viewHolder.stockDesc = (TextView) convertView.findViewById(R.id.stockDescription);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolderItem) convertView.getTag();
        }

        Stock stock = stocks.get(position);

        if (stock != null) {
            viewHolder.stockSymbol.setText(stocks.get(position).getDisplaySymbol());
            viewHolder.stockDesc.setText(stocks.get(position).getDescription());
        }

        return convertView;
    }

    @NonNull
    @Override
    public Filter getFilter() {
        return new StockFilter(stocks, this);
    }

    static class ViewHolderItem {
        TextView stockSymbol;
        TextView stockDesc;
    }
}