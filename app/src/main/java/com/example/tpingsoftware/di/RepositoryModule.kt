package com.example.tpingsoftware.di

import android.content.Context
import com.example.tpingsoftware.di.repository.LoginRepository
import com.example.tpingsoftware.di.repository.LoginRepositoryContract
import com.example.tpingsoftware.di.repository.RegisterRepository
import com.example.tpingsoftware.di.repository.RegisterRepositoryContract
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val RepositoryModule = module {
    factory<LoginRepositoryContract> { LoginRepository(get(), get() ,androidContext()) }
    factory<RegisterRepositoryContract> { RegisterRepository(get(), get(), get() , androidContext()) }
}

val FirebaseModule = module {
    fun provideFirebaseAuth(): FirebaseAuth{
        return Firebase.auth
    }

    fun provideFirebaseFirestore() : FirebaseFirestore{
        return FirebaseFirestore.getInstance()
    }

    fun provideFirebaseStorage() : FirebaseStorage {
        return  Firebase.storage
    }
    single { provideFirebaseAuth() }
    single { provideFirebaseFirestore() }
    single { provideFirebaseStorage() }
}