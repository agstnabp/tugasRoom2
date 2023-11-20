package com.example.tugasroom2.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room.databaseBuilder
import androidx.room.RoomDatabase

@Database(entities = [Note::class], version = 1, exportSchema = false)
abstract class NoteRoomDatabase : RoomDatabase() {
    abstract fun noteDao(): NoteDao?
    //memberikan akses ke objek NoteDao untuk melakukan operasi database terkait entitas Note.

    companion object {
        @Volatile
        private var INSTANCE: NoteRoomDatabase? = null
        fun getDatabase(context: Context): NoteRoomDatabase? {
            //membuat dan mengembalikan instance dari NoteRoomDatabase.
            if (INSTANCE == null) {
                synchronized(NoteRoomDatabase::class.java) {
                    //dibuat menggunakan databaseBuilder. Nama database menjadi "note_database".
                    // jika INSTANCE ada, maka instance yang ada akan digunakan.
                    INSTANCE = databaseBuilder(
                        context.applicationContext,
                        NoteRoomDatabase::class.java, "note_database"
                    )
                        .build()
                }
            }
            return INSTANCE
        }
    }
}