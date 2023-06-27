package com.example.tpingsoftware.ui.view

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.core.app.ActivityCompat
import androidx.lifecycle.Observer
import com.example.tpingsoftware.utils.Dialog
import com.example.tpingsoftware.databinding.ActivityRegisterBinding
import com.example.tpingsoftware.ui.viewModels.RegisterViewModel
import com.example.tpingsoftware.utils.AppPreferences
import com.example.tpingsoftware.utils.Constants.PICK_IMAGE_REQUEST
import org.koin.androidx.viewmodel.ext.android.viewModel
import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.LocationManager
import androidx.core.content.ContextCompat


class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding

    private val viewModel: RegisterViewModel by viewModel()

    private var hasImageProfile = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.progressBar.visibility = View.GONE

        setupViewModelObserver()
        setupCleanEditText()
        requestPermissionLocation()

        binding.btnRegister.setOnClickListener {
            viewModel.validationUser(
                binding.etName.text.toString(),
                binding.etLastName.text.toString(),
                binding.etEmail.text.toString(),
                binding.etPassword.text.toString(),
                binding.etRepeatPassword.text.toString(),
                AppPreferences.getLatitudeUser(this),
                AppPreferences.getLongitudeUser(this),
                hasImageProfile
            )
            showProgress()
        }

        binding.ibAddImageProfile.setOnClickListener {
            openGallery()
        }

        binding.ivProfile.setOnClickListener {
            openGallery()
        }

        binding.btnGetLocation.setOnClickListener {

            if (ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
            ) {

                val locationManager =
                    getSystemService(Context.LOCATION_SERVICE) as LocationManager
                val lastKnownLocation =
                    locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)

                lastKnownLocation?.let {
                    val latitude = lastKnownLocation.latitude
                    val longitude = lastKnownLocation.longitude

                    binding.etLocation.setText("$latitude, $longitude")

                    AppPreferences.setLocationUser(
                        this, latitude.toString(),
                        longitude.toString()
                    )
                }
            } else {
                var dialog = Dialog(
                    "Permisos Requeridos",
                    "Se necesita su permiso para accedar al GPS del dispositivo"
                )

                val builder = AlertDialog.Builder(this)
                builder.setTitle(dialog.title)
                builder.setMessage(dialog.description)
                builder.setPositiveButton("Aceptar") { accept, _ ->

                    requestPermissionLocation()
                    accept.dismiss()
                }
                val alertDialog = builder.create()
                alertDialog.show()
            }

        }


    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.data != null) {
            val selectedImageUri = data.data

            binding.ivProfile.setImageURI(selectedImageUri)

            selectedImageUri?.let { viewModel.saveImageUserInStorage(it) }

            hasImageProfile = true
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 1) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED &&
                grantResults[1] == PackageManager.PERMISSION_GRANTED
            ) {
            }
        }
    }


    private fun requestPermissionLocation() {

        val locationPermissions = arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )
        val requestCode = 1
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED &&
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {

            //Ya existen permisos

        } else {

            ActivityCompat.requestPermissions(this, locationPermissions, requestCode)
        }

    }

    private fun setupViewModelObserver() {
        viewModel.userValidationMutable.observe(this, Observer { userValidator ->
            userValidator?.let {
                if (!userValidator.nameError.isNullOrEmpty()) {
                    binding.tfName.error = userValidator.nameError
                }
                if (!userValidator.lastNameError.isNullOrEmpty()) {
                    binding.tfLastName.error = userValidator.lastNameError
                }
                if (!userValidator.emailError.isNullOrEmpty()) {
                    binding.tfEmail.error = userValidator.emailError
                }
                if (!userValidator.passError.isNullOrEmpty()) {
                    binding.tfPassword.error = userValidator.passError
                }
                if (!userValidator.lastNameError.isNullOrEmpty()) {
                    binding.tfRepeatPassword.error = userValidator.repeatPasswordError
                }
            }
        })

        viewModel.resultRegisterMutable.observe(this, Observer { result ->
            hideProgress()
            showAlertDialog(result)
        })

    }

    private fun setupCleanEditText() {


        binding.etEmail.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                binding.tfEmail.error = null
                hideProgress()
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
                hideProgress()
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
                hideProgress()
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
                hideProgress()
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
                hideProgress()
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                // Nothing use
            }

            override fun afterTextChanged(p0: Editable?) {
                // Nothing use
            }
        })
    }

    private fun showAlertDialog(dialog: Dialog) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle(dialog.title)
        builder.setMessage(dialog.description)
        builder.setPositiveButton("Aceptar") { accept, _ ->
            if (dialog.result == true) {
                viewModel.goToHome()
            }
            accept.dismiss()
        }
        val alertDialog = builder.create()
        alertDialog.show()
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, PICK_IMAGE_REQUEST)
    }

    private fun showProgress() {
        binding.progressBar.visibility = View.VISIBLE
        binding.btnRegister.visibility = View.INVISIBLE
    }

    private fun hideProgress() {
        binding.progressBar.visibility = View.GONE
        binding.btnRegister.visibility = View.VISIBLE
    }


}