package com.example.ahmad.myapplication;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

/**
 * Created by Ahmad on 28 ديس، 14 م.
 */
public class tabbedSwipeAdapter extends FragmentStatePagerAdapter {
    private int tabsCount = 0;
    private String[] theWords;

    public tabbedSwipeAdapter(FragmentManager fm, String[] words) {
        super(fm);
        tabsCount = words.length;
        theWords = words;
    }

    @Override
    public Fragment getItem(int i) {
        stringDisplayFrag ff = new stringDisplayFrag();
        ff.setStringToDisplay(theWords[i]);
        return ff;
    }

    @Override
    public int getCount() {
        return tabsCount;
    }
}
