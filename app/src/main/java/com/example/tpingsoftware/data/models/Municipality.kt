package com.example.tpingsoftware.data.models

import com.google.gson.annotations.SerializedName

data class Municipality(
    @SerializedName("id") val id : Int,
    @SerializedName("nombre") val name : String
)
