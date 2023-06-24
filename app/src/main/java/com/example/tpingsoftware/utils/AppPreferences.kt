package com.example.tpingsoftware.utils

import android.content.Context

class AppPreferences {
    companion object{

        private const val APP_PREF_NAME = "APP_PREF_NAME"
        private const val FILENAME_IMAGE_PROFILE = "FILENAME_IMAGE_PROFILE"


        fun setImageProfile(context:Context, fileName:String){
            val preferences = context.getSharedPreferences(APP_PREF_NAME, Context.MODE_PRIVATE)
            preferences.edit().putString(FILENAME_IMAGE_PROFILE, fileName).apply()
        }

        fun getImageProfile(context: Context):String? {
            val preferences = context.getSharedPreferences(APP_PREF_NAME, Context.MODE_PRIVATE)
            return preferences.getString(FILENAME_IMAGE_PROFILE, "")
        }
    }
}