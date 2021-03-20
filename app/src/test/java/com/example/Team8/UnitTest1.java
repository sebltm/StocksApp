package com.example.Team8;

import android.os.Build;

import androidx.annotation.RequiresApi;

import com.example.Team8.utils.API;
import com.example.Team8.utils.AnalysisPoint;
import com.example.Team8.utils.AnalysisType;
import com.example.Team8.utils.DataPoint;
import com.example.Team8.utils.DateTimeHelper;
import com.example.Team8.utils.JSON;
import com.example.Team8.utils.PricePoint;
import com.example.Team8.utils.Resolution;
import com.example.Team8.utils.Stock;
import com.example.Team8.utils.callbacks.HTTPCallback;
import com.example.Team8.utils.callbacks.StockDataCallback;
import com.example.Team8.utils.callbacks.StocksCallback;
import com.example.Team8.utils.http.HTTP_JSON;

import org.junit.Test;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import kotlin.jvm.functions.Function2;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class UnitTest1 {

    public UnitTest1() {
    }

    private void executeTest(Consumer<Runnable> test) {
        CountDownLatch signal = new CountDownLatch(1);
        test.accept(signal::countDown);
        try {
            signal.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void catchAssertionError(Runnable runnable, Runnable done) {
        try {
            runnable.run();
        } catch (AssertionError e) {
            e.printStackTrace();
            done.run();
        }
    }

    private void fetchAndCatchAssertionError(String url, Runnable done, HTTPCallback<JSON> callback) {
        HTTP_JSON.fetch(url, response -> catchAssertionError(() -> callback.response(response), done));
    }

    private void apiSearchAndCatchAssertionError(String query, Runnable done, StocksCallback callback) {
        API.getInstance().search(query, response -> catchAssertionError(() -> callback.response(response), done));
    }

    public void FETCH_DATA(HashMap<String, String> stock_info, Date fromDate, Date toDate, StockDataCallback callback) {
        new Stock(stock_info).fetchData(Resolution.types.get("D"), fromDate, toDate, callback);
    }

    private void FETCH_DATA_WITH_ASSERTION_ERROR_CATCH(HashMap<String, String> stock_info, Date fromDate, Date toDate, Runnable done, StockDataCallback callback) {
        FETCH_DATA(
                stock_info,
                fromDate, toDate,
                (price_points, stock) -> catchAssertionError(() -> callback.response(price_points, stock), done)
        );
    }

    @Test
    public void StockObjTest() {
        HashMap<String, String> hm_str_str = new HashMap<String, String>() {{
            put("currency", "");
            put("description", "");
            put("displaySymbol", "");
            put("figi", "");
            put("mic", "");
            put("symbol", "APC.DE");
            put("type", "");
        }};

        Stock s1 = new Stock(hm_str_str);

        assert (s1 instanceof Stock);

        Stock s2 = new Stock(hm_str_str);

        assert (s2 instanceof Stock);

        Object o = hm_str_str;

        Stock s3 = new Stock((HashMap<String, String>) o);

        assert (s3 instanceof Stock);

        Stock s4 = new Stock((HashMap<String, String>) o);

        assert (s4 instanceof Stock);

        System.out.println("ALL STOCK OBJECTS VALID");
    }

    @Test
    public void JSON_TEST() {
        JSON json = new JSON("{}");
        assertNotNull(json.getType());
        assertEquals("object", json.getType());
        assertTrue(json.getDataObj() instanceof HashMap);
        assertNull(json.getDataArray());
        assertEquals(0, json.getDataObj().keySet().size());
        System.out.println(json.getStr_data());

        json = new JSON("[]");
        assertNotNull(json.getType());
        assertEquals("array", json.getType());
        assertTrue(json.getDataArray() instanceof Object[]);
        assertNull(json.getDataObj());
        assertEquals(0, json.getDataArray().length);
        System.out.println(json.getStr_data());

        json = new JSON("[1,2,3,4,5]");
        assertNotNull(json.getType());
        assertEquals("array", json.getType());
        assertTrue(json.getDataArray() instanceof Object[]);
        assertNull(json.getDataObj());
        assertEquals(5, json.getDataArray().length);
        System.out.println(json.getStr_data());

        json = new JSON(null);
        assertNull(json.getType());
        assertNull(json.getStr_data());
        assertNull(json.getDataArray());
        assertNull(json.getDataObj());
        System.out.println(json.getStr_data());

        json = new JSON("{\"a\":1, \"b\":2, \"c\":3, \"d\":4, \"e\":5}");
        assertNotNull(json.getType());
        assertEquals("object", json.getType());
        assertTrue(json.getDataObj() instanceof HashMap);
        assertNull(json.getDataArray());
        assertEquals(5, json.getDataObj().keySet().size());
        assertEquals(5, json.getDataObj().values().size());
        assertEquals(new HashMap<String, Object>() {{
            put("a", 1);
            put("b", 2);
            put("c", 3);
            put("d", 4);
            put("e", 5);
        }}, json.getDataObj());
        System.out.println(json.getStr_data());

        json = new JSON("[1,{\"2\":{\"a\":1, \"b\":2, \"c\":3, \"d\":4, \"e\":5}},3,4,5]");
        assertNotNull(json.getType());
        assertEquals("array", json.getType());
        assertTrue(json.getDataArray() instanceof Object[]);
        assertNull(json.getDataObj());
        assertEquals(5, json.getDataArray().length);
        assertTrue(json.getDataArray()[1] instanceof HashMap);
        assertEquals(new HashMap<String, Object>() {{
            put("2", new HashMap<String, Object>() {{
                put("a", 1);
                put("b", 2);
                put("c", 3);
                put("d", 4);
                put("e", 5);
            }});
        }}, json.getDataArray()[1]);
        List<Object> arr = new ArrayList<>(Arrays.asList(json.getDataArray()));
        arr.remove(1);
        List<Integer> expected_ints = Arrays.asList(1,3,4,5);
        arr.forEach(o -> {
            try{
                assertTrue(expected_ints.contains(o));
            }catch (Exception e){
                throw new AssertionError(e.getMessage());
            }
        });
        System.out.println(json.getStr_data());

    }

    @Test
    public void API_SEARCH_TEST() {
        System.out.println("API SEARCH TEST");
        executeTest(done -> {
            apiSearchAndCatchAssertionError("apple", done, stocks -> {
                assertNotNull("SEARCH FOR APPLE IS NOT NULL", stocks);
                assertTrue("SEARCH FOR APPLE IS NON ZERO", stocks.size() > 0);
                System.out.println("SEARCH FOR APPLE FOUND");
                System.out.printf("SEARCH RESULTS SIZE: %s\n\n", stocks.size());
                done.run();
            });
        });

        executeTest(done -> {
            apiSearchAndCatchAssertionError("advvsdvs", done, stocks -> {
                assertNull("INVALID SEARCH IS NULL", stocks);
                System.out.println("NO SEARCH FOUND ON RANDOM SEARCH QUERY");
                done.run();
            });
        });
    }

    @Test
    public void typesTest() {
        assertEquals("Sat Dec 24 18:59:37 GMT 53132", DateTimeHelper.toDateTime("1614547709977").toString());
        System.out.println(DateTimeHelper.toDateTime("1614547709977"));

        assertEquals("Tue Feb 23 19:45:00 GMT 2021", DateTimeHelper.toDateTime("1614109500").toString());
        System.out.println(DateTimeHelper.toDateTime("1614109500"));

        assertEquals("[DATA_POINT] DateTime: \"Sat Dec 24 18:59:37 GMT 53132\" | Value: \"1\"",
                new DataPoint(new BigDecimal(1.0), DateTimeHelper.toDateTime("1614547709977")).toString());
        System.out.println(new DataPoint(new BigDecimal(1.0), DateTimeHelper.toDateTime("1614547709977")));

        assertEquals("[ANALYSIS_POINT] DateTime: \"" + new Date().toString() + "\" | Value: \"0\" | TYPE: EMA",
                new AnalysisPoint(AnalysisType.EMA, new BigDecimal(0.0), new Date()).toString());
        System.out.println(new AnalysisPoint(AnalysisType.EMA, new BigDecimal(0.0), new Date()));

        assertEquals("[DATA_POINT] DateTime: \"" + new Date().toString() + "\" | Value: \"0.0\"",
                new DataPoint(new BigDecimal("0.0"), new Date()).toString());
        System.out.println(new DataPoint(new BigDecimal("0.0"), new Date()));

        List<AnalysisType> analysisTypes = Arrays.asList(AnalysisType.values());
        System.out.println(analysisTypes);
        analysisTypes.forEach(analysisType -> {
            switch (analysisType) {
                case SMA:
                    assertSame(AnalysisType.SMA, analysisType);
                    break;
                case EMA:
                    assertSame(AnalysisType.EMA, analysisType);
                    break;
                case MACD:
                    assertSame(AnalysisType.MACD, analysisType);
                    break;
                case MACDAVG:
                    assertSame(AnalysisType.MACDAVG, analysisType);
                    break;
                default:
                    throw new AssertionError(String.format("AnalysisType \"%s\" Constant Not Found", analysisType));
            }
        });

        System.out.println(Resolution.types);
        Resolution.types.entrySet().forEach(key_val -> assertEquals(key_val.getValue(), key_val.getKey()));
    }

    @Test
    @SuppressWarnings("unchecked")
    public void getStockSymbolsTEST() {
        System.out.println("FETCHING ALL STOCKS");
        Consumer<Runnable> test = done -> {
            String getStockSymbolsURL = API.getInstance().getStockSymbolsURL();
            fetchAndCatchAssertionError(getStockSymbolsURL,
                    done,
                    response -> {
                        assertNotNull("ALL STOCKS SHOULD NOT BE NULL", response);
                        if (response == null) {
                            done.run();
                            return;
                        }
                        assertEquals("array", response.getType());
                        if (response.getType().equals("array")) {
                            for (Object o : response.getDataArray()) {
                                assertTrue("Search result item is a HashMap", o instanceof HashMap);
                                Stock s = new Stock((HashMap<String, String>) o);
                            }
                            Stock.stocks.forEach(stockSymbol -> {
                                System.out.println(stockSymbol.getSymbol());
                            });
                            System.out.println(Stock.stocks.size());
                        }
                        done.run();
                    });
        };
        executeTest(test);
    }

    private void arePricePointValuesSizeSame(PricePoint pricePoint) {
        List<Integer> value_sizes = Arrays.asList(
                pricePoint.getOpen().size(),
                pricePoint.getHigh().size(),
                pricePoint.getLow().size(),
                pricePoint.getClose().size(),
                pricePoint.getTimestamps().size()
        );
        boolean sizes_same = value_sizes.stream().distinct().count() == 1;
        assertTrue("ALL PRICEPOINT VALUES HAVE SAME SIZES", sizes_same);
    }

    @Test
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void getPricePointTEST() {
        System.out.println("FETCHING PRICE POINTS TEST");
        Consumer<Runnable> test = done -> {
            API api = API.getInstance();
            String getStockCandlesURL = api.getStockCandlesURL(
                    "AAPL",
                    String.valueOf(15),
                    DateTimeHelper.toDate(LocalDate.now().minusDays(5)),
                    DateTimeHelper.toDate(LocalDate.now())
            );

            fetchAndCatchAssertionError(getStockCandlesURL,
                    done,
                    response -> {
                        assertEquals("object", response.getType());
                        if (response.getType().equals("object")) {
                            HashMap<String, Object>  data = response.getDataObj();
                            boolean status = api.isValidStatus((String) data.get("s"));
                            if (!status) {
                                System.out.println("NO DATA FOUND");
                                done.run();
                                return;
                            }
                            PricePoint s_c = new PricePoint(data);
                            arePricePointValuesSizeSame(s_c);
                            System.out.println(s_c.getOpen());
                            System.out.println(s_c.getHigh());
                            System.out.println(s_c.getLow());
                            System.out.println(s_c.getClose());
                            System.out.println(s_c.getTimestamps());
                            System.out.println(getStockCandlesURL);
                        }
                        done.run();
                    });
        };
        executeTest(test);
    }

    @Test
    @SuppressWarnings("unchecked")
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void getSearchTEST() {
        System.out.println("SEARCH FOR STOCK");
        Consumer<Runnable> test = done -> {
//        SEARCH ENDPOINT KEEPS CHANGING RESULTS (NOT FIXED RESULTS), SOMETIMES AAPL DOESN'T SHOW UP IN RESULTS
            String getSearchURL;
            try {
                getSearchURL = API.getInstance().getSearchSymbolURL("apple");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
                done.run();
                return;
            }
            assertNotNull(getSearchURL);

            fetchAndCatchAssertionError(getSearchURL,
                    done,
                    response -> {
                        if (response == null) {
                            done.run();
                            return;
                        }
                        assertEquals("object", response.getType());
                        if (response.getType().equals("object")) {
                            HashMap<String, Object> data = response.getDataObj();
                            int count = 0;
                            try {
                                count = (int) data.get("count");
                            } catch (Exception ignored) {

                            }
                            if (count > 0) {
                                assertTrue("Search Results is an Object array", data.get("result") instanceof Object[]);
                                Object[] results = (Object[]) data.get("result");
                                System.out.println(String.format("SEARCH COUNT >> %s %s", count, results != null ? results.length : 0));
                                for (Object o : results) {
                                    assertTrue("Search result item is a HashMap", o instanceof HashMap);
                                    HashMap<String, String> r = (HashMap<String, String>) o;
                                    System.out.println(new Stock(r));
                                }
                            } else {
                                System.out.println("NO RESULTS");
                            }
                            System.out.println(getSearchURL);
                        }
                        done.run();
                    });
        };
        executeTest(test);
    }

    @Test
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void FETCH_data_TEST() {
        System.out.println("FETCH DATA TEST");
        BiConsumer<HashMap<String, String>, Runnable> func = (stock_info, done) -> {
            Stock s = new Stock(stock_info);
            s.fetchData(
                    Resolution.types.get("15"),
                    DateTimeHelper.toDate(LocalDate.now().minusDays(5)),
                    DateTimeHelper.toDate(LocalDate.now()),
                    (price_points, stock) -> {
                        assertNotNull("Stock is not null", stock);
                        System.out.println(String.format("%s %s", s.getSymbol(), price_points == null ? null : "DATA!!"));
                        if (price_points == null) {
//                        API IS LIMITED IN FREE TIER
//                        System.out.println(String.format("API ERROR, DATA NOT FOUND FOR SYMBOL: %s", s.getSymbol()));
                            done.run();
                            return;
                        }
                        arePricePointValuesSizeSame(price_points);
                        System.out.println(price_points.getOpen());
                        System.out.println(price_points.getHigh());
                        System.out.println(price_points.getLow());
                        System.out.println(price_points.getClose());
                        System.out.println(price_points.getTimestamps());
                        done.run();
                    }
            );
        };

        executeTest(done -> {
            func.accept(new HashMap<String, String>() {{
                put("currency", "USD");
                put("description", "APPLE INC");
                put("displaySymbol", "AAPL");
                put("figi", "");
                put("mic", "");
                put("symbol", "AAPL");
                put("type", "Common Stock");
            }}, done);
        });

        executeTest(done -> {
            func.accept(new HashMap<String, String>() {{
                put("currency", "");
                put("description", "");
                put("displaySymbol", "");
                put("figi", "");
                put("mic", "");
                put("symbol", "APC.DE");
                put("type", "");
            }}, done);
        });
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

    private void printAnalysisPointsAndClosePrices(List<AnalysisPoint> analysisPoints, List<DataPoint> close_prices, AnalysisType a_type) {
        int size = analysisPoints.size();
        for (int i = 0; i < size; i++) {
            printAnalysis(i, close_prices, a_type, analysisPoints.get(i));
        }
    }

    private void SMA_EMA_DEFAULT_TEST(PricePoint price_points, Stock stock, AnalysisType a_type,
                                      Function<Integer, List<AnalysisPoint>> calc, Function2<Integer, Integer, Boolean> validate) {
        assertNotNull("Stock is not null", stock);
        if (PricePointsNotFound(price_points)) {
            return;
        }
        arePricePointValuesSizeSame(price_points);
        List<DataPoint> close_prices = getClosePrice(price_points);
        printClosePrices(price_points);


        List<AnalysisPoint> analysisPoints = null;

        int close_prices_size = close_prices.size();

        Function<Integer, String> expected_error_msg = (nDays) -> String.format("EXPECTED ERROR FOR %s: No Points for nDays set to %s", a_type, nDays);
        String err_msg = expected_error_msg.apply(0);

        assertFalse(validate.invoke(0, close_prices_size));

        System.out.println(err_msg);
        analysisPoints = calc.apply(0);
        assertEquals(err_msg, 0, analysisPoints.size());

        int end_valid_i = a_type == AnalysisType.SMA ? close_prices_size : close_prices_size - 1;

        for (int i = 1; i <= end_valid_i; i++) {
            assertTrue(validate.invoke(i, close_prices_size));
            analysisPoints = calc.apply(i);
            assertTrue(String.format("%s: Points found %s ", a_type, analysisPoints.size()), analysisPoints.size() > 0);
            assertEquals(String.format("%s: CLOSE PRICES SIZE AND ANALYSIS POINTS SIZES ARE SAME", a_type), close_prices.size(), analysisPoints.size());
            printAnalysisPointsAndClosePrices(analysisPoints, close_prices, a_type);
        }

        int start_inv_i = a_type == AnalysisType.SMA ? close_prices_size + 1 : close_prices_size;

        for (int i = start_inv_i; i <= close_prices_size + 100; i++) {
            err_msg = expected_error_msg.apply(i);
            assertFalse(validate.invoke(i, close_prices_size));
            System.out.println(err_msg);
            analysisPoints = calc.apply(i);
            assertEquals(err_msg, 0, analysisPoints.size());
        }
    }

    @Test
    public void SMA_TEST() {
        System.out.println("SMA TEST");
        HashMap<String, String> AAPL = new HashMap<String, String>() {{
            put("currency", "USD");
            put("description", "APPLE INC");
            put("displaySymbol", "AAPL");
            put("figi", "");
            put("mic", "");
            put("symbol", "AAPL");
            put("type", "Common Stock");
        }};

        executeTest(done -> {
            FETCH_DATA_WITH_ASSERTION_ERROR_CATCH(
                    AAPL,
                    DateTimeHelper.toDate(LocalDate.now().minusDays(15)),
                    DateTimeHelper.toDate(LocalDate.now()),
                    done,
                    (price_points, stock) -> {
                        SMA_EMA_DEFAULT_TEST(price_points, stock, AnalysisType.SMA, stock::calculateSMA, this::validateSMA);
                        done.run();
                    }
            );
        });
    }

    @Test
    public void EMA_TEST() {
        System.out.println("EMA TEST");
        HashMap<String, String> AAPL = new HashMap<String, String>() {{
            put("currency", "USD");
            put("description", "APPLE INC");
            put("displaySymbol", "AAPL");
            put("figi", "");
            put("mic", "");
            put("symbol", "AAPL");
            put("type", "Common Stock");
        }};

        executeTest(done -> {
            FETCH_DATA_WITH_ASSERTION_ERROR_CATCH(
                    AAPL,
                    DateTimeHelper.toDate(LocalDate.now().minusDays(22)),
                    DateTimeHelper.toDate(LocalDate.now()),
                    done,
                    (price_points, stock) -> {
                        SMA_EMA_DEFAULT_TEST(price_points, stock, AnalysisType.EMA, stock::calculateEMA, this::validateEMA);
                        done.run();
                    }
            );
        });
    }

    private void MACD_MACDAVG_DEFAULT_TEST(PricePoint price_points, Stock stock,
                                           Date fromDate, Date toDate, AnalysisType a_type,
                                           Supplier<List<AnalysisPoint>> calc) throws Exception {
        assertNotNull("Stock is not null", stock);
        if (PricePointsNotFound(price_points)) {
            return;
        }
        arePricePointValuesSizeSame(price_points);
        List<DataPoint> close_prices = getClosePrice(price_points);
        printClosePrices(price_points);
        if (!validateMACD(fromDate, toDate, close_prices.size())) {
            throw new Exception(String.format("\n%s: Date difference must be greater.Current difference=%s\n", a_type, DateTimeHelper.dateDiff(fromDate, toDate)));
        }
        List<AnalysisPoint> analysisPoints = calc.get();
        assertEquals(String.format("%s: CLOSE PRICES SIZE AND ANALYSIS POINTS SIZES ARE SAME", a_type), close_prices.size(), analysisPoints.size());
        printAnalysisPointsAndClosePrices(analysisPoints, close_prices, a_type);
    }


    @Test
    public void MACD_TEST() {
        System.out.println("MACD TEST");
        HashMap<String, String> AAPL = new HashMap<String, String>() {{
            put("currency", "USD");
            put("description", "APPLE INC");
            put("displaySymbol", "AAPL");
            put("figi", "");
            put("mic", "");
            put("symbol", "AAPL");
            put("type", "Common Stock");
        }};

        BiConsumer<Date, Date> test = (fromDate, toDate) -> {
            executeTest(done -> {
                FETCH_DATA_WITH_ASSERTION_ERROR_CATCH(
                        AAPL,
                        fromDate,
                        toDate,
                        done,
                        (price_points, stock) -> {
                            try {
                                MACD_MACDAVG_DEFAULT_TEST(price_points, stock, fromDate, toDate, AnalysisType.MACD, stock::calculateMACD);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            done.run();
                        }
                );
            });
        };

        IntStream.iterate(0, i -> i + 27)
                .limit(10)
                .boxed()
                .collect(Collectors.toList())
                .forEach(i -> {
                    Date fromDate = DateTimeHelper.toDate(LocalDate.now().minusDays(i));
                    Date toDate = DateTimeHelper.toDate(LocalDate.now());
                    System.out.println(String.format("DATE DIFF: %s", DateTimeHelper.dateDiff(fromDate, toDate)));
                    test.accept(fromDate, toDate);
                });
    }

    @Test
    public void MACDAVG_TEST() {
        System.out.println("MACDAVG TEST");
        HashMap<String, String> AAPL = new HashMap<String, String>() {{
            put("currency", "USD");
            put("description", "APPLE INC");
            put("displaySymbol", "AAPL");
            put("figi", "");
            put("mic", "");
            put("symbol", "AAPL");
            put("type", "Common Stock");
        }};

        BiConsumer<Date, Date> test = (fromDate, toDate) -> {
            executeTest(done -> {
                FETCH_DATA_WITH_ASSERTION_ERROR_CATCH(
                        AAPL,
                        fromDate,
                        toDate,
                        done,
                        (price_points, stock) -> {
                            try {
                                MACD_MACDAVG_DEFAULT_TEST(price_points, stock, fromDate, toDate, AnalysisType.MACDAVG, stock::calculateMACDAVG);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            done.run();
                        }
                );
            });
        };

        IntStream.iterate(0, i -> i + 27)
                .limit(10)
                .boxed()
                .collect(Collectors.toList())
                .forEach(i -> {
                    Date fromDate = DateTimeHelper.toDate(LocalDate.now().minusDays(i));
                    Date toDate = DateTimeHelper.toDate(LocalDate.now());
                    System.out.println(String.format("DATE DIFF: %s", DateTimeHelper.dateDiff(fromDate, toDate)));
                    test.accept(fromDate, toDate);
                });
    }

    @Test
    public void IndicatorsTEST() {
        System.out.println("ALL INDICATORS TEST");
        SMA_TEST();
        EMA_TEST();
        MACD_TEST();
        MACDAVG_TEST();
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

    private boolean PricePointsNotFound(PricePoint price_points) {
//        API IS LIMITED IN FREE TIER
//        System.out.println(String.format("API ERROR, DATA NOT FOUND FOR SYMBOL");
        return price_points == null;
    }

    private List<DataPoint> getClosePrice(PricePoint price_points) {
        return price_points.getClose();
    }

    private void printClosePrices(PricePoint price_points) {
        System.out.print("Stock prices: ");
        System.out.println(getClosePrice(price_points));
        System.out.println(getClosePrice(price_points).size());
    }
}
