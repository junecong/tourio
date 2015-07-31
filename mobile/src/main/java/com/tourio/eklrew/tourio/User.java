package com.tourio.eklrew.tourio;

/**
 * Created by Prud on 7/29/2015.
 */
public class User {
    private int id;
    private String name;
    private String picUrl;

    public User(int id, String name, String picUrl) {
        this.id = id;
        this.name = name;
        this.picUrl = picUrl;
    }

    public String getName() {
        return name;
    }

    public String getPicUrl() {
        return picUrl;
    }

    public int getId() {
        return id;
    }
}
