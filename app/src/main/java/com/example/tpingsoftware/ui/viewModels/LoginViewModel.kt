package com.example.tpingsoftware.ui.viewModels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tpingsoftware.di.repository.LoginRepositoryContract
import kotlinx.coroutines.launch

class LoginViewModel(
    private val repository: LoginRepositoryContract
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
}