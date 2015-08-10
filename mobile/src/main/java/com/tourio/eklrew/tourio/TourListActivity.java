package com.tourio.eklrew.tourio;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.support.v4.view.MenuItemCompat;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by Prud on 7/24/2015.
 */
public class TourListActivity extends NavigationBarActivity {

    private ListView tourListView;
    private ArrayList<TourListItem> tours;
    private TourListAdapter tourAdapter;

    private Location mLocation;
    private boolean hasLocation = false;

    private Button nearMeButton;
    private Button ratingButton;
    private Button durationButton;
    private Button currentSortButton;
    private int currentSortType;
    final int SORT_NEAR_ME = 0;
    final int SORT_RATING = 1;
    final int SORT_DURATION = 2;

    private int currentCityIndex;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        contentFrame.addView((getLayoutInflater()).inflate(R.layout.activity_tour_list, null));

        initVars();
        //loadTours();

/*
        //final SwipeDetector swipeDetector = new SwipeDetector();
        //tourListView.setOnTouchListener(swipeDetector);

        tourListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                TourListItem item = (TourListItem) tourAdapter.getItem(position);

                if(swipeDetector.swipeDetected()) {
                    if(swipeDetector.getAction() == SwipeDetector.Action.LR) {
                        //swiped left to right
                        item.changeImage(TourListActivity.this,item.getImageView(),true);
                    } else if (swipeDetector.getAction() == SwipeDetector.Action.RL) {
                        //swiped right to left
                        item.changeImage(TourListActivity.this,item.getImageView(),false);
                    } else {
                        Intent detailIntent = new Intent(TourListActivity.this, DetailTourActivity.class)
                                .putExtra("tour_id", item.getTourId());
                        startActivity(detailIntent);
                    }
                }

                Intent detailIntent = new Intent(TourListActivity.this, DetailTourActivity.class)
                        .putExtra("tour_id", item.getTourId());
                startActivity(detailIntent);
            }
        });
        */
    }

    private void initVars() {
        nearMeButton = (Button) findViewById(R.id.near_me);
        ratingButton = (Button) findViewById(R.id.rating);
        durationButton = (Button) findViewById(R.id.duration);
        currentSortButton = nearMeButton;
        currentSortType = SORT_NEAR_ME;

        tourListView = (ListView) findViewById(R.id.tour_list);
        tourAdapter = new TourListAdapter(TourListActivity.this,
                new ArrayList<TourListItem>());
        tourListView.setAdapter(tourAdapter);

        currentSortType = SORT_NEAR_ME;
        currentCityIndex = 0;
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
        if (hasLocation) {
            sortNearMeHelper();
        }
        else {
            (new GetLocationAndSortTask()).execute();
        }
    }

    public void sortNearMeHelper() {
        TourioHelper.LayoutHelper.swapColors(currentSortButton,nearMeButton);
        currentSortButton = nearMeButton;
        currentSortType = SORT_NEAR_ME;
        Collections.sort(tours,new TourListItem.DistanceComparator(mLocation));
        refreshList();
        Log.v("current button","nearMe");
    }

    public void sortRating(View view) {
        TourioHelper.LayoutHelper.swapColors(currentSortButton,ratingButton);
        currentSortButton = ratingButton;
        currentSortType = SORT_RATING;
        Collections.sort(tours,new TourListItem.RatingComparator());
        refreshList();
        Log.v("current button", "rating");
    }

    public void sortDuration(View view) {
        TourioHelper.LayoutHelper.swapColors(currentSortButton,durationButton);
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
        for (String city : TourioHelper.CityHelper.CITIES) {
            Log.v("city",city);
        }

        MenuItem spinnerItem = menu.findItem(R.id.cities_spinner);
        final Spinner citySpinner = (Spinner) MenuItemCompat.getActionView(spinnerItem);
        final SpinnerAdapter cityAdapter = new ArrayAdapter(this,
                android.R.layout.simple_spinner_item, TourioHelper.CityHelper.CITIES);
        citySpinner.setAdapter(cityAdapter);
        citySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                currentCityIndex = pos;
                loadTours();
            }

            public void onNothingSelected(AdapterView<?> parent) {
                // Another interface callback
            }
        });

        return true;
    }

    public void loadTours () {
        (new FetchToursTask()).execute(getCurrentCityId());
        switch(currentSortType) {
            case SORT_NEAR_ME: sortNearMe(null); break;
            case SORT_RATING: sortRating(null); break;
            case SORT_DURATION: sortDuration(null); break;
        }
    }

    public int getCurrentCityId() {
        return TourioHelper.CityHelper.CITY_INDEX_TO_ID_MAP.get(currentCityIndex);
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


    public class GetLocationAndSortTask extends AsyncTask<Void,Void,Location> {
        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            dialog = ProgressDialog.show( TourListActivity.this, " " , "Waiting for location...", true);
        }

        @Override
        protected Location doInBackground(Void... params) {
            Location location = null;
            int timePassed = 0;
            while (location==null && timePassed<0) {
                LocationManager locationManager = (LocationManager)
                        getSystemService(Context.LOCATION_SERVICE);
                Criteria criteria = new Criteria();
                location = locationManager.getLastKnownLocation(locationManager
                        .getBestProvider(criteria, false));
                timePassed += 100;
                try {
                    Thread.sleep(100);
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
            if (location==null) {
                //Toast.makeText(TourListActivity.this,"Location not found",Toast.LENGTH_LONG).show();
                location = new Location("");
                location.setLatitude(37.872441);
                location.setLongitude(-122.259503);
            }
            else {
                hasLocation = true;
            }
            mLocation = location;
            sortNearMeHelper();
        }
    }

    public class FetchToursTask extends AsyncTask<Integer,Void,ArrayList<TourListItem>> {
        private ArrayList<TourListItem> getToursDataFromJson(String json) throws JSONException {
            ArrayList<TourListItem> tourListFromJson = new ArrayList<TourListItem>();

            JSONArray toursJsonArray = new JSONArray(json);

            for (int i=0;i<toursJsonArray.length();i+=2) {
                JSONObject tourJsonObject = toursJsonArray.getJSONObject(i);
                int tourId = tourJsonObject.getInt(TourioHelper.TourListJsonHelper.JSON_TOUR_ID);
                String tourName = tourJsonObject.getString(TourioHelper.TourListJsonHelper.JSON_TOUR_NAME);
                int tourRating = tourJsonObject.getInt(TourioHelper.TourListJsonHelper.JSON_TOUR_RATING);
                int tourDuration = tourJsonObject.getInt(TourioHelper.TourListJsonHelper.JSON_TOUR_DURATION);

                JSONArray stopsJsonArray = toursJsonArray.getJSONArray(i + 1);
                int numStops = stopsJsonArray.length();
                LatLng[] stopListFromJson = new LatLng[numStops];
                for (int j=0;j<numStops;j++) {
                    JSONObject stopJsonObject = stopsJsonArray.getJSONObject(j);
                    stopListFromJson[j] = new LatLng(
                            stopJsonObject.getDouble(TourioHelper.TourListJsonHelper.JSON_STOP_LATITUDE),
                            stopJsonObject.getDouble(TourioHelper.TourListJsonHelper.JSON_STOP_LONGITUDE)
                    );
                }

                //tourListFromJson.add(new TourListItem(tourId,tourName,tourDuration,tourRating,stopListFromJson));
                tourListFromJson.add(new TourListItem(tourId,tourName,tourDuration,tourRating,stopListFromJson,TourioHelper.TourDataHelper.hardCodedStopImages()));
            }

            return tourListFromJson;
        }

        @Override
        protected ArrayList<TourListItem> doInBackground(Integer... city) {
            String toursUrlString = TourioHelper.DataBaseUrlHelper.BASE_CITY_URL + city[0];
            Log.v("URL",toursUrlString);
            URL toursUrl;
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            String toursJsonStr = null;
            try {
                toursUrl = new URL(toursUrlString);
                urlConnection = (HttpURLConnection) toursUrl.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream ==null) {
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    buffer.append(line+"\n");
                }
                if (buffer.length() == 0) {
                    return null;
                }
                toursJsonStr = buffer.toString();
            } catch (IOException e) {
                Log.e("FetchToursTask","IOException - error fetching JSON from database");
                return null;
            }
            finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e("FetchToursTask", "IOException - error closing tours BufferedReader stream", e);
                    }
                }
            }

            try {
                return getToursDataFromJson(toursJsonStr);
            } catch (JSONException e) {
                Log.e("FetchToursTask","JSONException - error converting json");
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(ArrayList<TourListItem> result) {
            tours = result;
            tourAdapter.addAll(result);
            if (tours.isEmpty()) {
                Log.v("test tours","tours list empty");
            }
            for (TourListItem tour:tours) {
                Log.v("test tours","Name: "+tour.getName());
            }
        }
    }
}