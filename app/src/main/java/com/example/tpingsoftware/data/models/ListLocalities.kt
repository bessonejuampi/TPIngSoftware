package com.example.tpingsoftware.data.models

import com.google.gson.annotations.SerializedName

data class ListLocalities(
    @SerializedName("total") val total : Int,
    @SerializedName("localidades") val localities: List<Location>
)
