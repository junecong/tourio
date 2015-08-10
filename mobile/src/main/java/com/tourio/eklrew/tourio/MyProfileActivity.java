package com.tourio.eklrew.tourio;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ListView;

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

public class MyProfileActivity extends NavigationBarActivity {

    private ListView tourListView;
    private ArrayList<TourListItem> tours;
    private ProfileTourListAdapter tourAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        contentFrame.addView((getLayoutInflater()).inflate(R.layout.activity_my_profile, null));

        tourListView = (ListView) findViewById(R.id.profile_tour_list);
        View header = getLayoutInflater().inflate(R.layout.profile_list_header, null);
        tourListView.addHeaderView(header);
        tourAdapter = new ProfileTourListAdapter(this,
                new ArrayList<TourListItem>());
        tourListView.setAdapter(tourAdapter);

        FrameLayout ratingFrame = (FrameLayout) findViewById(R.id.rating_frame);

        (new FetchToursTask()).execute(TourioHelper.CityHelper.CITY_NAME_TO_ID_MAP.get("San Francisco"));

        TourioHelper.LayoutHelper.setRatingImage(getLayoutInflater(),ratingFrame, 4);
    }

    /*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_profile, menu);
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
                String[] stopPicListFromJson = new String[numStops];
                for (int j=0;j<numStops;j++) {
                    JSONObject stopJsonObject = stopsJsonArray.getJSONObject(j);
                    stopListFromJson[j] = new LatLng(
                            stopJsonObject.getDouble(TourioHelper.TourListJsonHelper.JSON_STOP_LATITUDE),
                            stopJsonObject.getDouble(TourioHelper.TourListJsonHelper.JSON_STOP_LONGITUDE)
                    );
                    stopPicListFromJson[j] = stopJsonObject.getString(TourioHelper.TourListJsonHelper.JSON_STOP_PIC_URL);
                }

                //tourListFromJson.add(new TourListItem(tourId,tourName,tourDuration,tourRating,stopListFromJson));
                tourListFromJson.add(new TourListItem(tourId,tourName,tourDuration,tourRating,stopListFromJson,stopPicListFromJson));
            }

            return tourListFromJson;
        }

        @Override
        protected ArrayList<TourListItem> doInBackground(Integer... city) {
            String toursUrlString = TourioHelper.DataBaseUrlHelper.BASE_CITY_URL + city[0];
            Log.v("URL", toursUrlString);
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
            tours = new ArrayList<TourListItem>(result.subList(0, 4));
            Collections.sort(tours, new TourListItem.RatingComparator());
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
