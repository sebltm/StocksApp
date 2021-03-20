package com.example.Team8.adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.Team8.R;
import com.example.Team8.database.SearchHistoryDatabase;
import com.example.Team8.utils.AnalysisType;
import com.example.Team8.utils.SearchHistoryItem;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class SearchHistoryAdapter<T extends Activity & SearchHistoryAdapter.SearchHistoryHolder.SearchItemEvent> extends RecyclerView.Adapter<SearchHistoryAdapter.SearchHistoryHolder> {

    private static final String format = "dd MMM yyyy";
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat(format, Locale.UK);
    private final List<SearchHistoryItem> localItems;
    private final T context;

    public SearchHistoryAdapter(List<SearchHistoryItem> searchHistoryItems, T context) {
        this.localItems = searchHistoryItems;
        this.context = context;
    }

    @NonNull
    @Override
    public SearchHistoryHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_history_item, parent, false);

        return new SearchHistoryHolder(view, context);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchHistoryHolder holder, int position) {
        SearchHistoryItem item = localItems.get(position);

        holder.setSearchHistoryItem(item);

        holder.getDateFrom().setText(dateFormat.format(item.getFrom()));
        holder.getDateTo().setText(dateFormat.format(item.getTo()));
        holder.getSymbol().setText(item.getStock().getDisplaySymbol());

        StringBuilder analysisBuilder = new StringBuilder();
        for (AnalysisType analysisType : item.getAnalysisTypes()) {
            analysisBuilder.append(analysisType.name()).append(", ");
        }

        String analysisTypeStr = analysisBuilder.
                deleteCharAt(analysisBuilder.length() - 1)
                .deleteCharAt(analysisBuilder.length() - 1)
                .toString();
        holder.getAnalysisTypes().setText(analysisTypeStr);
        holder.getDescription().setText(item.getStock().getDescription());
    }

    public void refreshInternalList(Integer numItems) {
        localItems.clear();

        SearchHistoryDatabase db = SearchHistoryDatabase.getInstance(context);
        localItems.addAll(db.getSearchHistoryDao().loadN(numItems));
    }

    public void addAll(List<SearchHistoryItem> searchHistoryItems) {
        localItems.addAll(searchHistoryItems);
        this.notifyDataSetChanged();
    }

    public void clear() {
        localItems.clear();
        this.notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return localItems.size();
    }

    public static class SearchHistoryHolder extends RecyclerView.ViewHolder {

        private final TextView dateFrom;
        private final TextView dateTo;
        private final TextView symbol;
        private final TextView analysisTypes;
        private final TextView description;

        private final Button repeatSearch;
        private final Button showSummary;

        private final SearchItemEvent searchItemEvent;

        public SearchHistoryHolder(@NonNull View itemView, SearchItemEvent context) {
            super(itemView);

            this.dateFrom = itemView.findViewById(R.id.search_history_date_from);
            this.dateTo = itemView.findViewById(R.id.search_history_date_to);
            this.symbol = itemView.findViewById(R.id.search_history_symbol);
            this.analysisTypes = itemView.findViewById(R.id.search_history_analysis_types);
            this.description = itemView.findViewById(R.id.search_history_description);

            this.repeatSearch = itemView.findViewById(R.id.search_hist_item_repeat);
            this.showSummary = itemView.findViewById(R.id.search_hist_item_summary);

            this.searchItemEvent = context;
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

        public TextView getAnalysisTypes() {
            return analysisTypes;
        }

        public TextView getDescription() {
            return description;
        }

        public void setSearchHistoryItem(SearchHistoryItem searchHistoryItem) {
            this.repeatSearch.setOnClickListener(v -> searchItemEvent.repeatSearch(searchHistoryItem));

            this.showSummary.setOnClickListener(v -> searchItemEvent.showSummary(searchHistoryItem));
        }

        public interface SearchItemEvent {
            void showSummary(SearchHistoryItem searchHistoryItem);

            void repeatSearch(SearchHistoryItem searchHistoryItem);
        }
    }

}

