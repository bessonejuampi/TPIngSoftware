package com.example.tpingsoftware.utils

class UserValidator() {
    var nameError: String? = null
    var lastNameError: String? = null
    var emailError: String? = null
    var passError: String? = null
    var repeatPasswordError: String? = null


    fun isSuccessfully(): Boolean {
        return nameError.isNullOrEmpty() && lastNameError.isNullOrEmpty() && emailError.isNullOrEmpty() &&
                passError.isNullOrEmpty() && repeatPasswordError.isNullOrEmpty()
    }
}