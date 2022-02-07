package com.example.note.ui.dialog

import com.example.note.ui.base.BaseViewModel
import org.koin.core.component.KoinApiExtension

@KoinApiExtension
class NoteDialogViewModel : BaseViewModel<NoteDialogNavigator>() {

    fun ok() = getNavigator()?.ok()

    fun cancel() = getNavigator()?.cancel()

}