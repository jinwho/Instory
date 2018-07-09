package com.jica.instory.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.jica.instory.database.dao.BandDao;
import com.jica.instory.database.dao.NoteDao;
import com.jica.instory.database.dao.ProfileDao;
import com.jica.instory.database.entity.Band;
import com.jica.instory.database.entity.Note;
import com.jica.instory.database.entity.Profile;

@Database(entities = {Band.class,Profile.class,Note.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    private static AppDatabase INSTANCE;
    private static final String DB_NAME = "AppDB.db";

    public abstract BandDao bandDao();
    public abstract ProfileDao profileDao();
    public abstract NoteDao noteDao();

    public static AppDatabase getInstance(Context context){
        if(INSTANCE == null){
            INSTANCE = buildDatabase(context);
        }
        return INSTANCE;
    }

    private static AppDatabase buildDatabase(final Context context) {

        return Room.databaseBuilder(context,AppDatabase.class,DB_NAME).allowMainThreadQueries().build();
    }
}