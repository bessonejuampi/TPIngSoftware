package com.example.tpingsoftware.data.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class ListProvinces(
    @SerializedName("provincias") var listProvinces:List<Province>?
): Parcelable