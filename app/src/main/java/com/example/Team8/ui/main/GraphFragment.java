package com.example.Team8.ui.main;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;

import com.example.Team8.R;
import com.example.Team8.SearchActivity;
import com.example.Team8.utils.AnalysisPoint;
import com.example.Team8.utils.AnalysisType;
import com.example.Team8.utils.DataPoint;
import com.example.Team8.utils.DateTimeHelper;
import com.example.Team8.utils.PricePoint;
import com.example.Team8.utils.SearchHistoryItem;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

public class GraphFragment extends Fragment {

    private static final String format = "dd-MM-yy";
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat(format, Locale.UK);
    private static boolean globalPriceActive = false;
    private final SearchHistoryItem searchItem;
    private final AnalysisType analysisType;
    private LineChart mpLineChart;
    private List<AnalysisPoint> analysisPoints;
    private PricePoint pricePoint;
    private View graphView;
    private boolean localPriceActive = false;
    private boolean dataNotLoaded = true;
    private SwitchCompat togglePrice;

    public GraphFragment(SearchHistoryItem searchItem, AnalysisType analysisType) {
        this.searchItem = searchItem;
        this.analysisType = analysisType;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        this.graphView = inflater.inflate(R.layout.fragment_graph, container, false);

        TextView stockSymbol = graphView.findViewById(R.id.graph_frag_stock_symbol);
        TextView dateFrom = graphView.findViewById(R.id.graph_frag_date_from);
        TextView dateTo = graphView.findViewById(R.id.graph_frag_date_to);
        togglePrice = graphView.findViewById(R.id.toggle_price_line);
        mpLineChart = graphView.findViewById(R.id.graph_frag_line_graph);

        setSummaryHandler();

        stockSymbol.setText(searchItem.getStock().getDisplaySymbol());
        dateFrom.setText(dateFormat.format(searchItem.getFrom()));
        dateTo.setText(dateFormat.format(searchItem.getTo()));

        togglePrice.setChecked(globalPriceActive);
        pricePoint = searchItem.getStock().getPriceHistory();

        togglePrice.setOnCheckedChangeListener((buttonView, isChecked) -> {
            globalPriceActive = isChecked;
            localPriceActive = isChecked;

            createChart(graphView, analysisPoints, pricePoint, localPriceActive);
        });

        return graphView;
    }

    private void setSummaryHandler() {
        Button summaryBtn = graphView.findViewById(R.id.summaryBtn);
        summaryBtn.setOnClickListener((view) ->
                searchItem.getStock().showSummary(
                        getContext(),
                        searchItem.getFrom(),
                        searchItem.getTo()
                )
        );
    }

    private List<Entry> pointToEntry(List<AnalysisPoint> analysisPoints) {
        return analysisPoints
                .stream()
                .map(point -> new Entry(point.getDateTime().getTime(), point.getValue().floatValue()))
                .collect(Collectors.toList());
    }

    private List<Entry> pointToEntry(PricePoint pricePoints) {
        List<Entry> entryList = new ArrayList<>();

        List<String> timestamps = pricePoints.getTimestamps();
        List<DataPoint> close_prices = pricePoints.getClose();
        int timestamps_size = timestamps.size();

        for (int i = 0; i < timestamps_size; i++) {
            long time = DateTimeHelper.toDateTime(timestamps.get(i)).getTime();
            long value = close_prices.get(i).getValue().longValue();

            entryList.add(new Entry(time, value));
        }

        return entryList;
    }

    private void createChart(View parentView, List<AnalysisPoint> analysisPoints, PricePoint pricePoint, boolean displayPrice) {
        mpLineChart = parentView.findViewById(R.id.graph_frag_line_graph);
        mpLineChart.setPinchZoom(true);

        Description description = new Description();
        description.setText("");
        mpLineChart.setDescription(description);

        mpLineChart.setNoDataText("Not enough data available to show a graph.\nPlease select a wider range of dates.");

        LineDataSet analysisDataset = new LineDataSet(pointToEntry(analysisPoints), analysisType.name());
        analysisDataset.setValueTextSize(9);
        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
        dataSets.add(analysisDataset);

        if (displayPrice) {
            LineDataSet priceDataset = new LineDataSet(pointToEntry(pricePoint), "Price");
            if (getActivity() != null && getActivity().getTheme() != null) {
                priceDataset.setColor(getResources().getColor(R.color.green, getActivity().getTheme()));
                priceDataset.setCircleColor(getResources().getColor(R.color.green, getActivity().getTheme()));
            }
            priceDataset.setValueTextSize(9);
            dataSets.add(priceDataset);
        }

        XAxis xAxis = mpLineChart.getXAxis();
        xAxis.setGranularity(1f);
        xAxis.setValueFormatter(new StockPriceFormat());
        xAxis.setLabelRotationAngle(30);

        xAxis.setAxisMinimum(searchItem.getFrom().getTime());

        LineData data = new LineData(dataSets);
        mpLineChart.setData(data);
        mpLineChart.invalidate();
    }

    @Override
    public void onResume() {
        super.onResume();

        new Thread(() -> {
            ProgressBar spinner = graphView.findViewById(R.id.spinner_graph);

            if (dataNotLoaded) {
                analysisPoints = searchItem.getStock().calculateSelectedIndicators(
                        analysisType,
                        searchItem.getFrom(),
                        searchItem.getTo(),
                        searchItem.getAnalysisDays(),
                        errors -> errors.forEach(error ->
                                getActivity().runOnUiThread(() -> Toast.makeText(getActivity(), error, Toast.LENGTH_LONG).show())
                        )
                );

                createChart(graphView, analysisPoints, pricePoint, globalPriceActive);

                dataNotLoaded = false;
            } else {
                if (localPriceActive != globalPriceActive) {
                    localPriceActive = globalPriceActive;

                    createChart(this.graphView, analysisPoints, pricePoint, localPriceActive);
                }
            }

            Activity activity = getActivity();
            if (activity != null) activity.runOnUiThread(() -> {
                spinner.setVisibility(View.INVISIBLE);
                togglePrice.setChecked(localPriceActive);
            });
        }).start();
    }

    static class StockPriceFormat extends ValueFormatter {
        @Override
        public String getAxisLabel(float value, AxisBase axis) {
            return dateFormat.format(value);
        }
    }
}
