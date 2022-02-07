package com.example.note.ui.base
import androidx.lifecycle.ViewModel
import org.koin.core.component.KoinApiExtension
import org.koin.core.component.KoinComponent
import java.lang.ref.WeakReference

@KoinApiExtension
abstract class BaseViewModel<N> : ViewModel(), KoinComponent {


    private var mNavigator: WeakReference<N>? = null

    open fun getNavigator(): N? {
        return mNavigator?.get()
    }

    open fun setNavigator(navigator: N) {
        mNavigator = WeakReference(navigator)
    }

}