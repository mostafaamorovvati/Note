package com.example.note.data.local.room.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.note.data.local.room.dao.NoteDao
import com.example.note.data.local.room.entities.Note


@Database(
    entities =
    [Note::class],
    version = 1
)
abstract class NoteDataBase: RoomDatabase() {

    abstract fun noteDao(): NoteDao

}