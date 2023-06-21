package com.example.tpingsoftware.ui.view

import android.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.lifecycle.Observer
import com.example.tpingsoftware.utils.Dialog
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
        setupCleanEditText()

        binding.btnRegister.setOnClickListener {
            viewModel.validationUser(
                binding.etName.text.toString(),
                binding.etLastName.text.toString(),
                binding.etEmail.text.toString(),
                binding.etPassword.text.toString(),
                binding.etRepeatPassword.text.toString()
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
                    binding.tfRepeatPassword.error = userValidator.repeatPasswordError
                }
            }
        })

        viewModel.resultRegisterMutable.observe(this, Observer { result ->
            showAlertDialog(result)
        })
    }

    private fun setupCleanEditText() {

        binding.etEmail.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                binding.tfEmail.error = null
            }
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                // Nothing use
            }
            override fun afterTextChanged(p0: Editable?) {
                // Nothing use
            }
        })

        binding.etPassword.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                binding.tfPassword.error = null
            }
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                // Nothing use
            }
            override fun afterTextChanged(p0: Editable?) {
                // Nothing use
            }
        })

        binding.etRepeatPassword.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                binding.tfRepeatPassword.error = null
            }
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                // Nothing use
            }
            override fun afterTextChanged(p0: Editable?) {
                // Nothing use
            }
        })

        binding.etName.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                binding.tfName.error = null
            }
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                // Nothing use
            }
            override fun afterTextChanged(p0: Editable?) {
                // Nothing use
            }
        })

        binding.etLastName.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                binding.tfLastName.error = null
            }
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                // Nothing use
            }
            override fun afterTextChanged(p0: Editable?) {
                // Nothing use
            }
        })
    }

    private fun showAlertDialog(dialog:Dialog){
        val builder = AlertDialog.Builder(this)
        builder.setTitle(dialog.title)
        builder.setMessage(dialog.description)
        builder.setPositiveButton("Aceptar") { accept, _ ->
            if (dialog.result == true){
                viewModel.goToHome()
            }
            accept.dismiss()
        }
        val alertDialog = builder.create()
        alertDialog.show()
    }
}