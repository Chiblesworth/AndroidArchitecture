package com.example.androidarchitecturedemo;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface NoteDao {
    // SQL code is automatically generated for CRUD operations
    @Insert
    void insert(Note note);

    @Update
    void update(Note note);

    @Delete
    void delete(Note note);

    @Query("DELETE FROM note_table")
    void deleteAllNotes();

    // If rows in the table do not match what is needed for the Note class error will occur
    @Query("SELECT * FROM note_table ORDER BY priority DESC")
    LiveData<List<Note>> getAllNotes();
}


/*
    Data Access Objects -- DAO need to be either interface
    or abstract classes because we don't provide the method body
    we just create a method and annotate it and Room does the rest
    of the work.
 */
