package com.example.Team8.tests;

import android.os.Build;

import androidx.annotation.RequiresApi;

import com.example.Team8.utils.API;
import com.example.Team8.utils.DateTimeHelper;
import com.example.Team8.utils.PricePoint;
import com.example.Team8.utils.Resolution;
import com.example.Team8.utils.Stock;
import com.example.Team8.utils.callbacks.StocksCallback;
import com.example.Team8.utils.http.HTTP_JSON;

import java.io.UnsupportedEncodingException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

public class Test {

    public Test() {
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public Test Run() {
//        getStockSymbolsTEST();
//        getStockCandlesTEST();
//
//        getPricePointTEST();
//
//        System.out.println(DateTimeHelper.toDateTime("1614547709977"));
//        System.out.println(DateTimeHelper.toDateTime("1614109500"));
//
//        System.out.println(new DataPoint(new BigDecimal(1.0), DateTimeHelper.toDateTime("1614547709977")));
//        System.out.println(new AnalysisPoint(AnalysisType.EMA, new BigDecimal(0.0), new Date()));
//        System.out.println(new DataPoint(new BigDecimal("0.0"), new Date()));
//
//        AnalysisType a = AnalysisType.EMA;
//        System.out.println(a);
//
//        System.out.println(Resolution.types);
//        System.out.println(Resolution.types.get("15"));
//
//        FETCH_data_TEST(new HashMap<String, Object>() {{
//            put("currency", "USD");
//            put("description", "APPLE INC");
//            put("displaySymbol", "AAPL");
//            put("figi", "");
//            put("mic", "");
//            put("symbol", "AAPL");
//            put("type", "Common Stock");
//        }});
//
//        FETCH_data_TEST(new HashMap<String, Object>() {{
//            put("currency", "");
//            put("description", "");
//            put("displaySymbol", "");
//            put("figi", "");
//            put("mic", "");
//            put("symbol", "APC.DE");
//            put("type", "");
//        }});
//
//        getSearchTEST();
//
//        SMA_TEST(new HashMap<String, Object>() {{
//            put("currency", "USD");
//            put("description", "APPLE INC");
//            put("displaySymbol", "AAPL");
//            put("figi", "");
//            put("mic", "");
//            put("symbol", "AAPL");
//            put("type", "Common Stock");
//        }});
//
//        EMA_TEST(new HashMap<String, Object>() {{
//            put("currency", "USD");
//            put("description", "APPLE INC");
//            put("displaySymbol", "AAPL");
//            put("figi", "");
//            put("mic", "");
//            put("symbol", "AAPL");
//            put("type", "Common Stock");
//        }});
//
//        MACD_TEST(new HashMap<String, Object>() {{
//            put("currency", "USD");
//            put("description", "APPLE INC");
//            put("displaySymbol", "AAPL");
//            put("figi", "");
//            put("mic", "");
//            put("symbol", "AAPL");
//            put("type", "Common Stock");
//        }});
//
//        MACDAVG_TEST(new HashMap<String, Object>() {{
//            put("currency", "USD");
//            put("description", "APPLE INC");
//            put("displaySymbol", "AAPL");
//            put("figi", "");
//            put("mic", "");
//            put("symbol", "AAPL");
//            put("type", "Common Stock");
//        }});
//
//        API.getInstance().search("apple", stocks -> {
//            if (stocks == null) {
//                return;
//            }
//            System.out.println(stocks.size());
//        });

        return this;
    }

    @SuppressWarnings("unchecked")
    public Test getStockSymbolsTEST() {
//        Toast.makeText(ctx, "FETCHING STOCK SYMBOLS", Toast.LENGTH_SHORT).show();
        String getStockSymbolsURL = API.getInstance().getStockSymbolsURL();
        HTTP_JSON.fetch(getStockSymbolsURL,
                response -> {
                    if (response == null) {
                        return;
                    }
                    if (response.getType().equals("array")) {
                        for (Object o : response.getDataArray()) {
                            Stock s = new Stock((HashMap<String, String>) o);
                        }
                        Stock.stocks.forEach(stockSymbol -> System.out.println(stockSymbol.getSymbol()));
                        System.out.println(Stock.stocks.size());
                    }
                });
        return this;
    }

    @SuppressWarnings("unchecked")
    public Test getAllStocks(StocksCallback callback) {
        HTTP_JSON.fetch(API.getInstance().getStockSymbolsURL(),
                response -> {
                    if (response == null) {
                        callback.response(new ArrayList<>());
                        return;
                    }
                    if (response.getType().equals("array")) {
                        for (Object o : response.getDataArray()) {
                            Stock s = new Stock((HashMap<String, String>) o);
                        }
                        callback.response(Stock.stocks);
                    } else {
                        callback.response(new ArrayList<>());
                    }
                });
        return this;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public Test getPricePointTEST() {
//        Toast.makeText(ctx, "FETCHING STOCK CANDLES", Toast.LENGTH_SHORT).show();
        API api = API.getInstance();
        String getStockCandlesURL = api.getStockCandlesURL(
                "AAPL",
                String.valueOf(15),
                DateTimeHelper.toDate(LocalDate.now().minusDays(5)),
                DateTimeHelper.toDate(LocalDate.now())
        );

        HTTP_JSON.fetch(getStockCandlesURL,
                response -> {
                    if (response.getType().equals("object")) {
                        HashMap<String, Object> data = response.getDataObj();
                        boolean status = api.isValidStatus((String) data.get("s"));
                        if (!status) {
                            System.out.println("NO DATA FOUND");
                            return;
                        }
                        PricePoint s_c = new PricePoint(data);
                        System.out.println(s_c.getOpen());
                        System.out.println(s_c.getHigh());
                        System.out.println(s_c.getLow());
                        System.out.println(s_c.getClose());
                        System.out.println(s_c.getTimestamps());
                        System.out.println(getStockCandlesURL);
                    }
                });
        return this;
    }

    @SuppressWarnings("unchecked")
    @RequiresApi(api = Build.VERSION_CODES.O)
    public Test getSearchTEST() {

//        SEARCH ENDPOINT KEEPS CHANGING RESULTS (NOT FIXED RESULTS), SOMETIMES AAPL DOESN'T SHOW UP IN RESULTS

//        Toast.makeText(ctx, "SEARCH FOR STOCK", Toast.LENGTH_SHORT).show();
        String getSearchURL;
        try {
            getSearchURL = API.getInstance().getSearchSymbolURL("apple");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return this;
        }

        HTTP_JSON.fetch(getSearchURL,
                response -> {
                    if (response == null) {
                        return;
                    }
                    if (response.getType().equals("object")) {
                        HashMap<String, Object> data = response.getDataObj();
                        int count = 0;
                        try {
                            count = (int) data.get("count");
                        } catch (Exception ignored) {

                        }
                        if (count > 0) {
                            Object[] results = (Object[]) data.get("result");
                            System.out.println(String.format("SEARCH COUNT >> %s %s", count, results != null ? results.length : 0));
                            for (Object o : results) {
                                HashMap<String, String> r = (HashMap<String, String>) o;
//                                System.out.println(new Stock(r));
                                FETCH_data_TEST(r);

                            }
                        } else {
                            System.out.println("NO RESULTS");
                        }
                        System.out.println(getSearchURL);
                    }
                });
        return this;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void FETCH_data_TEST(HashMap<String, String> stock_info) {
        Stock s = new Stock(stock_info);
        Calendar cal1 = (Calendar) Calendar.getInstance().clone();
        Calendar cal2 = (Calendar) Calendar.getInstance().clone();
        cal1.setTime(new Date(System.currentTimeMillis()));
        cal2.setTime(new Date(System.currentTimeMillis()));
        cal1.add(Calendar.DAY_OF_MONTH, -5);
        s.fetchData(
                Resolution.types.get("15"),
                cal1, cal2, 15,
                (priceHistory, stock) -> {
                    System.out.println(String.format("%s %s", s.getSymbol(), priceHistory == null ? null : "DATA!!"));
                    if (priceHistory == null) {
//                        API IS LIMITED IN FREE TIER
//                        System.out.println(String.format("API ERROR, DATA NOT FOUND FOR SYMBOL: %s", s.getSymbol()));
                        return;
                    }

                    System.out.println(priceHistory.getOpen());
                    System.out.println(priceHistory.getHigh());
                    System.out.println(priceHistory.getLow());
                    System.out.println(priceHistory.getClose());
                    System.out.println(priceHistory.getTimestamps());
                });
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public Test SMA_TEST(HashMap<String, String> stock_info) {
        Stock s = new Stock(stock_info);
        Calendar cal1 = (Calendar) Calendar.getInstance().clone();
        Calendar cal2 = (Calendar) Calendar.getInstance().clone();
        cal1.setTime(new Date(System.currentTimeMillis()));
        cal2.setTime(new Date(System.currentTimeMillis()));
        cal1.add(Calendar.DAY_OF_MONTH, -5);
        s.fetchData(
                Resolution.types.get("15"),
                cal1, cal2, 15,
                (price_points, stock) -> {
                    if (PricePointsNotFound(price_points)) return;
                    printClosePrices(price_points);
                    System.out.println(validateSMA(price_points.getClose().size() - 1, price_points.getClose().size()));
                    System.out.println(stock.calculateSMA(1).size());
                }
        );
        return this;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public Test EMA_TEST(HashMap<String, String> stock_info) {
        Stock s = new Stock(stock_info);
        Calendar cal1 = (Calendar) Calendar.getInstance().clone();
        Calendar cal2 = (Calendar) Calendar.getInstance().clone();
        cal1.setTime(new Date(System.currentTimeMillis()));
        cal2.setTime(new Date(System.currentTimeMillis()));
        cal1.add(Calendar.DAY_OF_MONTH, -5);
        s.fetchData(
                Resolution.types.get("15"),
                cal1, cal2, 15,
                (price_points, stock) -> {
                    if (PricePointsNotFound(price_points)) return;
                    printClosePrices(price_points);
                    System.out.println(stock.calculateEMA(17).size());
                }
        );
        return this;
    }

    private boolean validateMACD(Date date_1, Date date_2) {
        return DateTimeHelper.dateDiff(date_1, date_2) >= 38;
    }

    private boolean validateSMA(int nDays, int prices_length) {
        return nDays > 0 && nDays <= prices_length;
    }

    private boolean validateEMA(int nDays, int prices_length) {
        return nDays > 0 && nDays <= prices_length - 1;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public Test MACD_TEST(HashMap<String, String> stock_info) {
        boolean valid = validateMACD(DateTimeHelper.toDate(LocalDate.now().minusDays(38)), DateTimeHelper.toDate(LocalDate.now()));
        assert (valid);

        Stock s = new Stock(stock_info);
        Calendar cal1 = (Calendar) Calendar.getInstance().clone();
        Calendar cal2 = (Calendar) Calendar.getInstance().clone();
        cal1.setTime(new Date(System.currentTimeMillis()));
        cal2.setTime(new Date(System.currentTimeMillis()));
        cal1.add(Calendar.DAY_OF_MONTH, -5);
        s.fetchData(
                Resolution.types.get("15"),
                cal1, cal2, 15,
                (price_points, stock) -> {
                    if (PricePointsNotFound(price_points)) return;
                    printClosePrices(price_points);
                    System.out.println(stock.calculateMACD().size());
                }
        );
        return this;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public Test MACDAVG_TEST(HashMap<String, String> stock_info) {
        Stock s = new Stock(stock_info);
        Calendar cal1 = (Calendar) Calendar.getInstance().clone();
        Calendar cal2 = (Calendar) Calendar.getInstance().clone();
        cal1.setTime(new Date(System.currentTimeMillis()));
        cal2.setTime(new Date(System.currentTimeMillis()));
        cal1.add(Calendar.DAY_OF_MONTH, -5);
        s.fetchData(
                Resolution.types.get("15"),
                cal1, cal2, 15,
                (price_points, stock) -> {
                    if (PricePointsNotFound(price_points)) return;
                    printClosePrices(price_points);
                    System.out.println(stock.calculateMACDAVG().size());
                }
        );
        return this;
    }

    private boolean PricePointsNotFound(PricePoint price_points) {
//        API IS LIMITED IN FREE TIER
//        System.out.println(String.format("API ERROR, DATA NOT FOUND FOR SYMBOL");
        return price_points == null || price_points.getClose().isEmpty();
    }

    private void printClosePrices(PricePoint price_points) {
        System.out.print("Stock prices: ");
        System.out.println(price_points.getClose());
        System.out.println(price_points.getClose().size());
    }
}
