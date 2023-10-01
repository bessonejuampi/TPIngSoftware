package com.example.tpingsoftware.ui.view

import android.app.Activity
import android.content.Intent
import android.net.Uri
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
import com.example.tpingsoftware.data.models.Service
import com.example.tpingsoftware.databinding.ActivityAddServiceBinding
import com.example.tpingsoftware.ui.viewModels.AddServiceViewModel
import com.example.tpingsoftware.utils.Constants
import com.example.tpingsoftware.utils.DialogHelper
import com.example.tpingsoftware.utils.TypeDialog
import org.koin.androidx.viewmodel.ext.android.viewModel

class AddServiceActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddServiceBinding

    private val viewModel : AddServiceViewModel by viewModel()

    var provinces : ArrayList<Province>? = null

    private var provinceSelected : String? = null
    private var locationSelected : String? = null

    private var selectedImageLogoUri : Uri? = null
    private var idImage : Long? = null

    private var service: Service? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddServiceBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (intent.extras != null) {

            val bundle = intent.getBundleExtra(Constants.KEY_BUNDLE_SERVICE_TO_DETAILS)
            service = bundle!!.getParcelable(Constants.KEY_SERVICE)
        }

        viewModel.getProvinces()

        setupView()
        observeMutableLiveData()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == Constants.PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.data != null) {
            selectedImageLogoUri = data.data

            binding.ivLogoService.setImageURI(data.data)

            idImage = System.currentTimeMillis()
        }
    }

    private fun observeMutableLiveData() {
        viewModel.listProvincesLiveData.observe(this, Observer { listProvinces ->
            setProvincesExposedDropdownMenu(listProvinces)
        })

        viewModel.listLocalitiesLiveData.observe(this, Observer { listLocalities ->
            setLocalitiesExposedDropdownMenu(listLocalities)
        })

        viewModel.serviceLiveData.observe(this, Observer { serviceValidator ->

            hideLoading()

            serviceValidator?.let {
                if (!serviceValidator.titleError.isNullOrEmpty()){
                    binding.tilTitle.error = serviceValidator.titleError
                }

                if (!serviceValidator.descriptionError.isNullOrEmpty()){
                    binding.tilDescription.error = serviceValidator.descriptionError
                }

                if (!serviceValidator.locationError.isNullOrEmpty()){
                    binding.actvLocalities.error = serviceValidator.locationError
                }

                if (!serviceValidator.provinceError.isNullOrEmpty()){
                    binding.actvProvince.error = serviceValidator.provinceError
                }

                if (!serviceValidator.addressError.isNullOrEmpty()){
                    binding.tfAddress.error = serviceValidator.addressError
                }
            }
        })

        viewModel.resultRegisterServiceLiveData.observe(this, Observer { dialog ->

            hideLoading()

            if (dialog.result == TypeDialog.GO_TO_HOME) {
                DialogHelper.showResultDialog(
                    this,
                    dialog.title!!,
                    dialog.description!!,
                    { viewModel.goToHome() },
                    { viewModel.goToHome() })
            } else {
                DialogHelper.showResultDialog(this, dialog.title!!, dialog.description!!, { })
            }
        })
    }

    private fun setupView() {

        if (service != null) {

            binding.etTitle.setText(service!!.title)
            binding.etDescription.setText(service!!.description)
            binding.etAddress.setText(service!!.address)
            binding.tfProvince.editText?.setText(service!!.province)
            binding.tfLocalities.editText?.setText(service!!.location)

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

        binding.ivLogoService.setOnClickListener {
            openGallery()
        }

        binding.btnSaveService.setOnClickListener {

            showLoading()

            viewModel.validateService(
                binding.etTitle.text.toString(),
                binding.etDescription.text.toString(),
                provinceSelected,
                locationSelected,
                binding.etAddress.text.toString(),
                idImage,
                selectedImageLogoUri
            )
        }

        setupCleanEditText()
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

    private fun setupCleanEditText() {

        binding.etTitle.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                binding.tilTitle.error = null
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                // Nothing use
            }

            override fun afterTextChanged(p0: Editable?) {
                // Nothing use
            }
        })

        binding.etDescription.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                binding.tilDescription.error = null

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
                binding.actvLocalities.error = null

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
                binding.actvProvince.error = null
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                // Nothing use
            }

            override fun afterTextChanged(p0: Editable?) {
                // Nothing use
            }
        })
    }

    private fun showLoading() {

        binding.llBody.visibility = View.GONE
        binding.lyProgress.visibility = View.VISIBLE
    }

    private fun hideLoading() {

        binding.llBody.visibility = View.VISIBLE
        binding.lyProgress.visibility = View.GONE
    }
}