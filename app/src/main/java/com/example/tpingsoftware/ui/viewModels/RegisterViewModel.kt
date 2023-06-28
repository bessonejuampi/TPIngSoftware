package com.example.tpingsoftware.ui.viewModels

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tpingsoftware.R
import com.example.tpingsoftware.di.repository.RegisterRepositoryContract
import com.example.tpingsoftware.ui.view.HomeActivity
import com.example.tpingsoftware.utils.Dialog
import com.example.tpingsoftware.utils.UserValidator
import com.example.tpingsoftware.utils.Utils
import com.example.tpingsoftware.utils.isEmail
import com.example.tpingsoftware.utils.isText
import kotlinx.coroutines.launch
import android.widget.Toast
import com.example.tpingsoftware.ui.view.LoginActivity
import com.example.tpingsoftware.utils.AppPreferences
import com.example.tpingsoftware.utils.TypeDialog
import java.io.IOException

class RegisterViewModel(
    private val repository: RegisterRepositoryContract,
    val context: Context

) : ViewModel() {
    var userValidationMutable = MutableLiveData<UserValidator?>()
    var resultRegisterMutable = MutableLiveData<Dialog>()
    var showProgress = MutableLiveData<Boolean>()

    fun validationUser(
        name: String?,
        lastName: String?,
        email: String?,
        password: String?,
        repeatPassword: String?,
        latitude: String?,
        longitude: String?,
        hasImageProfile: Boolean
    ) {
        var userValidator = UserValidator()

        showProgress.value = true

        if (!name.isText()) {
            userValidator.nameError = context.getString(R.string.text_input_mandatory)
        }
        if (!lastName.isText()) {
            userValidator.lastNameError = context.getString(R.string.text_input_mandatory)
        }
        if (email.isNullOrEmpty()) {
            userValidator.emailError = context.getString(R.string.text_input_mandatory)
            if (!email.isEmail()) {
                userValidator.emailError = context.getString(R.string.text_input_format_email)
            }
        }
        if (password.isNullOrEmpty()) {
            userValidator.passError = context.getString(R.string.text_input_mandatory)
        }
        if (repeatPassword.isNullOrEmpty()) {
            userValidator.repeatPasswordError = context.getString(R.string.text_input_mandatory)
        }
        if (userValidator.passError.isNullOrEmpty() && userValidator.repeatPasswordError.isNullOrEmpty()) {
            if (repeatPassword != password) {
                userValidator.repeatPasswordError = context.getString(R.string.text_repeat_password)
                userValidator.passError = context.getString(R.string.text_repeat_password)
            }
        }

        if (userValidator.isSuccessfully()) {
            registerUser(
                email!!,
                password!!,
                name!!,
                lastName!!,
                latitude,
                longitude,
                hasImageProfile
            )
        } else {
            showProgress.value = false
        }
        userValidationMutable.value = userValidator

    }


    private fun registerUser(
        email: String,
        password: String,
        name: String,
        lastName: String,
        latitude: String?,
        longitude: String?,
        hasImageProfile: Boolean
    ) {
        val dialog = Dialog()

        viewModelScope.launch {
            val result = repository.registerNewUser(email, password)
            result.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    repository.sendEmailVerification()?.addOnCompleteListener {
                        resultRegisterMutable.value = dialog
                        saveUserInFirebaseFirestore(email, name, lastName, latitude, longitude, hasImageProfile)

                    }?.addOnFailureListener {
                        Toast.makeText(context, it.toString(), Toast.LENGTH_SHORT).show()
                    }


                } else {
                    dialog.title = "Algo ha salido mal"
                    dialog.description = task.exception.toString()
                    dialog.result = TypeDialog.DISMISS
                    resultRegisterMutable.value = dialog
                }
            }
                .addOnCompleteListener { task ->
                    if (task.isComplete){

                    }
                }
        }
    }

    private fun saveUserInFirebaseFirestore(
        email: String,
        name: String,
        lastName: String,
        latitude: String?,
        longitude: String?,
        hasImageProfile: Boolean
    ) {
        repository.saveUserInFireStore(name, lastName, email, latitude, longitude, hasImageProfile)
        val dialog = Dialog()
        dialog.title = "¡Felicidades!, un paso mas..."
        dialog.description = "Valida tu cuenta con el link que recibiras en en Email ingresado, luego podras usar tus datos para ingresar a la app"
        dialog.result = TypeDialog.GO_TO_HOME
        resultRegisterMutable.value = dialog
    }


    fun saveImageUserInStorage(image: Uri) {

        repository.saveUserImageInStorage(image)
    }

    fun goToLogin(){
        val intent = Intent(context, LoginActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(intent)
    }
}