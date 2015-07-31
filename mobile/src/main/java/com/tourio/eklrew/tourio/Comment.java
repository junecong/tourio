package com.tourio.eklrew.tourio;

import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Created by Prud on 7/25/2015.
 */
public class Comment {

    private User commenter;
    private String comment;
    private int rating;
    private GregorianCalendar time;

    public Comment(User commenter,String comment,int rating, GregorianCalendar time) {
        this.commenter = commenter;
        this.comment=comment;
        this.rating=rating;
        this.time=time;
    }

    public User getCommenter() {
        return commenter;
    }

    public String getComment() {
        return comment;
    }

    public int getRating() {
        return rating;
    }

    public GregorianCalendar getTime() {
        return time;
    }
}
