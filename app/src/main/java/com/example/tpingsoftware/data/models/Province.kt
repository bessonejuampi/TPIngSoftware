package com.example.tpingsoftware.data.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Province(
    @SerializedName("id") var id : Int,
    @SerializedName("nombre") var name : String
):Parcelable
