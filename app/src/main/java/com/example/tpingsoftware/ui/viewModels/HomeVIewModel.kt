package com.example.tpingsoftware.ui.viewModels

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tpingsoftware.data.models.Service
import com.example.tpingsoftware.di.repository.ServiceRepositoryContract
import com.example.tpingsoftware.ui.view.AddServiceActivity
import com.example.tpingsoftware.ui.view.EditProfileActivity
import com.example.tpingsoftware.ui.view.ForgotPasswordActivity
import com.example.tpingsoftware.ui.view.HiredServicesActivity
import com.example.tpingsoftware.ui.view.HomeActivity
import com.example.tpingsoftware.ui.view.LoginActivity
import com.example.tpingsoftware.ui.view.RequestsReceivedActivity
import com.example.tpingsoftware.utils.AppPreferences
import com.google.firebase.firestore.DocumentSnapshot
import kotlinx.coroutines.launch

class HomeVIewModel(
    private val context: Context,
    private val repository: ServiceRepositoryContract
):ViewModel() {


    private var numberOfImagesLoaded = 0

    private val _listServiceMutable = MutableLiveData<ArrayList<Service>>()
    val listServiceLiveData:LiveData<ArrayList<Service>> = _listServiceMutable

    private val _listServiceFromUserMutable = MutableLiveData<ArrayList<Service>>()
    val listServiceFromUserLiveData:LiveData<ArrayList<Service>> = _listServiceFromUserMutable

    private val _listServiceFavoritesFromUserMutable = MutableLiveData<List<Service>>()
    val listServiceFavoritesFromUserLiveData:LiveData<List<Service>> = _listServiceFavoritesFromUserMutable

    fun getAllService() {

        viewModelScope.launch {

            val response = repository.getAllService()
            response.addOnCompleteListener {
                if (it.isSuccessful){

                    toService(response.result.documents)
                }
            }
        }

    }

    fun getServiceFromUser(email:String){
        viewModelScope.launch {
            val response = repository.getServicesFromUser(email)

            response.get().addOnCompleteListener {

                if (it.isSuccessful){
                    toServiceFromUser(it.result.documents)
                }else{
                    _listServiceFromUserMutable.value?.clear()
                }
            }
        }
    }

    fun getFavoritesServices(user: String) {

        viewModelScope.launch {
            val response = repository.getFavoritesServices(user)
           toServiceFromFavorites(response)
        }
    }

    private fun toServiceFromFavorites(documents: List<DocumentSnapshot>) {

        val listService: ArrayList<Service> = arrayListOf()

        if(documents.isNotEmpty()){
            documents.forEach { document ->
                val service = Service(
                    document.id,
                    document.getString("title")!!,
                    document.getString("description")!!,
                    document.getString("province")!!,
                    document.getString("location")!!,
                    document.getString("address")!!,
                    document.getLong("idImage"),
                    document.getString("idProvider")!!,
                    null
                )

                if (service.idImage != null) {
                    getImageOfAService(service.idImage!!) { imageUri ->
                        service.imageUir = imageUri
                        numberOfImagesLoaded++
                        listService.add(service)

                        if (numberOfImagesLoaded == documents.size) {

                            _listServiceFromUserMutable.value = listService
                        }
                    }
                }else{
                    numberOfImagesLoaded++
                    listService.add(service)

                    if (numberOfImagesLoaded == documents.size) {

                        _listServiceFavoritesFromUserMutable.value = listService
                    }
                }



            }
        }else{
            _listServiceFavoritesFromUserMutable.value = ArrayList()
        }
    }

    private fun toServiceFromUser(documents: List<DocumentSnapshot>) {

        val listService: ArrayList<Service> = arrayListOf()

        if(documents.isNotEmpty()){
            documents.forEach { document ->
                val service = Service(
                    document.id,
                    document.getString("title")!!,
                    document.getString("description")!!,
                    document.getString("province")!!,
                    document.getString("location")!!,
                    document.getString("address")!!,
                    document.getLong("idImage"),
                    document.getString("idProvider")!!,
                    null
                )

                if (service.idImage != null) {
                    getImageOfAService(service.idImage!!) { imageUri ->
                        service.imageUir = imageUri
                        numberOfImagesLoaded++
                        listService.add(service)

                        if (numberOfImagesLoaded == documents.size) {

                            _listServiceFromUserMutable.value = listService
                        }
                    }
                }else{
                    numberOfImagesLoaded++
                    listService.add(service)

                    if (numberOfImagesLoaded == documents.size) {

                        _listServiceFromUserMutable.value = listService
                    }
                }



            }
        }else{
            _listServiceFromUserMutable.value = ArrayList()
        }

    }

    private fun getImageOfAService(idImage: Long, callback: (Uri) -> Unit){

        viewModelScope.launch {
            repository.getImageOfAService(idImage).downloadUrl.addOnSuccessListener {
                callback(it)
            }
        }
    }

    private fun toService(documents: List<DocumentSnapshot>) {

        val listService: ArrayList<Service> = arrayListOf()

        documents.forEach { document ->
            val service = Service(
                document.id,
                document.getString("title")!!,
                document.getString("description")!!,
                document.getString("province")!!,
                document.getString("location")!!,
                document.getString("address")!!,
                document.getLong("idImage"),
                document.getString("idProvider")!!,
                null
            )

            if (service.idImage != null) {
                getImageOfAService(service.idImage!!) { imageUri ->
                    service.imageUir = imageUri
                    numberOfImagesLoaded++
                    listService.add(service)

                    if (numberOfImagesLoaded == documents.size) {

                        _listServiceMutable.value = listService
                    }
                }
            }else{
                numberOfImagesLoaded++
                listService.add(service)

                if (numberOfImagesLoaded == documents.size) {

                    _listServiceMutable.value = listService
                }
            }



        }
    }



    fun SignOut(){
        AppPreferences.deletePreferences(context)
        goToLogin()
    }

    private fun goToLogin(){
        val intent = Intent(context, LoginActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(intent)
    }

    fun goToEditProfile() {
        val intent = Intent(context, EditProfileActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(intent)
    }

    fun goToAddService(){
        val intent = Intent(context, AddServiceActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(intent)
    }

    fun goToEditPass() {

        val intent = Intent(context, ForgotPasswordActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(intent)
    }

    fun goToRequestsReceived() {

        val intent = Intent(context, RequestsReceivedActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(intent)
    }

    fun goToHiredServices() {

        val intent = Intent(context, HiredServicesActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(intent)
    }
}