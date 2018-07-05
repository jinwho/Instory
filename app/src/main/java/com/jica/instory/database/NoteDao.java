package com.jica.instory.database;

import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

public interface NoteDao {
    //ID -> 프로필
    @Query("SELECT * FROM note WHERE nid IS :nid")
    Note get(Integer nid);

    //모든 프로필을 리턴한다.
    @Query("SELECT * FROM note")
    List<Note> getAll();

    //삽입
    @Insert
    void insertAll(Note... notes);

    //업데이트
    @Update
    void updateAll(Note... notes);

    //삭제
    @Delete
    void deleteAll(Note... notes);
}
