package com.tourio.eklrew.tourio;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.location.LocationManager;
import android.widget.Button;

import java.util.ArrayList;
import java.util.GregorianCalendar;

/**
 * Created by Prud on 7/29/2015.
 */
public class TourHelper {
    private static User prud = new User(1,"Prud L",null);
    private static User june = new User(3,"June Cong",null);
    private static User shawn = new User(2,"Shawn Huang",null);
    private static User aim = new User(5,"Aim P",null);

    public static ArrayList<Stop> hardCodedStops() {
        ArrayList<Stop> stops = new ArrayList<Stop>();
        stops.add(new Stop(1,"Indian Rock Park","a big rock",37.892537, -122.272594,"Scenic"));
        stops.add(new Stop(2,"Ici Ice Cream","hippie ice cream place",37.857598, -122.253266,"Eating"));
        stops.add(new Stop(3,"Sather Tower","biggest watchtower in the world",37.872320, -122.257791,"Scenic"));

        return stops;
    }

    public static TourListItem hardCodedTourListItem() {
        return new TourListItem(1,"Fun day in Berkeley","Berkeley",2,4,hardCodedStops());
    }

    public static Tour hardCodedTour() {
        return new Tour(1,"Fun day in Berkeley","Just a fun time in a college town","Berkeley",
                2, 4, prud, hardCodedStops(), hardCodedComments());
    }

    public static ArrayList<Comment> hardCodedComments() {
        ArrayList<Comment> comments = new  ArrayList<Comment>();

        comments.add(new Comment(june,"great tour!",4,new GregorianCalendar(2015,7,29,2,47)));
        comments.add(new Comment(shawn,"it was ok",3,new GregorianCalendar(2015,7,28,5,20)));
        comments.add(new Comment(aim,"best tour ever",5,new GregorianCalendar(2015,6,14,10,1)));

        return comments;
    }

    public static void swapColors(Button b1,Button b2) {
        int color1 = ((ColorDrawable) b1.getBackground()).getColor();
        int color2 = ((ColorDrawable) b2.getBackground()).getColor();
        b1.setBackgroundColor(color2);
        b2.setBackgroundColor(color1);
    }
}