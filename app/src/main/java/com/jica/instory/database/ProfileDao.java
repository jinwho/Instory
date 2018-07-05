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
    //ID -> 프로필
    @Query("SELECT * FROM profile WHERE pid IS :pid")
    Profile get(Integer pid);

    // 그룹의 id를 가지는 프로필 목록을 가져 온다
    @Query("SELECT * FROM profile WHERE bid IS :bid")
    List<Profile> gets(Integer bid);

    //모든 프로필을 리턴한다.
    @Query("SELECT * FROM profile")
    List<Profile> getAll();

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
