package com.tourio.eklrew.tourio;

import java.util.ArrayList;

/**
 * Created by Prud on 7/24/2015.
 */
public class Tour {
    private int id;
    private String name, description, city;
    private double duration, rating;
    private User creator;
    private ArrayList<Stop> stops;
    private ArrayList<Comment> comments;

    private int nextStopIndex;

    public Tour(int id, String name, String description, String city, double duration,double rating,
                User creator, ArrayList<Stop> stops,ArrayList<Comment> comments) {

        this.id = id;
        this.name = name;
        this.description = description;
        this.city = city;
        this.duration = duration;
        this.rating = rating;
        this.creator = creator;
        this.stops = stops;
        this.comments = comments;

        nextStopIndex = 0;
    }

    public int getId() {
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

    public String getDescription() {
        return description;
    }

    public User getCreator() {
        return creator;
    }

    public ArrayList<Comment> getComments() {
        return comments;
    }

    public int getNextStopIndex() {
        return nextStopIndex;
    }

    public Stop getNextStop() {
        return getStops().get(nextStopIndex);
    }

    public String getCreatorName() {
        return creator.getName();
    }

    public String getCreatorPicUrl() {
        return creator.getPicUrl();
    }

    public Stop goToNextStop() {
        Stop stop = getNextStop();
        nextStopIndex++;
        return stop;
    }
    public boolean isFirstStop() {
        return nextStopIndex ==1;
    }
    public boolean isLastStop() {
        return nextStopIndex ==stops.size();
    }
}