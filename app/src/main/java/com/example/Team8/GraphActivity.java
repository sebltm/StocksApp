package com.example.Team8;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.example.Team8.ui.main.GraphAdapter;
import com.google.android.material.tabs.TabLayout;

public class GraphActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph);

        String[] pageTitles = {"SMA", "EMA"};

        GraphAdapter graphAdapter = new GraphAdapter(getSupportFragmentManager(), 0);
        graphAdapter.setAttributes(pageTitles, pageTitles.length);

        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(graphAdapter);

        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);
    }
}