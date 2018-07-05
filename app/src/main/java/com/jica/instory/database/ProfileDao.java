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

    //여러개의 ID를 입력받아 프로필 목록을 리턴
    @Query("SELECT * FROM profile WHERE id IN (:Ids)")
    List<Profile> getByIds(Integer[] Ids);

    //ID를 입력받아 프로필을 리턴
    @Query("SELECT * FROM profile WHERE id IS :Id")
    Profile getById(Integer Id);

    //삽입하기
    @Insert
    void insertAll(Profile... profiles);

    //업데이트
    @Update
    void updateAll(Profile... profiles);

    //삭제하기
    @Delete
    void deleteAll(Profile... profiles);
}
