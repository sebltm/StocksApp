package com.example.Team8.utils;

import androidx.annotation.NonNull;

import com.example.Team8.StockCalc.ExponentialMovingAverage;
import com.example.Team8.StockCalc.MovingAverageConvergenceDivergence;
import com.example.Team8.StockCalc.SimpleMovingAverage;
import com.example.Team8.utils.callbacks.StockDataCallback;
import com.example.Team8.utils.http.HTTP_JSON;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import static com.example.Team8.utils.ArrayUtils.doubleArr;

public class Stock implements Serializable {

    public static ArrayList<Stock> stocks = new ArrayList<>();
    private final String currency;
    private final String description;
    private final String displaySymbol;
    private final String figi;
    private final String mic;
    private final String symbol;
    private final String type;
    private PricePoint priceHistory;


    public Stock(HashMap<String, ?> data) {
        currency = (String) data.get("currency");
        description = (String) data.get("description");
        displaySymbol = (String) data.get("displaySymbol");
        figi = (String) data.get("figi");
        mic = (String) data.get("mic");
        symbol = (String) data.get("symbol");
        type = (String) data.get("type");
        stocks.add(this);
    }

    public String getCurrency() {
        return currency;
    }

    public String getDescription() {
        return description;
    }

    public String getDisplaySymbol() {
        return displaySymbol;
    }

    public String getFigi() {
        return figi;
    }

    public String getMic() {
        return mic;
    }

    public String getSymbol() {
        return symbol;
    }

    public String getType() {
        return type;
    }

    @NonNull
    @Override
    public String toString() {
        return "Stock{" +
                "currency='" + currency + '\'' +
                ", description='" + description + '\'' +
                ", displaySymbol='" + displaySymbol + '\'' +
                ", figi='" + figi + '\'' +
                ", mic='" + mic + '\'' +
                ", symbol='" + symbol + '\'' +
                ", type='" + type + '\'' +
                '}';
    }

    private double[] get_double_prices(List<DataPoint> prices) {
        return doubleArr(prices.stream().map((data_point) -> data_point.value.doubleValue()).toArray());
    }

    private List<AnalysisPoint> generateAnalysisPoints(PricePoint pricePoint, List<DataPoint> prices, AnalysisType a_type, double[] a_values) {
        List<String> timestamps_arr = pricePoint.getTimestamps();

        //TODO can't have that here, move it to a UnitTest
        boolean debug = false;

        return new ArrayList<AnalysisPoint>() {{
            for (int i = 0; i < a_values.length; i++) {
                AnalysisPoint a_point = new AnalysisPoint(
                        a_type,
                        new BigDecimal(String.valueOf(a_values[i])),
                        DateTimeHelper.toDateTime(timestamps_arr.get(i))
                );

                //TODO related to point above
                if (debug) printAnalysis(i, prices, a_type, a_point);
                add(a_point);
            }
        }};
    }

    private void printAnalysis(int index, List<DataPoint> prices, AnalysisType a_type, AnalysisPoint analysisPoint) {
        System.out.println(String.format(
                "%1$s PRICE -> %2$s \n%3$s -> %4$s\n",
                "close".toUpperCase(),
                prices.get(index),
                a_type,
                analysisPoint
        ));
    }

    private MovingAverageConvergenceDivergence getMACDCalc(double[] prices, int fastPeriod, int slowPeriod, int signalPeriod) {
        try {
            return new MovingAverageConvergenceDivergence().calculate(
                    prices, fastPeriod, slowPeriod, signalPeriod
            );
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private MovingAverageConvergenceDivergence getDefaultMACDCalc(double[] prices) {
        return getMACDCalc(prices, 12, 26, 9);
    }

    public List<AnalysisPoint> calculateSMA(int nDays) {
        if (priceHistory == null) return new ArrayList<>();

        List<DataPoint> close_prices = priceHistory.getClose();

        try {
            double[] close_sma = new SimpleMovingAverage().calculate(get_double_prices(close_prices), nDays).getSMA();

            return generateAnalysisPoints(priceHistory, close_prices, AnalysisType.SMA, close_sma);
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public List<AnalysisPoint> calculateEMA(int nDays) {
        if (priceHistory == null) return new ArrayList<>();

        List<DataPoint> close_prices = priceHistory.getClose();

        try {
            double[] close_ema = new ExponentialMovingAverage().calculate(get_double_prices(close_prices), nDays).getEMA();

            return generateAnalysisPoints(priceHistory, close_prices, AnalysisType.EMA, close_ema);
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public List<AnalysisPoint> calculateMACD(int fastPeriod, int slowPeriod, int signalPeriod) {
        if (priceHistory == null) return new ArrayList<>();

        List<DataPoint> close_prices = priceHistory.getClose();

        try {
            MovingAverageConvergenceDivergence macd_calc = getMACDCalc(
                    get_double_prices(close_prices), fastPeriod, slowPeriod, signalPeriod
            );

            //TODO need a null check here
            double[] close_macd = macd_calc.getMACD();

            return generateAnalysisPoints(priceHistory, close_prices, AnalysisType.MACD, close_macd);
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public List<AnalysisPoint> calculateMACDAVG() {
        if (priceHistory == null) return new ArrayList<>();

        List<DataPoint> close_prices = priceHistory.getClose();

        try {
            MovingAverageConvergenceDivergence macd_calc = getDefaultMACDCalc(get_double_prices(close_prices));

            //SIGNAL IS MACDAVG
            double[] close_macd_signal = macd_calc.getSignal();

            //MACD DIFF = MACD - MACDAVG
            double[] close_macd_diff = macd_calc.getDiff();

            //MACD CROSSOVER
            int[] close_macd_crossover = macd_calc.getCrossover();

            return generateAnalysisPoints(priceHistory, close_prices, AnalysisType.MACDAVG, close_macd_signal);
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    private void setResponseCallback(StockDataCallback callback, PricePoint value) {
        if (callback != null) {
            callback.response(value, this);
        }
    }

    public void fetchData(String resolution, Date from, Date to, StockDataCallback callback) {
        API api = API.getInstance();
        String getStockCandlesURL;
        try {
            getStockCandlesURL = api.getStockCandlesURL(symbol, Resolution.types.get(resolution), from, to);
        } catch (Exception e) {
            setResponseCallback(callback, null);
            throw new NoSuchFieldError();
        }

        HTTP_JSON.fetch(getStockCandlesURL,
                response -> {
                    if (response == null) {
                        setResponseCallback(callback, null);
                        return;
                    }
                    if (response.getType().equals("object")) {

                        //TODO need to parametrize HashMap
                        HashMap data = response.getDataObj();
                        boolean status = api.isValidStatus((String) data.get("s"));
                        if (!status) {
                            setResponseCallback(callback, null);
                            return;
                        }
                        priceHistory = new PricePoint(data);
                        setResponseCallback(callback, priceHistory);
                    } else {
                        setResponseCallback(callback, null);
                    }
                });
    }

    public PricePoint getPriceHistory() {
        return priceHistory;
    }
}
