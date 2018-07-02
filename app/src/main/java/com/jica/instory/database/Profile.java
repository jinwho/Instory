package com.jica.instory.database;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;


@Entity
public class Profile {
    @PrimaryKey(autoGenerate = true)
    private int id;

    private int rating;
    private String name;
    private String comment;

    // add later
    /*
    private String number;
    private String email;
    private String address; // 주소를 입력하는 쉬운 방법은 없음..
    private String birthday;
    */

    /*
    group id를 가져야 함
    */

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
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
