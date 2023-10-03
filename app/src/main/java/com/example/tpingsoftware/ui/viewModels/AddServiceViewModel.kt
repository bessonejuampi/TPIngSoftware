package com.example.tpingsoftware.ui.viewModels

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tpingsoftware.R
import com.example.tpingsoftware.data.models.Location
import com.example.tpingsoftware.data.models.Province
import com.example.tpingsoftware.data.models.Service
import com.example.tpingsoftware.di.repository.ServiceRepositoryContract
import com.example.tpingsoftware.ui.view.HomeActivity
import com.example.tpingsoftware.utils.AppPreferences
import com.example.tpingsoftware.utils.Constants
import com.example.tpingsoftware.utils.Dialog
import com.example.tpingsoftware.utils.ServiceValidator
import com.example.tpingsoftware.utils.TypeDialog
import com.example.tpingsoftware.utils.isAddress
import com.example.tpingsoftware.utils.isText
import kotlinx.coroutines.launch
import java.util.UUID

class AddServiceViewModel(
    private val repository : ServiceRepositoryContract,
    private val context: Context
) : ViewModel() {

    private var _listProvincesMutable = MutableLiveData<ArrayList<Province>>()
    var listProvincesLiveData : LiveData<ArrayList<Province>> = _listProvincesMutable

    private var _listLocalitiesMutable = MutableLiveData<ArrayList<Location>>()
    var listLocalitiesLiveData : LiveData<ArrayList<Location>> = _listLocalitiesMutable

    private var _serviceMutableLiveData = MutableLiveData<ServiceValidator?>()
    var serviceLiveData : LiveData<ServiceValidator?> = _serviceMutableLiveData


    private var _resultRegisterServiceMutable = MutableLiveData<Dialog>()
    var resultRegisterServiceLiveData : LiveData<Dialog> = _resultRegisterServiceMutable
    private fun saveService(service: Service, imageSelected: Uri?) {

        viewModelScope.launch {

            val task = repository.saveService(service)
            if (imageSelected != null) {
                repository.saveImageServiceInStorage(imageSelected, service.idImage.toString())
            }

            task.addOnCompleteListener {
                if (it.isSuccessful){
                    _resultRegisterServiceMutable.value = Dialog(
                        "Registro exitoso!",
                        "Tu servicio ya esta publicado, solo resta esperar las solicitudes : )",
                        TypeDialog.GO_TO_HOME)
                }else{
                    _resultRegisterServiceMutable.value = Dialog(
                        "Algo ha salido mal...",
                        it.exception?.message,
                        TypeDialog.DISMISS)
                }
            }
        }

    }

    fun validateService(
        id:String?,
        title: String?,
        description: String?,
        province: String?,
        location: String?,
        address: String?,
        idImage: Long?,
        selectedImageLogoUri: Uri?
    ){
        var serviceValidator = ServiceValidator()

        if (!title.isText()){
            serviceValidator.titleError = context.getString(R.string.text_input_mandatory)
        }

        if (!description.isText()){
            serviceValidator.descriptionError = context.getString(R.string.text_input_mandatory)
        }

        if (province.isNullOrEmpty()){
            serviceValidator.provinceError = context.getString(R.string.text_input_mandatory)
        }

        if (location.isNullOrEmpty()){
            serviceValidator.locationError = context.getString(R.string.text_input_mandatory)
        }

        if (address.isNullOrEmpty()){
            serviceValidator.addressError = context.getString(R.string.text_input_mandatory)
        }

        if (serviceValidator.isSuccessfully()) {

            var idService = id
            if (id.isNullOrEmpty()){
                idService = UUID.randomUUID().toString()
            }
            val service = Service(
                idService!!,
                title!!,
                description!!,
                province!!,
                location!!,
                address!!,
                idImage,
                AppPreferences.getUserSession(context)!!,
                null
            )

            saveService(service, selectedImageLogoUri)
        }
        _serviceMutableLiveData.value = serviceValidator
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

    fun goToHome() {

        val intent = Intent(context, HomeActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(intent)

    }

}