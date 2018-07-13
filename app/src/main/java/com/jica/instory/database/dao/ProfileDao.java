package com.jica.instory.database.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.jica.instory.database.entity.Profile;
import com.jica.instory.database.entity.ProfileMinimal;

import java.util.List;

@Dao
public interface ProfileDao{
    //ID -> 프로필
    @Query("SELECT * FROM profile WHERE pid IS :pid")
    Profile get(Integer pid);

    // 그룹에 포함되는 프로필 목록
    @Query("SELECT * FROM profile WHERE bid IS :bid")
    List<Profile> groupBy(Integer bid);

    //모든 프로필
    @Query("SELECT * FROM profile")
    List<Profile> getAll();

    // 최소한의 프로필 정보
    // id, 레이팅, 이름, 한줄평
    @Query("SELECT pid, rating, name, comment,filename FROM profile")
    List<ProfileMinimal> getAllMinimal();

    //삽입
    @Insert
    void insert(Profile data);

    //업데이트
    @Update
    void update(Profile data);

    //삭제
    @Delete
    void delete(Profile data);

}
