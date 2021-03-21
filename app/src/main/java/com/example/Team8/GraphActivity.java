package com.example.Team8;

import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.example.Team8.adapters.GraphAdapter;
import com.example.Team8.ui.main.GraphFragment;
import com.example.Team8.utils.SearchHistoryItem;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class GraphActivity extends FragmentActivity {

    public static SearchHistoryItem searchItem;
    public ActivityResultLauncher<String> requestPermissionLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph);

        GraphAdapter graphAdapter = new GraphAdapter(this);
        graphAdapter.setAttributes(searchItem);

        ViewPager2 viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(graphAdapter);

        TabLayout tabLayout = findViewById(R.id.tabs);
        TabLayoutMediator tabLayoutMediator = new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> tab.setText(GraphActivity.searchItem.getAnalysisTypes().get(position).name()));
        tabLayoutMediator.attach();

        requestPermissionLauncher = registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
            if (isGranted) {
                int currentItem = viewPager.getCurrentItem();
                String tag = "f" + currentItem;
                GraphFragment fragment = ((GraphFragment) getSupportFragmentManager().findFragmentByTag("f" + currentItem));
                fragment.checkPermissions();
            }
        });
    }
}