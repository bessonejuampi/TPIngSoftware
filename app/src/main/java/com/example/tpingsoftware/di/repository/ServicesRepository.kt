package com.example.tpingsoftware.di.repository

import android.content.Context
import android.net.Uri
import android.util.Log
import com.example.tpingsoftware.data.models.Appreciation
import com.example.tpingsoftware.data.models.Availability
import com.example.tpingsoftware.data.models.Favorites
import com.example.tpingsoftware.data.models.Location
import com.example.tpingsoftware.data.models.Province
import com.example.tpingsoftware.data.models.Request
import com.example.tpingsoftware.data.models.Service
import com.example.tpingsoftware.data.network.ApiClient
import com.example.tpingsoftware.utils.AppPreferences
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.coroutines.tasks.await
import java.util.UUID
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

interface ServiceRepositoryContract {

    suspend fun getAllService(): Task<QuerySnapshot>

    suspend fun getServicesFromUser(email: String): Query

    suspend fun getProvinces(): ArrayList<Province>

    suspend fun getLocalities(idProvince: Int): ArrayList<Location>

    suspend fun saveService(service: Service): Task<Void>

    suspend fun saveAvailability(availability: ArrayList<Availability>, serviceId: String)

    fun saveImageServiceInStorage(image: Uri, id: String)

    suspend fun getImageOfAService(idImage: Long): StorageReference

    suspend fun sendRequest(
        idService: String,
        requestingUser: String,
        idProvider: String,
        titleService: String
    ): Task<Void>

    suspend fun getRequestReceived(user: String): Query

    suspend fun changeStateRequest(request: Request, newState: String): Task<Void>

    suspend fun getHiredServicesUser(user: String): Query

    suspend fun deleteService(service: Service): Task<Void>

    suspend fun isFavoriteService(idService: String): Boolean

    suspend fun addFavorite(idService: String): Boolean

    suspend fun getFavoritesServices(user: String): List<DocumentSnapshot>

    suspend fun deleteFavoriteService(idService: String)

    fun sendAppreciateService(rating: Double, comment: String, idService: String): Task<Void>

    suspend fun getAppreciations(idService: String): List<Appreciation>

}

class ServicesRepository(
    private val apiClient: ApiClient,
    private val firestore: FirebaseFirestore,
    private val storage: FirebaseStorage,
    private val context: Context
) : ServiceRepositoryContract {
    override suspend fun getAllService(): Task<QuerySnapshot> {

        return firestore.collection("services").get()
    }

    override suspend fun getServicesFromUser(email: String): Query {

        return firestore.collection("services").whereEqualTo("idProvider", email)
    }

    override suspend fun getProvinces(): ArrayList<Province> {

        val listProvinces = arrayListOf<Province>()
        val listProvincesResponse = apiClient.getProvinces()
        if (listProvincesResponse.isSuccessful) {
            listProvincesResponse.body()?.listProvinces?.forEach { province ->
                listProvinces.add(province)
            }
        }
        return listProvinces
    }

    override suspend fun getLocalities(idProvince: Int): ArrayList<Location> {

        val listLocalities = arrayListOf<Location>()
        val listLocalitiesFirstResponse = apiClient.getLocalities(idProvince, 1)
        if (listLocalitiesFirstResponse.isSuccessful) {
            val numberOfLocalities = listLocalitiesFirstResponse.body()?.total
            val secondResponse = apiClient.getLocalities(idProvince, numberOfLocalities!!)
            secondResponse.body()?.localities?.forEach { location ->
                if (location.municipality.id != 0) {
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

    override suspend fun saveAvailability(
        availability: ArrayList<Availability>,
        serviceId: String
    ) {

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

    override fun saveImageServiceInStorage(image: Uri, id: String) {
        storage.reference.child("imagesService/${id}").putFile(image)
    }

    override suspend fun getImageOfAService(idImage: Long): StorageReference {

        return storage.reference.child("imagesService//$idImage")
    }

    override suspend fun sendRequest(
        idService: String,
        requestingUser: String,
        idProvider: String,
        titleService: String
    ): Task<Void> {

        val id = UUID.randomUUID().toString()
        return firestore.collection("request")
            .document(id).set(
                hashMapOf(
                    "id" to id,
                    "idService" to idService,
                    "titleService" to titleService,
                    "idProvider" to idProvider,
                    "idRequestingUser" to requestingUser,
                    "state" to "pending"
                )

            )
    }

    override suspend fun getRequestReceived(user: String): Query {

        return firestore.collection("request").whereEqualTo("idProvider", user)
    }

    override suspend fun changeStateRequest(request: Request, newState: String): Task<Void> {

        val updates = hashMapOf<String, Any>("state" to newState)
        return firestore.collection("request").document(request.id).update(updates)
    }

    override suspend fun getHiredServicesUser(user: String): Query {

        return firestore.collection("request").whereEqualTo("idRequestingUser", user)
    }

    override suspend fun deleteService(service: Service): Task<Void> {

        if (service.idImage != null) {
            storage.reference.child("imagesService/${service.id}").delete()
        }

        firestore.collection("request")
            .whereEqualTo("idService", service.id)
            .whereEqualTo("state", "pending")
            .get()
            .addOnSuccessListener { documents ->

                documents.forEach {
                    firestore.collection("request")
                        .document(it.id)
                        .delete()
                }
            }

        return firestore.collection("services").document(service.id).delete()

    }

    override suspend fun isFavoriteService(idService: String): Boolean =
        suspendCoroutine { continuation ->
            firestore.collection("favorites").document(AppPreferences.getUserSession(context)!!)
                .get()
                .addOnSuccessListener { documentSnapshot ->
                    if (documentSnapshot.exists()) {
                        val currentFavorites = documentSnapshot.get("services") as List<String>?
                        val isFavorite = currentFavorites?.contains(idService) == true
                        continuation.resume(isFavorite)
                    } else {
                        continuation.resume(false)
                    }
                }
                .addOnFailureListener { exception ->
                    continuation.resume(false)
                }
        }


    override suspend fun addFavorite(idService: String): Boolean {
        return try {
            val favoritesCollection =
                firestore.collection("favorites").document(AppPreferences.getUserSession(context)!!)

            val documentSnapshot = favoritesCollection.get().await()

            if (documentSnapshot.exists()) {
                val currentFavorites = documentSnapshot.get("services") as? ArrayList<String>
                if (currentFavorites != null) {
                    currentFavorites.add(idService)
                    val updateData = hashMapOf("services" to currentFavorites)
                    favoritesCollection.update(updateData as Map<String, Any>).await()
                    true
                } else {
                    false
                }
            } else {
                val newFavorites = Favorites(mutableListOf(idService))
                favoritesCollection.set(newFavorites).await()
                true
            }
        } catch (e: Exception) {

            e.printStackTrace()
            false
        }
    }

    override suspend fun getFavoritesServices(user: String): List<DocumentSnapshot> =
        suspendCoroutine { continuation ->
            val favoritesCollection = firestore.collection("favorites").document(user)

            favoritesCollection.get()
                .addOnSuccessListener { documentSnapshot ->
                    if (documentSnapshot.exists()) {
                        val currentFavorites =
                            documentSnapshot.get("services") as? ArrayList<String>

                        if (!currentFavorites.isNullOrEmpty()) {
                            val servicesQuery = firestore.collection("services")
                                .whereIn(FieldPath.documentId(), currentFavorites)

                            servicesQuery.get()
                                .addOnSuccessListener { querySnapshot ->

                                    continuation.resume(querySnapshot.documents)
                                }
                                .addOnFailureListener { exception ->
                                    // Handle any errors here
                                    continuation.resume(emptyList())
                                }
                        } else {
                            continuation.resume(emptyList())
                        }
                    } else {
                        continuation.resume(emptyList())
                    }
                }
                .addOnFailureListener { exception ->
                    continuation.resume(emptyList())
                }
        }

    override suspend fun deleteFavoriteService(idService: String) {

        val favoritesCollection =
            firestore.collection("favorites").document(AppPreferences.getUserSession(context)!!)

        val documentSnapshot = favoritesCollection.get().await()

        if (documentSnapshot.exists()) {
            val currentFavorites = documentSnapshot.get("services") as? ArrayList<String>
            if (currentFavorites != null) {
                currentFavorites.remove(idService)
                val updateData = hashMapOf("services" to currentFavorites)
                favoritesCollection.update(updateData as Map<String, Any>).await()
            }
        }
    }

    override fun sendAppreciateService(
        rating: Double,
        comment: String,
        idService: String
    ): Task<Void> {

        val serviceRef = firestore.collection("services").document(idService)
        return serviceRef.update(
            "Appreciations",
            FieldValue.arrayUnion(Appreciation(rating, comment))
        )

    }

    override suspend fun getAppreciations(idService: String): List<Appreciation> = suspendCoroutine { continuation ->

        val documentReference = firestore.collection("services").document(idService)

        documentReference.get()
            .addOnSuccessListener { documentSnapshot ->
                if (documentSnapshot.exists()) {
                    val appreciations =
                        documentSnapshot.get("Appreciations") as? ArrayList<HashMap<String, Any>>

                    val listAppreciations = arrayListOf<Appreciation>()
                    appreciations?.forEach { appreciation ->
                        val rating = appreciation["rating"] as Double
                        val comment = appreciation["comment"] as String
                        listAppreciations.add(Appreciation(rating, comment))
                    }

                    continuation.resume(listAppreciations)
                } else {
                    continuation.resume(emptyList())
                }
            }
            .addOnFailureListener { e ->
                continuation.resume(emptyList())
            }
    }
}