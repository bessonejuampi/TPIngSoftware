package com.example.tpingsoftware.data.models

data class User(
    var email: String?,
    var name: String?,
    var lastName: String?,
    var province: String?,
    var location: String?,
    var address: String?,
    var hasImageProfile: Boolean?,
    var idImage: String?
)