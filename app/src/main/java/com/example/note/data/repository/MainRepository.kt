package com.example.note.data.repository

import com.example.note.data.local.room.db.IDBHelper
import com.example.note.data.local.room.entities.Note

class MainRepository(private val db: IDBHelper) {

    suspend fun saveNote(note: Note) = db.saveNote(note)

    suspend fun getNotes() = db.getNotes()

}