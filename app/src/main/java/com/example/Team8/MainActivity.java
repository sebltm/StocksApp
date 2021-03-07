package com.example.Team8;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.example.Team8.tests.Test;
import com.example.Team8.ui.main.ModelFacade;
import com.example.Team8.utils.Stock;

public class MainActivity extends AppCompatActivity {
    ModelFacade model;

    @RequiresApi(api = Build.VERSION_CODES.O)
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
            new Test(this).Run();
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
}