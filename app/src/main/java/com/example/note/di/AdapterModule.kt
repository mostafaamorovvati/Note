package com.example.note.di

import com.example.note.ui.main.NoteAdapter
import org.koin.dsl.module

val adapterModule = module {
    factory {
        NoteAdapter()
    }
}