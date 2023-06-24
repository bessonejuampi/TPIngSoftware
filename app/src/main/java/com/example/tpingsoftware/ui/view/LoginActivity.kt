package com.example.tpingsoftware.ui.view

import android.app.AlertDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.lifecycle.Observer
import com.example.tpingsoftware.R
import com.example.tpingsoftware.databinding.ActivityLoginBinding
import com.example.tpingsoftware.ui.viewModels.LoginViewModel
import com.example.tpingsoftware.utils.AppPreferences
import com.example.tpingsoftware.utils.Constants.GOOGLE_SING_IN
import com.example.tpingsoftware.utils.Dialog
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import org.koin.androidx.viewmodel.ext.android.viewModel

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    private val viewModel: LoginViewModel by viewModel()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        checkUserSession()

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
            showProgress()
        }

        binding.btGoogle.setOnClickListener {
            val configurationGoogle = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build()

            val clientGoogle = GoogleSignIn.getClient(this, configurationGoogle)
            clientGoogle.signOut()
            startActivityForResult(clientGoogle.signInIntent, GOOGLE_SING_IN)
        }

        viewModel.resultLogInMutable.observe(this, Observer { result ->
            hideProgress()
            showAlertDialog(result)
        })


    }

    override fun onStart() {
        super.onStart()
        binding.llLogin.visibility = View.VISIBLE
        binding.llRegister.visibility = View.VISIBLE
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == GOOGLE_SING_IN){
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            viewModel.LoginWithGoogle(task)





        }
    }

    private fun checkUserSession(){
        if (!AppPreferences.getUserSession(this).isNullOrEmpty()){
            viewModel.goToHome()
            binding.llLogin.visibility = View.INVISIBLE
            binding.llRegister.visibility = View.INVISIBLE
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

    private fun showProgress() {
        binding.progressBarLogin.visibility = View.VISIBLE
        binding.btLogin.visibility = View.INVISIBLE
    }

    private fun hideProgress() {
        binding.progressBarLogin.visibility =  View.GONE
        binding.btLogin.visibility = View.VISIBLE
    }

    private fun showAlertDialog(dialog: Dialog){
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