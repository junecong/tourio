package com.tourio.eklrew.tourio;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import com.google.android.gms.maps.model.LatLng;

import java.io.InputStream;
import java.util.Comparator;

/**
 * Created by Prud on 7/24/2015.
 */
public class TourListItem extends TourioListItem{
    private int id;
    private String name;
    private double duration, rating;
    private LatLng[] stops;
    private String[] stopImageUrls;
    private int currImageIndex;
    private ImageView imageView;

    private TourListItem(int id, String name, double duration,
                        double rating) {
        super();
        this.id = id;
        this.name = name;
        this.duration = duration;
        this.rating = rating;
    }

    public TourListItem(int id, String name, double duration,
                        double rating, LatLng[] stops) {
        this(id,name,duration,rating);
        this.stops = stops;
    }

    public TourListItem(int id, String name, double duration,
                        double rating, String[] stopImageUrls) {
        this(id,name,duration,rating);
        this.stopImageUrls = stopImageUrls;
        this.currImageIndex = 0;
        setPicUrl(stopImageUrls[currImageIndex]);
    }

    public TourListItem(int id, String name, double duration,
                        double rating, LatLng[] stops,String[] stopImageUrls) {
        this(id,name,duration,rating,stopImageUrls);
        this.stops = stops;
    }

    public int getTourId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double getDuration() {
        return duration;
    }

    public double getRating() {
        return rating;
    }

    public LatLng[] getStops() {
        return stops;
    }

    public void setImageView(ImageView imageView) {
        this.imageView = imageView;
    }

    public ImageView getImageView() {
        return imageView;
    }

    public void changeImage (Context context, ImageView imageView, boolean nextOrPrev) {
        currImageIndex += (nextOrPrev ? 1 : -1);
        if (currImageIndex == -1) {
            currImageIndex = stopImageUrls.length -1;
        }
        if (currImageIndex == stopImageUrls.length) {
            currImageIndex = 0;
        }

        setPicUrl(stopImageUrls[currImageIndex]);
        loadImage(imageView);
    }

    public static class DistanceComparator implements Comparator<TourListItem> {
        Location currentLocation;

        public DistanceComparator(Location currentLocation) {
            this.currentLocation = currentLocation;
        }

        @Override
        public int compare(TourListItem a,TourListItem b) {
            if (a.getStops().length==0 || b.getStops().length==0) {
                return 0;
            }

            LatLng locA = a.getStops()[0];
            LatLng locB = b.getStops()[0];
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
            return Double.compare(b.getRating(),a.getRating());
        }
    }

    public static class DurationComparator implements Comparator<TourListItem> {
        @Override
        public int compare(TourListItem a,TourListItem b) {
            return Double.compare(a.getDuration(),b.getDuration());
        }
    }
}