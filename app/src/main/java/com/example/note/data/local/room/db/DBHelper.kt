package com.example.note.data.local.room.db

import com.example.note.data.local.room.entities.Note

class DBHelper(private val db: NoteDataBase) : IDBHelper {

    override suspend fun saveNote(note: Note) = db.noteDao().saveNote(note)

    override suspend fun getNotes() = db.noteDao().getNotes()

    override suspend fun getNoteById(id: Int) = db.noteDao().getNoteById(id)

    override suspend fun updateNote(note: Note) = db.noteDao().updateNote(note)

    override suspend fun deleteNote(note: Note) = db.noteDao().deleteNote(note)


}