package com.example.tpingsoftware.data.models

import com.google.gson.annotations.SerializedName

data class Location(
    @SerializedName("municipio") var municipality : Municipality
)
