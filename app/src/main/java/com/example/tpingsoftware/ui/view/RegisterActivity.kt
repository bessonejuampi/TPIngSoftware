package com.example.tpingsoftware.ui.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import com.example.tpingsoftware.R
import com.example.tpingsoftware.databinding.ActivityRegisterBinding
import com.example.tpingsoftware.ui.viewModels.RegisterViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding

    private val viewModel:RegisterViewModel by viewModel()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupViewModelObserver()

        binding.btnRegister.setOnClickListener {
            viewModel.validationUser(
                binding.etName.text.toString(),
                binding.etLastName.text.toString(),
                binding.etEmail.text.toString(),
                binding.etPassword.text.toString(),
                binding.etPassword1.text.toString()
            )
        }


    }

    private fun setupViewModelObserver(){
        viewModel.userValidationMutable.observe(this, Observer { userValidator ->
            userValidator?.let {
                if (!userValidator.nameError.isNullOrEmpty()){
                    binding.tfName.error = userValidator.nameError
                }
                if (!userValidator.lastNameError.isNullOrEmpty()){
                    binding.tfLastName.error = userValidator.lastNameError
                }
                if (!userValidator.emailError.isNullOrEmpty()){
                    binding.tfEmail.error = userValidator.emailError
                }
                if (!userValidator.passError.isNullOrEmpty()){
                    binding.tfPassword.error = userValidator.passError
                }
                if (!userValidator.lastNameError.isNullOrEmpty()){
                    binding.tfPassword1.error = userValidator.repeatPasswordError
                }
            }
        })
    }
}