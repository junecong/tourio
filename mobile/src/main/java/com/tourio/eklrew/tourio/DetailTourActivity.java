package com.tourio.eklrew.tourio;

import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by Prud on 7/24/2015.
 */
public class DetailTourActivity extends NavigationBarActivity implements GoogleMap.OnMapClickListener {

    private int tourId;

    private GoogleMap map;
    private boolean mapExpanded = false;
    private int mapFragmentHeight;
    private Tour tour;
    private FrameLayout detailsFrame, ratingFrame;
    private StopListAdapter stopAdapter;
    private CommentListAdapter commentAdapter;
    private boolean onStops = true; //true if stops being shown, false if comments being shown
    Button stopsButton,commentsButton;
    LinearLayout tourDescriptionLayout;

    ImageView creatorImageView;
    TextView tourTitleView,tourDescriptionView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        contentFrame.addView((getLayoutInflater()).inflate(R.layout.activity_detail_tour, null));
        initViews();
        tourId = getIntent().getExtras().getInt("tour_id");
        (new FetchTourTask()).execute(); //gets tour from database and sets UI elements to it
    }

    public void initViews() {
        detailsFrame = (FrameLayout) findViewById(R.id.details_frame);
        ratingFrame = (FrameLayout) findViewById(R.id.rating_frame);
        tourDescriptionLayout = (LinearLayout) findViewById(R.id.tour_description_layout);

        stopsButton = (Button) findViewById(R.id.stops_button);
        commentsButton = (Button) findViewById(R.id.comments_button);

        creatorImageView = (ImageView) findViewById(R.id.user);
        tourTitleView = (TextView) findViewById(R.id.title);
        tourDescriptionView = (TextView) findViewById(R.id.tour_description);
    }

    public void setViews() {
        (new DownloadImageTask(creatorImageView)).execute(tour.getCreator().getPicUrl());
        showStops(null);
        TourioHelper.LayoutHelper.setRatingImage(getLayoutInflater(),
                ratingFrame, (int) (Math.round(tour.getRating())));

        tourTitleView.setText(tour.getName());
        tourDescriptionView.setText(tour.getDescription());
    }

    public void setMapFragment() {
        ArrayList<Stop> stops = tour.getStops();
        int numStops = stops.size();

        map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map))
                .getMap();

        Marker[] markers = new Marker[numStops];
        LatLng[] locations = new LatLng[numStops];
        for (int i=0;i<numStops;i++) {
            locations[i] = stops.get(i).getLocation();
            markers[i] = map.addMarker(new MarkerOptions().position(locations[i])
                    .title(stops.get(i).getName())
            );
        }

        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        for (Marker marker : markers) {
            builder.include(marker.getPosition());
        }

        LatLngBounds bounds = builder.build();

        Polyline line = map.addPolyline(new PolylineOptions()
                .add(locations)
                .width(5));

        int padding = 50; // offset from edges of the map in pixels
        final CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);

        map.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
            @Override
            public void onMapLoaded() {
                map.moveCamera(cu);
            }
        });
        map.setOnMapClickListener(this);
    }

    @Override
    public void onMapClick(LatLng point) {
        if (!mapExpanded) {
            mapExpanded = true;
            LinearLayout linearLayout = (LinearLayout) findViewById(R.id.hide_this);
            linearLayout.setVisibility(LinearLayout.GONE);
            LinearLayout linearLayout2 = (LinearLayout) findViewById(R.id.hide_this2);
            linearLayout2.setVisibility(LinearLayout.GONE);
            MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
            View view = mapFragment.getView();
            mapFragmentHeight = view.getHeight();
            LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(-1,-1);
            view.setLayoutParams(p);
            view.requestLayout();
            map.moveCamera(CameraUpdateFactory.zoomIn());
        }
    }

    @Override
    public void onBackPressed() {
        if (!mapExpanded) {
            super.onBackPressed();
        }
        else {
            mapExpanded = false;
            LinearLayout linearLayout = (LinearLayout) findViewById(R.id.hide_this);
            linearLayout.setVisibility(LinearLayout.VISIBLE);
            LinearLayout linearLayout2 = (LinearLayout) findViewById(R.id.hide_this2);
            linearLayout2.setVisibility(LinearLayout.VISIBLE);
            MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
            View view = mapFragment.getView();
            LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(-1,mapFragmentHeight);
            view.setLayoutParams(p);
            view.requestLayout();
            map.moveCamera(CameraUpdateFactory.zoomOut());
        }
    }

    public void startGPS(View view) {
        LatLng firstStopLocation = tour.getStops().get(0).getLocation();
        double latitude = firstStopLocation.latitude;
        double longitude = firstStopLocation.longitude;
        String uri = "google.navigation:q=" + String.valueOf(latitude) + "," + String.valueOf(longitude);
        Intent mapsIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
        mapsIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(mapsIntent);

        Intent startIntent = new Intent(this, NavigationListener.class);
        startService(startIntent);
    }

    private void swapButtons() {
        TourioHelper.LayoutHelper.swapColors(stopsButton, commentsButton);
    }

    public void showStops(View view) {
        if (!onStops) {
            swapButtons();
            onStops = true;
        }
        tourDescriptionLayout.setVisibility(LinearLayout.VISIBLE);

        detailsFrame.removeAllViews();
        detailsFrame.addView((getLayoutInflater()).inflate(R.layout.detail_activity_list, null));
        ListView stopListView = (ListView) findViewById(R.id.details_list);
        stopAdapter = new StopListAdapter(this, tour.getStops());
        stopListView.setAdapter(stopAdapter);

        stopListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Stop stop = (Stop) stopAdapter.getItem(position);
                showDetailStop(stop);
            }
        });
    }

    public void showDetailStop(Stop stop) {
        tourDescriptionLayout.setVisibility(LinearLayout.GONE);

        detailsFrame.removeAllViews();
        detailsFrame.addView((getLayoutInflater()).inflate(R.layout.detail_stop, null));
        TextView stopNameView = (TextView) findViewById(R.id.stop_name);
        TextView stopDescriptionView = (TextView) findViewById(R.id.stop_description);
        ImageView stopImageView = (ImageView) findViewById(R.id.stop_image);
        (new DownloadImageTask(stopImageView)).execute(stop.getPicUrl());
        stopNameView.setText(stop.getName());
        stopDescriptionView.setText(stop.getDescription());
    }

    public void showDetailComment(Comment comment) {
        detailsFrame.removeAllViews();
        detailsFrame.addView((getLayoutInflater()).inflate(R.layout.detail_comment, null));
        TextView commenterNameView = (TextView) findViewById(R.id.commenter_name);
        TextView commentTextView = (TextView) findViewById(R.id.comment_text);
        ImageView stopImageView = (ImageView) findViewById(R.id.commenter_image);
        (new DownloadImageTask(stopImageView)).execute(comment.getCommenter().getPicUrl());
        FrameLayout detailCommentRatingFrame = (FrameLayout) findViewById(R.id.detail_comment_rating_frame);
        commenterNameView.setText(comment.getCommenter().getName());
        commentTextView.setText(comment.getText());
        TourioHelper.LayoutHelper.setRatingImage(getLayoutInflater(), detailCommentRatingFrame, (int) (Math.round(comment.getRating())));
    }

    public void showComments(View view) {
        if (onStops) {
            swapButtons();
            onStops = false;
        }
        tourDescriptionLayout.setVisibility(LinearLayout.GONE);

        detailsFrame.removeAllViews();
        detailsFrame.addView((getLayoutInflater()).inflate(R.layout.detail_activity_list, null));
        ListView commentListView = (ListView) findViewById(R.id.details_list);
        commentAdapter = new CommentListAdapter(this, tour.getComments());
        commentListView.setAdapter(commentAdapter);

        commentListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Comment comment = (Comment) commentAdapter.getItem(position);
                showDetailComment(comment);
            }
        });
    }

    public class FetchTourTask extends AsyncTask<Void,Void,Tour> {
        private Tour getTourDataFromJson(String json) throws JSONException {

            JSONArray tourJsonArray = new JSONArray(json);

            JSONObject tourJsonObject = tourJsonArray.getJSONObject(0);
            int tourRating = tourJsonObject.getInt(TourioHelper.DetailTourJsonHelper.JSON_TOUR_RATING);
            String tourName = tourJsonObject.getString(TourioHelper.DetailTourJsonHelper.JSON_TOUR_NAME);
            String tourDescription = tourJsonObject.getString(TourioHelper.DetailTourJsonHelper.JSON_TOUR_DESCRIPTION);
            String tourCity = TourioHelper.CityHelper.CITY_ID_TO_NAME_MAP.get((
                    tourJsonObject.getInt(TourioHelper.DetailTourJsonHelper.JSON_TOUR_CITY_ID)));
            int tourCreatorId = tourJsonObject.getInt(TourioHelper.DetailTourJsonHelper.JSON_TOUR_CREATOR_ID);
            int tourDuration = tourJsonObject.getInt(TourioHelper.DetailTourJsonHelper.JSON_TOUR_DURATION);

            JSONObject creatorJsonObject = tourJsonArray.getJSONArray(1).getJSONObject(0);
            String creatorName = creatorJsonObject.getString(TourioHelper.DetailTourJsonHelper.JSON_CREATOR_NAME);
            String creatorPicUrl = creatorJsonObject.getString(TourioHelper.DetailTourJsonHelper.JSON_CREATOR_PIC_URL);
            User creator = new User(tourCreatorId,creatorName,creatorPicUrl);

            JSONArray stopsJsonArray = tourJsonArray.getJSONArray(2);
            ArrayList<Stop> stopListFromJson = new ArrayList<Stop>();
            for (int j=0;j<stopsJsonArray.length();j++) {
                JSONObject stopJsonObject = stopsJsonArray.getJSONObject(j);
                stopListFromJson.add( new Stop(
                        stopJsonObject.getString(TourioHelper.DetailTourJsonHelper.JSON_STOP_NAME),
                        stopJsonObject.getString(TourioHelper.DetailTourJsonHelper.JSON_STOP_DESCRIPTION),
                        stopJsonObject.getString(TourioHelper.DetailTourJsonHelper.JSON_STOP_PIC_URL),
                        stopJsonObject.getDouble(TourioHelper.DetailTourJsonHelper.JSON_STOP_LATITUDE),
                        stopJsonObject.getDouble(TourioHelper.DetailTourJsonHelper.JSON_STOP_LONGITUDE),
                        stopJsonObject.getInt(TourioHelper.DetailTourJsonHelper.JSON_STOP_CATEGORY_INDEX)
                ));
            }

            ArrayList<Comment> commentListFromJson = new ArrayList<Comment>();
            //TODO: get comments list

            return new Tour(tourId, tourName, tourDescription, tourCity, tourDuration,tourRating,
            creator, stopListFromJson,commentListFromJson);
        }

        @Override
        protected Tour doInBackground(Void... params) {
            String toursUrlString = TourioHelper.DataBaseUrlHelper.BASE_TOUR_URL + tourId;
            Log.v("URL", toursUrlString);
            URL tourUrl;
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            String tourJsonStr = null;
            try {
                tourUrl = new URL(toursUrlString);
                urlConnection = (HttpURLConnection) tourUrl.openConnection();
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
                tourJsonStr = buffer.toString();
            } catch (IOException e) {
                Log.e("FetchTourTask","IOException - error fetching JSON from database");
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
                        Log.e("FetchTourTask", "IOException - error closing tours BufferedReader stream", e);
                    }
                }
            }

            try {
                return getTourDataFromJson(tourJsonStr);
            } catch (JSONException e) {
                Log.e("FetchTourTask","JSONException - error converting json");
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Tour result) {
            tour = result;
            setMapFragment();
            setViews();
        }
    }

}
