package com.jica.instory.database;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity
public class Group {
    @PrimaryKey(autoGenerate = true)
    private int id;

    private String name;
}
