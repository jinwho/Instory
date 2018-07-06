package com.jica.instory.database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface BandDao extends BaseDao<Band>{
    //ID -> 그룹
    @Query("SELECT * FROM band WHERE bid IS :bid")
    Band get(Integer bid);

    //모든 그룹
    @Query("SELECT * FROM band")
    List<Band> getAll();
}
