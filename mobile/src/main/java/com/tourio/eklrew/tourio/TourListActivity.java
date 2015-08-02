package com.tourio.eklrew.tourio;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Point;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.Layout;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Created by Prud on 7/24/2015.
 */
public class TourListActivity extends NavigationBarActivity {

    private ListView tourListView;
    private ArrayList<TourListItem> tours;
    private TourListAdapter tourAdapter;

    private Location mLocation;

    private Button nearMeButton;
    private Button ratingButton;
    private Button durationButton;
    private Button currentSortButton;
    private int currentSortType;
    final int SORT_NEAR_ME = 0;
    final int SORT_RATING = 1;
    final int SORT_DURATION = 2;

    private int cityIndex;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        contentFrame.addView((getLayoutInflater()).inflate(R.layout.activity_tour_list, null));

        tourListView = (ListView) findViewById(R.id.tour_list);
        tours = TourHelper.randomTourListItems();
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

        initButtons();
        GetLocationTask getLocationTask = new GetLocationTask();
        getLocationTask.execute();
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

        if (location != null) {
            return location;
        }
        location = new Location("");
        location.setLatitude(37.0);
        location.setLongitude(-122.0);
        return location;
    }

    public void sortNearMe(View view) {
        TourHelper.swapColors(currentSortButton,nearMeButton);
        currentSortButton = nearMeButton;
        currentSortType = SORT_NEAR_ME;
        Collections.sort(tours,new TourListItem.DistanceComparator(mLocation));
        refreshList();
        Log.v("current button","nearMe");
    }

    public void sortRating(View view) {
        TourHelper.swapColors(currentSortButton,ratingButton);
        currentSortButton = ratingButton;
        currentSortType = SORT_RATING;
        Collections.sort(tours,new TourListItem.RatingComparator());
        refreshList();
        Log.v("current button", "rating");
    }

    public void sortDuration(View view) {
        TourHelper.swapColors(currentSortButton,durationButton);
        currentSortButton = durationButton;
        currentSortType = SORT_DURATION;
        Collections.sort(tours,new TourListItem.DurationComparator());
        refreshList();
        Log.v("current button", "duration");
    }

    private void refreshList() {
        tourAdapter.addAll(tours);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_tour_list, menu);
        final String[] cities = getResources().getStringArray(R.array.cities);
        for (String city:cities) {
            Log.v("city",city);
        }

        MenuItem spinnerItem = menu.findItem(R.id.cities_spinner);
        final Spinner citySpinner = (Spinner) MenuItemCompat.getActionView(spinnerItem);
        final SpinnerAdapter cityAdapter = ArrayAdapter.createFromResource(this,
                R.array.cities, android.R.layout.simple_spinner_item);
        citySpinner.setAdapter(cityAdapter);
        citySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                refreshList(cityAdapter.getItem(pos).toString());
            }

            public void onNothingSelected(AdapterView<?> parent) {
                // Another interface callback
            }
        });

        return true;
    }

    public void refreshList(String city) {
        Log.v("current city",city);
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


    public class GetLocationTask extends AsyncTask<Void,Void,Location> {
        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            dialog = ProgressDialog.show( TourListActivity.this, " " , "Waiting for location...", true);
        }

        @Override
        protected Location doInBackground(Void... params) {
            Location location = null;
            int timePassed = 0;
            while (location==null && timePassed<20000) {
                LocationManager locationManager = (LocationManager)
                        getSystemService(Context.LOCATION_SERVICE);
                Criteria criteria = new Criteria();
                location = locationManager.getLastKnownLocation(locationManager
                        .getBestProvider(criteria, false));
                timePassed += 1000;
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    Log.e("thread error","thread InterruptedException");
                    e.printStackTrace();
                }

                Log.v("location_debug",(location==null)? "location null" : location.getLatitude()+","+location.getLongitude());
            }
            return location;
        }

        @Override
        protected void onPostExecute(Location location) {
            dialog.dismiss();
            mLocation = location;
            sortNearMe(null);
        }
    }
}
