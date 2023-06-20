package com.example.tpingsoftware.di

import android.content.Context
import com.example.tpingsoftware.di.repository.LoginRepository
import com.example.tpingsoftware.di.repository.LoginRepositoryContract
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import org.koin.dsl.single

val RepositoryModule = module {
    factory<LoginRepositoryContract> { LoginRepository(get(), androidContext()) }
}

val FirebaseModule = module {
    fun provideFirebaseAuth(): FirebaseAuth{
        return Firebase.auth
    }
    single { provideFirebaseAuth() }
}