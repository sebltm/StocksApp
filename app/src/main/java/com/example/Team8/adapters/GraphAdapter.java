package com.example.Team8.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.Team8.ui.main.GraphFragment;
import com.example.Team8.utils.AnalysisType;
import com.example.Team8.utils.SearchHistoryItem;

import java.util.ArrayList;
import java.util.List;

public class GraphAdapter extends FragmentStateAdapter {

    private List<GraphFragment> fragments;
    private SearchHistoryItem searchItem;
    private List<AnalysisType> analysisTypes;
    private int numTabs;

    public GraphAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
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
    public Fragment createFragment(int position) {

        GraphFragment graphFragment;
        if (fragments.size() - 1 < position || fragments.get(position) == null) {
            graphFragment = new GraphFragment(this.searchItem, this.analysisTypes.get(position));
            fragments.set(position, graphFragment);
        } else {
            graphFragment = fragments.get(position);
        }

        return graphFragment;
    }

    @Override
    public int getItemCount() {
        return this.numTabs;
    }
}
