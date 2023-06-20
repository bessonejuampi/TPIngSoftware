package com.example.tpingsoftware.di.repository

import android.content.Context
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth

interface RegisterRepositoryContract{
    suspend fun registerNewUser(email:String, password:String): Task<AuthResult>
}

class RegisterRepository(
    private val auth : FirebaseAuth,
    private val context: Context
) : RegisterRepositoryContract{
    override suspend fun registerNewUser(email: String, password: String): Task<AuthResult> {
        return auth.createUserWithEmailAndPassword(email, password)
    }

}