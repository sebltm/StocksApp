package com.example.Team8.ui.main;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class GraphAdapter extends FragmentPagerAdapter {

    String[] titles;
    int numTabs;

    public GraphAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }

    public void setAttributes(String[] titles, int numTabs) {
        this.titles = titles;
        this.numTabs = numTabs;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return new GraphFragment();
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return titles[position];
    }

    @Override
    public int getCount() {
        return this.numTabs;
    }
}
