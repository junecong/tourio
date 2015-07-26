package com.tourio.eklrew.tourio;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBar.Tab;
import android.support.v7.app.ActionBarActivity;



import java.util.ArrayList;
import java.util.List;


public class TourListActivity extends ActionBarActivity {

    private ViewPager mPager;
    ListView tourListView;
    TourListAdapter tourAdapter;
    ActionBar mActionbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tour_list);

        mActionbar = getSupportActionBar();
        mActionbar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        mPager = (ViewPager) findViewById(R.id.pager);
        FragmentManager fm = getSupportFragmentManager();

        ViewPager.SimpleOnPageChangeListener pageChangeListener = new ViewPager.SimpleOnPageChangeListener(){
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                mActionbar.setSelectedNavigationItem(position);
            }
        };

        /** Setting the pageChange listener to the viewPager */
        mPager.setOnPageChangeListener(pageChangeListener);

        /** Creating an instance of FragmentPagerAdapter */
        MyFragmentPagerAdapter fragmentPagerAdapter = new MyFragmentPagerAdapter(fm);

        /** Setting the FragmentPagerAdapter object to the viewPager object */
        mPager.setAdapter(fragmentPagerAdapter);

        mActionbar.setDisplayShowTitleEnabled(true);

        /** Defining tab listener */
        ActionBar.TabListener tabListener = new ActionBar.TabListener() {

            @Override
            public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft) {
            }

            @Override
            public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {
                mPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft) {
            }
        };

        /** Creating fragment1 Tab */
        Tab tab = mActionbar.newTab()
                .setText("Near Me")
                .setTabListener(tabListener);

        mActionbar.addTab(tab);

        /** Creating fragment2 Tab */
        tab = mActionbar.newTab()
                .setText("Rating")
                .setTabListener(tabListener);

        mActionbar.addTab(tab);

        /** Creating fragment2 Tab */
        tab = mActionbar.newTab()
                .setText("Duration")
                .setTabListener(tabListener);

        mActionbar.addTab(tab);


        tourListView = (ListView) findViewById(R.id.tour_list);
        //Hardcoded tour list
        TourListItem tour = new TourListItem("SanFrancisco","Fun day in SF",2);
        List<TourListItem> tourList = new ArrayList<TourListItem>();
        for (int i=0;i<10;i++) {
            tourList.add(tour);
        }
        tourAdapter = new TourListAdapter(this,tourList);

        tourListView.setAdapter(tourAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.menu_action_bar, menu);
//        return super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu_action_bar, menu);

//        MenuItem searchItem = menu.findItem(R.id.search);
//        SearchView searchView =
//                (SearchView) MenuItemCompat.getActionView(searchItem);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
