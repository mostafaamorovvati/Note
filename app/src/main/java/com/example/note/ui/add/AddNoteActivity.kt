package com.example.note.ui.add

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import com.example.note.BR
import com.example.note.R
import com.example.note.data.local.room.entities.Note
import com.example.note.databinding.ActivityAddNoteBinding
import com.example.note.ui.base.BaseActivity
import com.example.note.ui.dialog.NoteDialog
import com.example.note.ui.dialog.NoteDialogNavigator
import com.example.note.utils.toast
import org.koin.android.viewmodel.ext.android.viewModel
import org.koin.core.component.KoinApiExtension
import java.util.*

@KoinApiExtension
class AddNoteActivity : BaseActivity<ActivityAddNoteBinding, AddNoteViewModel>(),
    AddNoteNavigator {

    private val mViewModel: AddNoteViewModel by viewModel()
    private lateinit var mBinding: ActivityAddNoteBinding
    private var note: Note? = null
    private var isUpdate = false


    companion object {

        const val IS_NOTE_SAVED_OR_UPDATE = "is_note_saved_or_update"

        private const val NOTE_KEY = "note_key"
        private const val IS_UPDATE = "is_update"

        fun openActivity(
            activity: FragmentActivity,
            note: Note?,
            isUpdate: Boolean
        ): Intent {
            val intent = Intent(activity, AddNoteActivity::class.java)
            intent.putExtra(NOTE_KEY, note)
            intent.putExtra(IS_UPDATE, isUpdate)
            return intent
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mBinding = getViewDataBinding()
        mViewModel.setNavigator(this)

        getExtras()
    }


    private fun getExtras() {
        if (intent != null) {
            mBinding.apply {
                note = intent.getParcelableExtra(NOTE_KEY)
                edtTitle.setText(note?.title)
                edtContent.setText(note?.content)
                isUpdate = intent.getBooleanExtra(IS_UPDATE, false)
            }
        }
    }


    private fun setResult() {
        val intent = Intent()
        intent.putExtra(IS_NOTE_SAVED_OR_UPDATE, true)
        setResult(RESULT_OK, intent)
    }

    override fun getBindingVariable() = BR.AddNoteViewModel

    override fun getLayoutId() = R.layout.activity_add_note

    override fun getViewModel() = mViewModel


    override fun onBack() {
        exit()
    }

    override fun onBackPressed() {
        exit()
    }

    override fun onSave() {
        if (isUpdate)
            updateNote()
        else
            saveNote()
    }

    private fun updateNote() {
        mBinding.apply {

            if (edtTitle.text.isNullOrEmpty() || edtContent.text.isNullOrEmpty()) {
                this@AddNoteActivity.toast(getString(R.string.fill_field_txt))
                return
            }

            note?.title = edtTitle.text.toString().trim()
            note?.content = edtContent.text.toString().trim()
            note?.date = Date().toString()

            note?.let { mViewModel.updateNote(it) }
            this@AddNoteActivity.toast(getString(R.string.note_uodated_txt))
            setResult()
            finish()
        }
    }


    private fun saveNote() {
        mBinding.apply {

            if (edtTitle.text.isNullOrEmpty() || edtContent.text.isNullOrEmpty()) {
                this@AddNoteActivity.toast(getString(R.string.fill_field_txt))
                return
            }

            val note = Note(
                title = edtTitle.text.toString().trim(),
                content = edtContent.text.toString().trim(),
                date = Date().toString(),
            )
            mViewModel.saveNote(note)
            this@AddNoteActivity.toast(getString(R.string.note_saved_txt))
            setResult()
            finish()
        }
    }

    private fun exit() {
        val title = mBinding.edtTitle.text.toString().trim()
        val content = mBinding.edtContent.text.toString().trim()

        if (title.isEmpty() && content.isEmpty()) {
            finish()
            return
        }

        if (isUpdate && title == note?.title && content == note?.content) {
            finish()
            return
        }

        NoteDialog(
            getString(R.string.exit_add_new_note_message_txt),
            getString(R.string.ok_txt),
            object : NoteDialogNavigator {

                override fun ok() {
                    finish()
                }

                override fun cancel() {

                }

            }
        ).show(supportFragmentManager, "")
    }

}