
package com.jica.instory.database;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.Nullable;

@Entity
public class Note {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private int profileID;

    private String title;
    private String content;

}

