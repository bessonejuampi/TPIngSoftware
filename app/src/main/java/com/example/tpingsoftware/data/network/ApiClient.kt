package com.example.tpingsoftware.data.network

import com.example.tpingsoftware.data.models.ListLocalities
import com.example.tpingsoftware.data.models.ListProvinces
import com.google.android.gms.common.internal.safeparcel.SafeParcelable.Param
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiClient {

    @GET("provincias")
    suspend fun getProvinces(
    ) :Response<ListProvinces>

    @GET("localidades")
    suspend fun getLocalities(
        @Query("provincia") idProvince : Int,
        @Query("max") max:Int) :Response<ListLocalities>
}