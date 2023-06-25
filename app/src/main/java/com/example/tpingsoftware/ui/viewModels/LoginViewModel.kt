package com.example.tpingsoftware.ui.viewModels

import android.content.Context
import android.content.Intent
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tpingsoftware.di.repository.LoginRepositoryContract
import com.example.tpingsoftware.ui.view.HomeActivity
import com.example.tpingsoftware.ui.view.RegisterActivity
import com.example.tpingsoftware.utils.AppPreferences
import com.example.tpingsoftware.utils.Dialog
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
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
                   AppPreferences.setUserSession(context,email)
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

    fun LoginWithGoogle(task: Task<GoogleSignInAccount>) {
        try {
            val account = task.getResult(ApiException::class.java)
            if (account != null){
                val credential = GoogleAuthProvider.getCredential(account.idToken, null)
                viewModelScope.launch {
                    repository.LogInWithGoogle(credential).addOnCompleteListener { task ->
                        if (task.isSuccessful){
                            AppPreferences.setUserSession(context, account.email.toString())
                            repository.saveUserInFireStore(account.givenName.toString(), account.familyName.toString(), account.email.toString())
                            goToHome()
                        }else{
                            val dialog = Dialog()
                            dialog.title = "Algo ha salido mal"
                            dialog.description = task.exception.toString()
                            dialog.result = false
                            resultLogInMutable.value = dialog
                        }
                    }
                }

            }
        }catch (e : ApiException){
            val dialog = Dialog()
            dialog.title = "Algo ha salido mal"
            dialog.description = task.exception.toString()
            dialog.result = false
            resultLogInMutable.value = dialog
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