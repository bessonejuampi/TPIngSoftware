package com.example.tpingsoftware.di.repository

import android.app.AlertDialog
import android.content.Context
import android.net.Uri
import com.example.tpingsoftware.data.models.Location
import com.example.tpingsoftware.data.models.Province
import com.example.tpingsoftware.data.network.ApiClient
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

interface UserRepositoryContract{
    suspend fun LogInUser(email:String, password:String): Task<AuthResult>

    suspend fun LogInWithGoogle(credential: AuthCredential): Task<AuthResult>

    fun saveUserInFireStore(name:String, lastName:String, email: String)

    fun isEmailVerified():Boolean

    suspend fun registerNewUser(email: String, password: String): Task<AuthResult>

    fun showAlertDialog(context: Context, title: String, description: String?)

    fun saveUserInFireStore(
        name: String,
        lastName: String,
        email: String,
        province: String?,
        location: String?,
        address: String?,
        hasImageProfile: Boolean,
        idImage:String?
    )

    fun saveUserImageInStorage(image: Uri, id:String)

    fun sendEmailVerification():Task<Void>?

    suspend fun getProvinces(): ArrayList<Province>

    suspend fun getLocalities(idProvince:Int):ArrayList<Location>

    suspend fun sendEmailResetPassword(email:String): Task<Void>

    suspend fun getUserDataFromFirebase(email: String): Task<DocumentSnapshot>

    suspend fun getImageProfile(idImage: String): StorageReference


}

class UserRepository(
    private val auth : FirebaseAuth,
    private val firestore : FirebaseFirestore,
    private val storage: FirebaseStorage,
    private val apiClient : ApiClient,
    private val context: Context
) : UserRepositoryContract{

    /// Login
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

    override fun isEmailVerified(): Boolean {
        return auth.currentUser!!.isEmailVerified
    }

    //Register

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
        province: String?,
        loaction: String?,
        address: String?,
        hasImageProfile: Boolean,
        idImage:String?
    ) {
        firestore.collection("users")
            .document(email).set(
                hashMapOf(
                    "name" to name,
                    "lastName" to lastName,
                    "province" to province,
                    "location" to loaction,
                    "address" to address,
                    "hasImageProfile" to hasImageProfile,
                    "idImage" to idImage
                )
            )
    }

    override fun saveUserImageInStorage(image: Uri, id:String) {
        storage.reference.child("imageProfile/${id}").putFile(image)
    }

    override fun sendEmailVerification(): Task<Void>? {
        return auth.currentUser?.sendEmailVerification()
    }

    override suspend fun getProvinces() : ArrayList<Province> {
        val listProvinces = arrayListOf<Province>()
        val listProvincesResponse = apiClient.getProvinces()
        if (listProvincesResponse.isSuccessful){
            listProvincesResponse.body()?.listProvinces?.forEach { province ->
                listProvinces.add(province)
            }
        }
        return listProvinces
    }

    override suspend fun getLocalities(idProvince: Int): ArrayList<Location> {
        val listLocalities = arrayListOf<Location>()
        val listLocalitiesFirstResponse = apiClient.getLocalities(idProvince, 1)
        if (listLocalitiesFirstResponse.isSuccessful){
            val numberOfLocalities =  listLocalitiesFirstResponse.body()?.total
            val secondResponse = apiClient.getLocalities(idProvince, numberOfLocalities!!)
            secondResponse.body()?.localities?.forEach { location ->
                if (location.municipality.id != 0){
                    listLocalities.add(location)
                }
            }
        }
        return listLocalities
    }

    //Forgot password
    override suspend fun sendEmailResetPassword(email: String): Task<Void> {
        return auth.sendPasswordResetEmail(email)
    }

    override suspend fun getUserDataFromFirebase(email: String): Task<DocumentSnapshot> {
        return firestore.collection("users").document(email).get()
    }

    override suspend fun getImageProfile(idImage: String): StorageReference {
       return storage.reference.child("imageProfile/$idImage")
    }

}