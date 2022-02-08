package com.example.note.ui.main

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.note.data.local.room.entities.Note
import com.example.note.databinding.NoteItemLayoutBinding
import com.example.note.ui.base.BaseViewHolder
import com.example.note.utils.gone
import com.example.note.utils.visible

class NoteAdapter : RecyclerView.Adapter<BaseViewHolder>() {

    var notes: MutableList<Note> = mutableListOf()
    var mListener: OnItemClickListener? = null

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

    inner class MainViewHolder(private val mBinding: NoteItemLayoutBinding) :
        BaseViewHolder(mBinding.root) {
        @SuppressLint("NotifyDataSetChanged")
        override fun onBind(position: Int) {
            val itemViewModel = NoteItemViewModel(notes[position])
            mBinding.noteItemViewModel = itemViewModel
            mBinding.executePendingBindings()

            if (notes[position].isSelected)
                mBinding.ivSelected.visible()
            else
                mBinding.ivSelected.gone()


            mBinding.root.setOnClickListener {
                mListener?.onItemClick(notes[position], position)
                notifyDataSetChanged()
            }

            mBinding.root.setOnLongClickListener {
                notes[position].isSelected = true
                mListener?.onItemLongClick(notes[position])
                notifyDataSetChanged()
                true
            }
        }
    }

    fun getNotesList() = notes.toMutableList()

    fun getSelectedItem() = notes.filter { it.isSelected }.toMutableList()

    @SuppressLint("NotifyDataSetChanged")
    fun deleteSelectedList(note: Note) {
        notes.remove(note)
        notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setData(items: MutableList<Note>) {
        notes = items
        notifyDataSetChanged()
    }

    interface OnItemClickListener {
        fun onItemClick(note: Note, position: Int)
        fun onItemLongClick(note: Note)
    }
}