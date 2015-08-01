package com.tourio.eklrew.tourio;

import android.location.Location;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.Comparator;

/**
 * Created by Prud on 7/24/2015.
 */
public class TourListItem {
    private int id;
    private String name, city;
    private double duration, rating;
    private ArrayList<Stop> stops;

    public TourListItem(int id, String name, String city, double duration,
                        double rating, ArrayList<Stop> stops) {
        this.id = id;
        this.name = name;
        this.city = city;
        this.duration = duration;
        this.rating = rating;
        this.stops = stops;
    }

    public int getTourId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getCity() {
        return city;
    }

    public double getDuration() {
        return duration;
    }

    public double getRating() {
        return rating;
    }

    public ArrayList<Stop> getStops() {
        return stops;
    }


    public static class DistanceComparator implements Comparator<TourListItem> {
        Location currentLocation;

        public DistanceComparator(Location currentLocation) {
            this.currentLocation = currentLocation;
        }

        @Override
        public int compare(TourListItem a,TourListItem b) {
            LatLng locA = a.getStops().get(0).getLocation();
            LatLng locB = b.getStops().get(0).getLocation();
            float[] resultsA = new float[1];
            float[] resultsB = new float[1];
            Location.distanceBetween(currentLocation.getLatitude(),currentLocation.getLongitude(),locA.latitude,locA.longitude,resultsA);
            Location.distanceBetween(currentLocation.getLatitude(),currentLocation.getLongitude(),locB.latitude,locB.longitude,resultsB);
            double distA = resultsA[0];
            double distB = resultsB[0];
            return Double.compare(distA,distB);
        }
    }

    public static class RatingComparator implements Comparator<TourListItem> {
        @Override
        public int compare(TourListItem a,TourListItem b) {
            return Double.compare(a.getRating(),b.getRating());
        }
    }

    public static class DurationComparator implements Comparator<TourListItem> {
        @Override
        public int compare(TourListItem a,TourListItem b) {
            return Double.compare(a.getRating(),b.getRating());
        }
    }

}
