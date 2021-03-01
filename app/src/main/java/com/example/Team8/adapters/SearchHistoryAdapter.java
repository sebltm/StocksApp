package com.example.Team8.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.Team8.R;
import com.example.Team8.utils.AnalysisType;
import com.example.Team8.utils.SearchHistoryItem;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class SearchHistoryAdapter extends RecyclerView.Adapter<SearchHistoryAdapter.SearchHistoryHolder> {

    private static final String format = "dd MMM yyyy";
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat(format, Locale.UK);
    private final List<SearchHistoryItem> localItems;

    public SearchHistoryAdapter(List<SearchHistoryItem> searchHistoryItems) {
        this.localItems = searchHistoryItems;
    }

    @NonNull
    @Override
    public SearchHistoryHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_history_item, parent, false);

        return new SearchHistoryHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchHistoryHolder holder, int position) {
        SearchHistoryItem item = localItems.get(position);

        holder.getDateFrom().setText(dateFormat.format(item.getFrom()));
        holder.getDateTo().setText(dateFormat.format(item.getTo()));
        holder.getSymbol().setText(item.getStock().getDisplaySymbol());

        StringBuilder analysisBuilder = new StringBuilder();
        for (AnalysisType analysisType : item.getAnalysisTypes()) {
            analysisBuilder.append(analysisType.name()).append(", ");
        }

        String analysisTypeStr = analysisBuilder.deleteCharAt(analysisBuilder.length() - 1).toString();
        holder.getAnalysisTypes().setText(analysisTypeStr);
        holder.getDescription().setText(item.getStock().getDescription());
    }

    @Override
    public int getItemCount() {
        return localItems.size();
    }

    public static class SearchHistoryHolder extends RecyclerView.ViewHolder {

        private final TextView dateFrom;
        private final TextView dateTo;
        private final TextView symbol;
        private final TextView exchange;
        private final TextView analysisTypes;
        private final TextView description;

        public SearchHistoryHolder(@NonNull View itemView) {
            super(itemView);

            this.dateFrom = (TextView) itemView.findViewById(R.id.search_history_date_from);
            this.dateTo = (TextView) itemView.findViewById(R.id.search_history_date_to);
            this.symbol = (TextView) itemView.findViewById(R.id.search_history_symbol);
            this.exchange = (TextView) itemView.findViewById(R.id.search_history_exchange);
            this.analysisTypes = (TextView) itemView.findViewById(R.id.search_history_analysis_types);
            this.description = (TextView) itemView.findViewById(R.id.search_history_description);
        }

        public TextView getDateFrom() {
            return dateFrom;
        }

        public TextView getDateTo() {
            return dateTo;
        }

        public TextView getSymbol() {
            return symbol;
        }

        public TextView getExchange() {
            return exchange;
        }

        public TextView getAnalysisTypes() {
            return analysisTypes;
        }

        public TextView getDescription() {
            return description;
        }
    }

}

