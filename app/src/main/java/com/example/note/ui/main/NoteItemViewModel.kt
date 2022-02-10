package com.example.note.ui.main

import android.view.View
import com.example.note.data.local.room.entities.Note

class NoteItemViewModel(
    private val note: Note
) {


    fun getTitle(): String = note.title

    fun getContent(): String = note.content

    fun getDate(): String = note.date.substring(0, 19)

    fun getNoteImage(): String {
        return note.image ?: ""
    }

    fun imageVisibility(): Int {
        return if (note.image != null) View.VISIBLE else View.GONE
    }

}