package com.jica.instory.database;

import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

public interface BandDao {
    //ID -> 프로필
    @Query("SELECT * FROM band WHERE bid IS :bid")
    Band get(Integer bid);

    //모든 프로필을 리턴한다.
    @Query("SELECT * FROM note")
    List<Band> getAll();

    //삽입
    @Insert
    void insertAll(Band... bands);

    //업데이트
    @Update
    void updateAll(Band... bands);

    //삭제
    @Delete
    void deleteAll(Band... bands);
}
