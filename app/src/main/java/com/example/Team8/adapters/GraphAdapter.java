package com.example.Team8.adapters;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.Team8.ui.main.GraphFragment;
import com.example.Team8.utils.AnalysisType;
import com.example.Team8.utils.SearchHistoryItem;

import java.util.ArrayList;
import java.util.List;

public class GraphAdapter extends FragmentPagerAdapter {

    private List<GraphFragment> fragments;
    private SearchHistoryItem searchItem;
    private List<AnalysisType> analysisTypes;
    private int numTabs;

    public GraphAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }

    public void setAttributes(SearchHistoryItem searchItem) {
        this.searchItem = searchItem;
        this.analysisTypes = searchItem.getAnalysisTypes();
        this.numTabs = analysisTypes.size();

        this.fragments = new ArrayList<>();
        for (int i = 0; i < analysisTypes.size(); i++) {
            fragments.add(null);
        }
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {

        GraphFragment graphFragment;
        if (fragments.size() - 1 < position || fragments.get(position) == null) {
            graphFragment = new GraphFragment(this.searchItem, this.analysisTypes.get(position));
            fragments.set(position, graphFragment);
        } else {
            graphFragment = fragments.get(position);
        }

        return graphFragment;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return analysisTypes.get(position).name();
    }

    @Override
    public int getCount() {
        return this.numTabs;
    }
}
