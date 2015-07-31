package com.tourio.eklrew.tourio;

import java.util.ArrayList;

/**
 * Created by Prud on 7/24/2015.
 */
public class Tour extends TourListItem {
    private String description;
    private User creator;
    private ArrayList<Comment> comments;
    private int currStopIndex;

    /*
    //Constructor for list item
    public Tour(int id, String name, String city, double duration,
                        double rating, ArrayList<Stop> stops) {
        super(id,name,city,duration,rating,stops);
    }
    */

    //Constructor for detailed tour
    public Tour(int id, String name, String description, String city, double duration,double rating,
                User creator, ArrayList<Stop> stops,ArrayList<Comment> comments) {
        super(id,name,city,duration,rating,stops);

        this.description = description;
        this.creator = creator;
        this.comments = comments;
        currStopIndex = 0;
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

    public int getCurrStopIndex() {
        return currStopIndex;
    }

    public Stop getCurrentStop() {
        return super.getStops().get(currStopIndex);
    }

    public String getCreatorName() {
        return creator.getName();
    }

    public String getCreatorPicUrl() {
        return creator.getPicUrl();
    }
}