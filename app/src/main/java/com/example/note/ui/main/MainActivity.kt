package com.example.note.ui.main

import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.note.utils.Status
import com.example.note.BR
import com.example.note.R
import com.example.note.data.local.room.entities.Note
import com.example.note.databinding.ActivityMainBinding
import com.example.note.ui.add.AddNoteActivity
import com.example.note.ui.add.AddNoteActivity.Companion.IS_NOTE_SAVED_OR_UPDATE
import com.example.note.ui.base.BaseActivity
import com.example.note.ui.dialog.NoteDialog
import com.example.note.ui.dialog.NoteDialogNavigator
import com.example.note.utils.gone
import com.example.note.utils.visible
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
                        mNoteAdapter.setData(it.data?.toMutableList() ?: mutableListOf())
                    }
                    Status.ERROR -> {

                    }
                    Status.LOADING -> {

                    }
                }
            }
        })
    }


    private fun setupNoteList() {
        mBinding.noteList.apply {
            mNoteAdapter.mListener = this@MainActivity
            adapter = mNoteAdapter
            layoutManager = LinearLayoutManager(this@MainActivity)
        }
    }

    override fun getBindingVariable() = BR.MainViewModel

    override fun getLayoutId() = R.layout.activity_main

    override fun getViewModel() = mViewModel


    override fun openAddNotePage() {
        activityResultLauncher.launch(AddNoteActivity.openActivity(this, null, false))
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
                mBinding.btnDelete.gone()
        }
    }


    override fun onItemLongClick() {
        mBinding.btnDelete.visible()
    }


    private fun showDeleteItemDialog(notes: MutableList<Note>) {
        NoteDialog(
            if (mNoteAdapter.getSelectedItem().size > 1)
                "Are you sure delete this items?"
            else
                "Are you sure delete the ${notes[0].title}?",
            getString(R.string.delete_txt),
            object : NoteDialogNavigator {
                override fun ok() {
                    for (i in notes) {
                        mViewModel.deleteNote(i)
                        mNoteAdapter.deleteSelectedList(i)
                    }
                    mBinding.btnDelete.gone()
                }

                override fun cancel() {

                }
            }
        ).show(supportFragmentManager, "")
    }


}