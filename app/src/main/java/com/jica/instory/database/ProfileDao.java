package com.jica.instory.database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface ProfileDao extends BaseDao<Profile>{
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
    @Query("SELECT pid, rating, name, comment FROM profile")
    List<ProfileMinimal> getAllMinimal();

}
