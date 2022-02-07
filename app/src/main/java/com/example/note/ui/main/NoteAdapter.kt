package com.example.note.ui.main

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.note.data.local.room.entities.Note
import com.example.note.databinding.NoteItemLayoutBinding
import com.example.note.ui.base.BaseViewHolder

class NoteAdapter : RecyclerView.Adapter<BaseViewHolder>() {

    var notes: MutableList<Note> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        return MainViewHolder(
            NoteItemLayoutBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) = holder.onBind(position)

    override fun getItemCount() = notes.size

    @SuppressLint("NotifyDataSetChanged")
    fun setData(items: MutableList<Note>) {
        notes = items
        notifyDataSetChanged()
    }


    inner class MainViewHolder(private val mBinding: NoteItemLayoutBinding) :
        BaseViewHolder(mBinding.root) {
        override fun onBind(position: Int) {
            val itemViewModel = NoteItemViewModel(notes[position])
            mBinding.noteItemViewModel = itemViewModel
            mBinding.executePendingBindings()
        }

    }
}