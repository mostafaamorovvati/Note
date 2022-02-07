package com.example.note.ui.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.example.note.R
import com.example.note.databinding.NoteDialogLayoutBinding
import com.example.note.ui.base.BaseDialog
import org.koin.android.viewmodel.ext.android.viewModel
import org.koin.core.component.KoinApiExtension

@KoinApiExtension
class NoteDialog(
    private val message: String,
    private val okButtonText: String,
    private val mListener: NoteDialogNavigator
) : BaseDialog<Any>(), NoteDialogNavigator {

    private lateinit var mBinding: NoteDialogLayoutBinding
    private val mViewModel: NoteDialogViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding = DataBindingUtil.inflate(
            layoutInflater,
            R.layout.note_dialog_layout,
            container,
            false
        )
        mBinding.noteDialogViewModel = mViewModel
        mViewModel.setNavigator(this)

        mBinding.tvMessage.text = message
        mBinding.btnOk.text = okButtonText

        return mBinding.root
    }

    override fun ok() = mListener.ok()

    override fun cancel() = dismissAllowingStateLoss()
}