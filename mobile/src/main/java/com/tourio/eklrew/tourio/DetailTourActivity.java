package com.tourio.eklrew.tourio;

import android.content.Intent;
import android.location.Location;
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

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
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
import com.google.android.gms.wearable.MessageApi;
import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.Wearable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Prud on 7/24/2015.
 */
public class DetailTourActivity extends NavigationBarActivity
        implements GoogleMap.OnMapClickListener, MessageApi.MessageListener,
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {

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
    TextView tourTitleView,tourDescriptionView,creatorNameView;

    private GoogleApiClient mGoogleApiClient;
    private Location mCurrentLocation;
    private LocationRequest mLocationRequest;

    Stop currStop;

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
        creatorNameView = (TextView) findViewById(R.id.creator_name);

        stopsButton = (Button) findViewById(R.id.stops_button);
        commentsButton = (Button) findViewById(R.id.comments_button);

        creatorImageView = (ImageView) findViewById(R.id.user);
        tourTitleView = (TextView) findViewById(R.id.title);
        tourDescriptionView = (TextView) findViewById(R.id.tour_description);
    }

    public void setViews() {
        (new DownloadImageTask(creatorImageView)).execute(tour.getCreator().getPicUrl());
        TourioHelper.LayoutHelper.setRatingImage(getLayoutInflater(),
                ratingFrame, (int) (Math.round(tour.getRating())));

        tourTitleView.setText(tour.getName());
        tourDescriptionView.setText(tour.getDescription());
        creatorNameView.setText(" " + tour.getCreatorName());
    }

    public void setMapFragment() {
        map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map))
                .getMap();

        ArrayList<Stop> stops = tour.getStops();
        int numStops = stops.size();
        LatLng[] locations = new LatLng[numStops];
        for (int i=0;i<numStops;i++) {
            locations[i] = stops.get(i).getLocation();
        }

        (new FetchPathTask()).execute(locations);
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
        LinearLayout tourDetailLayout = (LinearLayout) findViewById(R.id.tour_detail_layout);
        LinearLayout hideWhenStart = (LinearLayout) findViewById(R.id.hide_when_start);
        hideWhenStart.setVisibility(LinearLayout.GONE);
        tourDetailLayout.addView((getLayoutInflater()).inflate(R.layout.activity_transit_mobile, null));

        buildGoogleApiClient();
        Wearable.MessageApi.addListener(mGoogleApiClient, this);
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
        stopImageView.setImageBitmap(stop.getImage());
        stopNameView.setText(stop.getName());
        stopDescriptionView.setText(stop.getDescription());
    }

    public void showDetailComment(Comment comment) {
        detailsFrame.removeAllViews();
        detailsFrame.addView((getLayoutInflater()).inflate(R.layout.detail_comment, null));
        TextView commenterNameView = (TextView) findViewById(R.id.commenter_name);
        TextView commentTextView = (TextView) findViewById(R.id.comment_text);
        ImageView commentImageView = (ImageView) findViewById(R.id.commenter_image);
        commentImageView.setImageBitmap(comment.getImage());
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
        commentListView.setAdapter(commentAdapter);

        commentListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Comment comment = (Comment) commentAdapter.getItem(position);
                showDetailComment(comment);
            }
        });
    }

    @Override
    public void onMessageReceived(MessageEvent messageEvent) {
        String messagePath = messageEvent.getPath();
        Log.d("Message received:", ">>>" + messagePath + "<<<");

        /* Starts Google Maps navigation for starting tour. Navigates user from current location
         * to first stop. */
        if (messagePath.equals(TourioHelper.ReceiveMessageHelper.START_GPS)) {
            startGPS();
        }
        if (messagePath.equals(TourioHelper.ReceiveMessageHelper.SKIP_STOP)) {
            if (tour.isDone()) {
                stopLocationUpdates();
                sendNextStopMessage(0);
            }
            else {
                currStop = tour.goToNextStop();
                sendNextStopMessage(1);
            }
        }
    }

    private void sendNextStopMessage(int messageType) {
        int currStopStatus = 0;
        if (tour.isFirstStop()) {
            currStopStatus = 1;
        }
        if (tour.isLastStop()) {
            currStopStatus = 2;
        }
        Intent sendMessageIntent = new Intent(this,NextStopMessageService.class);
        sendMessageIntent.putExtra("message_type",messageType);
        sendMessageIntent.putExtra("stop_info",currStop.getName()+"|"+currStop.getDescription()+"|"+currStopStatus);
        startService(sendMessageIntent);
    }

    private void startGPS() {
        Uri gmmIntentUri = Uri.parse("google.navigation:q=" +
                currStop.getLatitude() + "," +
                currStop.getLongitude());
        Log.v("destination name",currStop.getName());
        Log.v("destination coordinates",currStop.getLatitude()+","+currStop.getLongitude());
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        mapIntent.setPackage("com.google.android.apps.maps");
        mapIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(mapIntent);
    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .addApi(Wearable.API)
                .build();
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnected(Bundle bundle) {
        Log.d("Log", ">>>LocationService GoogleApiClient connected<<<");

        mCurrentLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);
        startLocationUpdates();
    }

    protected void startLocationUpdates() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        LocationServices.FusedLocationApi.requestLocationUpdates(
                mGoogleApiClient, mLocationRequest, this);
    }

    protected void stopLocationUpdates() {
        LocationServices.FusedLocationApi.removeLocationUpdates(
                mGoogleApiClient, this);
    }

    @Override
    public void onConnectionSuspended(int i) {
        // Do nothing
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        // Do nothing
    }

    @Override
    public void onLocationChanged(Location location) {
        Location destination = new Location(currStop.getName());
        destination.setLatitude(currStop.getLatitude());
        destination.setLongitude(currStop.getLongitude());

        mCurrentLocation = location;

        //Log.d("Current latitude:", String.valueOf(mCurrentLocation.getLatitude()));
        //Log.d("Current longitude:", String.valueOf(mCurrentLocation.getLongitude()));
        //Log.d("Current distance:", String.valueOf(mCurrentLocation.distanceTo(destination)));

        if (mCurrentLocation.distanceTo(destination) < 10) {
            //stopLocationUpdates();
            if (tour.isDone()) {
                stopLocationUpdates();
                sendNextStopMessage(0);
            }
            else {
                currStop = tour.goToNextStop();
                sendNextStopMessage(2);
            }
        }
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
            for (int i=3;i<tourJsonArray.length();i+=2) {
                if (tourJsonArray.getJSONArray(i).length() == 0) {
                    continue;
                }
                JSONObject commenterJsonObject = tourJsonArray.getJSONArray(i).getJSONObject(0);
                JSONObject commentJsonObject = tourJsonArray.getJSONObject(i + 1);

                String commenterName = commenterJsonObject.getString(TourioHelper.DetailTourJsonHelper.JSON_COMMENTER_NAME);
                String commenterPicUrl = commenterJsonObject.getString(TourioHelper.DetailTourJsonHelper.JSON_COMMENTER_PIC_URL);

                String commentText = commentJsonObject.getString(TourioHelper.DetailTourJsonHelper.JSON_COMMENT_TEXT);
                int commentRating = commentJsonObject.getInt(TourioHelper.DetailTourJsonHelper.JSON_COMMENT_RATING);
                int commenterId = commentJsonObject.getInt(TourioHelper.DetailTourJsonHelper.JSON_COMMENTER_ID);
                //int commentTime = commentJsonObject.getInt(TourioHelper.DetailTourJsonHelper.JSON_COMMENT_TIME);

                //commentTime = (int) (0x00000000ffffffffL&commentTime);
                //Log.v("comment time",""+commentTime);
                commentListFromJson.add(new Comment(new User(commenterId, commenterName, commenterPicUrl),
                        commentText, commentRating)); //TourioHelper.DetailTourJsonHelper.intToCalendar(commentTime)
            }

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
            stopAdapter = new StopListAdapter(DetailTourActivity.this, tour.getStops());
            showStops(null);
            commentAdapter = new CommentListAdapter(DetailTourActivity.this, tour.getComments());
            currStop = tour.getStops().get(0);
            setMapFragment();
            setViews();
        }
    }

    //sets path in map
    public class FetchPathTask extends AsyncTask<LatLng[],Void,List<LatLng[]>> {
        LatLng[] stops;

        private List<LatLng[]> getPathDataFromJson(String json) throws JSONException {
            List<LatLng[]> route = new ArrayList<LatLng[]>();

            JSONObject JsonDirectionsObject = new JSONObject(json);
            JSONObject JsonRoute = JsonDirectionsObject.getJSONArray("routes").getJSONObject(0);
            JSONArray JsonLegs = JsonRoute.getJSONArray("legs");
            for (int i=0;i<JsonLegs.length();i++) {
                JSONArray JsonSteps = JsonLegs.getJSONObject(i).getJSONArray("steps");
                LatLng[] leg = new LatLng[JsonSteps.length()+1];
                JSONObject JsonLegStartLocation = JsonSteps.getJSONObject(0).getJSONObject("start_location");
                leg[0] = new LatLng(JsonLegStartLocation.getDouble("lat"),JsonLegStartLocation.getDouble("lng"));
                for (int j=0;j<JsonSteps.length();j++) {
                    JSONObject JsonStep = JsonSteps.getJSONObject(j);
                    JSONObject JsonEndLocation = JsonStep.getJSONObject("end_location");
                    double lat = JsonEndLocation.getDouble("lat");
                    double lng = JsonEndLocation.getDouble("lng");
                    leg[j+1] = new LatLng(lat,lng);
                }
                route.add(leg);
            }

            return route;
        }

        @Override
        protected List<LatLng[]> doInBackground(LatLng[]... params) {
            LatLng[] stops = params[0];
            this.stops = stops;
            String directionsUrlString = TourioHelper.GoogleMapsDirectionsApiHelper.BASE_DIRECTIONS_API_URL;
            directionsUrlString += "&origin=" + stops[0].latitude+","+stops[0].longitude;
            directionsUrlString += "&destination=" + stops[stops.length-1].latitude+","+stops[stops.length-1].longitude;
            directionsUrlString += "&waypoints=";
            for (int i=1;i<stops.length-1;i++) {
                directionsUrlString += stops[i].latitude+","+stops[i].longitude+"|";
            }
            //directionsUrlString += "&key="+TourioHelper.GOOGLE_MAPS_API_KEY;
            Log.v("directions URL", directionsUrlString);

            URL directionsUrl;
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            String directionsJsonStr = null;
            try {
                directionsUrl = new URL(directionsUrlString);
                urlConnection = (HttpURLConnection) directionsUrl.openConnection();
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
                directionsJsonStr = buffer.toString();
            } catch (IOException e) {
                Log.e("FetchPathTask","IOException - error fetching JSON from database");
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
                        Log.e("FetchPathTask", "IOException - error closing tours BufferedReader stream", e);
                    }
                }
            }

            try {
                return getPathDataFromJson(directionsJsonStr);
            } catch (JSONException e) {
                Log.e("FetchPathTask","JSONException - error converting json");
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(List<LatLng[]> result) {
            int numStops = stops.length;
            for (int i=0;i<result.size();i++) {
                map.addPolyline(new PolylineOptions()
                        .add(result.get(i))
                        .width(5));
            }

            Marker[] markers = new Marker[numStops];
            for (int i=0;i<numStops;i++) {
                markers[i] = map.addMarker(new MarkerOptions().position(stops[i])
                                .title(tour.getStops().get(i).getName())
                );
            }

            LatLngBounds.Builder builder = new LatLngBounds.Builder();
            for (Marker marker : markers) {
                builder.include(marker.getPosition());
            }

            LatLngBounds bounds = builder.build();

            int padding = 50; // offset from edges of the map in pixels
            final CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);

            map.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
                @Override
                public void onMapLoaded() {
                    map.moveCamera(cu);
                }
            });
            map.setOnMapClickListener(DetailTourActivity.this);
        }
    }
}