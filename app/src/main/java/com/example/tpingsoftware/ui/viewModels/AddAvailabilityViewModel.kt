package com.example.tpingsoftware.ui.viewModels

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tpingsoftware.data.models.Availability
import com.example.tpingsoftware.data.models.Service
import com.example.tpingsoftware.di.repository.ServiceRepositoryContract
import com.example.tpingsoftware.ui.view.HomeActivity
import com.example.tpingsoftware.utils.Dialog
import com.example.tpingsoftware.utils.TypeDialog
import kotlinx.coroutines.launch

class AddAvailabilityViewModel(
    private val context : Context,
    private val repository : ServiceRepositoryContract
):ViewModel() {

    private var _resultRegisterServiceMutable = MutableLiveData<Dialog>()
    var resultRegisterServiceLiveData : LiveData<Dialog> = _resultRegisterServiceMutable
    fun saveService(service: Service, imageSelected: Uri?, listItems: ArrayList<Availability>) {

        viewModelScope.launch {

            val task = repository.saveService(service)
            if (imageSelected != null) {
                repository.saveImageServiceInStorage(imageSelected, service.idImage.toString())
            }
            repository.saveAvailability(listItems, service.id)

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

    fun goToHome() {

        val intent = Intent(context, HomeActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(intent)

    }
}