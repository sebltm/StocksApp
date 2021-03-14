package com.example.Team8;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.WindowManager;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class MainActivity extends AppCompatActivity {

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS );
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        findViewById(R.id.mBttnSwitchSearch).setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, SearchActivity.class);
            startActivity(intent);
        });

        findViewById(R.id.mBttnSwitchSearchHistory).setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, SearchHistoryActivity.class);
            startActivity(intent);
        });

        findViewById(R.id.mBttnSwitchGraph).setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, GraphActivity.class);
            startActivity(intent);
        });
    }
}