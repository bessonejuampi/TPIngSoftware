package com.example.tpingsoftware.ui.viewModels

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tpingsoftware.data.models.Availability
import com.example.tpingsoftware.data.models.Service
import com.example.tpingsoftware.di.repository.ServiceRepositoryContract
import kotlinx.coroutines.launch

class AddAvailabilityViewModel(
    private val repository : ServiceRepositoryContract
):ViewModel() {
    fun saveService(service: Service, imageSelected: Uri?, listItems: ArrayList<Availability>) {

        viewModelScope.launch {

            repository.saveService(service)
            if (imageSelected != null) {
                repository.saveImageServiceInStorage(imageSelected, service.idImage.toString())
            }
            repository.saveAvailability(listItems, service.id)
        }

    }
}