package com.example.tpingsoftware.di.repository

import android.content.Context
import android.net.Uri
import com.example.tpingsoftware.data.models.Availability
import com.example.tpingsoftware.data.models.Location
import com.example.tpingsoftware.data.models.Province
import com.example.tpingsoftware.data.models.Service
import com.example.tpingsoftware.data.network.ApiClient
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.util.UUID

interface ServiceRepositoryContract{

    suspend fun getAllService(): Task<QuerySnapshot>

    suspend fun getProvinces(): ArrayList<Province>

    suspend fun getLocalities(idProvince:Int):ArrayList<Location>

    suspend fun saveService(service:Service): Task<Void>

    suspend fun saveAvailability(availability: ArrayList<Availability>, serviceId : String)

    fun saveImageServiceInStorage(image: Uri, id:String)

    suspend fun getImageOfAService(idImage:Long) : StorageReference

}

class ServicesRepository(
    private val apiClient : ApiClient,
    private val firestore : FirebaseFirestore,
    private val storage: FirebaseStorage,
    private val context: Context
) : ServiceRepositoryContract{
    override suspend fun getAllService(): Task<QuerySnapshot> {

        return firestore.collection("services").get()
    }

    override suspend fun getProvinces(): ArrayList<Province> {

        val listProvinces = arrayListOf<Province>()
        val listProvincesResponse = apiClient.getProvinces()
        if (listProvincesResponse.isSuccessful){
            listProvincesResponse.body()?.listProvinces?.forEach { province ->
                listProvinces.add(province)
            }
        }
        return listProvinces
    }

    override suspend fun getLocalities(idProvince: Int): ArrayList<Location> {

        val listLocalities = arrayListOf<Location>()
        val listLocalitiesFirstResponse = apiClient.getLocalities(idProvince, 1)
        if (listLocalitiesFirstResponse.isSuccessful){
            val numberOfLocalities =  listLocalitiesFirstResponse.body()?.total
            val secondResponse = apiClient.getLocalities(idProvince, numberOfLocalities!!)
            secondResponse.body()?.localities?.forEach { location ->
                if (location.municipality.id != 0){
                    listLocalities.add(location)
                }
            }
        }
        return listLocalities
    }

    override suspend fun saveService(service: Service): Task<Void> {
        return firestore.collection("services")
            .document(service.id).set(
                hashMapOf(
                    "id" to service.id,
                    "title" to service.title,
                    "description" to service.description,
                    "province" to service.province,
                    "location" to service.location,
                    "address" to service.address,
                    "idImage" to service.idImage,
                    "idProvider" to service.idProvider
                )
            )
    }

    override suspend fun saveAvailability(availability: ArrayList<Availability>, serviceId : String) {

        availability.forEach {
            val id = UUID.randomUUID().toString()
            firestore.collection("availability")
                .document(id).set(
                    hashMapOf(
                        "id" to id,
                        "date" to it.date,
                        "hour" to it.hour,
                        "idService" to serviceId
                    )
                )
        }


    }

    override fun saveImageServiceInStorage(image: Uri, id:String) {
        storage.reference.child("imagesService/${id}").putFile(image)
    }

    override suspend fun getImageOfAService(idImage: Long): StorageReference {

        return storage.reference.child("imagesService//$idImage")
    }
}