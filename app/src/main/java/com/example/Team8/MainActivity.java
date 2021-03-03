package com.example.Team8;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.example.Team8.ui.main.ModelFacade;
import com.example.Team8.utils.API;
import com.example.Team8.utils.DateTimeHelper;
import com.example.Team8.utils.HTTP_JSON;
import com.example.Team8.utils.JSON;
import com.example.Team8.utils.PricePoint;
import com.example.Team8.utils.Resolution;
import com.example.Team8.utils.Stock;
import com.example.Team8.utils.StockCandle;

import java.io.UnsupportedEncodingException;
import java.time.LocalDate;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {
    ModelFacade model;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
//        ViewPager viewPager = findViewById(R.id.view_pager);
//        viewPager.setAdapter(sectionsPagerAdapter);
//        TabLayout tabs = findViewById(R.id.tabs);
//        tabs.setupWithViewPager(viewPager);
//        model = ModelFacade.getInstance(this);

        findViewById(R.id.textButton).setOnClickListener(view -> {
            getStockSymbolsTEST();
        });

        findViewById(R.id.mBttnSwitchSearch).setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, SearchActivity.class);
            startActivity(intent);
        });

        findViewById(R.id.mBttnSwitchSearchHistory).setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, SearchHistoryActivity.class);
            startActivity(intent);
        });
    }

    private void getStockSymbolsTEST() {
        Toast.makeText(this, "FETCHING STOCK SYMBOLS", Toast.LENGTH_SHORT).show();
        String getStockSymbolsURL = API.getStockSymbols();
        HTTP_JSON.fetch(getStockSymbolsURL,
                response -> {
                    if (response == null) {
                        return;
                    }
                    JSON j = (JSON) response;
                    if (j.getType().equals("array")) {
                        for (Object o : j.getDataArray()) {
                            Stock s = new Stock((HashMap) o);
                        }
                        System.out.println(Stock.stock_symbols.size());
                        Stock.stock_symbols.forEach(stockSymbol -> {
                            System.out.println(stockSymbol.getSymbol());
                        });
                    }
                });
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void getStockCandlesTEST() {
        Toast.makeText(this, "FETCHING STOCK CANDLES", Toast.LENGTH_SHORT).show();
        String getStockCandlesURL = API.getStockCandles(
                "AAPL",
                String.valueOf(15),
                DateTimeHelper.toDate(LocalDate.now().minusDays(100)),
                DateTimeHelper.toDate(LocalDate.now())
        );

        HTTP_JSON.fetch(getStockCandlesURL,
                response -> {
                    JSON j = (JSON) response;
                    if (response.getType().equals("object")) {
                        HashMap data = j.getDataObj();
                        boolean status = API.isValidStatus((String) data.get("s"));
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
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void getPricePointTEST() {
        Toast.makeText(this, "FETCHING STOCK CANDLES", Toast.LENGTH_SHORT).show();
        String getStockCandlesURL = API.getStockCandles(
                "AAPL",
                String.valueOf(15),
                DateTimeHelper.toDate(LocalDate.now().minusDays(5)),
                DateTimeHelper.toDate(LocalDate.now())
        );

        HTTP_JSON.fetch(getStockCandlesURL,
                response -> {
                    JSON j = (JSON) response;
                    if (response.getType().equals("object")) {
                        HashMap data = j.getDataObj();
                        boolean status = API.isValidStatus((String) data.get("s"));
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
    }

    private void getSearchTEST() {

//        SEARCH ENDPOINT KEEPS CHANGING RESULTS (NOT FIXED RESULTS), SOMETIMES AAPL DOESN'T SHOW UP IN RESULTS

        Toast.makeText(this, "SEARCH FOR STOCK", Toast.LENGTH_SHORT).show();
        String getSearchURL;
        try {
            getSearchURL = API.getSearchSymbolURL("apple");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return;
        }

        HTTP_JSON.fetch(getSearchURL,
                response -> {
                    if(response == null){
                        return;
                    }
                    JSON j = (JSON) response;
                    if (response.getType().equals("object")) {
                        HashMap data = j.getDataObj();
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
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void FETCH_data_TEST(HashMap stock_info) {
        Stock s = new Stock(stock_info);
        s.fetchData(
                Resolution.types.get("15"),
                DateTimeHelper.toDate(LocalDate.now().minusDays(5)),
                DateTimeHelper.toDate(LocalDate.now()),
                (priceHistory, stock) -> {
                    System.out.println(String.format("%s %s", s.getSymbol(), priceHistory == null? null : "DATA!!"));
                    if(priceHistory == null){
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
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void SMA_TEST(HashMap stock_info) {
        Stock s = new Stock(stock_info);
        s.fetchData(
                Resolution.types.get("15"),
                DateTimeHelper.toDate(LocalDate.now().minusDays(5)),
                DateTimeHelper.toDate(LocalDate.now()),
                (priceHistory, stock) -> {
                    if(priceHistory == null){
//                        API IS LIMITED IN FREE TIER
//                        System.out.println(String.format("API ERROR, DATA NOT FOUND FOR SYMBOL: %s", s.getSymbol()));
                        return;
                    }
                    stock.calculateSMA(15);
                }
        );
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void EMA_TEST(HashMap stock_info) {
        Stock s = new Stock(stock_info);
        s.fetchData(
                Resolution.types.get("15"),
                DateTimeHelper.toDate(LocalDate.now().minusDays(5)),
                DateTimeHelper.toDate(LocalDate.now()),
                (priceHistory, stock) -> {
                    if(priceHistory == null){
//                        API IS LIMITED IN FREE TIER
//                        System.out.println(String.format("API ERROR, DATA NOT FOUND FOR SYMBOL: %s", s.getSymbol()));
                        return;
                    }
                    stock.calculateEMA(15);
                }
        );
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void MACD_TEST(HashMap stock_info) {
        Stock s = new Stock(stock_info);
        s.fetchData(
                Resolution.types.get("15"),
                DateTimeHelper.toDate(LocalDate.now().minusDays(5)),
                DateTimeHelper.toDate(LocalDate.now()),
                (priceHistory, stock) -> {
                    if(priceHistory == null){
//                        API IS LIMITED IN FREE TIER
//                        System.out.println(String.format("API ERROR, DATA NOT FOUND FOR SYMBOL: %s", s.getSymbol()));
                        return;
                    }
                    stock.calculateMACD(26,12,9);
                }
        );
    }
}