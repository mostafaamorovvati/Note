package com.example.note.ui.main

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mvvmproject.utils.Status
import com.example.note.BR
import com.example.note.R
import com.example.note.databinding.ActivityMainBinding
import com.example.note.ui.base.BaseActivity
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.viewModel
import org.koin.core.component.KoinApiExtension

@KoinApiExtension
class MainActivity : BaseActivity<ActivityMainBinding, MainViewModel>(), MainNavigator {

    private val mNoteAdapter: NoteAdapter by inject()
    private val mViewModel: MainViewModel by viewModel()
    private lateinit var mBinding: ActivityMainBinding

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
            adapter = mNoteAdapter
            layoutManager = LinearLayoutManager(this@MainActivity)
        }
    }

    override fun getBindingVariable() = BR.MainViewModel

    override fun getLayoutId() = R.layout.activity_main

    override fun getViewModel() = mViewModel
}