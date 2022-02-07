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
class AddViewModel(private val repo: AddNoteRepository) : BaseViewModel<AddNavigator>() {

    var saveNoteResult = MutableLiveData<Resource<Any>>()

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

}