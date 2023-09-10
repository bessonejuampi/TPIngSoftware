package com.example.tpingsoftware.ui.viewModels

import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tpingsoftware.data.models.Request
import com.example.tpingsoftware.data.models.Service
import com.example.tpingsoftware.data.models.getState
import com.example.tpingsoftware.di.repository.ServiceRepositoryContract
import com.example.tpingsoftware.ui.view.HomeActivity
import com.example.tpingsoftware.utils.Dialog
import com.example.tpingsoftware.utils.TypeDialog
import com.google.firebase.firestore.DocumentSnapshot
import kotlinx.coroutines.launch

class RequestReceivedViewModel(
    private val repository : ServiceRepositoryContract,
    private val context: Context
) : ViewModel() {

    private var _requestsReceivedMutable = MutableLiveData<ArrayList<Request>>()
    var requestsReceivedLiveData : LiveData<ArrayList<Request>> = _requestsReceivedMutable

    private val _listServiceFromUserMutable = MutableLiveData<ArrayList<Service>>()
    val listServiceFromUserLiveData:LiveData<ArrayList<Service>> = _listServiceFromUserMutable

    private val _resultMutable = MutableLiveData<Dialog>()
    val resultLiveData:LiveData<Dialog> = _resultMutable
    fun getRequestReceived(user: String?) {

        viewModelScope.launch {

            repository.getRequestReceived(user!!).get().addOnCompleteListener {
                if (it.isSuccessful){
                    _requestsReceivedMutable.value = toRequest(it.result.documents)
                }else{
                    _resultMutable.value = Dialog(
                        "Ha ocurrido un error",
                        it.exception?.message,
                        TypeDialog.DISMISS
                    )
                }
            }
        }

    }

    fun getServiceFromUser(email:String){
        viewModelScope.launch {
            val response = repository.getServicesFromUser(email)

            response.get().addOnCompleteListener {

                if (it.isSuccessful){
                    _listServiceFromUserMutable.value = toServiceFromUser(it.result.documents)
                }else{

                }
            }
        }
    }

    private fun toServiceFromUser(documents: List<DocumentSnapshot>): ArrayList<Service> {

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

            listService.add(service)
        }

        return listService

    }


    private fun toRequest(documents: List<DocumentSnapshot>): ArrayList<Request> {

        val listRequest : ArrayList<Request> = arrayListOf()

        documents.forEach { document ->

            val request = Request(
                document.id,
                document.getString("idProvider")!!,
                document.getString("idRequestingUser")!!,
                document.getString("idService")!!,
                getState(document.getString("state")!!)
            )

            listRequest.add(request)
        }

        return listRequest
    }

    fun acceptRequest(request: Request) {

        viewModelScope.launch {
            repository.changeStateRequest(request, "accepted").addOnCompleteListener {
                if (it.isSuccessful){
                    _resultMutable.value = Dialog(
                        "Solicitud aceptada!",
                        "Felicidades, ha aceptado un nueva solicitud de servico",
                        TypeDialog.GO_TO_HOME
                    )
                }else{
                    _resultMutable.value = Dialog(
                        "Ha ocurrido un error",
                        it.exception?.message,
                        TypeDialog.DISMISS
                    )
                }
            }
        }
    }

    fun finishRequest(request: Request) {
        viewModelScope.launch {
            repository.changeStateRequest(request, "finished").addOnCompleteListener {
                if (it.isSuccessful){
                    _resultMutable.value = Dialog(
                        "Servicio finalizado!",
                        "Felicidades, has completado un servicio",
                        TypeDialog.GO_TO_HOME
                    )
                }else{
                    _resultMutable.value = Dialog(
                        "Ha ocurrido un error",
                        it.exception?.message,
                        TypeDialog.DISMISS
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