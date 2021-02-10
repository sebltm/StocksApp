package com.example.Team8.ui.main;

import android.content.Context;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class SectionsPagerAdapter extends FragmentPagerAdapter
{
  private static final String[] TAB_TITLES = new String[]{ "FindQuote", "Analyse" };
    private final Context mContext;

  public SectionsPagerAdapter(Context context, FragmentManager fm)
  { super(fm);
    mContext = context;
  }

  @Override
  public Fragment getItem(int position)
  { // instantiate a fragment for the page.
    if (position == 0)
    { return findQuoteFragment.newInstance(mContext); }
    else
    if (position == 1)
    { return analyseFragment.newInstance(mContext); }
    return analyseFragment.newInstance(mContext); 
  }

  @Nullable
 @Override
  public CharSequence getPageTitle(int position) 
  { return TAB_TITLES[position]; }

  @Override
  public int getCount()
  { return 2; }
}
