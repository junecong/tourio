package com.tourio.eklrew.tourio;

import android.app.Activity;
import android.app.Service;
import android.content.Intent;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.wearable.Wearable;

/**
 * Created by Shawn on 8/1/2015.
 */
public class LocationService extends Service implements
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {

    private GoogleApiClient mGoogleApiClient;
    private Location mCurrentLocation;
    private LocationRequest mLocationRequest;

    @Override
    public int onStartCommand(Intent emptyIntent, int f, int id) {
        Log.d("Log", ">>>Initiated service for tracking user's location<<<");

        buildGoogleApiClient();

        return START_STICKY;
    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnected(Bundle bundle) {
        Log.d("Log", ">>>LocationService GoogleApiClient connected<<<");

        mCurrentLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);
        createLocationRequest();
        startLocationUpdates();
    }

    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(10000);
        mLocationRequest.setFastestInterval(5000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    protected void startLocationUpdates() {
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
        // This is a hardcoded destination for testing purpose.
        Location destination = new Location("The Exploratorium");
        destination.setLatitude(37.801089);
        destination.setLongitude(-122.398624);

        // Latitude:  37.8    37.8    37.8    37.8    37.8   37.8011
        // Longitude: -122.44 -122.43 -122.42 -122.41 -122.4 -122.3986

        mCurrentLocation = location;

        Log.d("Current latitude:", String.valueOf(mCurrentLocation.getLatitude()));
        Log.d("Current longitude:", String.valueOf(mCurrentLocation.getLongitude()));
        Log.d("Current distance:", String.valueOf(mCurrentLocation.distanceTo(destination)));

        if (mCurrentLocation.distanceTo(destination) < 10) {
            Log.d("Log", ">>>Arrival detected<<<");

            Intent arrivalServiceIntent = new Intent(this, ArrivalService.class);
            startService(arrivalServiceIntent);

            stopLocationUpdates();
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
