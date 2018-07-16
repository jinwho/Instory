
package com.jica.instory.database.entity;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;

import static android.arch.persistence.room.ForeignKey.CASCADE;

@Entity(foreignKeys = @ForeignKey(entity = Profile.class, parentColumns = "pid", childColumns = "pid", onDelete = CASCADE, onUpdate = CASCADE),
        indices = {@Index("pid")})
public class Note {
    @PrimaryKey(autoGenerate = true)
    private Integer nid;
    //노트의 주인 프로필 ID
    private Integer pid; //default 값이 있어야함

    private String date;
    private String content;

    public Integer getNid() {
        return nid;
    }

    public void setNid(Integer nid) {
        this.nid = nid;
    }

    public Integer getPid() {
        return pid;
    }

    public void setPid(Integer pid) {
        this.pid = pid;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}

