package com.tourio.eklrew.tourio;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by hanumal on 7/24/2015.
 */
public class Tour {
    private int tourId, guideId;
    private String tourName, guideName, tourDescription, city;
    private double duration, rating;
    private ArrayList<Stop> stops;
    private ArrayList<Comment> comments;
    private int currStopIndex;


    public Tour(int tourId, int guideId, String tourName, String guideName, String tourDescription,
                String city, double duration, double rating, ArrayList<Stop> stops,
                ArrayList<Comment> comments) {
        this.tourId = tourId;
        this.guideId = guideId;
        this.tourName = tourName;
        this.guideName = guideName;
        this.tourDescription = tourDescription;
        this.city = city;
        this.duration = duration;
        this.rating = rating;
        this.stops = stops;
        this.comments = comments;

        currStopIndex = 0;
    }

    public int getTourId() {
        return tourId;
    }

    public int getGuideId() {
        return guideId;
    }

    public String getTourName() {
        return tourName;
    }

    public String getGuideName() {
        return guideName;
    }

    public String getTourDescription() {
        return tourDescription;
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

    public ArrayList<Comment> getComments() {
        return comments;
    }

    public int getCurrStopIndex() {
        return currStopIndex;
    }

    public Stop getCurrentStop() {
        return stops.get(currStopIndex);
    }
}