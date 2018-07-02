package com.jica.instory.database;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;


@Entity
public class Profile {
    @PrimaryKey(autoGenerate = true)
    private int id;

    private float rating;
    private String name;
    private String comment;

    // add later
    /*
    private String number;
    private String email;
    private String address;
    private String birthday;
    */

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
