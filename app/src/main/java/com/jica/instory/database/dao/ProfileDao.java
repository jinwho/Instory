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

    // 그룹의 id를 가지는 프로필 목록을 가져 온다
    @Query("SELECT * FROM profile WHERE bid IS :bid")
    List<Profile> gets(Integer bid);

    //모든 프로필
    @Query("SELECT * FROM profile")
    List<Profile> getAll();

    // 최소한의 프로필 정보만을 리턴
    // id, 레이팅, 이름, 한줄평
    @Query("SELECT pid, rating, name, comment FROM profile")
    List<ProfileMinimal> getAllMinimal();

    //삽입
    @Insert
    void insertAll(Profile... data);

    //업데이트
    @Update
    void updateAll(Profile... data);

    //삭제
    @Delete
    void deleteAll(Profile... data);

}
