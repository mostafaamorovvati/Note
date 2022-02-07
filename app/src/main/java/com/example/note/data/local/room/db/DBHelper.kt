package com.example.note.data.local.room.db

import com.example.note.data.local.room.entities.Note

class DBHelper(private val db: NoteDataBase) : IDBHelper {

    override suspend fun saveNote(note: Note) = db.noteDao().saveNote(note)

}