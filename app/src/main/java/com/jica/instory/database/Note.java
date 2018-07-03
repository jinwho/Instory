
package com.jica.instory.database;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.Nullable;

@Entity
public class Note {
    //----------유니크한 숫자 정보
    @PrimaryKey(autoGenerate = true)
    private int id;

    @Nullable
    String Title = "";

    String Content;
    //Bitmap picture;
    int profileID;
    //every note have owner ID
    //if profile deleted than note should be deleted too
}

