package com.example.Team8;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.Team8.ui.main.ModelFacade;
import com.example.Team8.utils.API;
import com.example.Team8.utils.DateTimeHelper;
import com.example.Team8.utils.HTTP_JSON;
import com.example.Team8.utils.JSON;
import com.example.Team8.utils.Stock;
import com.example.Team8.utils.StockCandle;

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

        findViewById(R.id.textButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getStockSymbolsTEST();
//                getStockCandlesTEST();
            }
        });

        findViewById(R.id.mBttnSwitchSearch).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SearchActivity.class);
                startActivity(intent);
            }
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
}