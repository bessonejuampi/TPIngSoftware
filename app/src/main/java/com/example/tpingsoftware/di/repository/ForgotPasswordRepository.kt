package com.example.tpingsoftware.di.repository

import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth

interface ForgotPasswordContract {
    suspend fun sendEmailResetPassword(email:String): Task<Void>
}

class ForgotPasswordRepository(
    private val auth : FirebaseAuth
) : ForgotPasswordContract {
    override suspend fun sendEmailResetPassword(email: String): Task<Void> {
        return auth.sendPasswordResetEmail(email)
    }

}