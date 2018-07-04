package com.jica.instory.database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface ProfileDao {
    @Query("SELECT * FROM profile")
    List<Profile> getAll();

    @Query("SELECT * FROM profile WHERE id IN (:userIds)")
    List<Profile> getByIds(Integer[] userIds);

    @Query("SELECT * FROM profile WHERE id IS :userId")
    Profile getById(Integer userId);

    @Insert
    void insertAll(Profile... profiles);

    @Delete
    void deleteAll(Profile... profile);
}
