package com.example.tpingsoftware.ui.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.tpingsoftware.R
import com.example.tpingsoftware.databinding.ActivityForgotPasswordBinding
import org.koin.androidx.viewmodel.ext.android.viewModel
import com.example.tpingsoftware.ui.viewModels.ForgotPasswordViewModel

class ForgotPasswordActivity : AppCompatActivity() {

    private lateinit var binding: ActivityForgotPasswordBinding

    private val viewModel : ForgotPasswordViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityForgotPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btSendEmail.setOnClickListener {
            if (binding.etEmail.text.isNullOrEmpty()){
                Toast.makeText(this, "Por favor, ingrese su Email", Toast.LENGTH_SHORT).show()
            }else{
                viewModel.sendEmailResetPassword(binding.etEmail.text.toString())
            }
        }


    }
}