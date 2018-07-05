package com.jica.instory.database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface ProfileDao {
    //모든 프로필을 리턴한다.
    @Query("SELECT * FROM profile")
    List<Profile> getAll();

    //여러개의 ID -> 특정 프로필 목록       //그룹으로 바꿔야 할 듯
    @Query("SELECT * FROM profile WHERE pid IN (:pids)")
    List<Profile> getByIds(Integer[] pids);

    //ID -> 프로필
    @Query("SELECT * FROM profile WHERE pid IS :pid")
    Profile getById(Integer pid);

    //삽입
    @Insert
    void insertAll(Profile... profiles);

    //업데이트
    @Update
    void updateAll(Profile... profiles);

    //삭제
    @Delete
    void deleteAll(Profile... profiles);
}
