
package com.jica.instory.database;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;

/*@Entity(foreignKeys = @ForeignKey(entity = Profile.class,
        parentColumns = "id",
        childColumns = "profileID"))*/
public class Note {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private int profileID;

    private String title;
    private String content;

}

