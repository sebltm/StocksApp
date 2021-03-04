package com.example.Team8.utils;

import com.example.Team8.StockCalc.ExponentialMovingAverage;
import com.example.Team8.StockCalc.MovingAverageConvergenceDivergence;
import com.example.Team8.StockCalc.SimpleMovingAverage;
import com.example.Team8.utils.callbacks.StockDataCallback;
import com.example.Team8.utils.http.HTTP_JSON;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import static com.example.Team8.utils.ArrayUtils.doubleArr;

public class Stock {

    public static ArrayList<Stock> stocks = new ArrayList<Stock>();
    private final String currency;
    private final String description;
    private final String displaySymbol;
    private final String figi;
    private final String mic;
    private final String symbol;
    private final String type;
    private final List<PricePoint> priceHistory = new ArrayList<>();


    public Stock(HashMap data) {
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

    public List<AnalysisPoint> calculateSMA(int nDays) {
        AnalysisType a_type = AnalysisType.SMA;
        PricePoint p = priceHistory.get(priceHistory.size() - 1);
        List<AnalysisPoint> a_points = new ArrayList<AnalysisPoint>();
        List<String> timestamps_arr = p.getTimestamps();

        try {
            double[] close_sma = new SimpleMovingAverage().calculate(get_double_prices(p.getClose()), nDays).getSMA();

            a_points = new ArrayList<AnalysisPoint>() {{
                for (int i = 0; i < close_sma.length; i++) {
                    add(new AnalysisPoint(
                            a_type,
                            new BigDecimal(String.valueOf(close_sma[i])),
                            DateTimeHelper.toDateTime(timestamps_arr.get(i))
                    ));
                }
            }};
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(a_points);
        return a_points;
    }

    public List<AnalysisPoint> calculateEMA(int nDays) {
        AnalysisType a_type = AnalysisType.EMA;
        PricePoint p = priceHistory.get(priceHistory.size() - 1);
        List<AnalysisPoint> a_points = new ArrayList<AnalysisPoint>();
        List<String> timestamps_arr = p.getTimestamps();

        try {
            double[] close_ema = new ExponentialMovingAverage().calculate(get_double_prices(p.getClose()), nDays).getEMA();

            a_points = new ArrayList<AnalysisPoint>() {{
                for (int i = 0; i < close_ema.length; i++) {
                    add(new AnalysisPoint(
                            a_type,
                            new BigDecimal(String.valueOf(close_ema[i])),
                            DateTimeHelper.toDateTime(timestamps_arr.get(i))
                    ));
                }
            }};
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(a_points);
        return a_points;
    }

    public List<AnalysisPoint> calculateMACD(int fastPeriod, int slowPeriod, int signalPeriod) {
        AnalysisType a_type = AnalysisType.MACD;
        PricePoint p = priceHistory.get(priceHistory.size() - 1);
        List<AnalysisPoint> a_points = new ArrayList<AnalysisPoint>();
        List<String> timestamps_arr = p.getTimestamps();

        try {
            MovingAverageConvergenceDivergence macd_calc = new MovingAverageConvergenceDivergence().calculate(
                    get_double_prices(p.getClose()), fastPeriod, slowPeriod, signalPeriod
            );

            double[] close_macd = macd_calc.getMACD();
            double[] close_macd_diff = macd_calc.getDiff();
            double[] close_macd_signal = macd_calc.getSignal();
            int[] close_macd_crossover = macd_calc.getCrossover();

            a_points = new ArrayList<AnalysisPoint>() {{
                for (int i = 0; i < close_macd.length; i++) {
                    add(new AnalysisPoint(
                            a_type,
                            new BigDecimal(String.valueOf(close_macd[i])),
                            DateTimeHelper.toDateTime(timestamps_arr.get(i))
                    ));
                }
            }};
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(a_points);
        return a_points;
    }

    public List<AnalysisPoint> calculateMACDAVG() {
        return new ArrayList<>();
    }

    private void setResponseCallback(StockDataCallback callback, List<PricePoint> value) {
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

//        System.out.println(String.format("FETCH DATA FOR %2$s: %1$s", getStockCandlesURL, symbol));

        HTTP_JSON.fetch(getStockCandlesURL,
                response -> {
                    if (response == null) {
                        setResponseCallback(callback, null);
                        return;
                    }
                    if (response.getType().equals("object")) {
                        HashMap data = response.getDataObj();
                        boolean status = api.isValidStatus((String) data.get("s"));
                        if (!status) {
//                            System.out.println("NO DATA FOUND");
                            setResponseCallback(callback, null);
                            return;
                        }
                        PricePoint pp = new PricePoint(data);
                        priceHistory.add(pp);
                        setResponseCallback(callback, priceHistory);
                    } else {
                        setResponseCallback(callback, null);
                    }
                });
    }
}
