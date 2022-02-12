package com.example.note.ui.addNote

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.note.data.local.room.entities.Note
import com.example.note.data.repository.AddNoteRepository
import com.example.note.ui.base.BaseViewModel
import com.example.note.utils.log
import kotlinx.coroutines.launch
import org.koin.core.component.KoinApiExtension

@KoinApiExtension
class AddNoteViewModel(private val repo: AddNoteRepository) : BaseViewModel<AddNoteNavigator>() {


    fun saveNote(note: Note) {
        viewModelScope.launch {
            try {
                repo.saveNote(note)
            } catch (e: Exception) {
                log(e.toString())
            }
        }
    }

    fun updateNote(note: Note) {
        viewModelScope.launch {
            try {
                repo.updateNote(note)
            } catch (e: Exception) {
                log(e.toString())
            }
        }
    }


    fun onBack() = getNavigator()?.onBack()

    fun onSave() = getNavigator()?.onSave()

    fun openGallery() = getNavigator()?.openGallery()

    fun removeImage() = getNavigator()?.removeImage()


}