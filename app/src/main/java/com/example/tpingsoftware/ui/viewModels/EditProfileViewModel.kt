package com.example.tpingsoftware.ui.viewModels

import android.content.Context
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tpingsoftware.R
import com.example.tpingsoftware.data.models.Location
import com.example.tpingsoftware.data.models.Province
import com.example.tpingsoftware.data.models.User
import com.example.tpingsoftware.di.repository.UserRepositoryContract
import com.example.tpingsoftware.utils.AppPreferences
import com.example.tpingsoftware.utils.Dialog
import com.example.tpingsoftware.utils.TypeDialog
import com.example.tpingsoftware.utils.UserValidator
import com.example.tpingsoftware.utils.isAddress
import com.example.tpingsoftware.utils.isEmail
import com.example.tpingsoftware.utils.isText
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.storage.StorageReference
import kotlinx.coroutines.launch

class EditProfileViewModel(
    private val repository: UserRepositoryContract,
    private val context: Context
) : ViewModel() {

    private var _userMutableLiveData = MutableLiveData<User>()
    var userLiveData : LiveData<User> = _userMutableLiveData

    private var _imageProfileMutableLiveData = MutableLiveData<Uri>()
    var imageProfileLiveData : LiveData<Uri> = _imageProfileMutableLiveData

    private var _listProvincesMutable = MutableLiveData<ArrayList<Province>>()
    var listProvinceLiveData : LiveData<ArrayList<Province>> = _listProvincesMutable

    private var _listLocalitiesMutable = MutableLiveData<ArrayList<Location>>()
    var listLocalitiesLiveData : LiveData<ArrayList<Location>> = _listLocalitiesMutable

    private var _userValidationMutable = MutableLiveData<UserValidator?>()
    var userValidationLiveData : LiveData<UserValidator?> = _userValidationMutable



    fun validationUser(
        email :String,
        name: String?,
        lastName: String?,
        province:String?,
        location:String?,
        address:String?,
        hasImageProfile: Boolean,
        idImage: String?
    ) {
        var userValidator = UserValidator()

        //showProgress.value = true

        if (!name.isText()) {
            userValidator.nameError = context.getString(R.string.text_input_mandatory)
        }
        if (!lastName.isText()) {
            userValidator.lastNameError = context.getString(R.string.text_input_mandatory)
        }

        if (!province.isText()){
            userValidator.provinceError = context.getString(R.string.text_input_mandatory)
        }
        if (!location.isText()){
            userValidator.locationError = context.getString(R.string.text_input_mandatory)
        }

        if (address.isNullOrEmpty()){
            userValidator.addressError = context.getString(R.string.text_input_mandatory)
        }else{
            if (!address.isAddress()){
                userValidator.addressError = "Por favor, introduzca una driección válida"
            }
        }
        if (!address.isAddress()){
            userValidator.addressError = context.getString(R.string.text_input_mandatory)
        }

        if (userValidator.isSuccessfully()) {
            updateUser(email, name!!, lastName!!, province, location, address, hasImageProfile, idImage)
//            )
        } else {
            //showProgress.value = false
        }
        _userValidationMutable.value = userValidator

    }

    private fun updateUser(
        email: String,
        name: String,
        lastName: String,
        province: String?,
        location: String?,
        address: String?,
        hasImageProfile: Boolean,
        idImage: String?
    ) {

        viewModelScope.launch {
            val task = repository.updateUser(name, lastName, email, province, location, address, hasImageProfile, idImage)

            task.addOnCompleteListener {
                if (task.isSuccessful){
                    Log.i("Actualizado", "")
                }else{
                    Log.i("fallo actualizar", task.exception?.message.toString())
                }
            }
        }
    }



    fun getUserData(){

        viewModelScope.launch {
            repository.getUserDataFromFirebase(AppPreferences.getUserSession(context)!!)
                .addOnCompleteListener{ task ->
                    if (task.isSuccessful){
                        val user = fromFirstoreToUser(task.result)
                        _userMutableLiveData.value = user
                    }else{

                    }
            }

        }
    }

    fun getImageProfile(idImage : String) {

        viewModelScope.launch {
            repository.getImageProfile(idImage).downloadUrl.addOnSuccessListener {
                _imageProfileMutableLiveData.value = it
            }
        }
    }

    fun getProvinces(){
        viewModelScope.launch {
            val listProvinces = repository.getProvinces()
            if (!listProvinces.isNullOrEmpty()){
                _listProvincesMutable.value = listProvinces
            }
        }
    }

    fun getLocalities(idProvince : Int){
        viewModelScope.launch {
            val listLocalities = repository.getLocalities(idProvince)
            if (!listLocalities.isNullOrEmpty()){
                _listLocalitiesMutable.value = listLocalities
            }
        }
    }

    private fun fromFirstoreToUser(result: DocumentSnapshot) : User {

        var userImage: String? = null
        if (result.getBoolean("hasImageProfile")!!){
            userImage = result.getString("idImage")
        }
        return User(
            result.id,
            result.getString("name"),
            result.getString("lastName"),
            result.getString("province"),
            result.getString("location"),
            result.getString("address"),
            result.getBoolean("hasImageProfile"),
            userImage

        )
    }

    fun saveImageUserInStorage(image: Uri ,id:String) {

        repository.saveUserImageInStorage(image, id)
    }



}