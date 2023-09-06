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
import com.example.tpingsoftware.ui.view.AddAvailabilityActivity
import com.example.tpingsoftware.utils.AppPreferences
import com.example.tpingsoftware.utils.Constants
import com.example.tpingsoftware.utils.ServiceValidator
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

    fun validateService(
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
        }else {
            if (!address.isAddress()) {
                serviceValidator.addressError = "Por favor, introduzca una driección válida"
            }
        }

        if (serviceValidator.isSuccessfully()) {
            val service = Service(
                UUID.randomUUID().toString(),
                title!!,
                description!!,
                province!!,
                location!!,
                address!!,
                idImage,
                AppPreferences.getUserSession(context)!!,
                null
            )

            goToAddAvailability(service, selectedImageLogoUri)
        }
        _serviceMutableLiveData.value = serviceValidator
    }

    private fun goToAddAvailability(service: Service, selectedImageLogoUri: Uri?) {
        val bundle = Bundle()
        bundle.putParcelable(Constants.KEY_IMAGE_SELECTED, selectedImageLogoUri)
        bundle.putParcelable(Constants.KEY_SERVICE_BUNDLE, service)

        val intent = Intent(context, AddAvailabilityActivity::class.java)
        intent.putExtra(Constants.KEY_EXTRAS_ADD_AVAILABILITY,bundle)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(intent)
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

}