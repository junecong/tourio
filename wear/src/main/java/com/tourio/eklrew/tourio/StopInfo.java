package com.tourio.eklrew.tourio;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Prud on 8/3/2015.
 */
public class StopInfo implements Parcelable {
    private String name;
    private String description;
    private int status;

    public StopInfo(String name,String description,int status) {
        this.name = name;
        this.description = description;
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }
    public boolean isFirstStop() {
        return status==1;
    }
    public boolean isLastStop() {
        return status==2;
    }

    public static final Parcelable.Creator<StopInfo> CREATOR = new Parcelable.Creator<StopInfo>() {
        public StopInfo createFromParcel(Parcel in) {
            return new StopInfo(in);
        }

        public StopInfo[] newArray(int size) {
            return new StopInfo[size];
        }
    };

    private StopInfo(Parcel in) {
        name = in.readString();
        description = in.readString();
        status = in.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out,int flags) {
        out.writeString(name);
        out.writeString(description);
        out.writeInt(status);
    }
}