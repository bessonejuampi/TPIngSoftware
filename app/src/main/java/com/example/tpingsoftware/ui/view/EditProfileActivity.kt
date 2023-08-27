package com.example.tpingsoftware.ui.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.lifecycle.Observer
import com.example.tpingsoftware.R
import com.example.tpingsoftware.data.models.Location
import com.example.tpingsoftware.data.models.Province
import com.example.tpingsoftware.databinding.ActivityRegisterBinding
import com.example.tpingsoftware.ui.viewModels.EditProfileViewModel
import com.squareup.picasso.Picasso
import org.koin.androidx.viewmodel.ext.android.viewModel

class EditProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding

    private val viewModel: EditProfileViewModel by viewModel()

    var provinces : ArrayList<Province>? = null

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

    private fun setupView() {

        binding.tfPassword.visibility = View.GONE
        binding.tfRepeatPassword.visibility = View.GONE
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
    }

    private fun observeMutableLiveData() {
        viewModel.userLiveData.observe(this,  Observer{ user ->
            binding.etEmail.setText(user.email)
            binding.actvLocalities.setText(user.location)
            binding.actvProvince.setText(user.province)
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


}