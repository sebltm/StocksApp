package com.example.Team8.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.Team8.ui.main.GraphFragment;
import com.example.Team8.utils.AnalysisType;
import com.example.Team8.utils.SearchHistoryItem;

import java.util.List;

public class GraphAdapter extends FragmentStateAdapter {

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
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return new GraphFragment(this.searchItem, this.analysisTypes.get(position));
    }

    @Override
    public int getItemCount() {
        return this.numTabs;
    }
}
