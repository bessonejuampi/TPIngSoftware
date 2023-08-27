package com.example.tpingsoftware.ui.view

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.lifecycle.Observer
import com.example.tpingsoftware.R
import com.example.tpingsoftware.data.models.Location
import com.example.tpingsoftware.data.models.Province
import com.example.tpingsoftware.data.models.User
import com.example.tpingsoftware.databinding.ActivityRegisterBinding
import com.example.tpingsoftware.ui.viewModels.EditProfileViewModel
import com.example.tpingsoftware.utils.Constants
import com.squareup.picasso.Picasso
import org.koin.androidx.viewmodel.ext.android.viewModel

class EditProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding

    private val viewModel: EditProfileViewModel by viewModel()

    var provinces : ArrayList<Province>? = null

    var currentUser : User? = null

    private var provinceSelected : String? = null
    private var locationSelected : String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupView()

        viewModel.getUserData()
        viewModel.getProvinces()

        observeMutableLiveData()

    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == Constants.PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.data != null) {
            val selectedImageUri = data.data

            binding.ivProfile.setImageURI(selectedImageUri)

            val idImage = if (currentUser!!.idImage.isNullOrEmpty()){
                System.currentTimeMillis().toString()
            }else{
                currentUser!!.idImage
            }

            selectedImageUri?.let { viewModel.saveImageUserInStorage(it, idImage!!) }

            currentUser!!.hasImageProfile = true
        }
    }


    private fun setupView() {

        binding.tfPassword.visibility = View.GONE
        binding.tfRepeatPassword.visibility = View.GONE
        binding.tfEmail.visibility = View.GONE
        binding.btnRegister.text = "Actualizar datos"

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

        binding.ibAddImageProfile.setOnClickListener {
            openGallery()
        }

        binding.ivProfile.setOnClickListener {
            openGallery()
        }

        binding.btnRegister.setOnClickListener {
            viewModel.validationUser(
                currentUser!!.email!!,
                binding.etName.text.toString(),
                binding.etLastName.text.toString(),
                provinceSelected,
                locationSelected,
                binding.etAddress.text.toString(),
                currentUser!!.hasImageProfile!!,
                currentUser!!.idImage
            )
        }

        setupCleanEditText()
    }

    private fun observeMutableLiveData() {
        viewModel.userLiveData.observe(this,  Observer{ user ->

            currentUser = user
            binding.actvLocalities.setText(user.location)
            locationSelected = currentUser!!.location
            binding.actvProvince.setText(user.province)
            provinceSelected = currentUser!!.province
            binding.etName.setText(user.name)
            binding.etLastName.setText(user.lastName)
            binding.etAddress.setText(user.address)

            if (user.hasImageProfile!!){
                viewModel.getImageProfile(user.idImage!!)
            }
        })

        viewModel.imageProfileLiveData.observe(this, Observer { imageRef ->
            Picasso.get().load(imageRef).into(binding.ivProfile)
        })

        viewModel.listProvinceLiveData.observe(this, Observer { provinces ->
            setProvincesExposedDropdownMenu(provinces)
        })

        viewModel.listLocalitiesLiveData.observe(this, Observer { localities ->
            setLocalitiesExposedDropdownMenu(localities)
        })

        viewModel.userValidationLiveData.observe(this, Observer { userValidator ->
            userValidator?.let {
                if (!userValidator.nameError.isNullOrEmpty()) {
                    binding.tfName.error = userValidator.nameError
                }
                if (!userValidator.lastNameError.isNullOrEmpty()) {
                    binding.tfLastName.error = userValidator.lastNameError
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
            }
        })
    }

    private fun setupCleanEditText() {

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

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, Constants.PICK_IMAGE_REQUEST)
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