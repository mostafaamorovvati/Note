package com.example.note.data.repository

import com.example.note.data.local.room.db.IDBHelper
import com.example.note.data.local.room.entities.Note

class MainRepository(private val db: IDBHelper) {

    suspend fun getNotes() = db.getNotes()

    suspend fun deleteNote(note: Note) = db.deleteNote(note)

}