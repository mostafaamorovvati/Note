package com.example.note.data.repository

import com.example.note.data.local.room.db.IDBHelper

class MainRepository(private val db: IDBHelper) {

    suspend fun getNotes() = db.getNotes()

}