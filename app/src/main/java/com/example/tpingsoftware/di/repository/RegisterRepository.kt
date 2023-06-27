package com.example.tpingsoftware.di.repository

import android.app.AlertDialog
import android.content.Context
import android.net.Uri
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.UploadTask

interface RegisterRepositoryContract {
    suspend fun registerNewUser(email: String, password: String): Task<AuthResult>
    fun showAlertDialog(context: Context, title: String, description: String?)
    fun saveUserInFireStore(
        name: String,
        lastName: String,
        email: String,
        latitude: String?,
        longitude: String?,
        hasImageProfile: Boolean
    )

    fun saveUserImageInStorage(image: Uri)
}

class RegisterRepository(
    private val auth: FirebaseAuth,
    private val firestore: FirebaseFirestore,
    private val storage: FirebaseStorage,
    private val context: Context
) : RegisterRepositoryContract {
    override suspend fun registerNewUser(email: String, password: String): Task<AuthResult> {
        return auth.createUserWithEmailAndPassword(email, password)
    }

    override fun showAlertDialog(context: Context, title: String, description: String?) {
        val builder = AlertDialog.Builder(context)
        builder.setTitle(title)
        if (!description.isNullOrEmpty()) {
            builder.setMessage(description)
        }
        val alertDialog = builder.create()
        alertDialog.show()
    }

    override fun saveUserInFireStore(
        name: String,
        lastName: String,
        email: String,
        latitude: String?,
        longitude: String?,
        hasImageProfile: Boolean
    ) {
        firestore.collection("users")
            .document(email).set(
                hashMapOf(
                    "name" to name,
                    "lastName" to lastName,
                    "latitude" to latitude,
                    "longitude" to longitude,
                    "hasImageProfile" to hasImageProfile
                )
            )
    }

    override fun saveUserImageInStorage(image: Uri) {
        storage.reference.child("imageProfile/${auth.uid}").putFile(image)
    }
}