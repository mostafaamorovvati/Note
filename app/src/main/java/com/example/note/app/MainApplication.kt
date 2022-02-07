package com.example.note.app

import android.app.Application
import com.example.note.di.adapterModule
import com.example.note.di.dataBaseModule
import com.example.note.di.repositoryModule
import com.example.note.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class MainApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger(Level.NONE)
            androidContext(this@MainApplication)
            modules(
                listOf(dataBaseModule, repositoryModule, viewModelModule, adapterModule)
            )
        }

    }

}