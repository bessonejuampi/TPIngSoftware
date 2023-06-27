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
import com.example.tpingsoftware.utils.AppPreferences
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
            registerUser(email!!, password!!, name!!, lastName!!, latitude, longitude, hasImageProfile)
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
        hasImageProfile : Boolean
    ) {
        val dialog = Dialog()
        viewModelScope.launch {
            val result = repository.registerNewUser(email, password)
            result.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    repository.saveUserInFireStore(name, lastName, email, latitude, longitude, hasImageProfile)
                    dialog.title = "¡Felicidades!"
                    dialog.description = "Ya puedes usar tu cuenta para navegar por la app"
                    dialog.result = true
                    resultRegisterMutable.value = dialog
                } else {
                    dialog.title = "Algo ha salido mal"
                    dialog.description = task.exception.toString()
                    dialog.result = false
                    resultRegisterMutable.value = dialog
                }
            }
        }
    }

    fun saveImageToInternalStorage(imageUri: Uri) {

        try {
            val contentResolver = context.contentResolver
            val inputStream = contentResolver.openInputStream(imageUri)
            val bitmap = BitmapFactory.decodeStream(inputStream)
            inputStream?.close()

            val filename = Utils.generateRandomLettersAndNumbers()

            val outputStream = context.openFileOutput(filename, Context.MODE_PRIVATE)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
            outputStream.close()


            AppPreferences.setImageProfile(context, filename)

            Toast.makeText(context, "Imagen guardada exitosamente", Toast.LENGTH_SHORT).show()

        } catch (e: IOException) {
            e.printStackTrace()
            Toast.makeText(context, "Error al guardar la imagen", Toast.LENGTH_SHORT).show()
        }
    }

    fun saveImageUserInStorage(image:Uri){

        repository.saveUserImageInStorage(image)
    }

    fun goToHome() {
        val intent = Intent(context, HomeActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(intent)
    }
}