package com.example.Team8.tests;

import android.content.Context;
import android.os.Build;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import com.example.Team8.utils.API;
import com.example.Team8.utils.AnalysisPoint;
import com.example.Team8.utils.AnalysisType;
import com.example.Team8.utils.DataPoint;
import com.example.Team8.utils.DateTimeHelper;
import com.example.Team8.utils.http.HTTP_JSON;
import com.example.Team8.utils.PricePoint;
import com.example.Team8.utils.Resolution;
import com.example.Team8.utils.Stock;
import com.example.Team8.utils.StockCandle;
import com.example.Team8.utils.callbacks.StocksCallback;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class Test {

    private Context ctx;

    public Test(Context context) {
        ctx = context;
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
//        System.out.println(new DataPoint(new BigDecimal(0.0), new Date()));
//
//        AnalysisType a = AnalysisType.EMA;
//        System.out.println(a);
//
//        System.out.println(Resolution.types);
//        System.out.println(Resolution.types.get("15"));
//
//        FETCH_data_TEST(new HashMap<String, String>() {{
//            put("currency", "USD");
//            put("description", "APPLE INC");
//            put("displaySymbol", "AAPL");
//            put("figi", "");
//            put("mic", "");
//            put("symbol", "AAPL");
//            put("type", "Common Stock");
//        }});
//
//        FETCH_data_TEST(new HashMap<String, String>() {{
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
//        SMA_TEST(new HashMap<String, String>() {{
//            put("currency", "USD");
//            put("description", "APPLE INC");
//            put("displaySymbol", "AAPL");
//            put("figi", "");
//            put("mic", "");
//            put("symbol", "AAPL");
//            put("type", "Common Stock");
//        }});
//
//        EMA_TEST(new HashMap<String, String>() {{
//            put("currency", "USD");
//            put("description", "APPLE INC");
//            put("displaySymbol", "AAPL");
//            put("figi", "");
//            put("mic", "");
//            put("symbol", "AAPL");
//            put("type", "Common Stock");
//        }});
//
//        MACD_TEST(new HashMap<String, String>() {{
//            put("currency", "USD");
//            put("description", "APPLE INC");
//            put("displaySymbol", "AAPL");
//            put("figi", "");
//            put("mic", "");
//            put("symbol", "AAPL");
//            put("type", "Common Stock");
//        }});

        API.getInstance().search("apple", stocks -> {
            if (stocks == null) {
                return;
            }
            System.out.println(stocks.size());
        });

        return this;
    }

    public Test getStockSymbolsTEST() {
        Toast.makeText(ctx, "FETCHING STOCK SYMBOLS", Toast.LENGTH_SHORT).show();
        String getStockSymbolsURL = API.getInstance().getStockSymbolsURL();
        HTTP_JSON.fetch(getStockSymbolsURL,
                response -> {
                    if (response == null) {
                        return;
                    }
                    if (response.getType().equals("array")) {
                        for (Object o : response.getDataArray()) {
                            Stock s = new Stock((HashMap) o);
                        }
                        Stock.stocks.forEach(stockSymbol -> {
                            System.out.println(stockSymbol.getSymbol());
                        });
                        System.out.println(Stock.stocks.size());
                    }
                });
        return this;
    }

    public Test getAllStocks(StocksCallback callback) {
        HTTP_JSON.fetch(API.getInstance().getStockSymbolsURL(),
                response -> {
                    if (response == null) {
                        callback.response(new ArrayList<Stock>());
                        return;
                    }
                    if (response.getType().equals("array")) {
                        for (Object o : response.getDataArray()) {
                            Stock s = new Stock((HashMap) o);
                        }
                        callback.response(Stock.stocks);
                    } else {
                        callback.response(new ArrayList<Stock>());
                    }
                });
        return this;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public Test getStockCandlesTEST() {
        Toast.makeText(ctx, "FETCHING STOCK CANDLES", Toast.LENGTH_SHORT).show();
        API api = API.getInstance();
        String getStockCandlesURL = api.getStockCandlesURL(
                "AAPL",
                String.valueOf(15),
                DateTimeHelper.toDate(LocalDate.now().minusDays(100)),
                DateTimeHelper.toDate(LocalDate.now())
        );

        HTTP_JSON.fetch(getStockCandlesURL,
                response -> {
                    if (response.getType().equals("object")) {
                        HashMap data = response.getDataObj();
                        boolean status = api.isValidStatus((String) data.get("s"));
                        if (!status) {
                            System.out.println("NO DATA FOUND");
                            return;
                        }
                        StockCandle s_c = new StockCandle(data);
                        System.out.println(s_c.getO());
                        System.out.println(s_c.getH());
                        System.out.println(s_c.getL());
                        System.out.println(s_c.getC());
                        System.out.println(s_c.getV());
                        System.out.println(s_c.getT());
                        System.out.println(getStockCandlesURL);
                    }
                });
        return this;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public Test getPricePointTEST() {
        Toast.makeText(ctx, "FETCHING STOCK CANDLES", Toast.LENGTH_SHORT).show();
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
                        HashMap data = response.getDataObj();
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

    @RequiresApi(api = Build.VERSION_CODES.O)
    public Test getSearchTEST() {

//        SEARCH ENDPOINT KEEPS CHANGING RESULTS (NOT FIXED RESULTS), SOMETIMES AAPL DOESN'T SHOW UP IN RESULTS

        Toast.makeText(ctx, "SEARCH FOR STOCK", Toast.LENGTH_SHORT).show();
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
                        HashMap data = response.getDataObj();
                        int count = (int) data.get("count");
                        if (count > 0) {
                            Object[] results = (Object[]) data.get("result");
                            System.out.println(String.format("SEARCH COUNT >> %s %s", count, results.length));
                            for (Object o : results) {
                                HashMap r = (HashMap) o;
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
    public Test FETCH_data_TEST(HashMap stock_info) {
        Stock s = new Stock(stock_info);
        s.fetchData(
                Resolution.types.get("15"),
                DateTimeHelper.toDate(LocalDate.now().minusDays(5)),
                DateTimeHelper.toDate(LocalDate.now()),
                (priceHistory, stock) -> {
                    System.out.println(String.format("%s %s", s.getSymbol(), priceHistory == null ? null : "DATA!!"));
                    if (priceHistory == null) {
//                        API IS LIMITED IN FREE TIER
//                        System.out.println(String.format("API ERROR, DATA NOT FOUND FOR SYMBOL: %s", s.getSymbol()));
                        return;
                    }
                    priceHistory.forEach((pp) -> {
                        System.out.println(pp.getOpen());
                        System.out.println(pp.getHigh());
                        System.out.println(pp.getLow());
                        System.out.println(pp.getClose());
                        System.out.println(pp.getTimestamps());
                    });
                }
        );
        return this;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public Test SMA_TEST(HashMap stock_info) {
        Stock s = new Stock(stock_info);
        s.fetchData(
                Resolution.types.get("15"),
                DateTimeHelper.toDate(LocalDate.now().minusDays(5)),
                DateTimeHelper.toDate(LocalDate.now()),
                (priceHistory, stock) -> {
                    if (priceHistory == null) {
//                        API IS LIMITED IN FREE TIER
//                        System.out.println(String.format("API ERROR, DATA NOT FOUND FOR SYMBOL: %s", s.getSymbol()));
                        return;
                    }
                    stock.calculateSMA(15);
                }
        );
        return this;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public Test EMA_TEST(HashMap stock_info) {
        Stock s = new Stock(stock_info);
        s.fetchData(
                Resolution.types.get("15"),
                DateTimeHelper.toDate(LocalDate.now().minusDays(5)),
                DateTimeHelper.toDate(LocalDate.now()),
                (priceHistory, stock) -> {
                    if (priceHistory == null) {
//                        API IS LIMITED IN FREE TIER
//                        System.out.println(String.format("API ERROR, DATA NOT FOUND FOR SYMBOL: %s", s.getSymbol()));
                        return;
                    }
                    stock.calculateEMA(15);
                }
        );
        return this;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public Test MACD_TEST(HashMap stock_info) {
        Stock s = new Stock(stock_info);
        s.fetchData(
                Resolution.types.get("15"),
                DateTimeHelper.toDate(LocalDate.now().minusDays(5)),
                DateTimeHelper.toDate(LocalDate.now()),
                (priceHistory, stock) -> {
                    if (priceHistory == null) {
//                        API IS LIMITED IN FREE TIER
//                        System.out.println(String.format("API ERROR, DATA NOT FOUND FOR SYMBOL: %s", s.getSymbol()));
                        return;
                    }
                    stock.calculateMACD(26, 12, 9);
                }
        );
        return this;
    }
}
