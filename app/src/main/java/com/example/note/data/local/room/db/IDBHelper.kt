package com.example.note.data.local.room.db

import com.example.note.data.local.room.entities.Note

interface IDBHelper {

    suspend fun saveNote(note: Note)

    suspend fun getNotes(): List<Note>

    suspend fun updateNote(note: Note)

    suspend fun deleteNote(note: Note)


}