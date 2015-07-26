package com.tourio.eklrew.tourio;

import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Created by Prud on 7/25/2015.
 */
public class Comment {

    private int commenterId; //id of the user who made this comment
    private String commenterName;
    private String comment;
    private int rating;
    private GregorianCalendar time;

    public Comment(int commenterId,String commenterName,String comment,int rating, GregorianCalendar time) {
        this.commenterId = commenterId;
        this.commenterName=commenterName;
        this.comment=comment;
        this.rating=rating;
        this.time=time;
    }

    public int getCommenterId() {
        return commenterId;
    }

    public String getCommenterName() {
        return commenterName;
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
