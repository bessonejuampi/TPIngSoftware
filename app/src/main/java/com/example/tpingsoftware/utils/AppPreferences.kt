package com.example.tpingsoftware.utils

import android.content.Context

class AppPreferences {
    companion object{

        private const val APP_PREF_NAME = "APP_PREF_NAME"
        private const val FILENAME_IMAGE_PROFILE = "FILENAME_IMAGE_PROFILE"
        private const val USER_SESSION = "USER_SESSION"

        fun setImageProfile(context:Context, fileName:String){
            val preferences = context.getSharedPreferences(APP_PREF_NAME, Context.MODE_PRIVATE)
            preferences.edit().putString(FILENAME_IMAGE_PROFILE, fileName).apply()
        }

        fun getImageProfile(context: Context):String? {
            val preferences = context.getSharedPreferences(APP_PREF_NAME, Context.MODE_PRIVATE)
            return preferences.getString(FILENAME_IMAGE_PROFILE, null)
        }

        fun setUserSession(context:Context, email: String){
            val preferences = context.getSharedPreferences(APP_PREF_NAME, Context.MODE_PRIVATE)
            preferences.edit().putString(USER_SESSION, email).apply()
        }

        fun getUserSession(context: Context):String?{
            val preferences = context.getSharedPreferences(APP_PREF_NAME, Context.MODE_PRIVATE)
            return preferences.getString(USER_SESSION, null)
        }

        fun deletePreferences(context: Context){
            val preferences = context.getSharedPreferences(APP_PREF_NAME, Context.MODE_PRIVATE)
            preferences.edit().clear().apply()
        }
    }
}