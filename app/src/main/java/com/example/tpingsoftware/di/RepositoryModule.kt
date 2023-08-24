package com.example.tpingsoftware.di

import com.example.tpingsoftware.di.repository.UserRepository
import com.example.tpingsoftware.di.repository.UserRepositoryContract
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val RepositoryModule = module {
    factory<UserRepositoryContract> { UserRepository(get(), get() ,get(), get(), androidContext()) }
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
