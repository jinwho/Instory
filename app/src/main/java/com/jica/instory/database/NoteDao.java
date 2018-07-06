package com.jica.instory.database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface NoteDao extends BaseDao<Note> {
    //ID -> 노트
    @Query("SELECT * FROM note WHERE nid IS :nid")
    Note get(Integer nid);

    //모든 노트
    @Query("SELECT * FROM note")
    List<Note> getAll();
}
