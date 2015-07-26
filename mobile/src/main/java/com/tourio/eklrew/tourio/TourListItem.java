package com.tourio.eklrew.tourio;

import java.util.ArrayList;

/**
 * Created by hanumal on 7/24/2015.
 */
public class TourListItem {
    private int tourId;
    private String name, city;
    private double duration, rating;
    private ArrayList<Stop> stops;

    public TourListItem(int tourId, String name, String city, double duration,
                        double rating, ArrayList<Stop> stops) {
        this.tourId = tourId;
        this.name = name;
        this.city = city;
        this.duration = duration;
        this.rating = rating;
        this.stops = stops;
    }

    public int getTourId() {
        return tourId;
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
}
