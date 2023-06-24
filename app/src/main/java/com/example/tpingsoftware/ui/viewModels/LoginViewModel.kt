package com.example.tpingsoftware.ui.viewModels

import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tpingsoftware.di.repository.LoginRepositoryContract
import com.example.tpingsoftware.ui.view.RegisterActivity
import kotlinx.coroutines.launch

class LoginViewModel(
    private val repository: LoginRepositoryContract,
    private val context:Context
) :ViewModel() {

    fun RegisterNewUser(email:String, password:String){
        viewModelScope.launch {
            val result = repository.registerNewUser(email, password)
           result.addOnCompleteListener { task ->
               if (task.isSuccessful) {
                   // Sign in success, update UI with the signed-in user's information
                   Log.d("CreaateUser", "createUserWithEmail:success")
                   //val user = auth.currentUser
                   //updateUI(user)
               } else {
                   // If sign in fails, display a message to the user.
                   Log.d("CreaateUser", "createUserWithEmail:failure", task.exception)
                   //updateUI(null)
               }
           }
        }
    }


    fun goToRegisterUser(){
        val intent = Intent(context, RegisterActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(intent)
    }
}