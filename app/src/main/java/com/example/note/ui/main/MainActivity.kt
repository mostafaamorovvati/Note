package com.example.note.ui.main

import android.os.Bundle
import android.view.animation.Animation
import android.view.animation.ScaleAnimation
import androidx.activity.result.contract.ActivityResultContracts
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
    }

    private fun setupNoteObserver() {
        mViewModel.notes.observe(this, {
            it?.let {
                when (it.status) {
                    Status.SUCCESS -> {
                        mBinding.noteList.visible()
                        mBinding.fab.visible()
                        mBinding.progressBar.gone()
                        mNoteAdapter.setData(it.data?.toMutableList() ?: mutableListOf())
                    }
                    Status.ERROR -> {
                        mBinding.noteList.gone()
                        mBinding.progressBar.gone()
                        mBinding.fab.visible()
                    }
                    Status.LOADING -> {
                        mBinding.noteList.gone()
                        mBinding.fab.gone()
                        mBinding.progressBar.visible()
                    }
                }
            }
        })
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
        mNoteAdapter.unselectedItems()
        mBinding.btnDelete.startAnimation(hideAnimation(mBinding.btnDelete))
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
                "Are you sure you want to delete these items?"
            else
                "Do you want to delete the ${notes[0].title}?",
            getString(R.string.delete_txt),
            object : NoteDialogNavigator {
                override fun ok() {
                    for (i in notes) {
                        mViewModel.deleteNote(i)
                        mNoteAdapter.deleteSelectedList(i)
                    }
                    mBinding.btnDelete.startAnimation(hideAnimation(mBinding.btnDelete))
                }

                override fun cancel() {

                }
            }
        ).show(supportFragmentManager, "")
    }


}