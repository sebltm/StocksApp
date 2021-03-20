package com.example.Team8.utils;

import android.content.Context;
import android.text.Html;
import android.text.Spanned;

import androidx.annotation.NonNull;
import androidx.arch.core.util.Function;

import com.example.Team8.StockCalc.ExponentialMovingAverage;
import com.example.Team8.StockCalc.MovingAverageConvergenceDivergence;
import com.example.Team8.StockCalc.SimpleMovingAverage;
import com.example.Team8.utils.callbacks.StockDataCallback;
import com.example.Team8.utils.http.HTTP_JSON;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import static com.example.Team8.utils.ArrayUtils.doubleArr;

public class Stock implements Serializable {

    public static final ArrayList<Stock> stocks = new ArrayList<>();
    private final String currency;
    private final String description;
    private final String displaySymbol;
    private final String figi;
    private final String mic;
    private final String symbol;
    private final String type;
    private PricePoint priceHistory;

    public Stock(HashMap<String, String> data) {
        currency = data.get("currency");
        description = data.get("description");
        displaySymbol = data.get("displaySymbol");
        figi = data.get("figi");
        mic = data.get("mic");
        symbol = data.get("symbol");
        type = data.get("type");
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
        return doubleArr(prices.stream().map((data_point) -> data_point.getValue().doubleValue()).toArray());
    }

    private List<AnalysisPoint> generateAnalysisPoints(PricePoint pricePoint, List<DataPoint> prices, AnalysisType a_type, double[] a_values) {
        List<String> timestamps_arr = pricePoint.getTimestamps();
        return new ArrayList<AnalysisPoint>() {{
            for (int i = 0; i < a_values.length; i++) {
                AnalysisPoint a_point = new AnalysisPoint(
                        a_type,
                        new BigDecimal(String.valueOf(a_values[i])),
                        DateTimeHelper.toDateTime(timestamps_arr.get(i))
                );
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

            if (macd_calc == null) return new ArrayList<>();

            double[] close_macd = macd_calc.getMACD();

            return generateAnalysisPoints(priceHistory, close_prices, AnalysisType.MACD, close_macd);
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public List<AnalysisPoint> calculateMACD() {
        return calculateMACD(12, 26, 9);
    }

    public List<AnalysisPoint> calculateMACDAVG() {
        if (priceHistory == null) return new ArrayList<>();

        List<DataPoint> close_prices = priceHistory.getClose();

        try {
            MovingAverageConvergenceDivergence macd_calc = getDefaultMACDCalc(get_double_prices(close_prices));

            if (macd_calc == null) return new ArrayList<>();

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

    public List<AnalysisPoint> calculateSelectedIndicators(AnalysisType analysisType, Date fromDateTime, Date toDateTime, int nDays, Consumer<List<String>> onErrorCallback) {
        List<AnalysisPoint> a_points = new ArrayList<>();
        List<String> errors = new ArrayList<>();

        if (priceHistory == null) {
            onErrorCallback.accept(errors);
            return a_points;
        }

        List<DataPoint> stockPrices = priceHistory.getClose();
        int stockPricesCount = stockPrices.size();

        String macd_error_msg = String.format("The difference between the from and to dates must be greater for the %s analysis", analysisType);
        String sma_ema_error_msg_1 = String.format("Days should be between 1 and %s for this %s analysis", analysisType == AnalysisType.EMA ? stockPricesCount - 1 : stockPricesCount, analysisType);

        switch (analysisType) {
            case SMA:
                if (!validateSMA(nDays, stockPricesCount)) {
                    String error_msg = stockPricesCount == 1 && nDays != 1 ? String.format("Days should be set to 1 for this %s analysis", analysisType) : sma_ema_error_msg_1;
                    errors.add(error_msg);
                    break;
                }
                a_points = calculateSMA(nDays);
                break;
            case EMA:
                if (!validateEMA(nDays, stockPricesCount)) {
                    String error_msg_2 = stockPricesCount - 1 == 1 && nDays != 1 ? String.format("Days should be set to 1 for this %s analysis", analysisType) : sma_ema_error_msg_1;
                    String error_msg = stockPricesCount < 2 ? String.format("The difference between the current dates needs to be greater for the %s analysis", analysisType) : error_msg_2;
                    errors.add(error_msg);
                    break;
                }
                a_points = calculateEMA(nDays);
                break;
            case MACD:
                if (!validateMACD(fromDateTime, toDateTime, stockPricesCount)) {
                    errors.add(macd_error_msg);
                    break;
                }
                a_points = calculateMACD();
                break;
            case MACDAVG:
                if (!validateMACD(fromDateTime, toDateTime, stockPricesCount)) {
                    errors.add(macd_error_msg);
                    break;
                }
                a_points = calculateMACDAVG();
                break;
        }
        onErrorCallback.accept(errors);
        return a_points;
    }

    private boolean validateMACD(Date date_1, Date date_2, int prices_length) {
        return DateTimeHelper.dateDiff(date_1, date_2) > 0 && prices_length >= 27;
    }

    private boolean validateSMA(int nDays, int prices_length) {
        return nDays > 0 && nDays <= prices_length;
    }

    private boolean validateEMA(int nDays, int prices_length) {
        return nDays > 0 && nDays <= prices_length - 1;
    }

    private void setResponseCallback(StockDataCallback callback, PricePoint value) {
        if (callback != null) {
            callback.response(value, this);
        }
    }

    /**
     * Compensate for analysis latent days
     *
     * @param resolution: String
     * @param from:       Date
     * @param to:         Date
     * @param numDays:    int
     * @param callback:   StockDataCallback
     */
    public void fetchData(String resolution, Calendar from, Calendar to, int numDays, StockDataCallback callback) {
        Calendar extraDays = DateTimeHelper.addBusinessDays(from, -numDays);
        Date dateFrom = extraDays.getTime();
        Date dateTo = to.getTime();
        fetchData(resolution, dateFrom, dateTo, callback);
    }

    /**
     * @param resolution: String
     * @param from:       Date
     * @param to:         Date
     * @param callback:   StockDataCallback
     */
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
                        HashMap<String, Object> data = response.getDataObj();
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

    public void setPriceHistory(PricePoint priceHistory) {
        this.priceHistory = priceHistory;
    }

    public Spanned getSummaryHTML(Date from, Date to) {
        Function<String, Spanned> toHTML = (text) -> Html.fromHtml(text, Html.FROM_HTML_MODE_COMPACT);
        return toHTML.apply(getSummary(from, to));
    }

    public String getSummary(Date from, Date to) {
        List<BigDecimal> close_prices = getClosePricesValues();

        double max = getMaxPrice(close_prices).doubleValue();
        double min = getMinPrice(close_prices).doubleValue();
        double diff_max_min = max - min;
        double sum_max_min =  max + min;
        double diff_percent  = sum_max_min > 0? (diff_max_min / (sum_max_min / 2)) * 100 : 0;

        String summary = String.format(
                "For stock <b>%1$s</b> (%2$s) from %3$s to %4$s. " +
                        "The difference in the stock value was <b>%5$s%%</b>. " +
                        "The highest value was <b>%6$s USD</b>, while the lowest value was <b>%7$s USD</b>.",
                getSymbol(),
                getDescription(),
                DateTimeHelper.getDateStringOnly(from),
                DateTimeHelper.getDateStringOnly(to),
                new DecimalFormat("#.##").format(diff_percent),
                max,
                min
        );

        return summary;
    }

    public void showSummary(Context context, Date from, Date to){
        new MaterialAlertDialogBuilder(context)
                .setTitle("Summary")
                .setMessage(getSummaryHTML(from, to))
                .show();
    }

    private List<BigDecimal> getClosePricesValues() {
        if(priceHistory == null) return new ArrayList<>();
        return priceHistory.getClose()
                .stream()
                .map(DataPoint::getValue).collect(Collectors.toList());
    }

    private BigDecimal getMaxPrice(List<BigDecimal> prices) {
        return prices
                .stream()
                .max(Comparator.comparing(BigDecimal::doubleValue))
                .orElseGet(() -> new BigDecimal("0.0"));
    }

    private BigDecimal getMinPrice(List<BigDecimal> prices) {
        return prices
                .stream()
                .min(Comparator.comparing(BigDecimal::doubleValue))
                .orElseGet(() -> new BigDecimal("0.0"));
    }
}
