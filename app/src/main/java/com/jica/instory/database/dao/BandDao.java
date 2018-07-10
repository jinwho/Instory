package com.jica.instory.database.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.jica.instory.database.entity.Band;
import com.jica.instory.database.entity.Note;

import java.util.List;

@Dao
public interface BandDao{
    //ID -> 그룹
    @Query("SELECT * FROM band WHERE bid IS :bid")
    Band get(Integer bid);

    //모든 그룹
    @Query("SELECT * FROM band")
    List<Band> getAll();

    //삽입
    @Insert
    void insert(Band data);

    //업데이트
    @Update
    void update(Band data);

    //삭제
    @Delete
    void delete(Band data);
}
