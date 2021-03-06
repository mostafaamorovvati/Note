package com.example.note.ui.addNote

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import com.bumptech.glide.Glide
import com.example.note.BR
import com.example.note.R
import com.example.note.data.local.room.entities.Note
import com.example.note.databinding.ActivityAddNoteBinding
import com.example.note.ui.base.BaseActivity
import com.example.note.ui.dialog.NoteDialog
import com.example.note.ui.dialog.NoteDialogNavigator
import com.example.note.utils.*
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
    private var imageUri: Uri? = null

    private val getImageFromGallery =
        registerForActivityResult(ActivityResultContracts.GetContent()) {

            if (it != null) {
                mBinding.apply {
                    imageContainer.visible()
                    btnRemoveImage.visible()
                    imageView.setImageURI(it)
                    imageUri = it
                }
            }

        }


    companion object {

        const val IS_NOTE_SAVED_OR_UPDATE = "is_note_saved_or_update"

        private const val PERMISSION_REQUEST_CODE = 10
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
                imageUri = Uri.parse(note?.image ?: "")


                if (note?.image != null) {
                    imageContainer.visible()
                    btnRemoveImage.visible()

                    try {
                        Glide
                            .with(this@AddNoteActivity)
                            .load(imageUri)
                            .placeholder(R.drawable.ic_launcher_background)
                            .into(imageView)
                        Log.d("LOG_TAG", "try: Ok")
                    } catch (e: Exception) {
                        Log.d("LOG_TAG", "catch: ${e.message}")
                    }

                } else {
                    imageContainer.invisible()
                    btnRemoveImage.gone()
                }
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

    override fun openGallery() {
        val hasStoragePermission = hasStoragePermission()
        if (hasStoragePermission) {
            getImageFromGallery.launch("image/*")
        } else {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ),
                PERMISSION_REQUEST_CODE
            )
        }
    }

    override fun removeImage() {
        mBinding.apply {
            imageView.setImageURI(null)
            imageContainer.startAnimation(hideAnimation(imageContainer))
            btnRemoveImage.startAnimation(hideAnimation(btnRemoveImage))
            imageUri = null
        }
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
            note?.image = imageUri?.toString()
            note?.isSelected = false

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
                image = if (imageUri.toString().isEmpty()) null else imageUri.toString(),
                isSelected = false
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
            if (isUpdate)
                getString(R.string.exit_update_new_note_message_txt)
            else
                getString(R.string.exit_save_new_note_message_txt),
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

    private fun hasStoragePermission(): Boolean {
        val permissionCheckResult = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.READ_EXTERNAL_STORAGE
        )
        return permissionCheckResult == PackageManager.PERMISSION_GRANTED
    }

    @SuppressLint("MissingPermission")
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.getOrNull(0) == PackageManager.PERMISSION_GRANTED) {
                getImageFromGallery.launch("image/*")
            } else {
                AlertDialog.Builder(this)
                    .setTitle(getString(R.string.permission_txt))
                    .setMessage(getString(R.string.permission_rejected_txt))
                    .setPositiveButton(getString(R.string.ok_txt)) { _, _ ->
                    }
                    .setCancelable(false)
                    .show()
            }
        }
    }
}