package com.example.tpingsoftware

import android.app.Application
import com.example.tpingsoftware.di.RepositoryModule
import com.example.tpingsoftware.di.repository.ViewModelsModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class AppController : Application(){
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@AppController)
            modules(arrayListOf(RepositoryModule, ViewModelsModule))
        }
    }
}