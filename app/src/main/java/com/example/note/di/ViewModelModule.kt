package com.example.note.di

import com.example.note.ui.add.AddNoteViewModel
import com.example.note.ui.dialog.NoteDialogViewModel
import com.example.note.ui.main.MainViewModel
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.core.component.KoinApiExtension
import org.koin.dsl.module

@OptIn(KoinApiExtension::class)

val viewModelModule = module {

   viewModel {
       MainViewModel(get())
   }

    viewModel {
        AddNoteViewModel(get())
    }

    viewModel {
        NoteDialogViewModel()
    }

}