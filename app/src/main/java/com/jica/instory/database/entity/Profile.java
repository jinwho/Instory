package com.jica.instory.database.entity;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;

import java.util.Calendar;
import java.util.Date;

import static android.arch.persistence.room.ForeignKey.CASCADE;
import static android.arch.persistence.room.ForeignKey.NO_ACTION;
import static android.arch.persistence.room.ForeignKey.SET_NULL;


@Entity(foreignKeys = @ForeignKey(entity = Band.class,parentColumns = "bid",childColumns = "bid",onDelete = SET_NULL, onUpdate = CASCADE),
        indices = {@Index("bid")})
public class Profile {
    @PrimaryKey(autoGenerate = true)
    private Integer pid;
    //해당 하는 그룹의 ID
    private Integer bid;

    //레이팅,이름,한줄평
    private int rating;
    private String name;
    private String comment;

    //번호,이메일,주소
    private String phone;
    private String email;
    private String address;

    // 생일
    private int year;
    private int month;
    private int day;

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    //프로필 사진 파일 이름
    private String filename;


    //getters, setters
    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public Integer getPid() {
        return pid;
    }

    public void setPid(Integer pid) {
        this.pid = pid;
    }

    public Integer getBid() {
        return bid;
    }

    public void setBid(Integer gid) {
        this.bid = gid;
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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
