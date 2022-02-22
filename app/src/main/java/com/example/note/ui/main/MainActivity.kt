package com.example.note.ui.main

import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.note.BR
import com.example.note.R
import com.example.note.data.local.room.entities.Note
import com.example.note.databinding.ActivityMainBinding
import com.example.note.ui.addNote.AddNoteActivity
import com.example.note.ui.addNote.AddNoteActivity.Companion.IS_NOTE_SAVED_OR_UPDATE
import com.example.note.ui.base.BaseActivity
import com.example.note.ui.dialog.NoteDialog
import com.example.note.ui.dialog.NoteDialogNavigator
import com.example.note.utils.*
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.viewModel
import org.koin.core.component.KoinApiExtension

@KoinApiExtension
class MainActivity : BaseActivity<ActivityMainBinding, MainViewModel>(),
    MainNavigator,
    NoteAdapter.OnItemClickListener {

    private val mNoteAdapter: NoteAdapter by inject()
    private val mViewModel: MainViewModel by viewModel()
    private lateinit var mBinding: ActivityMainBinding
    private var notesList: MutableList<Note> = mutableListOf()

    private val activityResultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == RESULT_OK) {
                val data = it.data
                if (data != null) {
                    val isNoteSaved = data.getBooleanExtra(IS_NOTE_SAVED_OR_UPDATE, false)

                    if (isNoteSaved)
                        mViewModel.getNotes()

                }
            }
        }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mBinding = getViewDataBinding()
        mViewModel.setNavigator(this)

        setupNoteObserver()
        setupNoteList()

        mBinding.edtSearch.addTextChangedListener {
            searchNote(it.toString())

        }
    }

    private fun searchNote(input: String) {
        val filteredList: ArrayList<Note> = arrayListOf()
        for (i in notesList) {
            if (i.title.contains(input)) {
                filteredList.add(i)
            }
        }
        mNoteAdapter.setData(filteredList)
    }

    private fun setupNoteObserver() {
        mViewModel.notes.observe(this) {
            it?.let {
                mBinding.apply {
                    when (it.status) {
                        Status.SUCCESS -> {
                            fab.visible()
                            progressBar.gone()

                            if (it.data.isNullOrEmpty()) {
                                noteList.gone()
                                edtSearch.gone()
                                emptyMessageContainer.visible()
                            } else {
                                noteList.visible()
                                edtSearch.visible()
                                emptyMessageContainer.gone()
                                notesList = it.data.toMutableList()
                                mNoteAdapter.setData(notesList)
                            }
                        }
                        Status.ERROR -> {
                            noteList.gone()
                            edtSearch.gone()
                            progressBar.gone()
                            emptyMessageContainer.visible()
                            fab.visible()
                        }
                        Status.LOADING -> {
                            noteList.gone()
                            edtSearch.gone()
                            fab.gone()
                            emptyMessageContainer.gone()
                            progressBar.visible()
                        }
                    }
                }
            }
        }
    }


    private fun setupNoteList() {
        mBinding.noteList.apply {
            mNoteAdapter.mListener = this@MainActivity
            adapter = mNoteAdapter
            val staggeredLayoutManager =
                StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
            staggeredLayoutManager.gapStrategy =
                StaggeredGridLayoutManager.GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS
            layoutManager = staggeredLayoutManager
        }
    }

    override fun getBindingVariable() = BR.MainViewModel

    override fun getLayoutId() = R.layout.activity_main

    override fun getViewModel() = mViewModel


    override fun openAddNotePage() {
        activityResultLauncher.launch(AddNoteActivity.openActivity(this, null, false))
        if (mNoteAdapter.getSelectedItem().size > 0) {
            mNoteAdapter.unselectedItems()
            mBinding.btnDelete.startAnimation(hideAnimation(mBinding.btnDelete))
        }
    }

    override fun onDeleteBtnClick() {
        showDeleteItemDialog(mNoteAdapter.getSelectedItem())
    }

    override fun onItemClick(note: Note, position: Int) {

        if (mNoteAdapter.getSelectedItem().isEmpty()) {
            activityResultLauncher.launch(AddNoteActivity.openActivity(this, note, true))
        } else {
            val notes = mNoteAdapter.getNotesList()
            notes[position].isSelected = !notes[position].isSelected
            if (mNoteAdapter.getSelectedItem().isEmpty())
                mBinding.btnDelete.startAnimation(hideAnimation(mBinding.btnDelete))
        }
    }


    override fun onItemLongClick() {
        mBinding.btnDelete.visible()
        mBinding.btnDelete.startAnimation(showAnimation())
    }


    private fun showDeleteItemDialog(notes: MutableList<Note>) {
        NoteDialog(
            if (mNoteAdapter.getSelectedItem().size > 1)
                getString(R.string.delete_multi_item_message_txt)
            else
                "Do you want to delete the \"${notes[0].title}\"?",
            getString(R.string.delete_txt),
            object : NoteDialogNavigator {
                override fun ok() {

                    if (notes.size > 1) {
                        val ids = ArrayList<Int>()
                        for (i in 0 until notes.size) {
                            ids.add(notes[i].id)
                            mNoteAdapter.deleteNoteFromList(notes[i])
                        }
                        mViewModel.deleteMultiItem(ids)
                    } else {
                        mViewModel.deleteNote(notes[0])
                        mNoteAdapter.deleteNoteFromList(notes[0])
                    }

                    mBinding.btnDelete.startAnimation(hideAnimation(mBinding.btnDelete))

                    if (mNoteAdapter.getNotesSize() == 0)
                        mViewModel.getNotes()
                }

                override fun cancel() {

                }
            }
        ).show(supportFragmentManager, "")
    }


    override fun onBackPressed() {
        if (mNoteAdapter.getSelectedItem().size > 0) {
            mNoteAdapter.unselectedItems()
            mBinding.btnDelete.startAnimation(hideAnimation(mBinding.btnDelete))
        } else
            super.onBackPressed()
    }
}