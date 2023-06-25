package com.example.tpingsoftware.di.repository

import android.app.AlertDialog
import android.content.Context
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

interface RegisterRepositoryContract{
    suspend fun registerNewUser(email:String, password:String): Task<AuthResult>
    fun showAlertDialog(context:Context, title:String, description:String?)
    fun saveUserInFireStore(name:String, lastName:String, email: String, imageProfile:String?, latitude:String?, longitude:String?)
}

class RegisterRepository(
    private val auth : FirebaseAuth,
    private val firestore : FirebaseFirestore,
    private val context: Context
) : RegisterRepositoryContract{
    override suspend fun registerNewUser(email: String, password: String): Task<AuthResult> {
        return auth.createUserWithEmailAndPassword(email, password)
    }

    override fun showAlertDialog(context: Context, title: String, description: String?) {
        val builder = AlertDialog.Builder(context)
        builder.setTitle(title)
        if (!description.isNullOrEmpty()){
            builder.setMessage(description)
        }
        val alertDialog = builder.create()
        alertDialog.show()
    }

    override fun saveUserInFireStore(name: String, lastName: String, email: String, imageProfile:String?, latitude:String?, longitude:String?) {
        firestore.collection("users")
            .document(email).set(
                hashMapOf(
                    "name" to name,
                    "lastName" to lastName,
                    "imageProfile" to imageProfile,
                    "latitude" to latitude,
                    "longitude" to longitude
                )
            )
    }

}