package com.tourio.eklrew.tourio;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.location.LocationManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.Arrays;
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
        return new TourListItem(1,"Fun day in Berkeley",2,4,new LatLng[] {
                    new LatLng(37.892537, -122.272594),
                    new LatLng(37.857598, -122.253266),
                    new LatLng(37.872320, -122.257791)});
    }

    public static ArrayList<TourListItem> randomTourListItems() {
        LatLng[] coordinateList = randomCoordinateList();
        ArrayList<TourListItem> tours = new ArrayList<TourListItem>();
        int index = 0;
        for (int i=0;i<100;i+=4) {
            index++;
            tours.add(new TourListItem(index,"Tour "+index,Math.random()*8,Math.random()*5,
                    Arrays.copyOfRange(coordinateList, i, i+4)));
        }
        return tours;
    }

    public static LatLng[] randomCoordinateList() {
        LatLng[] coordinateList = new LatLng[100];
        for (int i=0;i<100;i++) {
            coordinateList[i] = new LatLng(Math.random()+36,Math.random()-123);
        }
        return coordinateList;
    }

    public static Tour hardCodedTour() {
        return new Tour(1,"Fun day in Berkeley","Just a fun time in a college town","Berkeley",
                2, 4, prud, hardCodedStops(), hardCodedComments());
    }

    public static ArrayList<Comment> hardCodedComments() {
        ArrayList<Comment> comments = new  ArrayList<Comment>();

        comments.add(new Comment(june,"great tour!",4,new GregorianCalendar(2015,7,29,2,47)));
        comments.add(new Comment(shawn, "it was ok", 3, new GregorianCalendar(2015, 7, 28, 5, 20)));
        comments.add(new Comment(aim, "best tour ever", 5, new GregorianCalendar(2015, 6, 14, 10, 1)));

        return comments;
    }

    public static void swapColors(Button b1,Button b2) {
        int color1 = ((ColorDrawable) b1.getBackground()).getColor();
        int color2 = ((ColorDrawable) b2.getBackground()).getColor();
        b1.setBackgroundColor(color2);
        b2.setBackgroundColor(color1);
    }

    public static void setRatingImage(Context context,FrameLayout ratingFrame,int rating) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        ratingFrame.addView(inflater.inflate(R.layout.rating, null));
        if (rating<5) {
            ImageView star5 = (ImageView) ratingFrame.findViewById(R.id.star5);
            star5.setVisibility(ImageView.INVISIBLE);
        }
        if (rating<4) {
            ImageView star4 = (ImageView) ratingFrame.findViewById(R.id.star4);
            star4.setVisibility(ImageView.INVISIBLE);
        }
        if (rating<3) {
            ImageView star3 = (ImageView) ratingFrame.findViewById(R.id.star3);
            star3.setVisibility(ImageView.INVISIBLE);
        }
        if (rating<2) {
            ImageView star2 = (ImageView) ratingFrame.findViewById(R.id.star2);
            star2.setVisibility(ImageView.INVISIBLE);
        }
        if (rating<1) {
            ImageView star1 = (ImageView) ratingFrame.findViewById(R.id.star1);
            star1.setVisibility(ImageView.INVISIBLE);
        }
        Log.v("rating",""+rating);
    }
}