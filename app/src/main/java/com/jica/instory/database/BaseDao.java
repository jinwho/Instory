package com.jica.instory.database;

import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Update;

public interface BaseDao<DataType> {

    //삽입
    @Insert
    void insertAll(DataType... data);

    //업데이트
    @Update
    void updateAll(DataType... data);

    //삭제
    @Delete
    void deleteAll(DataType... data);
}
