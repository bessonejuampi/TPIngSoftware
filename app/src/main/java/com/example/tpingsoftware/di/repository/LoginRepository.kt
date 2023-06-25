package com.example.tpingsoftware.di.repository

import android.content.Context
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

interface LoginRepositoryContract{
    suspend fun LogInUser(email:String, password:String): Task<AuthResult>

    suspend fun LogInWithGoogle(credential: AuthCredential):Task<AuthResult>

    fun saveUserInFireStore(name:String, lastName:String, email: String)

}

class LoginRepository(
    private val auth : FirebaseAuth,
    private val firestore : FirebaseFirestore,
    private val context: Context
) : LoginRepositoryContract{
    override suspend fun LogInUser(email: String, password: String): Task<AuthResult> {
        return auth.signInWithEmailAndPassword(email, password)
    }

    override suspend fun LogInWithGoogle(credential: AuthCredential): Task<AuthResult> {
        return auth.signInWithCredential(credential)
    }

    override fun saveUserInFireStore(name: String, lastName: String, email: String) {
        firestore.collection("users")
            .document(email).set(
                hashMapOf("name" to name, "lastName" to lastName)
            )
    }

}