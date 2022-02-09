package com.example.note.ui.main

import android.annotation.SuppressLint
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.ScaleAnimation
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.note.data.local.room.entities.Note
import com.example.note.databinding.NoteItemLayoutBinding
import com.example.note.ui.base.BaseViewHolder
import com.example.note.utils.*

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
            mBinding.apply {
                val itemViewModel = NoteItemViewModel(notes[position])
                noteItemViewModel = itemViewModel
                executePendingBindings()

                if (notes[position].isSelected) {
                    ivSelected.visible()
                    ivSelected.startAnimation(showAnimation())
                    root.startAnimation(repeatAnimation())
                } else {
                    ivSelected.startAnimation(hideAnimation(ivSelected))
                }


                if (notes[position].image != null) {
                    ivImage.visible()
                    Glide
                        .with(ivImage.context)
                        .load(Uri.parse(notes[position].image))
                        .into(ivImage)
                } else
                    ivImage.gone()


                root.setOnClickListener {
                    mListener?.onItemClick(notes[position], position)
                    notifyItemChanged(position)
                }

                root.setOnLongClickListener {
                    notes[position].isSelected = true
                    mListener?.onItemLongClick()
                    notifyItemChanged(position)
                    true
                }
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

    @SuppressLint("NotifyDataSetChanged")
    fun unselectedItems() {
        for (i in notes) {
            i.isSelected = false
        }
        notifyDataSetChanged()
    }


    interface OnItemClickListener {
        fun onItemClick(note: Note, position: Int)
        fun onItemLongClick()
    }


}