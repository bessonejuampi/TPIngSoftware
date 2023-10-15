package com.example.tpingsoftware.ui.viewModels

import android.content.Context
import android.content.Intent
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tpingsoftware.data.models.Appreciation
import com.example.tpingsoftware.di.repository.ServiceRepositoryContract
import com.example.tpingsoftware.ui.view.HomeActivity
import com.example.tpingsoftware.utils.Dialog
import com.example.tpingsoftware.utils.TypeDialog
import kotlinx.coroutines.launch

class DetailServiceVIewModel(
    private val repository: ServiceRepositoryContract,
    private val context: Context
) : ViewModel() {

    private var _requestServiceMutable = MutableLiveData<Dialog>()
    var requestServiceLiveData: LiveData<Dialog> = _requestServiceMutable

    private var _isFavoriteServiceMutable = MutableLiveData<Boolean>()
    var isFavoriteServiceLiveData:LiveData<Boolean> = _isFavoriteServiceMutable

    private var _appreciationMutable = MutableLiveData<List<Appreciation>>()
    var appreciationLiveData:LiveData<List<Appreciation>> = _appreciationMutable


    fun sendRequest(
        idService: String,
        requestingUser: String?,
        idProvider: String,
        titleService: String
    ) {

        viewModelScope.launch {

            repository.sendRequest(idService, requestingUser!!, idProvider, titleService)
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        _requestServiceMutable.value = Dialog(
                            "Solicitud enviada!",
                            "Podrá revisar el estado de su solicitud en la pestaña de Servicios contratados",
                            TypeDialog.GO_TO_HOME
                        )
                    } else {
                        _requestServiceMutable.value = Dialog(
                            "Ha ocurrio un error",
                            it.exception?.message,
                            TypeDialog.DISMISS
                        )
                    }
                }
        }
    }

    fun addFavorite(idService: String) {

        viewModelScope.launch {
            val addFavoriteIsSuccess = repository.addFavorite(idService)
            if (!addFavoriteIsSuccess) {
                _requestServiceMutable.value = Dialog(
                    "Ha ocurrido un error",
                    "No hemos podido guardar este servicio en tunlista de favoritos. Por favor, intentalo nuevamente",
                    TypeDialog.DISMISS
                )
            }
        }
    }

    fun isFavoriteService(idService: String) {

        viewModelScope.launch {
            if (repository.isFavoriteService(idService)){
                _isFavoriteServiceMutable.value = true
            }
        }
    }

    fun deleteFavorite(idService: String) {

        viewModelScope.launch {
            repository.deleteFavoriteService(idService)
        }
    }

    fun goToHome() {
        val intent = Intent(context, HomeActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

        context.startActivity(intent)
    }

    fun getAppreciations(idService: String) {

        viewModelScope.launch {
            _appreciationMutable.value = repository.getAppreciations(idService)
        }

    }


}