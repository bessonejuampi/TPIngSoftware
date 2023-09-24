package com.example.tpingsoftware.ui.viewModels

import android.content.Context
import android.content.Intent
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tpingsoftware.data.models.Service
import com.example.tpingsoftware.di.repository.ServiceRepositoryContract
import com.example.tpingsoftware.ui.view.HomeActivity
import com.example.tpingsoftware.utils.Dialog
import com.example.tpingsoftware.utils.TypeDialog
import kotlinx.coroutines.launch

class EditMyServiceViewModel(
    private val context: Context,
    private val repository:ServiceRepositoryContract
):ViewModel() {

    private var _resultEditServiceMutable = MutableLiveData<Dialog>()
    var resultEditServiceLiveData:LiveData<Dialog> = _resultEditServiceMutable
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
}