package com.example.tpingsoftware.utils

class ServiceValidator() {
    var titleError : String? = null
    var descriptionError : String? = null
    var provinceError : String? = null
    var locationError : String? = null
    var addressError : String? = null

    fun isSuccessfully(): Boolean {
        return titleError.isNullOrEmpty() && descriptionError.isNullOrEmpty() &&
                provinceError.isNullOrEmpty() && locationError.isNullOrEmpty() && addressError.isNullOrEmpty()
    }

}