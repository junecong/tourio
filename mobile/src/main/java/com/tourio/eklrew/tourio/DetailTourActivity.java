package com.tourio.eklrew.tourio;

import android.app.Activity;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
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
public class DetailTourActivity extends ActionBarActivity implements GoogleMap.OnMapClickListener {

    GoogleMap map;
    boolean mapExpanded = false;
    int mapFragmentHeight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_tour);

        Tour tour = TourHelper.hardCodedTour();
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
            @Override public void onMapLoaded() { map.moveCamera(cu); } });
        map.setOnMapClickListener(this);
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
}
