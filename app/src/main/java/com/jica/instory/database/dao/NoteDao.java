package com.jica.instory.database.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.jica.instory.database.entity.Note;
import com.jica.instory.database.entity.Profile;

import java.util.List;

@Dao
public interface NoteDao{
    //ID -> 노트
    @Query("SELECT * FROM note WHERE nid IS :nid")
    Note get(Integer nid);

    //모든 노트
    @Query("SELECT * FROM note")
    List<Note> getAll();

    //특정 프로필 아이디를 가진 모든 노트
    @Query("SELECT * FROM note WHERE pid IS :pid")
    List<Note> ownBy(Integer pid);

    //삽입
    @Insert
    void insertAll(Note... data);

    //업데이트
    @Update
    void updateAll(Note... data);

    //삭제
    @Delete
    void deleteAll(Note... data);
}
