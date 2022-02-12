package com.example.note.ui.main

import android.net.Uri
import android.view.View
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.example.note.data.local.room.entities.Note

class NoteItemViewModel(private val note: Note) {

    fun getTitle(): String = note.title

    fun getContent(): String = note.content

    fun getDate(): String = note.date.substring(0, 19)

    fun getNoteImage(): String = note.image ?: ""

    fun imageVisibility(): Int = if (note.image != null) View.VISIBLE else View.GONE

}

@BindingAdapter("loadImage")
fun loadImage(view: ImageView, imageUri: String?) {
    Glide
        .with(view.context)
        .load(Uri.parse(imageUri))
        .into(view)
}