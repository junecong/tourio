package com.tourio.eklrew.tourio;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by Admin on 7/25/15.
 */
public class MyFragmentPagerAdapter extends FragmentPagerAdapter {

    final int PAGE_COUNT = 3;

    /** Constructor of the class */
    public MyFragmentPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    /** This method will be invoked when a page is requested to create */
    @Override
    public Fragment getItem(int arg0) {
        Bundle data = new Bundle();
        switch(arg0){

            /** tab1 is selected */
            case 0:
                NearMe fragment1 = new NearMe();
                return fragment1;

            /** tab2 is selected */
            case 1:
                Rating fragment2 = new Rating();
                return fragment2;

            case 2:
                Duration fragment3 = new Duration();
                return fragment3;
        }
        return null;
    }

    /** Returns the number of pages */
    @Override
    public int getCount() {
        return PAGE_COUNT;
    }
}
