package com.tourio.eklrew.tourio;

import java.util.GregorianCalendar;

/**
 * Created by Prud on 7/25/2015.
 */
public class Comment {

    private User commenter;
    private String text;
    private int rating;
    private GregorianCalendar time;

    public Comment(User commenter,String text,int rating, GregorianCalendar time) {
        this.commenter = commenter;
        this.text = text;
        this.rating=rating;
        this.time=time;
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
