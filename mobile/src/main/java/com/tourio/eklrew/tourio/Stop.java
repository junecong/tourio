package com.tourio.eklrew.tourio;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.android.gms.maps.model.LatLng;

import java.util.Dictionary;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Prud on 7/25/2015.
 */
public class Stop /*implements Parcelable*/ {
    private int id;
    private String name, description, picUrl;
    private LatLng location;
    private int categoryIndex;
    private final String[] categories = {"Scenic","Eating","Drinking","Activity"};

    public Stop(int id,String name,String description,String picUrl, double latitude,double longitude,int categoryIndex) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.picUrl = picUrl;
        this.location = new LatLng(latitude,longitude);
        this.categoryIndex = categoryIndex;
    }

    public Stop(int id,String name,String description,String picUrl,double latitude,double longitude,String category) throws IllegalArgumentException {
        for (int i=0;i<categories.length;i++) {
            if (categories[i].equals(category)) {
                categoryIndex = i;
            }
        }
        if (category==null) {
            throw new IllegalArgumentException("Category not valid");
        }
        this.id = id;
        this.name = name;
        this.description = description;
        this.picUrl = picUrl;
        this.location = new LatLng(latitude,longitude);
    }

    public String getName() {
        return name;
    }
    public double getLatitude() {
        return location.latitude;
    }
    public double getLongitude() {
        return location.longitude;
    }
    public String getDescription() {
        return description;
    }
    public int getId() {
        return id;
    }
    public LatLng getLocation() {
        return location;
    }
    public String getCategory() {return categories[categoryIndex];}

    /*
    public static final Parcelable.Creator<Stop> CREATOR = new Parcelable.Creator<Stop>() {
        public Stop createFromParcel(Parcel in) {
            return new Stop(in);
        }

        public Stop[] newArray(int size) {
            return new Stop[size];
        }
    };

    private Stop(Parcel in) {
        name = in.readString();
        description = in.readString();
        latitude = in.readDouble();
        longitude = in.readDouble();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out,int flags) {
        out.writeString(name);
        out.writeString(description);
        out.writeDouble(latitude);
        out.writeDouble(longitude);
    }
    */
}
