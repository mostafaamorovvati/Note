package com.example.note.di

import com.example.note.data.repository.MainRepository
import org.koin.dsl.module

val repositoryModule = module {

    factory {
        MainRepository(get())
    }

}