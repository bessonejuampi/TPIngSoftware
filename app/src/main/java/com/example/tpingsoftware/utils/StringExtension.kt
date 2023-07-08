package com.example.tpingsoftware.utils

import android.util.Patterns

fun String?.isNumber(): Boolean {
    return if (this.isNullOrEmpty()) {
        false
    } else {
        try {
            this.toDouble() // Si llega a esta punto y no puede convertirlo a un numero significa
            // que habia caracteres en el string, entoces retorna un false
            true
        } catch (e: Exception) {
            false
        }
    }
}
fun String?.isText(): Boolean {
    return !this.isNullOrEmpty()
}

fun String?.isAddress():Boolean {

    val addressRegexExpression = Regex("^[a-zA-Z]+ [0-9]+$")
    return addressRegexExpression.matches(this.toString())
}

fun String?.isEmail():Boolean{
    return Patterns.EMAIL_ADDRESS.matcher(this.toString()).matches()
}