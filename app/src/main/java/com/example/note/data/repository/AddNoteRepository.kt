package com.example.note.data.repository

import com.example.note.data.local.room.db.IDBHelper
import com.example.note.data.local.room.entities.Note

class AddNoteRepository(private val db: IDBHelper) {

    suspend fun saveNote(note: Note) = db.saveNote(note)

    suspend fun updateNote(note: Note) = db.updateNote(note)

}