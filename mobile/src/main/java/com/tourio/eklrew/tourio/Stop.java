package com.tourio.eklrew.tourio;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Prud on 7/25/2015.
 */
public class Stop /*implements Parcelable*/ {
    private int stopId;
    private String name;
    private String description;
    private double latitude;
    private double longitude;


    public Stop(int stopId,String name,String description,double latitude,double longitude) {
        this.stopId = stopId;
        this.name = name;
        this.description = description;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getName() {
        return name;
    }
    public double getLatitude() {
        return latitude;
    }
    public double getLongitude() {
        return longitude;
    }
    public String getDescription() {
        return description;
    }
    public int getStopId() {
        return stopId;
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
