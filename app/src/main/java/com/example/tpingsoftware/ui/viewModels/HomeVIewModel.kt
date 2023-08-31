package com.example.tpingsoftware.ui.viewModels

import android.content.Context
import android.content.Intent
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tpingsoftware.data.models.Service
import com.example.tpingsoftware.di.repository.ServiceRepositoryContract
import com.example.tpingsoftware.ui.view.AddServiceActivity
import com.example.tpingsoftware.ui.view.EditProfileActivity
import com.example.tpingsoftware.ui.view.ForgotPasswordActivity
import com.example.tpingsoftware.ui.view.HomeActivity
import com.example.tpingsoftware.ui.view.LoginActivity
import com.example.tpingsoftware.utils.AppPreferences
import com.google.firebase.firestore.DocumentSnapshot
import kotlinx.coroutines.launch

class HomeVIewModel(
    private val context: Context,
    private val repository: ServiceRepositoryContract
):ViewModel() {

    private val _listServiceMutable = MutableLiveData<ArrayList<Service>>()
    fun getAllService() {



        viewModelScope.launch {
            val response = repository.getAllService()
            response.addOnCompleteListener {
                if (response.isSuccessful){

                    toService(response.result.documents)
                }
            }
        }

    }

    private fun toService(documents: List<DocumentSnapshot>) {

        val listService : ArrayList<Service> = arrayListOf()

        documents.forEach { document ->
            val service = Service(document.id, document.getString("title"), )
        }
    }


    fun SignOut(){
        AppPreferences.deletePreferences(context)
        goToLogin()
    }

    private fun goToLogin(){
        val intent = Intent(context, LoginActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(intent)
    }

    fun goToEditProfile() {
        val intent = Intent(context, EditProfileActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(intent)
    }

    fun goToAddService(){
        val intent = Intent(context, AddServiceActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(intent)
    }

    fun goToEditPass() {

        val intent = Intent(context, ForgotPasswordActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(intent)
    }


}