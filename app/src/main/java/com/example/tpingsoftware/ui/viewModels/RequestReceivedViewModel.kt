package com.example.tpingsoftware.ui.viewModels

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tpingsoftware.di.repository.ServiceRepositoryContract
import kotlinx.coroutines.launch

class RequestReceivedViewModel(
    private val repository : ServiceRepositoryContract,
    private val context: Context
) : ViewModel() {

    fun getRequestReceived(user: String?) {

        viewModelScope.launch {

            repository.getRequestReceived(user!!).get().addOnCompleteListener {

                if (it.isSuccessful){
                    Log.i("", "")
                }else{
                    Log.i("", "")
                }
            }
        }

    }
}