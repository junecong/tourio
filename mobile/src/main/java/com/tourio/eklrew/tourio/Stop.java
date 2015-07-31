package com.tourio.eklrew.tourio;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by Prud on 7/25/2015.
 */
public class Stop /*implements Parcelable*/ {
    private int id;
    private String name;
    private String description;
    private LatLng location;


    public Stop(int id,String name,String description,double latitude,double longitude) {
        this.id = id;
        this.name = name;
        this.description = description;
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
