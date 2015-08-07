package com.tourio.eklrew.tourio;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import java.io.InputStream;
import java.util.GregorianCalendar;

/**
 * Created by Prud on 7/25/2015.
 */
public class Comment extends TourioListItem{

    private User commenter;
    private String text;
    private int rating;
    private GregorianCalendar time;

    public Comment(User commenter,String text,int rating, GregorianCalendar time) {
        this(commenter,text,rating);
        this.time=time;
    }

    public Comment(User commenter,String text,int rating) {
        super();
        this.commenter = commenter;
        this.text = text;
        this.rating=rating;
        setPicUrl(commenter.getPicUrl());
    }

    public User getCommenter() {
        return commenter;
    }

    public String getText() {
        return text;
    }

    public int getRating() {
        return rating;
    }

    public GregorianCalendar getTime() {
        return time;
    }
}
