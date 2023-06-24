package com.example.tpingsoftware.ui.viewModels

import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tpingsoftware.di.repository.LoginRepositoryContract
import com.example.tpingsoftware.ui.view.HomeActivity
import com.example.tpingsoftware.ui.view.RegisterActivity
import com.example.tpingsoftware.utils.Dialog
import kotlinx.coroutines.launch

class LoginViewModel(
    private val repository: LoginRepositoryContract,
    private val context:Context
) :ViewModel() {

    var resultLogInMutable = MutableLiveData<Dialog>()

    fun LogIn(email:String, password:String){

        viewModelScope.launch {
            val result = repository.LogInUser(email, password)
           result.addOnCompleteListener { task ->
               if (task.isSuccessful) {
                   goToHome()
               } else {
                   val dialog = Dialog()
                   dialog.title = "Algo ha salido mal"
                   dialog.description = task.exception.toString()
                   dialog.result = false
                   resultLogInMutable.value = dialog
               }
           }
        }
    }


    fun goToRegisterUser(){
        val intent = Intent(context, RegisterActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(intent)
    }

    fun goToHome(){
        val intent = Intent(context, HomeActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(intent)
    }
}