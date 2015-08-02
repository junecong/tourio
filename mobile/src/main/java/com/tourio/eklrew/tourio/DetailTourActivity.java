package com.tourio.eklrew.tourio;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.FrameLayout;
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

import java.util.ArrayList;

/**
 * Created by Prud on 7/24/2015.
 */
public class DetailTourActivity extends NavigationBarActivity implements GoogleMap.OnMapClickListener {

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        contentFrame.addView((getLayoutInflater()).inflate(R.layout.activity_detail_tour, null));

        tour = TourHelper.hardCodedTour();

        initVars();
        setMapFragment();
        showStops(null);

        TourHelper.setRatingImage(getLayoutInflater(),ratingFrame,(int) (Math.round(tour.getRating())));
    }

    public void initVars() {
        detailsFrame = (FrameLayout) findViewById(R.id.details_frame);
        ratingFrame = (FrameLayout) findViewById(R.id.rating_frame);
        tourDescriptionLayout = (LinearLayout) findViewById(R.id.tour_description_layout);

        stopsButton = (Button) findViewById(R.id.stops_button);
        commentsButton = (Button) findViewById(R.id.comments_button);
    }

    private Tour getTourFromDatabase() {
        int tourId = getIntent().getExtras().getInt("tour_id");

        return null;
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
        TourHelper.swapColors(stopsButton, commentsButton);
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
        stopNameView.setText(stop.getName());
        stopDescriptionView.setText(stop.getDescription());
    }

    public void showDetailComment(Comment comment) {
        detailsFrame.removeAllViews();
        detailsFrame.addView((getLayoutInflater()).inflate(R.layout.detail_comment, null));
        TextView commenterNameView = (TextView) findViewById(R.id.commenter_name);
        TextView commentTextView = (TextView) findViewById(R.id.comment_text);
        FrameLayout detailCommentRatingFrame = (FrameLayout) findViewById(R.id.detail_comment_rating_frame);
        commenterNameView.setText(comment.getCommenter().getName());
        commentTextView.setText(comment.getText());
        TourHelper.setRatingImage(getLayoutInflater(), detailCommentRatingFrame, (int) (Math.round(comment.getRating())));
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
}
