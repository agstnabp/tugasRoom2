package com.example.tugasroom2.database

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "note_table")
data class Note(
    //deklarasi id
    @PrimaryKey(autoGenerate = true)
    @NonNull
    val id: Int=0,
    //deklarasi title
    @ColumnInfo(name = "title")
    val title: String,
    //deklarasi desc
    @ColumnInfo(name = "description")
    val description: String,
    //deklarasi date
    @ColumnInfo(name = "date")
    val date: String
)