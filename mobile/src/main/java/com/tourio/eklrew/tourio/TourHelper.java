package com.tourio.eklrew.tourio;

import java.util.ArrayList;

/**
 * Created by Prud on 7/29/2015.
 */
public class TourHelper {
    public static ArrayList<Stop> hardCodedStops() {
        ArrayList<Stop> stops = new ArrayList<Stop>();
        stops.add(new Stop(1,"Indian Rock Park","a big rock",37.892537, -122.272594));
        stops.add(new Stop(2,"Ici Ice Cream","hippie ice cream place",37.857598, -122.253266));
        stops.add(new Stop(3,"Sather Tower","biggest watchtower in the world",37.872320, -122.257791));

        return stops;
    }

    public static TourListItem hardCodedTour() {
        return new TourListItem(1,"Fun day in Berkeley","Berkeley",2,4,hardCodedStops());
    }
}