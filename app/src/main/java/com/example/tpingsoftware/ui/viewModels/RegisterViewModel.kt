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
import com.example.tpingsoftware.ui.view.HomeActivity
import com.example.tpingsoftware.utils.Dialog
import com.example.tpingsoftware.utils.UserValidator
import com.example.tpingsoftware.utils.Utils
import com.example.tpingsoftware.utils.isEmail
import com.example.tpingsoftware.utils.isText
import kotlinx.coroutines.launch
import android.widget.Toast
import com.example.tpingsoftware.data.models.Location
import com.example.tpingsoftware.data.models.Province
import com.example.tpingsoftware.di.repository.UserRepositoryContract
import com.example.tpingsoftware.ui.view.LoginActivity
import com.example.tpingsoftware.utils.AppPreferences
import com.example.tpingsoftware.utils.TypeDialog
import com.example.tpingsoftware.utils.isAddress
import java.io.IOException

class RegisterViewModel(
    private val repository: UserRepositoryContract,
    val context: Context

) : ViewModel() {
    var userValidationMutable = MutableLiveData<UserValidator?>()
    var resultRegisterMutable = MutableLiveData<Dialog>()
    var showProgress = MutableLiveData<Boolean>()
    var listProvincesMutable = MutableLiveData<ArrayList<Province>>()
    var listLocalitiesMutable = MutableLiveData<ArrayList<Location>>()

    fun validationUser(
        name: String?,
        lastName: String?,
        email: String?,
        password: String?,
        repeatPassword: String?,
        province:String?,
        location:String?,
        address:String?,
        hasImageProfile: Boolean,
        idImage: String?
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
        }else {
            if (!email.isEmail()) {
                userValidator.emailError = context.getString(R.string.text_input_format_email)
            }
        }
        if (!province.isText()){
            userValidator.provinceError = context.getString(R.string.text_input_mandatory)
        }
        if (!location.isText()){
            userValidator.locationError = context.getString(R.string.text_input_mandatory)
        }

        if (address.isNullOrEmpty()){
            userValidator.addressError = context.getString(R.string.text_input_mandatory)
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
                province,
                location,
                address,
                hasImageProfile,
                idImage
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
        province: String?,
        location: String?,
        address: String?,
        hasImageProfile: Boolean,
        idImage: String?
    ) {
        val dialog = Dialog()

        viewModelScope.launch {
            val result = repository.registerNewUser(email, password)
            result.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    repository.sendEmailVerification()?.addOnCompleteListener {
                        resultRegisterMutable.value = dialog
                        saveUserInFirebaseFirestore(email, name, lastName, province, location, address, hasImageProfile, idImage)

                    }?.addOnFailureListener {
                        Toast.makeText(context, it.toString(), Toast.LENGTH_SHORT).show()
                    }


                } else {
                    dialog.title = "Algo ha salido mal"
                    dialog.description = task.exception!!.message.toString()
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
        province: String?,
        location: String?,
        address: String?,
        hasImageProfile: Boolean,
        idImage:String?
    ) {
        repository.saveUserInFireStore(name, lastName, email, province, location,address, hasImageProfile, idImage)
        val dialog = Dialog()
        dialog.title = "¡Felicidades!, un paso mas..."
        dialog.description = "Valida tu cuenta con el link que recibiras en en Email ingresado, luego podras usar tus datos para ingresar a la app"
        dialog.result = TypeDialog.GO_TO_HOME
        resultRegisterMutable.value = dialog
    }


    fun saveImageUserInStorage(image: Uri ,id:String) {

        repository.saveUserImageInStorage(image, id)
    }

    fun getProvinces(){
        viewModelScope.launch {
            val listProvinces = repository.getProvinces()
            if (!listProvinces.isNullOrEmpty()){
                listProvincesMutable.value = listProvinces
            }
        }
    }

    fun getLocalities(idProvince : Int){
        viewModelScope.launch {
            val listLocalities = repository.getLocalities(idProvince)
            if (!listLocalities.isNullOrEmpty()){
                listLocalitiesMutable.value = listLocalities
            }
        }
    }
    fun goToLogin(){
        val intent = Intent(context, LoginActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(intent)
    }
}