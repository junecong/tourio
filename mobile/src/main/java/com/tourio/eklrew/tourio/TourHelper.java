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
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Prud on 7/29/2015.
 */
public class TourHelper {
    //base url for database access
    public static final String BASE_DATABASE_URL = "http://52.27.112.77:5000";
    //BASE_CITY_URL + id gives the list of tours in city with given city id
    public static final String BASE_CITY_URL = BASE_DATABASE_URL + "/city/";
    //BASE_TOUR_URL + id gives the info for tour with given tour id
    public static final String BASE_TOUR_URL = BASE_DATABASE_URL + "/tour/";
    //BASE_COMMENTS_URL + id gives the list of comments with given tour id
    public static final String BASE_COMMENTS_URL = BASE_DATABASE_URL + "/comments/";
    //BASE_USER_URL + id gives the info for user with given id
    public static final String BASE_USER_URL = BASE_DATABASE_URL + "/user/";

    public static final String GOOGLE_MAPS_API_KEY = "AIzaSyBYIzHjtA9e_PnCUlVXPTD0WQ6nvJKnPbE";
    public static final String BASE_STATIC_MAPS_API_URL = "https://maps.googleapis.com/maps/api/staticmap?" +
            "size=400x200&" +
            "key=" + GOOGLE_MAPS_API_KEY;


    //Hard coded city list
    public static final String[] cities = {"San Francisco","Chicago"};

    //Hard coded dictionary of city to its id in database
    public static final Map<String,Integer> CITY_NAME_TO_ID_MAP;
    static
    {
        CITY_NAME_TO_ID_MAP = new HashMap<String,Integer>();
        CITY_NAME_TO_ID_MAP.put("San Francisco",1);
        CITY_NAME_TO_ID_MAP.put("Chicago",3);
    }

    //generated map of city to its index in list
    public static final Map<String,Integer> CITY_NAME_TO_INDEX_MAP;
    static {
        CITY_NAME_TO_INDEX_MAP = new HashMap<String,Integer>();
            for (int i=0;i<cities.length;i++) {
                CITY_NAME_TO_INDEX_MAP.put(cities[i], i);
            }
    }

    //generated map of city index to city id
    public static final Map<Integer,Integer> CITY_INDEX_TO_ID_MAP;
    static {
        CITY_INDEX_TO_ID_MAP = new HashMap<Integer,Integer>();
        for (int i=0;i<cities.length;i++) {
            CITY_INDEX_TO_ID_MAP.put(i,CITY_NAME_TO_ID_MAP.get(cities[i]));
        }
    }

    private static User prud = new User(1,"Prud L",null);
    private static User june = new User(3,"June Cong",null);
    private static User shawn = new User(2,"Shawn Huang",null);
    private static User aim = new User(5,"Aim P",null);

    private static void setMaps() {

    }

    public static char randomChar() {
        int rnd = (int) (Math.random() * 52); // or use Random or whatever
        char base = (rnd < 26) ? 'A' : 'a';
        return (char) (base + rnd % 26);
    }

    public static String randomString() {
        String s = "";
        for (int i=0;i<100+Math.random()*200;i++) {
            s+=randomChar();
        }
        return s;
    }

    public static ArrayList<Stop> hardCodedStops() {
        ArrayList<Stop> stops = new ArrayList<Stop>();
        stops.add(new Stop(1,"Indian Rock Park",randomString(),"",37.892537, -122.272594,"Scenic"));
        stops.add(new Stop(2,"Ici Ice Cream",randomString(),"",37.857598, -122.253266,"Eating"));
        stops.add(new Stop(3,"Sather Tower",randomString(),"",37.872320, -122.257791,"Scenic"));

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
        ArrayList<Comment> comments = new ArrayList<Comment>();

        comments.add(new Comment(june, "great tour!",4,new GregorianCalendar(2015, 7, 29, 2, 47)));
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

    public static void setRatingImage(LayoutInflater inflater,FrameLayout ratingFrame,int rating) {
        ratingFrame.removeAllViews();
        ratingFrame.addView(inflater.inflate(R.layout.rating, null));
        ImageView[] stars = {
                (ImageView) ratingFrame.findViewById(R.id.star1),
                (ImageView) ratingFrame.findViewById(R.id.star2),
                (ImageView) ratingFrame.findViewById(R.id.star3),
                (ImageView) ratingFrame.findViewById(R.id.star4),
                (ImageView) ratingFrame.findViewById(R.id.star5)
        };

        for (int i=0;i<rating;i++) { stars[i].setVisibility(ImageView.VISIBLE);}
        for (int i=rating;i<5;i++) { stars[i].setVisibility(ImageView.GONE);}
        Log.v("rating",""+rating);
    }
}