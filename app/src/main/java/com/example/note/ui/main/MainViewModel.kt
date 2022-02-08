package com.example.note.ui.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.note.utils.Resource
import com.example.note.data.local.room.entities.Note
import com.example.note.data.repository.MainRepository
import com.example.note.ui.base.BaseViewModel
import kotlinx.coroutines.launch
import org.koin.core.component.KoinApiExtension

@KoinApiExtension
class MainViewModel(private val repo: MainRepository) : BaseViewModel<MainNavigator>() {


    var notes = MutableLiveData<Resource<List<Note>>>()

    init {
        getNotes()
    }


    fun getNotes() {
        notes.postValue(Resource.loading(null))
        viewModelScope.launch {
            try {
                val note = repo.getNotes()
                if (note.isNullOrEmpty()) {
                    Resource.error("list is null or empty", null)
                } else
                    notes.postValue(Resource.success(note))
            } catch (e: Exception) {
                notes.postValue(Resource.error(e.message.toString(), null))
            }
        }
    }

    fun deleteNote(note: Note) {
        viewModelScope.launch {
            repo.deleteNote(note)
        }
    }

    fun openAddNotePage() {
        getNavigator()?.openAddNotePage()
    }

    fun onDeleteBtnClick(){
        getNavigator()?.onDeleteBtnClick()
    }

}