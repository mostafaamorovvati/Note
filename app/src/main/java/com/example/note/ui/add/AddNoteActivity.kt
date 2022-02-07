package com.example.note.ui.add

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import com.example.mvvmproject.utils.Status
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mBinding = getViewDataBinding()
        mViewModel.setNavigator(this)

        saveNoteObserver()
    }

    private fun saveNoteObserver() {
        mViewModel.saveNoteResult.observe(this, {
            it?.let {
                when (it.status) {
                    Status.SUCCESS -> {
                        this@AddNoteActivity.toast("Note saved")
                        finish()
                    }
                    Status.ERROR -> {

                    }
                    Status.LOADING -> {

                    }
                }
            }
        })
    }

    override fun getBindingVariable() = BR.AddNoteViewModel

    override fun getLayoutId() = R.layout.activity_add_note

    override fun getViewModel() = mViewModel

    companion object {
        fun openActivity(activity: FragmentActivity): Intent {
            val intent = Intent(activity, AddNoteActivity::class.java)
            return intent
        }
    }

    override fun onBack() {
        exit()
    }

    override fun onBackPressed() {
        exit()
    }

    override fun onSave() {
        saveNote()
    }


    private fun saveNote() {
        mBinding.apply {

            if (edtTitle.text.isNullOrEmpty() || edtContent.text.isNullOrEmpty()) {
                this@AddNoteActivity.toast("Please fill title and content")
                return
            }

            val note = Note(
                title = edtTitle.text.toString().trim(),
                content = edtContent.text.toString().trim(),
                date = Date().toString(),
            )
            mViewModel.saveNote(note)
        }
    }

    private fun exit() {
        if (mBinding.edtTitle.text.isNullOrEmpty() && mBinding.edtContent.text.isNullOrEmpty()) {
            finish()
            return
        }
        NoteDialog(
            "Your note will not be saved if you exit",
            "Ok",
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