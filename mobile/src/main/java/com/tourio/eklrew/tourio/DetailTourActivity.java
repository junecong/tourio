package com.tourio.eklrew.tourio;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        int tourId = getIntent().getExtras().getInt("tour_id");

        super.onCreate(savedInstanceState);
        contentFrame.addView((getLayoutInflater()).inflate(R.layout.activity_detail_tour, null));

        tour = TourHelper.hardCodedTour();
        ArrayList<Stop> stops = tour.getStops();
        int numStops = stops.size();

        map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map))
                .getMap();

        Marker[] markers = new Marker[numStops];
        LatLng[] locations = new LatLng[numStops];
        for (int i=0;i<numStops;i++) {
            locations[i] = stops.get(i).getLocation();
            markers[i] = map.addMarker(new MarkerOptions().position(locations[i])
                    .title(stops.get(i).getName()));
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

        showStops(null);
    }

    @Override
    public void onMapClick(LatLng point) {
        if (!mapExpanded) {
            mapExpanded = true;
            LinearLayout linearLayout = (LinearLayout) findViewById(R.id.hide_this);
            linearLayout.setVisibility(LinearLayout.GONE);
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
            MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
            View view = mapFragment.getView();
            LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(-1,mapFragmentHeight);
            view.setLayoutParams(p);
            view.requestLayout();
            map.moveCamera(CameraUpdateFactory.zoomOut());
        }
    }

    public void startGPS(View view) {
//        LatLng firstStopLocation = tour.getStops().get(0).getLocation();
//        double latitude = firstStopLocation.latitude;
//        double longitude = firstStopLocation.longitude;
//        String uri = "google.navigation:q=" + String.valueOf(latitude) + "," + String.valueOf(longitude);
//        Intent mapsIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
//        mapsIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        startActivity(mapsIntent);

        Intent navIntent = new Intent(this, NavigationListener.class);
        startService(navIntent);

        // Shawn will be changing this to MobileConnectActivity
        Intent transitIntent = new Intent(this, MobileTransitActivity.class);
        startActivity(transitIntent);
    }

    public void showStops(View view) {
        ListView stopListView = (ListView) findViewById(R.id.listView);
        stopListView.setAdapter(new StopListAdapter(this, tour.getStops()));
        Button stopsButton = (Button) findViewById(R.id.stops_button);
        Button commentsButton = (Button) findViewById(R.id.comments_button);
        TourHelper.swapColors(stopsButton, commentsButton);
    }

    public void showComments(View view) {
        ListView commentListView = (ListView) findViewById(R.id.listView);
        commentListView.setAdapter(new CommentListAdapter(this, tour.getComments()));
        Button stopsButton = (Button) findViewById(R.id.stops_button);
        Button commentsButton = (Button) findViewById(R.id.comments_button);
        TourHelper.swapColors(stopsButton,commentsButton);
    }

    /*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_detail_tour, menu);
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
