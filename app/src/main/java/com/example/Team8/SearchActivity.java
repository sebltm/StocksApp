package com.example.Team8;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.Team8.utils.Stock;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class SearchActivity extends AppCompatActivity {

    private static final List<Stock> STOCKS = new ArrayList<Stock>() {{
        add(new Stock(new HashMap<String, String>() {{
            put("currency", "USD");
            put("description", "APPLE INC");
            put("displaySymbol", "AAPL");
            put("figi", "");
            put("mic", "");
            put("symbol", "AAPL");
            put("type", "Common Stock");
        }}));

        add(new Stock(new HashMap<String, String>() {{
            put("currency", "USD");
            put("description", "AAON");
            put("displaySymbol", "AAON");
            put("figi", "");
            put("mic", "");
            put("symbol", "AAON");
            put("type", "Common Stock");
        }}));

        add(new Stock(new HashMap<String, String>() {{
            put("currency", "USD");
            put("description", "AMERICAN AIRLINES");
            put("displaySymbol", "AAL");
            put("figi", "");
            put("mic", "");
            put("symbol", "AAL");
            put("type", "Common Stock");
        }}));
    }};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        EditText editTextFromDate = (EditText) findViewById(R.id.mDatePickerFrom);
        EditText editTextToDate = (EditText) findViewById(R.id.mDatePickerTo);

        DatePickerFragment fromDate = new DatePickerFragment(editTextFromDate, this);
        DatePickerFragment toDate = new DatePickerFragment(editTextToDate, this);

        ArrayAdapter<Stock> stockAdapter = new StockAdapter(this, R.layout.stock_dropdown_item, STOCKS);
        AutoCompleteTextView stockAutocomplete = (AutoCompleteTextView) findViewById(R.id.stockDropdown);
        stockAutocomplete.setAdapter(stockAdapter);
    }

    //TODO refactor this into its own file
    public static class DatePickerFragment implements View.OnClickListener, DatePickerDialog.OnDateSetListener {

        private final EditText editText;
        private final Calendar cal;
        private final Context ctx;
        private static final String format = "dd MMM yyyy";
        private static final SimpleDateFormat dateFormat = new SimpleDateFormat(format, Locale.UK);

        public DatePickerFragment(EditText editText, Context ctx) {
            this.editText = editText;
            this.editText.setOnClickListener(this);
            this.cal = Calendar.getInstance();
            this.ctx = ctx;

            editText.setText(dateFormat.format(cal.getTime()));
        }

        @Override
        public void onClick(View v) {
            new DatePickerDialog(this.ctx, this, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH)).show();
        }

        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            cal.set(year, month, dayOfMonth);
            editText.setText(dateFormat.format(cal.getTime()));
        }
    }


    //TODO refactor this into its own file
    public static class StockAdapter extends ArrayAdapter<Stock> {

        private final Context ctx;
        private final List<Stock> stocks;
        private final List<Stock> stocksDefault;
        private final int resource;

        public StockAdapter(@NonNull Context context, int resource, @NonNull List<Stock> stocks) {
            super(context, resource, stocks);
            this.ctx = context;
            this.stocks = stocks;
            this.stocksDefault = new ArrayList<>(stocks);
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
            return new Filter() {
                @Override
                public CharSequence convertResultToString(Object resultValue) {
                    return ((Stock) resultValue).getDisplaySymbol();
                }

                @Override
                protected FilterResults performFiltering(CharSequence constraint) {
                    FilterResults filterResults = new FilterResults();
                    List<Stock> stockSuggestions = new ArrayList<>();

                    if (constraint != null) {
                        for (Stock stock : stocks) {
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
                        notifyDataSetChanged();
                    } else if (constraint == null) {
                        stocks.addAll(stocksDefault);
                        notifyDataSetInvalidated();
                    }
                }
            };
        }

        static class ViewHolderItem {
            TextView stockSymbol;
            TextView stockDesc;
        }
    }
}