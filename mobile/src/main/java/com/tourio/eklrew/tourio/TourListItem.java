package com.tourio.eklrew.tourio;

/**
 * Created by hanumal on 7/24/2015.
 */
public class TourListItem {
    private String location;
    private String name;
    private double rating;

    public TourListItem(String location,String name,double rating) {
        this.location=location;
        this.name=name;
        this.rating=rating;
    }

    public String getLocation() {return location;}
    public String getName() {return name;}
    public double getRating() {return rating;}
}
