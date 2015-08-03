package com.tourio.eklrew.tourio;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by Prud on 7/25/2015.
 */
public class Stop /*implements Parcelable*/ {
    private int id;
    private String name, description, picUrl;
    private LatLng location;
    private int categoryIndex;
    final String[] categories = TourioHelper.StopCategoriesHelper.STOP_CATEGORIES;

    public Stop(String name,String description,String picUrl, double latitude,double longitude) {
        this.name = name;
        this.description = description;
        this.picUrl = picUrl;
        this.location = new LatLng(latitude,longitude);
    }

    public Stop(int id,String name,String description,String picUrl, double latitude,double longitude) {
        this(name,description,picUrl,latitude,longitude);
        this.id = id;
    }

    public Stop(int id,String name,String description,String picUrl, double latitude,double longitude,int categoryIndex) {
        this(id,name,description,picUrl,latitude,longitude);
        this.categoryIndex = categoryIndex;
    }

    public Stop(String name,String description,String picUrl, double latitude,double longitude,int categoryIndex) {
        this(name,description,picUrl,latitude,longitude);
        this.categoryIndex = categoryIndex;
    }

    public Stop(int id,String name,String description,String picUrl,double latitude,double longitude,String category) throws IllegalArgumentException {
        this(id,name,description,picUrl,latitude,longitude);
        Integer index = TourioHelper.StopCategoriesHelper.STOP_CATEGORY_NAME_TO_INDEX_MAP.get(category);
        if (index == null) {
            throw new IllegalArgumentException(category+" is not a valid category for a stop.");
        }
        else {
            categoryIndex = index;
        }
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
    public String getPicUrl() {
        return picUrl;
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
