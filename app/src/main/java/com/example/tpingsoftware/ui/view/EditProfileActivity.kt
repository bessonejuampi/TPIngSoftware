package com.example.tpingsoftware.ui.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import com.example.tpingsoftware.databinding.ActivityRegisterBinding
import com.example.tpingsoftware.ui.viewModels.EditProfileViewModel
import com.squareup.picasso.Picasso
import org.koin.androidx.viewmodel.ext.android.viewModel

class EditProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding

    private val viewModel: EditProfileViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.tfPassword.visibility = View.GONE
        binding.tfRepeatPassword.visibility = View.GONE
        viewModel.getUserData()
        observeMutableLiveData()
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
    }


}