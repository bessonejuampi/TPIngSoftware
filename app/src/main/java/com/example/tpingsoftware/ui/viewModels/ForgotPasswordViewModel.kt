package com.example.tpingsoftware.ui.viewModels

import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tpingsoftware.di.repository.ForgotPasswordContract
import com.example.tpingsoftware.di.repository.ForgotPasswordRepository
import com.example.tpingsoftware.ui.view.LoginActivity
import kotlinx.coroutines.launch

class ForgotPasswordViewModel(
    private val repository: ForgotPasswordContract,
    private val context: Context
) : ViewModel() {

    fun sendEmailResetPassword(email: String) {
        viewModelScope.launch {
            val result = repository.sendEmailResetPassword(email)
            result.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(
                        context,
                        "Email enviado con éxito, por favor revise su casilla de mensajes",
                        Toast.LENGTH_SHORT
                    ).show()
                    goToLogin()

                }else{
                    Toast.makeText(
                        context,
                        task.exception!!.message.toString(),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    private fun goToLogin(){
        val intent = Intent(context, LoginActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(intent)
    }
}