package com.example.note.ui.add

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.mvvmproject.utils.Resource
import com.example.note.data.local.room.entities.Note
import com.example.note.data.repository.AddNoteRepository
import com.example.note.ui.base.BaseViewModel
import kotlinx.coroutines.launch
import org.koin.core.component.KoinApiExtension

@KoinApiExtension
class AddNoteViewModel(private val repo: AddNoteRepository) : BaseViewModel<AddNoteNavigator>() {

    var saveNoteResult = MutableLiveData<Resource<Any>>()
    var updateNoteResult = MutableLiveData<Resource<Any>>()


    fun saveNote(note: Note) {
        saveNoteResult.postValue(Resource.loading(null))
        viewModelScope.launch {
            try {
                repo.saveNote(note)
                saveNoteResult.postValue(Resource.success(null))
            } catch (e: Exception) {
                saveNoteResult.postValue(Resource.error(e.message.toString(), null))
            }
        }
    }


    fun updateNote(note: Note) {
        updateNoteResult.postValue(Resource.loading(null))
        viewModelScope.launch {
            try {
                repo.updateNote(note)
                updateNoteResult.postValue(Resource.success(null))
            } catch (e: Exception) {
                updateNoteResult.postValue(Resource.error(e.message.toString(), null))
            }
        }
    }


    fun onBack(){
        getNavigator()?.onBack()
    }

    fun onSave(){
        getNavigator()?.onSave()
    }

}