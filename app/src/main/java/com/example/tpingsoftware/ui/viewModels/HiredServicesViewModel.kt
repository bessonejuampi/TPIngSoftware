package com.example.tpingsoftware.ui.viewModels

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tpingsoftware.data.models.Request
import com.example.tpingsoftware.data.models.getState
import com.example.tpingsoftware.di.repository.ServiceRepositoryContract
import com.example.tpingsoftware.utils.Dialog
import com.example.tpingsoftware.utils.TypeDialog
import com.google.firebase.firestore.DocumentSnapshot
import kotlinx.coroutines.launch

class HiredServicesViewModel(
    private val context: Context,
    private val repository : ServiceRepositoryContract
) : ViewModel() {

    private var _hiredServicesMutable = MutableLiveData<ArrayList<Request>>()
    var hiredServicesLiveData : LiveData<ArrayList<Request>> = _hiredServicesMutable

    private val _resultMutable = MutableLiveData<Dialog>()
    val resultLiveData:LiveData<Dialog> = _resultMutable

    fun getHiredServicesFromUser(user: String) {

        viewModelScope.launch {
            repository.getHiredServicesUser(user).get().addOnCompleteListener {

                if (it.isSuccessful){
                    _hiredServicesMutable.value = toRequest(it.result.documents)
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

    private fun toRequest(documents: List<DocumentSnapshot>): ArrayList<Request> {

        val listRequest : ArrayList<Request> = arrayListOf()

        documents.forEach { document ->

            val request = Request(
                document.id,
                document.getString("idProvider")!!,
                document.getString("titleService")!!,
                document.getString("idRequestingUser")!!,
                document.getString("idService")!!,
                getState(document.getString("state")!!)
            )

            listRequest.add(request)
        }

        return listRequest
    }

}