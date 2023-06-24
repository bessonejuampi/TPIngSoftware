package com.example.tpingsoftware.ui.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import com.example.tpingsoftware.R
import com.example.tpingsoftware.databinding.ActivityLoginBinding
import com.example.tpingsoftware.ui.viewModels.LoginViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    private val viewModel: LoginViewModel by viewModel()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btLogin.isEnabled = false

        setupListenerEmailAndPassword()

        binding.btRegister.setOnClickListener {
            viewModel.goToRegisterUser()
        }

        binding.btLogin.setOnClickListener {
            viewModel.LogIn(
                binding.etEmail.text.toString(),
                binding.etPassword.text.toString()
            )
        }

    }

    private fun setupListenerEmailAndPassword() {
        binding.etEmail.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                // Verificar si el campo de texto del email no está vacío
                val email = binding.etEmail.text.toString()
                val pass = binding.etPassword.text.toString()
                binding.btLogin.isEnabled = email.isNotEmpty() && pass.isNotEmpty()
            }
        })

        binding.etPassword.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                // Verificar si el campo de texto de la contraseña no está vacío
                val email = binding.etEmail.text.toString()
                val contraseña = binding.etPassword.text.toString()
                binding.btLogin.isEnabled = email.isNotEmpty() && contraseña.isNotEmpty()
            }
        })
    }
}