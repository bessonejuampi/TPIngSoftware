package com.example.tpingsoftware.data.models

data class Request(
    val id :String,
    val idProvider:String,
    val idRequestingUser:String,
    val idService : String,
    val state : TypeStates
)

enum class TypeStates {
    PENDING,
    FINISHED,
    ACCEPTED,
    REJECTED,
    ERROR
}

fun getState(state : String): TypeStates {
    return when(state){
        "pending" -> TypeStates.PENDING
        "finished" -> TypeStates.FINISHED
        "accepted" -> TypeStates.ACCEPTED
        "rejected" -> TypeStates.REJECTED
        else -> TypeStates.ERROR
    }
}