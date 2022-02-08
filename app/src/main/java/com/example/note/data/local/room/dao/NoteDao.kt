package com.example.note.data.local.room.dao

import androidx.room.*
import com.example.note.data.local.room.entities.Note

@Dao
interface NoteDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveNote(note: Note)

    @Query("SELECT * FROM note_table ORDER BY `id` DESC")
    suspend fun getNotes(): List<Note>

    @Delete
    suspend fun deleteNote(note: Note)


}