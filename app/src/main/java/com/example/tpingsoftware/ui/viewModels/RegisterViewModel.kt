package com.example.tpingsoftware.ui.viewModels

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tpingsoftware.di.repository.RegisterRepositoryContract
import com.example.tpingsoftware.utils.UserValidator
import com.example.tpingsoftware.utils.isEmail
import com.example.tpingsoftware.utils.isText
import kotlinx.coroutines.launch

class RegisterViewModel(
    val repository : RegisterRepositoryContract,
    val context: Context
):ViewModel() {
    var userValidationMutable = MutableLiveData<UserValidator?>()

    fun validationUser(
        name:String?,
        lastName:String?,
        email:String?,
        password:String?,
        repeatPassword:String?
    ){
        var userValidator = UserValidator()

        if (!name.isText()){
            userValidator.nameError = "Este campo es obligatorio"
        }
        if (!lastName.isText()){
            userValidator.lastNameError = "Este campo es obligatorio"
        }
        if (!email.isEmail()){
            userValidator.emailError = "Este campo es obligatorio"
        }
        if (!password.isNullOrEmpty()){
            userValidator.passError = "Este campos es obligatorio"
        }
        if (!repeatPassword.isNullOrEmpty()){
            userValidator.repeatPasswordError = "Este campos es obligatorio"
        }
        if (!userValidator.passError.isNullOrEmpty() && userValidator.repeatPasswordError.isNullOrEmpty()){
            if (repeatPassword!=password){
                userValidator.repeatPasswordError = "Las contraseñas no coinsiden"
                userValidator.passError = "Las contraseñas no coinsiden"
            }
        }


        if (userValidator.isSuccesully()){
            registerUser(email!!, password!!)

        }
        userValidationMutable.value = userValidator

    }


    private fun registerUser(email:String, password:String){
        viewModelScope.launch {
            val result = repository.registerNewUser(email, password)
        }
    }
}