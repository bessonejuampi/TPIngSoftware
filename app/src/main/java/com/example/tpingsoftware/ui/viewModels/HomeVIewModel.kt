package com.example.tpingsoftware.ui.viewModels

import android.content.Context
import android.content.Intent
import androidx.lifecycle.ViewModel
import com.example.tpingsoftware.ui.view.EditProfileActivity
import com.example.tpingsoftware.ui.view.HomeActivity
import com.example.tpingsoftware.ui.view.LoginActivity
import com.example.tpingsoftware.utils.AppPreferences

class HomeVIewModel(
    private val context: Context
):ViewModel() {

    fun SignOut(){
        AppPreferences.deletePreferences(context)
        goToLogin()
    }

    private fun goToLogin(){
        val intent = Intent(context, LoginActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(intent)
    }

    fun goToEditProfile() {
        val intent = Intent(context, EditProfileActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(intent)    }
}