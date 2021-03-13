package com.example.Team8.ui.main;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.Team8.R;
import com.example.Team8.utils.AnalysisPoint;
import com.example.Team8.utils.AnalysisType;
import com.example.Team8.utils.Resolution;
import com.example.Team8.utils.SearchHistoryItem;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class GraphFragment extends Fragment {

    private static final String format = "dd-mm-yy";
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat(format, Locale.UK);
    LineChart mpLineChart;
    SearchHistoryItem searchItem;
    AnalysisType analysisType;
    List<AnalysisPoint> analysisPoints;

    private boolean dataNotLoaded = true;

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
        View graphView = inflater.inflate(R.layout.fragment_graph, container, false);

        TextView stockSymbol = graphView.findViewById(R.id.graph_frag_stock_symbol);
        TextView dateFrom = graphView.findViewById(R.id.graph_frag_date_from);
        TextView dateTo = graphView.findViewById(R.id.graph_frag_date_to);
        mpLineChart = graphView.findViewById(R.id.graph_frag_line_graph);

        stockSymbol.setText(searchItem.getStock().getDisplaySymbol());
        dateFrom.setText(dateFormat.format(searchItem.getFrom()));
        dateTo.setText(dateFormat.format(searchItem.getTo()));

        if (dataNotLoaded) {
            searchItem.getStock().fetchData(
                    Resolution.types.get("D"),
                    searchItem.getFrom(), searchItem.getTo(),
                    (price_points, stock) -> {
                        if (price_points == null || price_points.size() == 0) {
                            return;
                        }

                        int analysisDays = searchItem.getAnalysisDays();

                        switch (analysisType) {
                            case EMA:
                                analysisPoints = searchItem.getStock().calculateEMA(analysisDays);
                                break;
                            case SMA:
                                analysisPoints = searchItem.getStock().calculateSMA(analysisDays);
                                break;
                            case MACD:
                                analysisPoints = searchItem.getStock().calculateMACD(9, 25, 2);
                            case MACDAVG:
                                analysisPoints = searchItem.getStock().calculateMACDAVG();
                        }

                        createChart(graphView, analysisPoints);
                    });

            dataNotLoaded = false;
        } else {
            createChart(graphView, analysisPoints);
        }

        return graphView;
    }

    private List<Entry> analysisToEntry(List<AnalysisPoint> analysisPoints) {
        List<Entry> entryList = new ArrayList<>();

        for (AnalysisPoint point : analysisPoints) {
            entryList.add(new Entry(point.getDateTime().getTime(), point.getValue().floatValue()));
            System.out.println(entryList.get(entryList.size() - 1).toString());
        }

        return entryList;
    }

    private void createChart(View parentView, List<AnalysisPoint> analysisPoints) {
        mpLineChart = parentView.findViewById(R.id.graph_frag_line_graph);
        LineDataSet lineDataset = new LineDataSet(analysisToEntry(analysisPoints), analysisType.name());
        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
        dataSets.add(lineDataset);

        XAxis xAxis = mpLineChart.getXAxis();
        xAxis.setGranularity(1f);
        xAxis.setValueFormatter(new StockPriceFormat());

        LineData data = new LineData(dataSets);
        mpLineChart.setData(data);
        mpLineChart.invalidate();
    }

    static class StockPriceFormat extends ValueFormatter {
        @Override
        public String getAxisLabel(float value, AxisBase axis) {
            return dateFormat.format(value);
        }
    }
}
