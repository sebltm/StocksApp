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

    private static final String format = "dd MMM yyyy";
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat(format, Locale.UK);
    LineChart mpLineChart;
    SearchHistoryItem searchItem;
    AnalysisType analysisType;
    List<AnalysisPoint> analysisPoints;

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

        switch (analysisType) {
            case EMA:
                analysisPoints = searchItem.getStock().calculateEMA(15);
                break;
            case SMA:
                analysisPoints = searchItem.getStock().calculateSMA(15);
                break;
            case MACD:
                analysisPoints = searchItem.getStock().calculateMACD(9, 26, 2);
            case MACDAVG:
                analysisPoints = searchItem.getStock().calculateMACDAVG();
        }

        createChart(graphView, analysisPoints);

        return graphView;
    }

    private List<Entry> analysisToEntry(List<AnalysisPoint> analysisPoints) {
        List<Entry> entryList = new ArrayList<>();
        for (AnalysisPoint point : analysisPoints) {
            entryList.add(new Entry(point.getDateTime().getTime(), point.getValue().floatValue()));
        }

        return entryList;
    }

    private void createChart(View parentView, List<AnalysisPoint> analysisPoints) {
        mpLineChart = (LineChart) parentView.findViewById(R.id.graph_frag_line_graph);
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
