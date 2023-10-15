package com.example.tpingsoftware.ui.viewModels

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tpingsoftware.data.models.Appreciation
import com.example.tpingsoftware.data.models.Location
import com.example.tpingsoftware.data.models.Province
import com.example.tpingsoftware.data.models.Service
import com.example.tpingsoftware.di.repository.ServiceRepositoryContract
import com.example.tpingsoftware.ui.view.AddServiceActivity
import com.example.tpingsoftware.ui.view.HomeActivity
import com.example.tpingsoftware.utils.Constants
import com.example.tpingsoftware.utils.Dialog
import com.example.tpingsoftware.utils.TypeDialog
import kotlinx.coroutines.launch

class EditMyServiceViewModel(
    private val context: Context,
    private val repository:ServiceRepositoryContract
):ViewModel() {

    private var _resultEditServiceMutable = MutableLiveData<Dialog>()
    var resultEditServiceLiveData:LiveData<Dialog> = _resultEditServiceMutable

    private var _appreciationMutable = MutableLiveData<List<Appreciation>>()
    var appreciationLiveData:LiveData<List<Appreciation>> = _appreciationMutable

    fun deleteService(service: Service) {

        viewModelScope.launch {

            repository.deleteService(service)
                .addOnCompleteListener {
                    if (it.isSuccessful){
                        _resultEditServiceMutable.value = Dialog(
                            "Servicio eliminado",
                            "EL servicio fue eliminado junto con sus datos y contrataciones que aún no fueron aceptadas o finalizadas",
                            TypeDialog.GO_TO_HOME
                        )
                    }else{
                        _resultEditServiceMutable.value = Dialog(
                            "Ha ocurrido un error",
                            it.exception?.message,
                            TypeDialog.GO_TO_HOME
                        )
                    }
                }
        }
    }

    fun goToHome(){
        val intent = Intent(context, HomeActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

        context.startActivity(intent)
    }

    fun goToEditService(service: Service) {

        val bundle = Bundle()
        bundle.putParcelable(Constants.KEY_SERVICE, service)

        val intent = Intent(context, AddServiceActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.putExtra(Constants.KEY_BUNDLE_SERVICE_TO_DETAILS, bundle)

        context.startActivity(intent)
    }

    fun getAppreciations(idService: String) {

        viewModelScope.launch {
            _appreciationMutable.value = repository.getAppreciations(idService)
        }
    }
}