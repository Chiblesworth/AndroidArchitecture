package com.example.androidarchitecturedemo;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "note_table") // Creates necessary code to create an SQLite Object
public class Note {
    @PrimaryKey(autoGenerate = true) // Sets PK and makes it increment by 1 each new Note added.
    private int id;
    private String title;
    private String description;
    private int priority;

    public Note(String title, String description, int priority) {
        this.title = title;
        this.description = description;
        this.priority = priority;
    }

    public void setId(int id) {
        this.id = id; // Used in the case of editing a Note
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public int getPriority() {
        return priority;
    }
}
