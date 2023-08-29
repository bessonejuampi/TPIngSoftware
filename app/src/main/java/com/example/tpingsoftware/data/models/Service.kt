package com.example.tpingsoftware.data.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Service(
    var id : String,
    var title : String,
    var description : String,
    var province : String,
    var location : String,
    var address : String,
    var idImage : Long? = null,
    var idProvider : String
):Parcelable