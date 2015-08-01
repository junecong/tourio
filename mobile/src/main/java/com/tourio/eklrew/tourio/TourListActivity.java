package com.tourio.eklrew.tourio;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.Layout;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ListView;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Prud on 7/24/2015.
 */
public class TourListActivity extends NavigationBarActivity {

    private ListView tourListView;
    private ArrayList<TourListItem> tours;
    private TourListAdapter tourAdapter;

    private Button nearMeButton;
    private Button ratingButton;
    private Button durationButton;

    private Button currentSortButton;
    private int cityIndex;

    final String[] cities = {"Berkeley","San Francisco"};
    final int INDEX_BERKELEY = 0;
    final int INDEX_SAN_FRANCISCO = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_tour_list);
        contentFrame.addView((getLayoutInflater()).inflate(R.layout.activity_tour_list,null));

        initButtons();

        tourListView = (ListView) findViewById(R.id.tour_list);

        //Hardcoded tour list
        TourListItem tour = TourHelper.hardCodedTourListItem();
        tours = new ArrayList<TourListItem>();
        for (int i=0;i<10;i++) {
            tours.add(tour);
        }
        tourAdapter = new TourListAdapter(this,tours);
        tourListView.setAdapter(tourAdapter);

        tourListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                TourListItem item = (TourListItem) tourAdapter.getItem(position);
                Intent detailIntent = new Intent(TourListActivity.this, DetailTourActivity.class)
                        .putExtra("tour_id", item.getTourId());
                startActivity(detailIntent);
            }
        });
    }

    private void initButtons() {
        nearMeButton = (Button) findViewById(R.id.near_me);
        ratingButton = (Button) findViewById(R.id.rating);
        durationButton = (Button) findViewById(R.id.duration);
        currentSortButton = nearMeButton;

        nearMeButton.setBackgroundColor(Color.parseColor("#ffffffff"));
        ratingButton.setBackgroundColor(Color.parseColor("#ffff9800"));
        durationButton.setBackgroundColor(Color.parseColor("#ffff9800"));
    }



    private Location getCurrentLocation() {
        LocationManager locationManager = (LocationManager)
                getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();

        Location location = locationManager.getLastKnownLocation(locationManager
                .getBestProvider(criteria, false));
        return location;
    }

    public void sortNearMe(View view) {
        TourHelper.swapColors(currentSortButton,nearMeButton);
        currentSortButton = nearMeButton;
        Collections.sort(tours,new TourListItem.DistanceComparator(getCurrentLocation()));
        refreshList();
    }

    public void sortRating(View view) {
        TourHelper.swapColors(currentSortButton,ratingButton);
        currentSortButton = ratingButton;
        Collections.sort(tours,new TourListItem.RatingComparator());
        refreshList();
    }

    public void sortDuration(View view) {
        TourHelper.swapColors(currentSortButton,durationButton);
        currentSortButton = durationButton;
        Collections.sort(tours,new TourListItem.DurationComparator());
        refreshList();
    }

    private void refreshList() {
        tourAdapter.refreshAdapter(tours);
    }

    /*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_tour_list, menu);
        return true;
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
    */
}
