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
import android.content.pm.PackageManager
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.core.content.ContextCompat
import com.example.tpingsoftware.R
import com.example.tpingsoftware.data.models.Location
import com.example.tpingsoftware.data.models.Province
import com.example.tpingsoftware.utils.TypeDialog


class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding

    private val viewModel: RegisterViewModel by viewModel()

    private var hasImageProfile = false
    private var idImage = ""

    var provinces : ArrayList<Province>? = null

    private var provinceSelected : String? = null
    private var locationSelected : String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.progressBar.visibility = View.GONE

        setupViewModelObserver()
        setupCleanEditText()


        viewModel.getProvinces()

        binding.btnRegister.setOnClickListener {
            viewModel.validationUser(
                binding.etName.text.toString(),
                binding.etLastName.text.toString(),
                binding.etEmail.text.toString(),
                binding.etPassword.text.toString(),
                binding.etRepeatPassword.text.toString(),
                provinceSelected,
                locationSelected,
                binding.etAddress.text.toString(),
                hasImageProfile,
                idImage
            )
            showProgress()
        }

        binding.ibAddImageProfile.setOnClickListener {
            openGallery()
        }

        binding.ivProfile.setOnClickListener {
            openGallery()
        }

        binding.actvProvince.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id ->
            val selectedOption = parent.getItemAtPosition(position).toString()
            provinceSelected = selectedOption
            if (provinces != null){
                provinces?.forEach { province ->
                    if (selectedOption == province.name){
                        viewModel.getLocalities(province.id)
                    }
                }
            }
        }

        binding.actvLocalities.onItemClickListener = AdapterView.OnItemClickListener{ parent, view, position, id ->
            locationSelected = parent.getItemAtPosition(position).toString()
        }
    }

    private fun setProvincesExposedDropdownMenu(listProvinces:ArrayList<Province>) {
        val items = arrayListOf<String>()
        listProvinces.forEach {
            items.add(it.name)
        }

        provinces = listProvinces
        val adapter = ArrayAdapter(this, R.layout.list_items_provinces, items)
        (binding.tfProvince.editText as? AutoCompleteTextView)?.setAdapter(adapter)

    }


    private fun setLocalitiesExposedDropdownMenu(listLocalities:ArrayList<Location>) {
        val items = arrayListOf<String>()
        listLocalities.forEach {
            items.add(it.municipality.name)
        }
        val cleanItems = items.distinct()
        val sortedItems = cleanItems.sortedBy { it }
        val adapter = ArrayAdapter(this, R.layout.list_items_localities, sortedItems)
        (binding.tfLocalities.editText as? AutoCompleteTextView)?.setAdapter(adapter)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.data != null) {
            val selectedImageUri = data.data

            binding.ivProfile.setImageURI(selectedImageUri)

            idImage = System.currentTimeMillis().toString()


            selectedImageUri?.let { viewModel.saveImageUserInStorage(it, idImage) }

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
                if (!userValidator.provinceError.isNullOrEmpty()) {
                    binding.tfProvince.error = userValidator.provinceError
                }
                if (!userValidator.locationError.isNullOrEmpty()) {
                    binding.tfLocalities.error = userValidator.locationError
                }
                if (!userValidator.addressError.isNullOrEmpty()) {
                    binding.tfAddress.error = userValidator.addressError
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

        viewModel.listProvincesMutable.observe(this, Observer { provinces ->
            setProvincesExposedDropdownMenu(provinces)
        })

        viewModel.listLocalitiesMutable.observe(this, Observer { localities ->
            setLocalitiesExposedDropdownMenu(localities)
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

        binding.etAddress.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                binding.tfAddress.error = null
                hideProgress()
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                // Nothing use
            }

            override fun afterTextChanged(p0: Editable?) {
                // Nothing use
            }
        })

        binding.actvLocalities.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                binding.tfLocalities.error = null
                hideProgress()
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                // Nothing use
            }

            override fun afterTextChanged(p0: Editable?) {
                // Nothing use
            }
        })

        binding.actvProvince.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                binding.tfProvince.error = null
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
            if (dialog.result == TypeDialog.GO_TO_HOME) {
                viewModel.goToLogin()
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