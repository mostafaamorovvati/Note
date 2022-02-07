package com.example.note.di

import android.app.Application
import androidx.room.Room
import com.example.note.data.local.room.db.NoteDataBase
import com.example.note.utils.DATABASE_NAME
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module

val dataBaseModule = module {

    fun provideDatabase(application: Application): NoteDataBase {
        return Room.databaseBuilder(application, NoteDataBase::class.java, DATABASE_NAME)
            .fallbackToDestructiveMigration()
            .build()
    }

    single { provideDatabase(androidApplication()) }


}