package com.example.tpingsoftware.utils

import kotlin.random.Random

class Utils {
    companion object {
        fun generateRandomLettersAndNumbers(): String {
            val characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789"
            val sb = StringBuilder(5)
            val random = Random

            for (i in 0 until 5) {
                val index = random.nextInt(characters.length)
                val randomChar = characters[index]
                sb.append(randomChar)
            }

            return sb.toString()
        }
    }



}