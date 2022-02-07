package com.example.note.ui.main

import com.example.note.data.local.room.entities.Note

class NoteItemViewModel(private val note: Note) {


    fun getTitle(): String = note.title

    fun getContent(): String = note.content

    fun getDate(): String = note.date

}